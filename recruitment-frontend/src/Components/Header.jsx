import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../Model/AuthContext';

export default function Header() {
    const { user, logout } = useAuth();
    const navigate = useNavigate();

    // Navigate to the landing page
    const onLogoClick = () => {
        navigate('/');
    };

    // Navigate to the About page
    const onCompanyClick = () => {
        navigate('/about');
    };

    // Navigate to the appropriate home/dashboard based on user role
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

    // Handle logout and redirect to landing page
    const onLogout = async () => {
        await logout();
        navigate('/signin');
    };

    return (
        <header className="landing-header" style={styles.header}>
            <div
                className="company-name"
                onClick={onLogoClick}
                style={{ cursor: 'pointer', marginRight: '20px' }}
            >
                HireMe
            </div>
            <div className="header-right" style={styles.headerRight}>
                <div
                    className="company-name"
                    onClick={onCompanyClick}
                    style={{ cursor: 'pointer', marginRight: '20px' }}
                >
                    About Us
                </div>
                {user ? (
                    <>
                        <button className="home-btn" onClick={onHomeClick} style={styles.button}>
                            Home
                        </button>
                        <button className="logout-btn" onClick={onLogout} style={styles.logoutButton}>
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        <button className="login-btn" onClick={() => navigate('/signin')} style={styles.button}>
                            Login
                        </button>
                        <button className="get-started-btn" onClick={() => navigate('/signup')} style={styles.button}>
                            Sign up
                        </button>
                    </>
                )}
            </div>
        </header>
    );
}

const styles = {
    header: {
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        padding: '1rem 2rem',
        backgroundColor: '#f8f9fa',
        boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
    },
    logo: {
        height: '40px',
    },
    headerRight: {
        display: 'flex',
        alignItems: 'center',
    },
    button: {
        marginRight: '10px',
        padding: '8px 16px',
        fontSize: '1rem',
        cursor: 'pointer',
        border: 'none',
        borderRadius: '4px',
        backgroundColor: '#007bff',
        color: '#fff',
    },
    logoutButton: {
        marginRight: '10px',
        padding: '8px 16px',
        fontSize: '1rem',
        cursor: 'pointer',
        border: 'none',
        borderRadius: '4px',
        backgroundColor: '#ff0000',
        color: '#fff',
    }
};
