import React from 'react';
import { useNavigate } from 'react-router-dom';
import LandingpageView from '../View/LandingpageView.jsx';

export default function LandingpagePresenter() {
    const navigate = useNavigate();

    // Navigate to the login page
    const onLoginClick = () => {
      navigate('/signin');
    };
  
    // Navigate to the signup page
    const onGetStartedClick = () => {
      navigate('/signup');
    };
  
    // Navigate to the about page when the company name is clicked
    const onCompanyClick = () => {
      navigate('/about');
    };
  
    return (
      <LandingpageView 
        onLoginClick={onLoginClick} 
        onGetStartedClick={onGetStartedClick}
        onCompanyClick={onCompanyClick}
      />
    );
  };