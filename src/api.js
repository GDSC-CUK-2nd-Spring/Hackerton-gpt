import axios from 'axios';

export const fetchMealPlan = async (calorie) => {
  try {
    const requestBody = {
      model: "gpt-3.5-turbo",
      temperature: 0.8,
      messages: [
        {
          role: "user",
          content: `하루에 ${calorie} 칼로리를 섭취하는 상세 식단을 만들어 주세요. 각 식사에 대한 설명과 칼로리 정보를 포함해 주세요.`
        }
      ]
    };

    const response = await axios.post('http://localhost:8080/api/v1/chatGpt/prompt', requestBody, {
      headers: {
        'Content-Type': 'application/json',
      }
    });

    console.log(response.data[0].choices[0].message.content);  // 응답 데이터 구조를 확인합니다
    return response.data[0].choices[0].message.content;
    
  } catch (error) {
    console.error('Error fetching meal plan:', error);
    throw error;
  }
};
