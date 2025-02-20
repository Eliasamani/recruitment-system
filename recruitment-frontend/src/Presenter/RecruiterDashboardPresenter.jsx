// RecruiterDashboardPresenter.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import RecruiterDashboardView from '../View/RecruiterDashboardView';

export default function RecruiterDashboardPresenter() {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            const response = await fetch(process.env.REACT_APP_API_URL + '/api/auth/logout', { method: 'POST', credentials: 'include' });
            if (response.ok) {
                navigate('/signin');
            } else {
                console.error('Logout failed');
            }
        } catch (err) {
            console.error('Error during logout:', err);
        }
    };

    const handleManageApplications = () => {
        navigate('/recruiter/applications');
    };

    // Dummy recruiter data for MVP testing
    const dummyRecruiter = {
        firstName: 'John',
        lastName: 'Doe',
        email: 'john.doe@example.com'
    };

    return (
        <RecruiterDashboardView
            recruiter={dummyRecruiter}
            loading={false}
            error={null}
            onLogout={handleLogout}
            onManageApplications={handleManageApplications}
        />
    );
}
