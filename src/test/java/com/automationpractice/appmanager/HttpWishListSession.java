package com.automationpractice.appmanager;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;

import com.automationpractice.model.LigalCredentials;
import com.automationpractice.model.Products;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class HttpWishListSession extends HttpSessionHelper {

	public HttpWishListSession(ApplicationManager app) {
		this.setApp(app);
		this.getContext().setCookieStore(getCookieStore());
		// Enable following REDIRECTIONS (302) on POST
		this.setHttpClient(HttpClients.custom().setRedirectStrategy(new LaxRedirectStrategy()).build());
	}

	public boolean navigateToPdpUsing(Products products)
			throws JsonSyntaxException, IOException, IllegalStateException, URISyntaxException {
		URIBuilder getRequest = new URIBuilder(getApp().getProperty("web.baseUrl") + "index.php");
		// query string params
		getRequest.setParameter("id_product", String.valueOf(products.getId())).setParameter("controller", "product");
		// request header
		String[][] headerParams = { { "Cookie", getCookieValue(getCookieStore(), this.getWebCookie()) } };
		HttpGet get = createGetRequestWithParams(getRequest.toString(), headerParams);
		CloseableHttpResponse response = getHttpClient().execute(get, this.getContext());
		isHttpStatusCodeOK(response);
		String body = getTextFrom(response);
		return body.toLowerCase()
				.contains(String.format("<title>%s</title>", products.getProductName() + " - my store"));
	}

	public String addProductToWishListUsing(Products products, LigalCredentials credentials)
			throws IOException, URISyntaxException {
		URIBuilder getRequest = new URIBuilder(getApp().getProperty("web.baseUrl") + "modules/blockwishlist/cart.php");
		// query string params
		addStringParamsUsingPdpInfoWith(products, credentials, getRequest, String.valueOf(this.getRand()),
				String.valueOf((getTimeStamp().getTime())));
		// request header
		String[][] headerParams = createHeaderParamsUsingPdpIndoWith(products,
				getCookieValue(getCookieStore(), this.getWebCookie()));
		HttpGet get = createGetRequestWithParams(getRequest.toString(), headerParams);
		CloseableHttpResponse response = getHttpClient().execute(get, this.getContext());
		isHttpStatusCodeOK(response);
		return getTextFrom(response);
	}

	public boolean addedToWishListAs(Products products)
			throws ClientProtocolException, IOException, URISyntaxException {
		URIBuilder getRequest = new URIBuilder(getApp().getProperty("web.baseUrl"));
		// query string params
		addStringParamsUsingWishListInfoWith(getRequest);
		// request header
		String[][] headerParams = createHeaderParamsUsingCookieWith(
				getCookieValue(getCookieStore(), this.getWebCookie()));
		HttpGet get = createGetRequestWithParams(getRequest.toString(), headerParams);
		CloseableHttpResponse response = getHttpClient().execute(get, this.getContext());
		isHttpStatusCodeOK(response);
		String json = getTextFrom(response);
		JsonElement addedProducts = new JsonParser().parse(parsePureHtmlWithRegExUsing("^.*(\\[.*\\])\\;$", json));
		String wishListId = parsePureHtmlWithRegExUsing("^.*\\('block-order-detail'\\,\\s\\'(\\d{4}).*$", json);
		boolean bool = isAdded(products, addedProducts) ? deleteWishListWith(wishListId) : false;
		return bool;
	}

	private boolean deleteWishListWith(String wishListId)
			throws URISyntaxException, ClientProtocolException, IOException {
		URIBuilder getRequest = new URIBuilder(getApp().getProperty("web.baseUrl"));
		// query string params
		addStringParamsUsingWishListWithAddedProductInfoWith(getRequest, this.getRand(), wishListId, getTimeStamp());
		// request header
		String[][] headerParams = createHeaderParamsUsingCookieWith(
				getCookieValue(getCookieStore(), this.getWebCookie()));
		HttpGet get = createGetRequestWithParams(getRequest.toString(), headerParams);
		CloseableHttpResponse response = getHttpClient().execute(get, this.getContext());
		isHttpStatusCodeOK(response);
		String json = getTextFrom(response);
		JsonElement addedProducts = new JsonParser().parse(parsePureHtmlWithRegExUsing("^.*(\\[.*\\])\\;$", json));
		return isWishListEmpty(addedProducts);
	}

}