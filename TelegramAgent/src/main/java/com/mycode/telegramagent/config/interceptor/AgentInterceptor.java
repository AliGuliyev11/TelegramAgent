package com.mycode.telegramagent.config.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class AgentInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String auth = request.getHeader("Authorization");
        if(auth != null){
//            UserDTO user = new UserDTO();
//            String[] chunks = auth.split("\\.");
//            Base64.Decoder decoder = Base64.getDecoder();
//            String data = new String(decoder.decode(chunks[1]));
//            JsonNode payload = new ObjectMapper().readValue(data,JsonNode.class);
//            String slug= BasicUtil.createSlug(payload.get("preferred_username").textValue());
//            user.setUsername(slug);
//            user.setEmail(payload.get("email").textValue());
//            request.setAttribute("user",user);
            return false;
        }
        return true;
    }
}
