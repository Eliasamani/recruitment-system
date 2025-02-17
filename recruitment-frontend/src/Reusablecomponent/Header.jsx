import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function Header() {
    const navigate = useNavigate();

    // Navigate to the landing page
    const onLogoClick = () => {
        navigate('/');
    };

    // Navigate to the about page
    const onCompanyClick = () => {
        navigate('/about');
    };

    return (
        <header className="landing-header">
            <div className="header-left">
                {/* Make the logo clickable to navigate back to the landing page */}
                <img
                    src="/path/to/logo.png"
                    alt="Logo"
                    className="logo"
                    onClick={onLogoClick}
                    style={{ cursor: 'pointer' }}
                />
            </div>
            <div className="header-right">
                {/* Make the company name clickable to navigate to the About page */}
                <div
                    className="company-name"
                    onClick={onCompanyClick}
                    style={{ cursor: 'pointer', marginRight: '20px' }}
                >
                    HireMe
                </div>
                <button className="login-btn" onClick={() => navigate('/signin')}>Login</button>
                <button className="get-started-btn" onClick={() => navigate('/signup')}>Get Started</button>
            </div>
        </header>
    );
};

