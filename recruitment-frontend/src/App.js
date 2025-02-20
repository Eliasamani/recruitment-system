import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import LandingpagePresenter from './Presenter/LandingpagePresenter';
import SigninPresenter from './Presenter/SigninPresenter';
import SignupPresenter from './Presenter/SignupPresenter';
import AboutcompanyPresenter from './Presenter/AboutcompanyPresenter';  
import ApplicantDashboardPresenter from './Presenter/ApplicantDashboardPresenter';
import RecruiterDashboardPresenter from './Presenter/RecruiterDashboardPresenter';
import RecruiterApplicationPresenter from './Presenter/RecruiterApplicationPresenter';
import { AuthProvider } from './AuthContext';

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
                    <Route path="/recruiter/dashboard" element={<RecruiterDashboardPresenter />} />
                    <Route path="/recruiter/applications" element={<RecruiterApplicationPresenter />} />
                    <Route path="/unauthorized" element={<h1>Unauthorized</h1> } />
                </Routes>
            </div>
        </Router>
        </AuthProvider>
    );
}

export default App;