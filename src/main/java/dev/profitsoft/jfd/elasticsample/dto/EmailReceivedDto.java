package dev.profitsoft.jfd.elasticsample.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
@ToString
public class EmailReceivedDto {
    @Email(message = "recipient should be valid")
    @Size(max = 320, message = "Length of recipient must be less than 320 characters")
    @NotBlank(message = "recipient is required")
    private String recipient;
    @NotBlank(message = "subject is required")
    @Size(max = 128, message = "Length of subject must be less than 128 characters")
    private String subject;
    @NotBlank(message = "content is required")
    @Size(max = 1024, message = "Length of content must be less than 1024 characters")
    private String content;
}
