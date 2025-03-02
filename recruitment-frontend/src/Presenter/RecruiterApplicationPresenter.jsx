import React, { useState, useEffect } from "react";
import RecruiterApplicationView from "../View/RecruiterApplicationView";
import { fetchApplications, fetchApplicationDetails, updateApplicationStatus } from "../Model/RecruiterApplicationModel";

export default function RecruiterApplicationPresenter  () {
    const [applications, setApplications] = useState([]);
    const [expandedApplications, setExpandedApplications] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        fetchApplications()
            .then(setApplications)
            .catch(err => setError(err.message))
            .finally(() => setLoading(false));
    }, []);

    const handleFetchApplicationDetails = async (applicationId) => {
        try {
            const details = await fetchApplicationDetails(applicationId);
            setExpandedApplications(prev => ({
                ...prev,
                [applicationId]: !prev[applicationId] ? details : null
            }));
        } catch (err) {
            setError(err.message);
        }
    };

    const handleStatusChange = async (applicationId, status) => {
        try {
            await updateApplicationStatus(applicationId, status);
            setApplications(prev =>
                prev.map(app => (app.applicationId === applicationId ? { ...app, status } : app))
            );
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <RecruiterApplicationView
            applications={applications}
            loading={loading}
            error={error}
            fetchApplicationDetails={handleFetchApplicationDetails}
            expandedApplications={expandedApplications}
            handleStatusChange={handleStatusChange}
        />
    );
};

