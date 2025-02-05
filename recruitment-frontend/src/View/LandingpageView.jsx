import React from 'react';

const LandingPageView = ({ onLoginClick, onGetStartedClick, onCompanyClick }) => {
  return (
    <div className="landing-page">
      <header className="landing-header">
        <div className="header-left">
          {/* Replace the src below with the actual path to your logo image */}
          <img src="/path/to/logo.png" alt="Logo" className="logo" />
        </div>
        <div className="header-right">
          {/* Make the company name clickable to navigate to the About page */}
          <div 
            className="company-name" 
            onClick={onCompanyClick} 
            style={{ cursor: 'pointer' }}
          >
            HireMe
          </div>
          <button className="login-btn" onClick={onLoginClick}>Login</button>
          <button className="get-started-btn" onClick={onGetStartedClick}>Get Started</button>
        </div>
      </header>

      <main className="landing-body">
        <h1>Stand out with an impressive resume</h1>
        <p>
          Connect with top companies and gain valuable insights for your professional journey.
        </p>
        <h2>Unlock Your Potential with HireMe</h2>
        <p>Exciting Career Opportunities</p>
      </main>
    </div>
  );
};

export default LandingPageView;
