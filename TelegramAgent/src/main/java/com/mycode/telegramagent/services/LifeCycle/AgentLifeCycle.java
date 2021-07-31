package com.mycode.telegramagent.services.LifeCycle;

import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.services.email.EmailServiceImpl;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;


@Getter
@Setter
@NoArgsConstructor
public class AgentLifeCycle {
    EmailServiceImpl emailService;

    public AgentLifeCycle(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @Value("${agent.email.confirmation.limit}")
    long limitConfirmationEmail;

    @Value("${email.verify.agent.url}")
    String emailUrl;

    @PostPersist
    private void afterPost(Agent agent) {
        String url = emailUrl + agent.getHashCode();
        String text = "Hi," + agent.getAgencyName() + ".This is confirmation link click <a href=" + url + ">here</a>."
                + "This link expired "+limitConfirmationEmail+" minutes after";
        emailService.sendSimpleMessage(agent.getEmail(), "Verification email",
                text);
    }
}
