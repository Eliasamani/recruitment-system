import React from 'react';
import Header from '../Components/Header.jsx'; // Import the reusable Header


const LandingPageView = ({ onLoginClick, onGetStartedClick, onCompanyClick }) => {
    return (
        <div className="landing-page">
            {/* Use the reusable Header component */}
            <Header />

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
