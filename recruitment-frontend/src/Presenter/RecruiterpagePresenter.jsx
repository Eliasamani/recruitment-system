import React, { useEffect, useState } from 'react';
import RecruiterpageView from '../View/RecruiterpageView';

export default function RecruiterpagePresenter() {
    const [recruiter, setRecruiter] = useState(null); // Holds the recruiter information
    const [loading, setLoading] = useState(true); // Loading indicator
    const [error, setError] = useState(''); // Error message, if any

    useEffect(() => {
        // Fetch recruiter details from the backend
        const fetchRecruiter = async () => {
            try {
                setLoading(true); // Start loading
                const response = await fetch(`${process.env.REACT_APP_API_URL}/api/recruiter`, {
                    credentials: 'include', // Include cookies for authentication
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch recruiter details.');
                }

                const recruiterData = await response.json();
                setRecruiter(recruiterData); // Set the fetched recruiter data
            } catch (err) {
                console.error('Error fetching recruiter details:', err);
                setError('Failed to fetch recruiter details. Please try again later.');
            } finally {
                setLoading(false); // Stop loading
            }
        };

        fetchRecruiter(); // Call the fetch function when the component mounts
    }, []); // Empty dependency array ensures this runs once on mount

    return (
        <RecruiterpageView
            recruiter={recruiter}
            loading={loading}
            error={error}
        />
    );
}