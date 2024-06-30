package com.example.chatgpt1.service;

import com.example.chatgpt1.dto.GptRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface GptService {
    List<Map<String, Object>> prompt(GptRequestDto gptRequestDto);

    List<Map<String, Object>> modelList();

    Map<String, Object> isValidModel(String modelName);

}
