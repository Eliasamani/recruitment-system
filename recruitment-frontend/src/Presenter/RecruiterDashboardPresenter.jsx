import React from 'react';
import { useNavigate } from 'react-router-dom';
import RecruiterDashboardView from '../View/RecruiterDashboardView';
import Header from '../Reusablecomponent/Header';
import { useAuth } from '../AuthContext';

export default function RecruiterDashboardPresenter() {
    const { user, loading, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await logout();
        navigate('/signin');
    };

    const handleManageApplications = () => {
        navigate('/recruiter/applications');
    };

    // Once loading is finished:
    if (!loading) {
        if (!user) {
            // No user is logged in; redirect to sign in.
            navigate('/signin');
            return null;
        } else if (user.role !== 1) {
            // User is logged in but not a recruiter; redirect to unauthorized.
            navigate('/unauthorized');
            return null;
        }
    }

    return (
        <div>
            <Header />
            <RecruiterDashboardView
                recruiter={user}
                loading={loading}
                error={!user && !loading ? 'User not found or unauthorized' : null}
                onLogout={handleLogout}
                onManageApplications={handleManageApplications}
            />
        </div>
    );
}
