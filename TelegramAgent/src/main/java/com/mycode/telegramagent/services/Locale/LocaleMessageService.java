package com.mycode.telegramagent.services.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @author Ali Guliyev
 * @version 1.0
 */

@Service
public class LocaleMessageService {
    private Locale locale;
    private final MessageSource messageSource;

    public LocaleMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public String getMessage(String message) {


        locale = new Locale("en", "EN");

        return messageSource.getMessage(message, null, locale);
    }

    public String getMessage(String message, Object... args) {
        locale = new Locale("en", "EN");
        return messageSource.getMessage(message, args, locale);
    }

}
