package com.mycode.telegramagent.services.email;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendMultipleEmailMessage(String to, List<String> stringList, String subject, String text) throws MessagingException;
}
