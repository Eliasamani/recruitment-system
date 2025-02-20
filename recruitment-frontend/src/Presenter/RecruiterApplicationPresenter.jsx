import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import RecruiterApplicationView from "../View/RecruiterApplicationView";

export default function RecruiterApplicationPresenter() {
    const navigate = useNavigate();
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [expandedApplications, setExpandedApplications] = useState({});

    useEffect(() => {
        const fetchApplications = async () => {
            try {
                setLoading(true);
                const response = await fetch(`${process.env.REACT_APP_API_URL}/api/recruiter/applications`, {
                    credentials: "include",
                });

                if (!response.ok) {
                    throw new Error("Unable to retrieve applications.");
                }

                const data = await response.json();
                setApplications(data);
            } catch (err) {
                console.error("Error fetching applications:", err);
                setError("Could not fetch applications. Please try again later.");
            } finally {
                setLoading(false);
            }
        };

        fetchApplications();
    }, []);

    const fetchApplicationDetails = async (applicationId) => {
        if (expandedApplications[applicationId]) {
            setExpandedApplications(prev => ({ ...prev, [applicationId]: false }));
            return;
        }

        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/recruiter/applications/${applicationId}`, {
                credentials: "include",
            });

            if (!response.ok) {
                throw new Error("Failed to fetch application details.");
            }

            const detailedApp = await response.json();

            setApplications(prev =>
                prev.map(app => (app.applicationId === applicationId ? { ...app, ...detailedApp } : app))
            );

            setExpandedApplications(prev => ({ ...prev, [applicationId]: true }));
        } catch (error) {
            console.error("Error fetching application details:", error);
        }
    };

    const handleStatusChange = async (id, status) => {
        try {
            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/recruiter/applications/${id}/update-status`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ status }),
                credentials: "include",
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Failed to update status");
            }

            setApplications(prev =>
                prev.map(app => (app.applicationId === id ? { ...app, status } : app))
            );
        } catch (error) {
            console.error("Error updating application status:", error);
            setError("Failed to update application status.");
        }
    };

    const handleBack = () => {
        navigate("/recruiter/dashboard");
    };

    return (
        <RecruiterApplicationView
            applications={applications}
            loading={loading}
            error={error}
            fetchApplicationDetails={fetchApplicationDetails}
            expandedApplications={expandedApplications}
            handleStatusChange={handleStatusChange}
            onBack={handleBack}
        />
    );
}
