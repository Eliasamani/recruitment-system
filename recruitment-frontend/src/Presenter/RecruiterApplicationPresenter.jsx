import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import RecruiterApplicationView from "../View/RecruiterApplicationView";
import Header from '../Components/Header';
import { useAuth } from "../AuthContext";

export default function RecruiterApplicationPresenter() {
    const { user, loading: authLoading } = useAuth();
    const navigate = useNavigate();

    // Local state for applications
    const [applications, setApplications] = useState([]);
    const [appLoading, setAppLoading] = useState(true);
    const [error, setError] = useState("");
    const [expandedApplications, setExpandedApplications] = useState({});

    // Check authentication: if auth is done and no valid recruiter, redirect
    useEffect(() => {
        if (!authLoading) {
            if (!user) {
                navigate("/signin");
            } else if (user.role !== 1) {
                navigate("/unauthorized");
            }
        }
    }, [authLoading, user, navigate]);

    // Fetch applications only if the user is authenticated and is a recruiter
    useEffect(() => {
        const fetchApplications = async () => {
            try {
                setAppLoading(true);
                const response = await fetch(
                    `${process.env.REACT_APP_API_URL}/api/recruiter/applications`,
                    { credentials: "include" }
                );
                if (!response.ok) {
                    throw new Error("Unable to retrieve applications.");
                }
                const data = await response.json();
                setApplications(data);
            } catch (err) {
                console.error("Error fetching applications:", err);
                setError("Could not fetch applications. Please try again later.");
            } finally {
                setAppLoading(false);
            }
        };

        if (!authLoading && user && user.role === 1) {
            fetchApplications();
        }
    }, [authLoading, user]);

    const fetchApplicationDetails = async (applicationId) => {
        if (expandedApplications[applicationId]) {
            setExpandedApplications(prev => ({ ...prev, [applicationId]: false }));
            return;
        }
        try {
            const response = await fetch(
                `${process.env.REACT_APP_API_URL}/api/recruiter/applications/${applicationId}`,
                { credentials: "include" }
            );
            if (!response.ok) {
                throw new Error("Failed to fetch application details.");
            }
            const detailedApp = await response.json();
            setApplications(prev =>
                prev.map(app =>
                    app.applicationId === applicationId ? { ...app, ...detailedApp } : app
                )
            );
            setExpandedApplications(prev => ({ ...prev, [applicationId]: true }));
        } catch (error) {
            console.error("Error fetching application details:", error);
        }
    };

    const handleStatusChange = async (id, status) => {
        try {
            const response = await fetch(
                `${process.env.REACT_APP_API_URL}/api/recruiter/applications/${id}/update-status`,
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({ status }),
                    credentials: "include",
                }
            );
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

    return (
        <div>
            <Header />
            <RecruiterApplicationView
                recruiter={user}
                applications={applications}
                loading={authLoading || appLoading}
                error={error}
                fetchApplicationDetails={fetchApplicationDetails}
                expandedApplications={expandedApplications}
                handleStatusChange={handleStatusChange}
            />
        </div>
    );
}
