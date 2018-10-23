package com.automationpractice.tests;

import java.io.IOException;
import java.util.Iterator;

import org.testng.annotations.DataProvider;

import com.automationpractice.model.LigalCredentials;
import com.automationpractice.model.Products;

public class TestDataProviders extends TestDataGenerator {

	// **************INVALID/ILLEGAL TEST DATA********************//

	@DataProvider(name = "illegalCredentials")
	public static Object[][] getIlligalCredentials() {
		Object[][] generatedTestData = new Object[20][2];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row] = generateValidFormatCredentials();
		}
		return generatedTestData;
	}

	@DataProvider(name = "invalidEmail")
	public static Object[] generateInvalidEmail() {
		Object[] generatedTestData = new Object[40];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row] = generateInvalidFormatEmails();
		}
		return generatedTestData;
	}

	@DataProvider(name = "invalidPasswordAndValidEmail")
	public static Object[][] generateInvalidPasswordAndValidEmail() {
		Object[][] generatedTestData = new Object[20][2];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row][0] = generateValidFormatEmails();
			generatedTestData[row][1] = generateInvalidFormatPasswords();
		}
		return generatedTestData;
	}

	@DataProvider(name = "invalidEmailAndValidPassword")
	public static Object[][] generateInvalidEmailAndValidPassword() {
		Object[][] generatedTestData = new Object[20][2];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row][0] = generateInvalidFormatEmails();
			generatedTestData[row][1] = generateValidFormatPasswords();
		}
		return generatedTestData;
	}

	// **************VALID/LEGAL TEST DATA********************//

	@DataProvider(name = "validCredentialsForRegistrationPage")
	public static Object[][] generateligalCredentialsForRegistrationPage() {
		Object[][] generatedTestData = new Object[1][9];
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row] = generateValidFormatCredentialsForRegistrationPage();
		}
		return generatedTestData;
	}

	@DataProvider(name = "validCredentialsForRegistrationController")
	public static Object[][] generateLigalCredentialsForController() {
		Object[][] generatedTestData = new Object[10][9];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row] = generateValidFormatCredentialsForRegistrationController();
		}
		return generatedTestData;
	}

	@DataProvider(name = "getValidProductsFromPropertyFile")
	public static Iterator<Products> generateValidProducts() throws InterruptedException, IOException {
		return readProductList().iterator();
	}

	@DataProvider(name = "getLigalCredentialsForAuthenticationControllerFromPropertyFile")
	public static Iterator<LigalCredentials> generateValidCredentialsForController()
			throws InterruptedException, IOException {
		return readLigalCredentialsList().iterator();
	}

	//TODO try multidimensional SET
	@DataProvider(name = "getLigalCredentialsAndProductIdsForPdpControllerFromPropertyFiles")
	protected static Object[][] generatePdpDataForWishListController() throws InterruptedException, IOException {
		Object[][] generatedTestData = new Object[3][2];
		Iterator<Products> productList = readProductList().iterator();
		Iterator<LigalCredentials> ligalCredentialsList = readLigalCredentialsList().iterator();
		while (ligalCredentialsList.hasNext()) {
			// loop over 2D array
			for (int row = 0; row < generatedTestData.length; row++) {
				generatedTestData[row][0] = productList.next();
				generatedTestData[row][1] = ligalCredentialsList.next();
			}
		}

		return generatedTestData;
	}
}
