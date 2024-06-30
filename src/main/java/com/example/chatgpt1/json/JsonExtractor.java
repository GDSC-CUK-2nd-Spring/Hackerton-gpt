package com.example.chatgpt1.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
public class JsonExtractor {
    private final ObjectMapper objectMapper;

    public JsonExtractor() {
        this.objectMapper = new ObjectMapper();
    }

    public String extractContentJson(String jsonResponse) throws IOException {
        ChatCompletionResponse response = objectMapper.readValue(jsonResponse, ChatCompletionResponse.class);
        return objectMapper.writeValueAsString(response.getChoices().get(0).getMessage());
    }

    public static void main(String[] args) {
        String jsonResponse = "{\n" +
                "    \"id\": \"chatcmpl-9fdoat9EGtlDO2GenIIgRZeVo21Yd\",\n" +
                "    \"object\": \"chat.completion\",\n" +
                "    \"created\": 1719711524,\n" +
                "    \"model\": \"gpt-3.5-turbo-0125\",\n" +
                "    \"choices\": [\n" +
                "        {\n" +
                "            \"index\": 0,\n" +
                "            \"message\": {\n" +
                "                \"role\": \"assistant\",\n" +
                "                \"content\": \"Breakfast:\\n- 1 hard-boiled egg (70 calories)\\n- 1 small apple (77 calories)\\n- 1 cup black coffee (2 calories)\\nTotal: 149 calories\\n\\nLunch:\\n- Mixed green salad with 1 cup of lettuce, 1/2 cup cherry tomatoes, 1/4 cup cucumber, and 1 tablespoon of balsamic vinaigrette dressing (100 calories)\\n- 1 small grilled chicken breast (142 calories)\\nTotal: 242 calories\\n\\nDinner:\\n- 3 oz grilled salmon fillet (155 calories)\\n- 1/2 cup steamed broccoli (27 calories)\\n- 1/2 cup quinoa (111 calories)\\nTotal: 293 calories\\n\\nSnack\"\n" +
                "            },\n" +
                "            \"logprobs\": null,\n" +
                "            \"finish_reason\": \"length\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"usage\": {\n" +
                "        \"prompt_tokens\": 31,\n" +
                "        \"completion_tokens\": 150,\n" +
                "        \"total_tokens\": 181\n" +
                "    },\n" +
                "    \"system_fingerprint\": null\n" +
                "}";

        JsonExtractor extractor = new JsonExtractor();
        try {
            String contentJson = extractor.extractContentJson(jsonResponse);
            System.out.println("Extracted Content JSON:");
            System.out.println(contentJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
