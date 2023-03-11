package com.alpkonca.rowMatch.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// This DTO is used to create a new team as the create team endpoint requires a name and a userId
@Data
public class CreateTeamDto {
    @NotBlank(message = "name must be sent and cannot be empty") // validator
    private String name;

    private int userId;

}
