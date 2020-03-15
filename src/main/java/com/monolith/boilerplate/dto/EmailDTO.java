package com.monolith.boilerplate.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmailDTO {
    private String to;
    private String subject;
    private String text;
    private String receiverId;
}
