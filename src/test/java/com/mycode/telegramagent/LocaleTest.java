package com.mycode.telegramagent;

import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LocaleTest {

    @Autowired
    LocaleMessageService localeMessageService;

    @Test
    void getJasperPriceTest() {
        String expected = "Price";
        Assertions.assertEquals(expected, localeMessageService.getMessage("jasper.price"));
    }

    @Test
    void getJasperDateTest() {
        String expected = "Date";
        Assertions.assertEquals(expected, localeMessageService.getMessage("jasper.date"));
    }

    @Test
    void getJasperDescriptionTest() {
        String expected = "Description";
        Assertions.assertEquals(expected, localeMessageService.getMessage("jasper.description"));
    }

    @Test
    void getJasperNoteTest() {
        String expected = "Note";
        Assertions.assertEquals(expected, localeMessageService.getMessage("jasper.note"));
    }

    @Test
    void getWarningMessageTest() {
        String expected = "The offer has expired. The offer will no longer come.";
        Assertions.assertEquals(expected, localeMessageService.getMessage("warning.message"));
    }

    @Test
    void getWarningRepeatTest() {
        String expected = "Offer has expired. Your request has not been registered.";
        Assertions.assertEquals(expected, localeMessageService.getMessage("warning.repeat"));
    }

    @Test
    void getAgentEmailTextSignUpTest() {
        String expected = "We are really excited for you to join our community! You are just one click away from activate your account.This link expired 30 minutes after";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.lifecycle.email.text", 30));
    }

    @Test
    void getConfirmButtonTextTest() {
        String expected = "Activate Account!";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.confirmation.button.text"));
    }

    @Test
    void getConfirmSubjectTest() {
        String expected = "Verification email";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.confirmation.subject"));
    }

    @Test
    void getSendPasswordSubjectTest() {
        String expected = "Your new password";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.send.password.subject"));
    }

    @Test
    void getForgotPasswordSubjectTest() {
        String expected = "Forgot password";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.forgot.password.subject"));
    }

    @Test
    void geForgotPassButtonTextTest() {
        String expected = "Send Password";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.forgot.password.verified.button.text"));
    }

    @Test
    void getForgotPassNotVerifiedTextTest() {
        String expected = "Your email not verified.";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.forgot.password.not-verified.text"));
    }

    @Test
    void getForgotPassVerifiedTextTest() {
        String expected = "Please,click this link to confirm your email.Then we will send you password.";
        Assertions.assertEquals(expected, localeMessageService.getMessage("agent.forgot.password.verified.text"));
    }


}
