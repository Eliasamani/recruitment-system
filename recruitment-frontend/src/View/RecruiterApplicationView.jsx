import React from "react";

export default function RecruiterApplicationView({ applications, loading, error, fetchApplicationDetails, expandedApplications, handleStatusChange }) {
    if (loading) return <p style={styles.loadingContainer}>Loading applications...</p>;
    if (error) return <p style={styles.errorContainer}>{error}</p>;

    // Mapping of competence IDs to human-readable names
    const competenceMapping = {
        1: "Ticket Sales",
        2: "Lotteries",
        3: "Roller Coaster Operator"
    };

    return (
        <div style={styles.container}>
            <h2 style={styles.heading}>Recruiter Applications</h2>
            <table style={styles.table}>
                <thead>
                    <tr>
                        <th style={styles.tableHeader}>Applicant</th>
                        <th style={styles.tableHeader}>Status</th>
                        <th style={styles.tableHeader}>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {applications.map(app => (
                        <React.Fragment key={app.applicationId}>
                            {/* Main Row */}
                            <tr>
                                <td style={styles.tableCell}>{app.person.firstname} {app.person.lastname}</td>
                                <td style={styles.tableCell}>{app.status}</td>
                                <td style={styles.actionButtonsContainer}>
                                    <button
                                        style={styles.showDetailsBtn}
                                        onClick={() => fetchApplicationDetails(app.applicationId)}
                                    >
                                        {expandedApplications[app.applicationId] ? "Hide Details" : "Show Details"}
                                    </button>
                                    <button
                                        style={styles.approveBtn}
                                        onClick={() => handleStatusChange(app.applicationId, "APPROVED")}
                                    >
                                        Approve
                                    </button>
                                    <button
                                        style={styles.rejectBtn}
                                        onClick={() => handleStatusChange(app.applicationId, "REJECTED")}
                                    >
                                        Reject
                                    </button>
                                </td>
                            </tr>

                            {/* Expandable Details Row */}
                            {expandedApplications[app.applicationId] && (
                                <tr>
                                    <td colSpan="3" style={styles.detailsContainer}>
                                        <strong>Competence:</strong>
                                        {app.competences && app.competences.length > 0 ? (
                                            <ul style={styles.detailsList}>
                                                {app.competences.map(comp => (
                                                    <li key={comp.id} style={styles.detailsItem}>
                                                        {competenceMapping[comp.competence_type] || "Unknown Competence"} - {comp.experience} years
                                                    </li>
                                                ))}
                                            </ul>
                                        ) : (
                                            <p>No competences</p>
                                        )}

                                        <strong>Availability:</strong>
                                        {app.availabilities && app.availabilities.length > 0 ? (
                                            <ul style={styles.detailsList}>
                                                {app.availabilities.map(avail => (
                                                    <li key={avail.id} style={styles.detailsItem}>
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
}

// ✅ Styles
const styles = {
    container: {
        maxWidth: "900px",
        margin: "2rem auto",
        backgroundColor: "#ffffff",
        borderRadius: "10px",
        padding: "2rem",
        boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)",
        transition: "all 0.3s ease",
    },
    heading: {
        textAlign: "center",
        fontSize: "2rem",
        color: "#333",
    },
    table: {
        width: "100%",
        borderCollapse: "collapse",
        marginTop: "1rem",
        backgroundColor: "#ffffff",
    },
    tableHeader: {
        backgroundColor: "#007bff",
        color: "white",
        fontSize: "1rem",
        padding: "12px",
    },
    tableCell: {
        border: "1px solid #ddd",
        padding: "12px",
        textAlign: "left",
    },

    showDetailsBtn: {
        padding: "6px 12px",
        borderRadius: "5px",
        fontSize: "0.9rem",
        fontWeight: "600",
        cursor: "pointer",
        transition: "all 0.3s ease",
        backgroundColor: "#007bff",
        color: "white",
        border: "none",
    },
    showDetailsBtnHover: {
        backgroundColor: "#0056b3",
    },
    approveBtn: {
        backgroundColor: "#28a745",
        color: "white",
        border: "none",
        padding: "6px 12px",
        borderRadius: "5px",
        fontSize: "0.9rem",
        fontWeight: "600",
        cursor: "pointer",
        transition: "all 0.3s ease",
    },
    approveBtnHover: {
        backgroundColor: "#218838",
    },
    rejectBtn: {
        backgroundColor: "#dc3545",
        color: "white",
        border: "none",
        padding: "6px 12px",
        borderRadius: "5px",
        fontSize: "0.9rem",
        fontWeight: "600",
        cursor: "pointer",
        transition: "all 0.3s ease",
    },
    rejectBtnHover: {
        backgroundColor: "#c82333",
    },
    detailsContainer: {
        backgroundColor: "#f9f9f9",
        padding: "1rem",
        borderRadius: "5px",
        marginTop: "5px",
    },
    detailsList: {
        listStyle: "none",
        padding: "0",
    },
    detailsItem: {
        background: "#e9ecef",
        margin: "5px 0",
        padding: "8px",
        borderRadius: "5px",
    },
    loadingContainer: {
        textAlign: "center",
        padding: "2rem",
        fontSize: "1.5rem",
        color: "#666",
    },
    errorContainer: {
        textAlign: "center",
        padding: "2rem",
        fontSize: "1.5rem",
        color: "#d00",
    },
};

