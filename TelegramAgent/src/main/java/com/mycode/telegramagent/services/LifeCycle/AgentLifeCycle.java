package com.mycode.telegramagent.services.LifeCycle;

import com.mycode.telegramagent.models.Agent;
import com.mycode.telegramagent.services.email.EmailServiceImpl;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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


    @PostPersist
    private void afterPost(Agent agent) {
        String url = " http://localhost:8082/api/v1/auth/confirm/" + agent.getHashCode();
        String text="Hi," + agent.getAgencyName() + ".This is confirmation link click <a href=" + url + ">here</a>";
        emailService.sendSimpleMessage(agent.getEmail(), "Verification email",
                text);
    }
}
