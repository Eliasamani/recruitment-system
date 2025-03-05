/**
 * ApplicantDashboardView.jsx
 *
 * This component renders the Applicant Dashboard user interface.
 * It receives data and event handlers via props.
 */

import React from 'react';
import '../App.css'; // Make sure you apply the updated CSS here
import Header from '../Components/Header';

const ApplicantDashboardView = ({
    user,
    competences,
    status,
    error,
    selectedCompetence,
    yearsOfExperience,
    expertise,
    availability,
    fromDate,
    toDate,
    onCompetenceChange,
    onYearsExperienceChange,
    onFromDateChange,
    onToDateChange,
    onAddExpertise,
    onAddAvailability,
    onSubmit,
    onCancel,
}) => (
        <div>
            <Header />
            <div className="applicant-page">
                <div className="applicant-header">
                    <h1>Welcome, {user?.username || 'Applicant'}!</h1>
                    <p>Please complete your application below.</p>
                </div>
                <div className="applicant-body">
                    <form onSubmit={(e) => e.preventDefault()}>
                        {error && <div className="error-container"><p>{error}</p></div>}

                        {/* Expertise Section */}
                        <section className="form-section">
                            <h2>Areas of Expertise</h2>
                            <div className="form-group">
                                <label>Competence</label>
                                <select value={selectedCompetence} onChange={onCompetenceChange}>
                                    <option value="">Select an Expertise</option>
                                    {competences.map((comp) => (
                                        <option key={comp.competence_id} value={comp.competence_id}>
                                            {comp.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            <div className="form-group">
                                <label>Years of Experience</label>
                                <input
                                    type="number"
                                    value={yearsOfExperience}
                                    onChange={onYearsExperienceChange}
                                    placeholder="e.g. 3"
                                />
                            </div>
                            <button type="button" onClick={onAddExpertise}>Add Expertise</button>

                            {expertise.length > 0 && (
                                <ul>
                                    {expertise.map((exp, idx) => (
                                        <li key={idx}>
                                            {competences.find(c => c.competence_id === exp.competence_id)?.name || 'Unknown'} 
                                            – {exp.years_of_experience} years
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </section>

                        {/* Availability Section */}
                        <section className="form-section">
                            <h2>Availability</h2>
                            <div className="form-group">
                                <label>From</label>
                                <input type="date" value={fromDate} onChange={onFromDateChange} />
                            </div>
                            <div className="form-group">
                                <label>To</label>
                                <input type="date" value={toDate} onChange={onToDateChange} />
                            </div>
                            <button type="button" onClick={onAddAvailability}>Add Availability</button>

                            {availability.length > 0 && (
                                <ul>
                                    {availability.map((period, idx) => (
                                        <li key={idx}>
                                            <strong>From:</strong> {period.from_date} – <strong>To:</strong> {period.to_date}
                                        </li>
                                    ))}
                                </ul>
                            )}
                        </section>

                        <button type="button" onClick={onSubmit}>Submit Application</button>
                        <button type="button" onClick={onCancel}>Cancel</button>
                    </form>
                </div>
            </div>
        </div>
  );

export default ApplicantDashboardView;