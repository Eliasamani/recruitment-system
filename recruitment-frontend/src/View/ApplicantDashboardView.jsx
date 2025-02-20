import React from 'react';

export default function ApplicantDashboardView({ user, loading, error }) {
    if (loading) {
        return (
            <div className="loading-container">
                <h2>Loading your dashboard...</h2>
            </div>
        );
    }

    if (error) {
        return (
            <div className="error-container" style={{ color: 'red', textAlign: 'center' }}>
                <h2>Error</h2>
                <p>{error}</p>
            </div>
        );
    }

    if (!user) {
        return (
            <div className="no-user-container" style={{ textAlign: 'center' }}>
                <h2>No User Information Available</h2>
                <p>Please log in again to access your account.</p>
            </div>
        );
    }

    return (
        <div className="applicant-page">
            <header className="applicant-header">
                <h1>Welcome, {user.firstName} {user.lastName}!</h1>
            </header>
            <main className="applicant-body">
                <section className="user-info">
                    <h2>Your Account Details</h2>
                    <p><strong>Email:</strong> {user.email}</p>
                    {/* Add more user-specific information here */}
                </section>

                <section className="dashboard-content">
                    <h2>Dashboard Overview</h2>
                    <p>This is your dashboard where you can view and manage your account details.</p>
                    {/* Add additional dashboard content or navigation links here */}
                    <ul>
                        <li><a href="#profile">Edit Profile</a></li>
                        <li><a href="#resumes">Manage Resumes</a></li>
                        <li><a href="#applications">View Job Applications</a></li>
                    </ul>
                </section>
            </main>
        </div>
    );
}