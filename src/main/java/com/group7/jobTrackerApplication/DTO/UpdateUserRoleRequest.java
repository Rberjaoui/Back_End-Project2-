package com.group7.jobTrackerApplication.DTO;

import com.group7.jobTrackerApplication.model.Role;
import jakarta.validation.constraints.NotNull;

public class UpdateUserRoleRequest {

    @NotNull(message = "Role is required")
    private Role role;

    public UpdateUserRoleRequest(){
    }


    public String getRole(){
        return role;
    }

    public void setRole( String role){
        this.role = role;
    }
}
