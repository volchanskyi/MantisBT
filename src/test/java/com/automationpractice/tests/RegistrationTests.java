package com.automationpractice.tests;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.automationpractice.appmanager.HttpSession;
import com.automationpractice.appmanager.RegistrationHelper;

public class RegistrationTests extends TestBase {

    final private Logger logger = LoggerFactory.getLogger(TestBase.class);

    @BeforeMethod
    private void beforeMethod(Method method, Object[] parameters) {
	logger.debug("Start test " + method.getName() + " with params " + Arrays.asList(parameters));

    }

    @Test
    public void testRegistration() throws MessagingException, IOException, InterruptedException {
	long now = System.currentTimeMillis();
	String email = "automationpractice_" + now;
	String password = "testPWD001";
//	String link = "https://blablabla"; (not implemented yet) DO NOT DELETE
	HttpSession session = APP.newSession();
	assertTrue(session.createEmail(email));
	assertTrue(session.signUp(email + "@mailinator.com"));
//	assertTrue(session.verifyActivationLink(link)); (automationpractice.com doesn`t send an activation link) DO NOT DELETE
	assertTrue(session.register(email + "@mailinator.com", "My account - My Store", "Ivan", "lastName", password, "178 Somewhere Dr.",
		"San Francisco", "94132", "5", "4158962578"));
    }
    
    @Test
    public void testRegistrationWithExistedCredsAPI() throws IOException {
	HttpSession session = APP.newSession();
	String apiMsg = session.registerExistedAccountWithAPI("volchanskij@gmail.com"); 
	assertEquals(apiMsg, "An account using this email address has already been registered. Please enter a valid password or request a new one. ");
    }
    
    @Test
    public void testRegistrationWithNotExistedCredsAPI() throws IOException {
	HttpSession session = APP.newSession();
	String apiMsg = session.registerExistedAccountWithAPI("volchanskij@");
	assertEquals(apiMsg, "Invalid email address.");
    }
    
    @Test
    public void testRegistrationUsingUI() throws MessagingException, IOException, InterruptedException {
	long now = System.currentTimeMillis();
	String email = "automationpractice_" + now;
	String password = "testPWD001";
//	String link = "https://blablabla"; (not implemented yet) DO NOT DELETE
	RegistrationHelper regHelper = APP.registration();
//	HttpSession session = APP.newSession();
//	assertTrue(session.createEmail(email));
//	assertTrue(session.signUp(email + "@mailinator.com"));
//	assertTrue(session.verifyActivationLink(link)); (automationpractice.com doesn`t send an activation link) DO NOT DELETE
	regHelper.initRegistrationUsingEmail(email + "@mailinator.com").fillOutRegistrationForm("Ivan", "lastName", password, "178 Somewhere Dr.", "San Francisco", "94132",
		 "California", "4158962578");
	assertTrue(regHelper.verify("My account - My Store"));
	//"My account - My Store"
    }
    


    @AfterMethod(alwaysRun = true)
    private void logTestStop(Method method, Object[] parameters) {
	logger.debug("Stop test " + method.getName());

    }

}
