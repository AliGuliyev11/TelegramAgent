package com.mycode.telegramagent.repositories;

import com.mycode.telegramagent.enums.RolePriority;
import com.mycode.telegramagent.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Role getRoleByRolePriority(RolePriority rolePriority);
}
