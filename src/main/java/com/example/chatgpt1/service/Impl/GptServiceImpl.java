package com.example.chatgpt1.service.Impl;

import com.example.chatgpt1.config.GptConfig;
import com.example.chatgpt1.dto.GptRequestDto;
import com.example.chatgpt1.service.GptService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class GptServiceImpl implements GptService {

    private final GptConfig gptConfig;

    public GptServiceImpl(GptConfig gptConfig) {
        this.gptConfig = gptConfig;
    }

    @Value("${openai.model}")
    private String model;

    /**
     * 사용 가능한 모델 리스트를 조회하는 비즈니스 로직
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> modelList() {
        log.debug("[+] 모델 리스트를 조회합니다.");
        List<Map<String, Object>> resultList = new ArrayList<>();

        HttpHeaders headers = gptConfig.httpHeaders();

        ResponseEntity<String> response = gptConfig.restTemplate()
                .exchange(
                        "https://api.openai.com/v1/models",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class);

        try {
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> data = om.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> dataModels = (List<Map<String, Object>>) data.get("data");
            resultList.addAll(dataModels);
            for (Map<String, Object> object : resultList) {
                log.debug("ID: " + object.get("id"));
                log.debug("Object: " + object.get("object"));
                log.debug("Created: " + object.get("created"));
                log.debug("Owned By: " + object.get("owned_by"));
            }
        } catch (JsonMappingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (JsonProcessingException e) {
            log.debug("JsonProcessingException :: " + e.getMessage());
        }

        return resultList;
    }

    /**
     * 모델이 유효한지 확인하는 비즈니스 로직
     *
     * @param modelName
     * @return
     */
    @Override
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회합니다. 모델 : " + modelName);
        Map<String, Object> result = new HashMap<>();

        HttpHeaders headers = gptConfig.httpHeaders();

        ResponseEntity<String> response = gptConfig.restTemplate()
                .exchange(
                        "https://api.openai.com/v1/models/" + modelName,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class);

        try {
            ObjectMapper om = new ObjectMapper();
            result = om.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Map<String, Object>> prompt(GptRequestDto requestDto) {
        log.debug("[+] 프롬프트를 수행합니다.");

        List<Map<String, Object>> result = new ArrayList<>();

        // HTTP 헤더 설정
        HttpHeaders headers = gptConfig.httpHeaders();

        // ObjectMapper 생성
        ObjectMapper om = new ObjectMapper();

        // OpenAI API 요청을 위한 DTO 구성
        GptRequestDto openAiRequestDto = GptRequestDto.builder()
                .model(requestDto.getModel())
                .temperature(requestDto.getTemperature())
                .messages(Collections.singletonList(
                        Map.of("role", "user", "content", requestDto.getMessages().get(0).get("content"))
                ))
                .build();

        try {
            // 요청 본문을 JSON 문자열로 변환
            String requestBody = om.writeValueAsString(openAiRequestDto);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // OpenAI API 호출
            ResponseEntity<String> response = gptConfig.restTemplate().exchange(
                    "https://api.openai.com/v1/chat/completions",
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            // 응답 데이터 처리
            Map<String, Object> responseData = om.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            result.add(responseData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("API 요청을 위한 JSON 처리 중 오류 발생: " + e.getMessage(), e);
        }

        return result;
    }










//    @Override
//    public List<Map<String, Object>> prompt(GptRequestDto requestDto) {
//        log.debug("[+] 프롬프트를 수행합니다.");
//
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        HttpHeaders headers = gptConfig.httpHeaders();
//
//        ObjectMapper om = new ObjectMapper();
//
//        requestDto = GptRequestDto.builder()
//                .model(model)
//                .prompt(requestDto.getPrompt())
//                .messages(Collections.singletonList(
//                        Map.of("role", "user", "content", requestDto.getPrompt())
//                ))
//                .temperature(0.8f)
//                .build();
//
//        try {
//            String requestBody = om.writeValueAsString(requestDto);
//            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
//
//            ResponseEntity<String> response = gptConfig.restTemplate()
//                    .exchange(
//                            "https://api.openai.com/v1/chat/completions",
//                            HttpMethod.POST,
//                            requestEntity,
//                            String.class);
//
//            List<Map<String, Object>> responseData = om.readValue(response.getBody(), new TypeReference<List<Map<String, Object>>>() {});
//            result.addAll(responseData);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return result;
//    }
}
