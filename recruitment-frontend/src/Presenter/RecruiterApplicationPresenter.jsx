import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Header from "../Components/Header";
import RecruiterDashboardView from "../View/RecruiterDashboardView";
import RecruiterApplicationView from "../View/RecruiterApplicationView";
import { useAuth } from "../Model/AuthContext.jsx";

/**
 * RecruiterPresenter component.
 *
 * This component combines both the dashboard overview and the applications management
 * functionalities into a single module. It allows the recruiter to toggle between
 * viewing their account details and managing job applications.
 *
 * @component
 */
export default function RecruiterApplicationPresenter() {
  const { user, loading, logout } = useAuth();
  const navigate = useNavigate();

  // State for toggling between views ("dashboard" or "applications")
  const [activeView, setActiveView] = useState("dashboard");

  // State for applications
  const [applications, setApplications] = useState([]);
  const [appLoading, setAppLoading] = useState(true);
  const [appError, setAppError] = useState("");
  const [expandedApplications, setExpandedApplications] = useState({});

  /**
   * Checks authentication and recruiter role.
   */
  useEffect(() => {
    if (!loading) {
      if (!user) {
        navigate("/signin");
      } else if (user.role !== 1) {
        navigate("/unauthorized");
      }
    }
  }, [loading, user, navigate]);

  /**
   * Fetches applications when the recruiter switches to the "applications" view.
   */
  useEffect(() => {
    if (!loading && user && user.role === 1 && activeView === "applications") {
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
          setAppError("Could not fetch applications. Please try again later.");
        } finally {
          setAppLoading(false);
        }
      };
      fetchApplications();
    }
  }, [loading, user, activeView]);

  /**
   * Toggles the detailed view of a specific application.
   *
   * @param {number} applicationId - The ID of the application to toggle.
   */
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

  /**
   * Updates the status of an application.
   *
   * @param {number} id - The application ID.
   * @param {string} status - The new status.
   */
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
      setAppError("Failed to update application status.");
    }
  };

  /**
   * Handles recruiter logout.
   */
  const handleLogout = async () => {
    await logout();
    navigate('/signin');
  };

  /**
   * Changes the active view.
   *
   * @param {string} view - The view to switch to ("dashboard" or "applications").
   */
  const handleViewChange = (view) => {
    setActiveView(view);
  };

  return (
    <div>
      <Header />
      <div style={{ padding: "1rem", textAlign: "center" }}>
        <button onClick={() => handleViewChange("dashboard")} style={{ marginRight: "1rem" }}>
          Dashboard
        </button>
        <button onClick={() => handleViewChange("applications")}>
          Manage Applications
        </button>
      </div>
      {activeView === "dashboard" && (
        <RecruiterDashboardView
          recruiter={user}
          loading={loading}
          error={!user && !loading ? "User not found or unauthorized" : null}
          onLogout={handleLogout}
          onManageApplications={() => handleViewChange("applications")}
        />
      )}
      {activeView === "applications" && (
        <RecruiterApplicationView
          applications={applications}
          loading={loading || appLoading}
          error={appError}
          fetchApplicationDetails={fetchApplicationDetails}
          expandedApplications={expandedApplications}
          handleStatusChange={handleStatusChange}
        />
      )}
    </div>
  );
}
