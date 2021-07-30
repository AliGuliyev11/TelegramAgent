package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.models.JasperMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JasperMessageRepo extends JpaRepository<JasperMessage, Long> {
    JasperMessage getJasperMessageByKeyword(String keyword);
}
