import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import LandingpagePresenter from './Presenter/LandingpagePresenter';
import SigninPresenter from './Presenter/SigninPresenter';
import SignupPresenter from './Presenter/SignupPresenter';
import AboutcompanyPresenter from './Presenter/AboutcompanyPresenter';
import CandidatepagePresenter from './Presenter/CandidatepagePresenter';
import RecruiterpagePresenter from './Presenter/RecruiterpagePresenter';

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/" element={<LandingpagePresenter />} />
                    <Route path="/signin" element={<SigninPresenter />} />
                    <Route path="/signup" element={<SignupPresenter />} />
                    <Route path="/about" element={<AboutcompanyPresenter />} />
                    <Route path="/candidate" element={<CandidatepagePresenter />} />
                    <Route path="/recruiter" element={<RecruiterpagePresenter />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
