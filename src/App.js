import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, useNavigate } from 'react-router-dom';
import './App.css';
import Recommend from './Recommend';
import { fetchMealPlan } from './api';

function Home() {
  const navigate = useNavigate();
  const [calorie, setCalorie] = useState('');

  const handleInputChange = (event) => {
    setCalorie(event.target.value);
  };

  const handleConfirm = () => {
    fetchMealPlan(calorie)
      .then(mealPlan => {
        console.log('mealPlan:', mealPlan);
        navigate('/recommend', { state: { mealPlan } });
      })
      .catch(error => {
        console.error('Error fetching meal plan:', error);
        alert('식단 데이터를 가져오는 중 오류가 발생했습니다.');
      });
  };

  
  return (
    <div className="App">
      <header className="App-header">
        <h1>뭐 먹을 텨</h1>
        <p>얼마나 먹을 텨 </p>
        <input 
          type="text" 
          placeholder="Calorie" 
          value={calorie}
          onChange={handleInputChange}
        />
        <button onClick={handleConfirm}>오키</button>
      </header>
    </div>
  );
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/recommend" element={<Recommend />} />
      </Routes>
    </Router>
  );
}

export default App;
