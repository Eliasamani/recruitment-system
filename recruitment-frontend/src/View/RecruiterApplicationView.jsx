import React from "react";

export default function RecruiterApplicationView  ({
    applications,
    loading,
    error,
    fetchApplicationDetails,
    expandedApplications,
    handleStatusChange
}) {
    if (loading) return <p className="loading-container">Loading applications...</p>;
    if (error) return <p className="error-container">{error}</p>;

    const competenceMapping = {
        1: "Ticket Sales",
        2: "Lotteries",
        3: "Roller Coaster Operator"
    };

    return (
        <div className="recruiter-container">
            <h2 className="heading">Recruiter Applications</h2>
            <table className="application-table">
                <thead>
                    <tr>
                        <th>Applicant</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {applications.map(app => (
                        <React.Fragment key={app.applicationId}>
                            <tr>
                                <td>{app.person.firstname} {app.person.lastname}</td>
                                <td>{app.status}</td>
                                <td>
                                    <button onClick={() => fetchApplicationDetails(app.applicationId)}>
                                        {expandedApplications[app.applicationId] ? "Hide Details" : "Show Details"}
                                    </button>
                                    <button onClick={() => handleStatusChange(app.applicationId, "APPROVED")}>
                                        Approve
                                    </button>
                                    <button onClick={() => handleStatusChange(app.applicationId, "REJECTED")}>
                                        Reject
                                    </button>
                                </td>
                            </tr>

                            {expandedApplications[app.applicationId] && (
                                <tr>
                                    <td colSpan="3">
                                        <strong>Competence:</strong>
                                        {app.competences && app.competences.length > 0 ? (
                                            <ul>
                                                {app.competences.map(comp => (
                                                    <li key={comp.id}>
                                                        {competenceMapping[comp.competence_type] || "Unknown Competence"} - {comp.experience} years
                                                    </li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <p>No competences</p>
                                        )}

                                        <strong>Availability:</strong>
                                        {app.availabilities && app.availabilities.length > 0 ? (
                                            <ul>
                                                {app.availabilities.map(avail => (
                                                    <li key={avail.id}>
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
