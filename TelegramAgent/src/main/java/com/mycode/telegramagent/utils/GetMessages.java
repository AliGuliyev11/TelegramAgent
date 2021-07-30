package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.models.JasperMessage;
import com.mycode.telegramagent.repositories.JasperMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.json.JSONObject;

public class GetMessages {

    public static String getJasperMessage(String keyword, String language, JasperMessageRepo messageRepo, LocaleMessageService messageService) {
        JasperMessage message = messageRepo.getJasperMessageByKeyword(keyword);
        if (message == null) {
            return null;
        } else {
            JSONObject text = new JSONObject(message.getMessage());

            try{
                return text.getString(language.toUpperCase());
            }catch (Exception e){
                return messageService.getMessage(keyword);
            }

        }

    }
}