package com.example.chatgpt1.controller;

import com.example.chatgpt1.dto.GptRequestDto;
import com.example.chatgpt1.service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

public class GptController {
    @Autowired
    private GptService gptService;

    @Autowired
    public GptController(GptService gptService){
        this.gptService = gptService;
    }

    /**
     * [API] ChatGPT 모델 리스트를 조회
     */
    @GetMapping("/modelList")
    public ResponseEntity<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = gptService.modelList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * [API] ChatGPT 유효한 모델인지 조회합니다.
     *
     * @param modelName
     * @return
     */
    @GetMapping("/model")
    public ResponseEntity<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = gptService.isValidModel(modelName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * [API] ChatGPT 모델 리스트를 조회합니다.
     */
    @PostMapping("/prompt")
    public ResponseEntity<List<Map<String, Object>>> selectPrompt(@RequestBody GptRequestDto gptRequestDto) {
        List<Map<String, Object>> result = gptService.prompt(gptRequestDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

//    public List<Map<String, Object>> selectPromptLambda(GptRequestDto gptRequestDto) {
//        return gptService.prompt(gptRequestDto);
//    }

}
