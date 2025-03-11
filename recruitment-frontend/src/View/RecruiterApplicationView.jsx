import React from "react";
import '../App.css';


/**
 * RecruiterApplicationView component.
 *
 * This component renders the recruiter applications view. It displays a table of job applications,
 * each with options to show/hide details, approve, or reject the application.
 * It also handles loading and error states.
 *
 * @constant
 * @param {Object[]} applications - Array of application objects.
 * @param {boolean} loading - Indicates whether the applications are still loading.
 * @param {string} error - Error message to display, if any.
 * @param {function} fetchApplicationDetails - Callback to fetch detailed data for an application.
 * @param {Object} expandedApplications - Map indicating which application details are expanded.
 * @param {function} handleStatusChange - Callback to update the status of an application.
 * @param {function} onBack - Callback for navigating back (if applicable).
 * @returns {JSX.Element} The recruiter applications view.
 */

const RecruiterApplicationView = ({
    applications,
    loading,
    error,
    fetchApplicationDetails,
    expandedApplications,
    handleStatusChange,
    onBack,
}) => {
    if (loading) return <p className="recruiter-loading">Loading applications...</p>;
    if (error) return <p className="recruiter-error">{error}</p>;

    const competenceMapping = {
        1: "Ticket Sales",
        2: "Lotteries",
        3: "Roller Coaster Operator"
    };

    return (
        <div className="recruiter-app-container">
            <h2 className="recruiter-app-heading">Recruiter Applications</h2>
            <table className="recruiter-app-table">
                <thead>
                    <tr>
                        <th className="recruiter-app-th">Applicant</th>
                        <th className="recruiter-app-th">Status</th>
                        <th className="recruiter-app-th">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {applications.map(app => (
                        <React.Fragment key={app.applicationId}>
                            <tr>
                                <td className="recruiter-app-td">
                                    {app.person.firstname} {app.person.lastname}
                                </td>
                                <td className="recruiter-app-td">{app.status}</td>
                                <td className="recruiter-app-actions">
                                    <button
                                        className="show-details-btn"
                                        onClick={() => fetchApplicationDetails(app.applicationId)}
                                    >
                                        {expandedApplications[app.applicationId] ? "Hide Details" : "Show Details"}
                                    </button>
                                    <button
                                        className="approve-btn"
                                        onClick={() => handleStatusChange(app.applicationId, "APPROVED")}
                                    >
                                        Approve
                                    </button>
                                    <button
                                        className="reject-btn"
                                        onClick={() => handleStatusChange(app.applicationId, "REJECTED")}
                                    >
                                        Reject
                                    </button>
                                </td>
                            </tr>

                            {expandedApplications[app.applicationId] && (
                                <tr>
                                    <td colSpan="3" className="recruiter-app-details">
                                        <strong>Competence:</strong>
                                        {app.competences && app.competences.length > 0 ? (
                                            <ul className="recruiter-details-list">
                                                {app.competences.map(comp => (
                                                    <li key={comp.id} className="recruiter-details-item">
                                                        {competenceMapping[comp.competence_type] || "Unknown"} - {comp.experience} years
                                                    </li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <p>No competences</p>
                                        )}

                                        <strong>Availability:</strong>
                                        {app.availabilities && app.availabilities.length > 0 ? (
                                            <ul className="recruiter-details-list">
                                                {app.availabilities.map(avail => (
                                                    <li key={avail.id} className="recruiter-details-item">
                                                        {avail.fromDate} - {avail.toDate}
                                                    </li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <p>No availability</p>
                                        )}
                                    </td>
                                </tr>
                            )}
                        </React.Fragment>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

export default RecruiterApplicationView;
