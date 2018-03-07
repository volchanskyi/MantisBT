package com.automationpractice.tests;

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
	String link = "";
	HttpSession session = APP.newSession();
	assertTrue(session.signUp(email + "@mailinator.com"));
	assertTrue(session.createEmail(email));
//	assertTrue(session.verifyLink(link));
	assertTrue(session.register(email + "@mailinator.com", "My account - My Store", "Ivan", "Test", password, "178 Meadowbrook Dr.",
		"San Francisco", "94132", "5", "4158962578"));
    }
    


    @AfterMethod(alwaysRun = true)
    private void logTestStop(Method method, Object[] parameters) {
	logger.debug("Stop test " + method.getName());

    }

}
