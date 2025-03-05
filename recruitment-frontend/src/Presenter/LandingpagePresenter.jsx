import React from 'react';
import { useNavigate } from 'react-router-dom';
import LandingpageView from '../View/LandingpageView.jsx';

export default function LandingpagePresenter() {
    const navigate = useNavigate();

    /**
     * Navigates to the Sign In page.
     */
    const onLoginClick = () => {
        navigate('/signin');
    };

    /**
     * Navigates to the Sign Up page.
     */
    const onGetStartedClick = () => {
        navigate('/signup');
    };

    /**
     * Navigates to the About page.
     */    const onCompanyClick = () => {
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