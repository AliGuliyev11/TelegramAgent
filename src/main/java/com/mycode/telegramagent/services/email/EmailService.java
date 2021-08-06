package com.mycode.telegramagent.services.email;

import com.mycode.telegramagent.dto.MailDTO;

import javax.mail.MessagingException;
import java.util.List;

public interface EmailService {
    void sendSimpleMessage(MailDTO mailDTO);
    void sendMultipleEmailMessage(String to, List<String> stringList, String subject, String text) throws MessagingException;
}
