package com.mycode.telegramagent.utils;

import com.mycode.telegramagent.models.AgentMessage;
import com.mycode.telegramagent.repositories.AgentMessageRepo;
import com.mycode.telegramagent.services.Locale.LocaleMessageService;
import org.json.JSONObject;

/**
 * @author Ali Guliyev
 * @version 1.0
 * @implNote Class for getting message with keyword
 */

public class GetMessages {


    /** Method for getting message from database
     * @implNote if message not in database orb language not supported program gets message from locale bundle
     * @param keyword message keyword,see JasperMessage entity
     * @param language user request language
     * @param messageRepo message entity
     * @param messageService message service which get message from locale bundle
     * @return String*/

    public static String getJasperMessage(String keyword, String language, AgentMessageRepo messageRepo, LocaleMessageService messageService) {
        AgentMessage message = messageRepo.getJasperMessageByKeyword(keyword);
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
