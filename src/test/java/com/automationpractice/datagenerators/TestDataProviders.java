package com.automationpractice.datagenerators;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;

import com.automationpractice.model.LigalCredentials;
import com.automationpractice.model.Products;
import com.automationpractice.model.RegistrationFormData;

public class TestDataProviders {

	// **************INVALID/ILLEGAL TEST DATA********************//

	@DataProvider(name = "illegalCredentials")
	protected static String[][] getIlligalCredentials() {
		String[][] generatedTestData = new String[20][2];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row] = TestDataGenerator.generateValidFormatCredentials();
		}
		return generatedTestData;
	}

	@DataProvider(name = "invalidEmail")
	protected static String[] generateInvalidEmail() {
		String[] generatedTestData = new String[40];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row] = TestDataGenerator.generateInvalidFormatEmails();
		}
		return generatedTestData;
	}

	@DataProvider(name = "invalidPasswordAndValidEmail")
	protected static String[][] generateInvalidPasswordAndValidEmail() {
		String[][] generatedTestData = new String[20][2];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row][0] = TestDataGenerator.generateValidFormatEmails();
			generatedTestData[row][1] = TestDataGenerator.generateInvalidFormatPasswords();
		}
		return generatedTestData;
	}

	@DataProvider(name = "invalidEmailAndValidPassword")
	protected static String[][] generateInvalidEmailAndValidPassword() {
		String[][] generatedTestData = new String[20][2];
		// loop over 2D array
		for (int row = 0; row < generatedTestData.length; row++) {
			generatedTestData[row][0] = TestDataGenerator.generateInvalidFormatEmails();
			generatedTestData[row][1] = TestDataGenerator.generateValidFormatPasswords();
		}
		return generatedTestData;
	}

	// **************VALID/LEGAL TEST DATA********************//

	@DataProvider(name = "validCredentialsForRegistrationController")
	protected static Iterator<RegistrationFormData> generateRegistrationFormDataForRegistratonPage()
			throws InterruptedException, IOException {
		return TestDataModelGenerator.generateRegistrationFormData().iterator();
	}

	@DataProvider(name = "getValidProductsFromPropertyFile")
	protected static Iterator<Products> generateValidProducts() throws InterruptedException, IOException {
		return TestDataModelGenerator.readProductList().iterator();
	}

	@DataProvider(name = "getLigalCredentialsForAuthenticationControllerFromPropertyFile")
	protected static Iterator<LigalCredentials> generateValidCredentialsForController()
			throws InterruptedException, IOException {
		return TestDataModelGenerator.readLigalCredentialsList().iterator();
	}

	@DataProvider(name = "getLigalCredentialsAndProductIdsForPdpControllerFromPropertyFiles")
	protected static Object[][] generatePdpDataForWishListController() throws InterruptedException, IOException {
		// Create a dynamic 2D array with the size of the validCredentials
		// entries(accounts)
		Object[][] generatedTestData = new Object[TestDataModelGenerator.readLigalCredentialsList().size()][2];
		List<LigalCredentials> ligalCredentialsList = new ArrayList<LigalCredentials>();
		List<Products> productList = new ArrayList<Products>();
		// convert sets to lists
		ligalCredentialsList.addAll(TestDataModelGenerator.readLigalCredentialsList());
		productList.addAll(TestDataModelGenerator.readProductList());
		// fill out the array with the lists values (sets values)
		for (int i = 0; i < ligalCredentialsList.size(); i++) {
			generatedTestData[i][0] = ligalCredentialsList.get(i);
			generatedTestData[i][1] = productList.get(i);
		}
		return generatedTestData;
	}
}