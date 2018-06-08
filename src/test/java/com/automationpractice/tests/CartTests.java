package com.automationpractice.tests;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automationpractice.appmanager.HttpSession;
import com.automationpractice.model.Products;

public class CartTests extends TestBase {

    final private Logger logger = LoggerFactory.getLogger(TestBase.class);

    @BeforeMethod
    private void beforeMethod(Method method, Object[] parameters) {
	logger.debug("Start test " + method.getName() + " with params " + Arrays.asList(parameters));

    }

    @Test
    public void testAddProductToCart() throws IOException {
	HttpSession session = APP.newSession();
	session.insertCookie(
		"PrestaShop-a30a9934ef476d11b6cc3c983616e364=ETZ4rE2I8tHyDOLSyZS1u2BGhYl3CIqtHvDp1ThUMwSuVGa61504FBDvu5sOuHUrb6svJ75NVHmISPROJCO0cKWzyBnRrVvRzyNNHAVyIlRCT16pN2Ey88SOZ%2FErsAWLkdkjD8Hlzq0TVjIXYFLXNQblmftp%2BYJCDrIgnLoh7kWH9dt3sqCUQp9rTWcNPc7d1Actgh%2FbvDZIOTEqNt1%2BdPAPPKXZk1zHx8iw6GtBO5k%3D000175");
	// generate token
	String token = "75bcfffc7e0bb8dec3cd64163aeff58c";
	// clean up cart before test
	session.cleanUpCart(token);
	// add item to the cart
	session.addProductToCart("3", "1", token);
	// get items from the cart and save them to the init value
	Set<Products> oldCart = session.getProductsFromCart(token);
	// create a model object (new product item with real ID)
	Products newProduct = new Products().withId(6).withQuantity(1);
	// add the product from the previous stage to the cart and save it to var
	Products newAddedProduct = session.addProductToCart(newProduct);
	// get items from the cart and save them to the new value
	Set<Products> newCart = session.getProductsFromCart(token);
	// use model object (new product item with real ID)
	// and add it to the the init value of the cart
	oldCart.add(newAddedProduct);
	// clean up cart after test
	session.cleanUpCart(token);
	// verify the init and new cart values are equal
	assertEquals(newCart, oldCart);
    }

    @AfterMethod(alwaysRun = true)
    private void logTestStop(Method method, Object[] parameters) {
	logger.debug("Stop test " + method.getName());

    }

}