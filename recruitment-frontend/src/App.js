/**
 * App.js
 *
 * This is the main application component that configures the React Router
 * and defines all application routes.
 */

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';

// Import presenter components.
import LandingpagePresenter from './Presenter/LandingpagePresenter';
import SigninPresenter from './Presenter/SigninPresenter';
import SignupPresenter from './Presenter/SignupPresenter';
import AboutcompanyPresenter from './Presenter/AboutcompanyPresenter';
import ApplicantDashboardPresenter from './Presenter/ApplicantDashboardPresenter';
import RecruiterApplicationPresenter from './Presenter/RecruiterApplicationPresenter';
import ForgotPasswordPresenter from './Presenter/ForgotPasswordPresenter';
import ProfilePresenter from './Presenter/ProfilePresenter';  // <-- Import ProfilePresenter

// Import the authentication provider.
import { AuthProvider } from './Model/AuthContext';

/**
 * App component.
 *
 * Wraps the entire application within the AuthProvider and Router,
 * and defines the application routes.
 *
 * @returns {JSX.Element} The main application component.
 */
function App() {
    return (
        <AuthProvider>
            <Router>
                <div className="App">
                    <Routes>
                        <Route path="/" element={<LandingpagePresenter />} />
                        <Route path="/signin" element={<SigninPresenter />} />
                        <Route path="/signup" element={<SignupPresenter />} />
                        <Route path="/about" element={<AboutcompanyPresenter />} />
                        <Route path="/applicant/dashboard" element={<ApplicantDashboardPresenter />} />
                        <Route path="/recruiter/applications" element={<RecruiterApplicationPresenter />} />
                        <Route path="/forgot-password" element={<ForgotPasswordPresenter />} />
                        <Route path="/profile" element={<ProfilePresenter />} /> {/* New Profile route */}
                        <Route path="/unauthorized" element={<h1>Unauthorized</h1>} />
                    </Routes>
                </div>
            </Router>
        </AuthProvider>
    );
}

export default App;

