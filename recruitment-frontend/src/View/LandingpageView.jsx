/**
 * LandingPageView.jsx
 *
 * This component renders the user interface for the Landing Page.
 * It displays a Header and main content, including a headline and descriptive text.
 *
 * Following Oracle’s Java coding conventions for naming, formatting, and documentation.
 */

import React from 'react';
import Header from '../Components/Header.jsx'; // Import the reusable Header component

const LandingPageView = ({
    onLoginClick,
    onGetStartedClick,
    onCompanyClick,
}) => (
    <div className="landing-page">
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

export default LandingPageView;
