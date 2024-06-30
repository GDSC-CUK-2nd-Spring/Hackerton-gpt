import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import './Recommend.css';

function Recommend() {
  const location = useLocation();
  const navigate = useNavigate();
  const { mealPlan } = location.state || {};

  // 응답 받은 데이터를 파싱하여 mealPlan 객체로 변환하는 함수
  const parseMealPlan = (content) => {
    const lines = content.split('\n');
    const meals = {};
    let currentMeal = null;
    let currentItems = [];
    let currentTotalCalories = null;
    let overallTotalCalories = null;

    lines.forEach(line => {
      if (line.includes('총 칼로리:')) {
        // 전체 총 칼로리 정보를 추출
        overallTotalCalories = line.match(/\d+/)[0] + ' 칼로리';
        return;
      }
      if (line.includes(':') && !line.includes('-')) {
        if (currentMeal) {
          meals[currentMeal] = {
            title: currentMeal,
            items: currentItems,
            totalCalories: currentTotalCalories
          };
        }
        currentMeal = line.split(':')[0];
        currentItems = [];
        currentTotalCalories = null;
      } else if (line.trim() && line.includes('-')) {
        currentItems.push(line.replace('-', '').trim());
      } else if (line.trim().toLowerCase().includes('total:')) {
        currentTotalCalories = line.match(/\d+/)[0] + ' 칼로리';
      }
    });

    if (currentMeal) {
      meals[currentMeal] = {
        title: currentMeal,
        items: currentItems,
        totalCalories: currentTotalCalories
      };
    }

    return {
      intro: content.split('\n')[0], // 첫 번째 줄은 intro로 간주
      meals: meals,
      overallTotalCalories: overallTotalCalories
    };
  };

  const parsedMealPlan = mealPlan ? parseMealPlan(mealPlan) : null;

  const handleCopy = () => {
    if (!parsedMealPlan) return;

    const mealText = `${parsedMealPlan.intro}\n\n` +
      Object.values(parsedMealPlan.meals).map(meal => 
        `${meal.title} \n${meal.items.join('\n')}`
      ).join('\n\n') +
      `\n\n총 칼로리: ${parsedMealPlan.overallTotalCalories}`;
      
    navigator.clipboard.writeText(mealText)
      .then(() => alert('추천 식단이 복사되었습니다.'))
      .catch(err => console.error('클립보드 복사 실패:', err));
  };

  const handleShare = () => {
    const shareableLink = window.location.href;
    navigator.clipboard.writeText(shareableLink)
      .then(() => alert('링크가 복사되었습니다.'))
      .catch(err => console.error('클립보드 복사 실패:', err));
  };

  const handleHome = () => {
    navigate('/');
  };

  const handleRetry = () => {
    navigate('/');
  };

  if (!parsedMealPlan) {
    return <div>Loading...</div>;
  }

  return (
    <div className="Recommend">
      <div className="Recommendation-result">
        <div className="top-button-group">
          <button onClick={handleHome}>Home</button>
          <h2>이거 먹을텨? ({parsedMealPlan.overallTotalCalories})</h2>
          <button onClick={handleRetry}>다시 하기</button>
        </div>
        
        {Object.values(parsedMealPlan.meals).map((meal, index) => (
          <div key={index} className="meal-section">
            <h2>{meal.title} {meal.totalCalories && `(${meal.totalCalories})`}</h2>
            <ul>
              {meal.items.map((item, idx) => (
                <li key={idx}>{item}</li>
              ))}
            </ul>
          </div>
        ))}
        <div className="button-group">
          <button onClick={handleCopy}>복사</button>
          <button onClick={handleShare}>공유하기</button>
        </div>
      </div>
    </div>
  );
}

export default Recommend;
