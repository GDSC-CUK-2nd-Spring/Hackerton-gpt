package com.example.chatgpt1.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.example.chatgpt1.config.GptConfig;
import com.example.chatgpt1.controller.GptController;
import com.example.chatgpt1.dto.GptRequestDto;
import com.example.chatgpt1.service.GptService;
import com.example.chatgpt1.service.Impl.GptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

//public class MyLambdaHandler implements RequestHandler<GptRequestDto, List<Map<String, Object>>> {
////    private final GptService gptService = new GptServiceImpl(new GptConfig());
////    @Override
////    public List<Map<String, Object>> handleRequest(GptRequestDto gptRequestDto, Context context) {
////        // GptService의 prompt 메서드 호출
////        List<Map<String, Object>> result = gptService.prompt(gptRequestDto);
////        return result;
////    }
//
//}

@Component

public class MyLambdaHandler implements RequestHandler<GptRequestDto, List<Map<String, Object>>> {


//    private final GptService gptService;
//
//    @Autowired
//    public MyLambdaHandler(GptService gptService) {
//        this.gptService = gptService;
//    }
//
//    @Override
//    public List<Map<String, Object>> handleRequest(GptRequestDto gptRequestDto, Context context) {
//        // Lambda 핸들러에서 수행할 비즈니스 로직을 여기에 작성합니다.
//        //return gptService.prompt(gptRequestDto);
//        GptController gptController = new GptController(gptRequestDto);
//        return GptController.selectPrompt(gptRequestDto);
//    }
    private GptController gptController;

    public MyLambdaHandler() {
        // Spring ApplicationContext 초기화

        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GptConfig.class)) {
            this.gptController = context.getBean(GptController.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Spring context", e);
        }
    }

    @Override
    public List<Map<String, Object>> handleRequest(GptRequestDto gptRequestDto, Context context) {
        // Lambda 핸들러에서 GptController를 사용
        ResponseEntity<List<Map<String, Object>>> response = gptController.selectPrompt(gptRequestDto);
        return response.getBody();
    }
}