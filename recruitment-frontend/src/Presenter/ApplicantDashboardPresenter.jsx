import React, { useEffect, useState } from 'react';
import ApplicantDashboardView from '../View/ApplicantDashboardView';
import Header from '../Reusablecomponent/Header';

export default function ApplicantDashboardPresenter() {
    const [user, setUser] = useState(null); // Holds the user information
    const [loading, setLoading] = useState(true); // Loading indicator
    const [error, setError] = useState(''); // Error message, if any

    useEffect(() => {
        // Fetch user details from the backend
        const fetchUser = async () => {
            try {
                setLoading(true); // Start loading
                const response = await fetch(`${process.env.REACT_APP_API_URL}/api/user`, {
                    credentials: 'include', // Include cookies for authentication
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch user details.');
                }

                const userData = await response.json();
                setUser(userData); // Set the fetched user data
            } catch (err) {
                console.error('Error fetching user details:', err);
                setError('Failed to fetch user details. Please try again later.');
            } finally {
                setLoading(false); // Stop loading
            }
        };

        fetchUser(); // Call the fetch function when the component mounts
    }, []); // Empty dependency array ensures this runs once on mount

    return (
        <div>
            <Header />
            <ApplicantDashboardView
                user={user}
                loading={loading}
                error={error}
            />
        </div>
    );
}