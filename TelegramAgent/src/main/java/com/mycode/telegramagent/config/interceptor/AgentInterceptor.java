package com.mycode.telegramagent.config.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycode.telegramagent.dto.AgentDto;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * @author Ali Guliyev
 * @version 1.0
 * */

@Component
public class AgentInterceptor implements HandlerInterceptor {

    /** Every requests enters this method */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String auth = request.getHeader("Authorization");
        System.out.println(auth);
        if(auth != null){
            AgentDto agent = new AgentDto();
            String[] chunks = auth.split("\\.");
            Base64.Decoder decoder = Base64.getDecoder();
            String data = new String(decoder.decode(chunks[1]));
            JsonNode jsonNode = new ObjectMapper().readValue(data,JsonNode.class);

            agent.setEmail(jsonNode.get("email").textValue());
            request.setAttribute("user",agent);
        }
        return true;
    }


}
