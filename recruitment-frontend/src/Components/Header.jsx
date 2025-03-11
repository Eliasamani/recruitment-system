import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Model/AuthContext.jsx';
import '../App.css'; // Import the main CSS file for styling

export default function Header() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    /** 
     * Navigates to the landing page when the logo is clicked.
     */
    const onLogoClick = () => {
        navigate('/');
    };

    /** 
     * Navigates to the About Us page.
     */
    const onCompanyClick = () => {
        navigate('/about');
    };

    /** 
     * Navigates to the appropriate home/dashboard page based on the user's role.
     * - Role 1: Recruiter dashboard
     * - Role 2: Applicant dashboard
     * - Default: Landing page
     */
    const onHomeClick = () => {
        if (user) {
            if (user.role === 1) {
                navigate('/recruiter/dashboard');
            } else if (user.role === 2) {
                navigate('/applicant/dashboard');
            } else {
                navigate('/');
            }
        } else {
            navigate('/');
        }
    };

    /** 
     * Logs out the user and redirects to the Sign-In page.
     * Uses asynchronous handling since logout() might be an async operation.
     */
    const onLogout = async () => {
        await logout();
        navigate('/signin');
    };

    return (
        <header className="landing-header header-flex">
            {/* Company logo with a clickable action to navigate home */}
            <div
                className="company-name clickable mr-20"
                onClick={onLogoClick}
            >
                HireMe
            </div>

            {/* Right-side section of the header with navigation and authentication options */}
            <div className="header-right">
                
                {/* About Us navigation link */}
                <div
                    className="company-name clickable mr-20"
                    onClick={onCompanyClick}
                >
                    About Us
                </div>

                {/* Conditional rendering based on user authentication */}
                {user ? (
                    <>
                        {/* Navigation buttons for authenticated users */}
                        <button className="home-btn header-btn" onClick={onHomeClick}>
                            Home
                        </button>
                        <button
                            className="profile-btn header-btn"
                            onClick={() => navigate('/profile')}
                        >
                            Profile
                        </button>
                        <button className="logout-btn header-logout-btn" onClick={onLogout}>
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        {/* Login and Sign-up buttons for non-authenticated users */}
                        <button
                            className="login-btn header-btn"
                            onClick={() => navigate('/signin')}
                        >
                            Login
                        </button>
                        <button
                            className="get-started-btn header-btn"
                            onClick={() => navigate('/signup')}
                        >
                            Sign up
                        </button>
                    </>
                )}
            </div>
        </header>
    );
}
