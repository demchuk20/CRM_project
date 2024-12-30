package org.project.crm.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewUser {
    @NotNull
    @NotEmpty
    public String username;
    @NotNull
    @NotEmpty
    public String password;
    @NotNull
    @NotEmpty
    public String passwordRepeated;
}
