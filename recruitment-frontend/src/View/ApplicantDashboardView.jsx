import React from 'react';
import '../App.css'; // Make sure you apply the updated CSS here

export default function ApplicantDashboardView({
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
}) {
  // Renders a status message when the application is not in "unsent" state
  const renderStatusMessage = () => {
    if (status === 'accepted') {
      return (
        <div className="success-message">
          <h3>Your application has been accepted!</h3>
          <p>We will contact you soon!</p>
        </div>
      );
    } else if (status === 'rejected') {
      return (
        <div className="error-message">
          <h3>Sorry, we chose to go with other applicants.</h3>
          <p>We will inform you when the next application period opens up.</p>
        </div>
      );
    } else if (
      status === 'unhandled' ||
      status === 'Missing Availability' ||
      status === 'Missing Competence'
    ) {
      return (
        <div className="success-message">
          <h3>Your application is under review!</h3>
          <p>We are processing your details and will get back to you shortly.</p>
        </div>
      );
    } else {
      return (
        <div className="error-message">
          <h3>Something went wrong.</h3>
          <p>Please log out and try again. If the issue persists, contact support.</p>
        </div>
      );
    }
  };

  // Renders the complete application form when status is "unsent"
  const renderApplicationForm = () => {
    if (status !== 'unsent') {
      return renderStatusMessage();
    }

    return (
      <form onSubmit={(e) => e.preventDefault()}>
        {error && (
          <div className="error-container">
            <p>{error}</p>
          </div>
        )}

        {/* Expertise Section */}
        <section className="form-section">
          <h2>Areas of Expertise</h2>
          <p className="section-info">
            Select your skill or domain and specify how many years of experience you have.
          </p>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="competenceSelect">Competence</label>
              <select
                id="competenceSelect"
                className="form-control"
                value={selectedCompetence}
                onChange={onCompetenceChange}
              >
                <option value="">Select an Expertise</option>
                {competences.map((comp) => (
                  <option key={comp.competence_id} value={comp.competence_id}>
                    {comp.name}
                  </option>
                ))}
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="yearsOfExperience">Years of Experience</label>
              <input
                type="number"
                id="yearsOfExperience"
                className="form-control"
                placeholder="e.g. 3"
                value={yearsOfExperience}
                onChange={onYearsExperienceChange}
              />
            </div>

            <div className="form-group add-button-group">
              <button type="button" className="add-button" onClick={onAddExpertise}>
                Add Expertise
              </button>
            </div>
          </div>

          {expertise.length > 0 && (
            <div className="expertise-list">
              <h3>Your Expertise</h3>
              <ul>
                {expertise.map((expert, idx) => {
                  const compName =
                    competences.find((c) => c.competence_id === expert.competence_id)?.name ||
                    'Competence not found';
                  return (
                    <li key={idx}>
                      <strong>{compName}</strong> – {expert.years_of_experience} years
                    </li>
                  );
                })}
              </ul>
            </div>
          )}
        </section>

        <hr />

        {/* Availability Section */}
        <section className="form-section">
          <h2>Availability</h2>
          <p className="section-info">
            Provide your start and end dates for when you are available to work.
          </p>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="fromDate">From</label>
              <input
                type="date"
                id="fromDate"
                className="form-control"
                value={fromDate}
                onChange={onFromDateChange}
              />
            </div>

            <div className="form-group">
              <label htmlFor="toDate">To</label>
              <input
                type="date"
                id="toDate"
                className="form-control"
                value={toDate}
                onChange={onToDateChange}
              />
            </div>

            <div className="form-group add-button-group">
              <button type="button" className="add-button" onClick={onAddAvailability}>
                Add Availability
              </button>
            </div>
          </div>

          {availability.length > 0 && (
            <div className="availability-list">
              <h3>Your Availability</h3>
              <ul>
                {availability.map((period, idx) => (
                  <li key={idx}>
                    <strong>From:</strong> {period.from_date} &mdash;{' '}
                    <strong>To:</strong> {period.to_date}
                  </li>
                ))}
              </ul>
            </div>
          )}
        </section>

        <div className="button-group">
          <button type="button" className="submit-button" onClick={onSubmit}>
            Submit Application
          </button>
          <button type="button" className="cancel-button" onClick={onCancel}>
            Cancel
          </button>
        </div>
      </form>
    );
  };

  return (
    <div className="applicant-page">
      <div className="applicant-header">
        <h1>Welcome, {user.username || 'Applicant'}!</h1>
        <p>Please complete your application below.</p>
      </div>

      <div className="applicant-body">{renderApplicationForm()}</div>
    </div>
  );
}
