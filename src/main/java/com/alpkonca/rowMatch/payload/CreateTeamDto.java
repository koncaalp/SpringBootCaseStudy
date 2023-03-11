package com.alpkonca.rowMatch.payload;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTeamDto {
    @NotBlank(message = "name must be sent and cannot be empty")
    private String name;

    @Min(value = 1, message = "userId must be sent and cannot be empty")
    private int userId;

}
