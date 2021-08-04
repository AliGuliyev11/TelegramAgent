package com.mycode.telegramagent.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
public class MailDTO {
    String to;
    String subject;
    String text;
    String templateName;
    String link;
    String buttonName;
}
