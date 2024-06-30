package com.example.chatgpt1.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GptRequestDto {

    private String model;
    private float temperature;
    private List<Map<String, Object>> messages; // Map<String, Object>으로 변경

    @Builder
    GptRequestDto(String model, float temperature, List<Map<String, Object>> messages) { // Map<String, Object>으로 변경
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
    }

}


