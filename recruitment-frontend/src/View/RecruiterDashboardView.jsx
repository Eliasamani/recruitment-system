import React from 'react';

export default function RecruiterDashboardView({ recruiter, loading, error, onLogout, onManageApplications }) {
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

    if (!recruiter) {
        return (
            <div className="no-recruiter-container" style={{ textAlign: 'center' }}>
                <h2>No Recruiter Information Available</h2>
                <p>Please log in again to access your account.</p>
            </div>
        );
    }

    return (
        <div className="recruiter-page">
            <header className="recruiter-header">
                <h1>
                    Welcome, {recruiter.firstName} {recruiter.lastName}!
                </h1>
            </header>
            <main className="recruiter-body">
                <section className="recruiter-info">
                    <h2>Your Account Details</h2>
                    <p>
                        <strong>Email:</strong> {recruiter.email}
                    </p>
                    {/* Add more recruiter-specific information here */}
                </section>

                <section className="dashboard-content">
                    <h2>Dashboard Overview</h2>
                    <p>
                        This is your dashboard where you can manage job postings, applications, and more.
                    </p>
                    <button
                        onClick={onManageApplications}
                        style={{
                            padding: '8px 16px',
                            marginTop: '16px',
                            cursor: 'pointer',
                            backgroundColor: '#2196F3',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '4px'
                        }}
                    >
                        Manage Applications
                    </button>
                </section>
            </main>
        </div >
    );
}
