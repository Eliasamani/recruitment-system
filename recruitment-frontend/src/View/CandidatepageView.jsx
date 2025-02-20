import React from 'react';
import '../App.css';

export default function CandidatepageView({
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
  // Renders status message based on application status
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
          <h3>Sorry, we chose to go with other candidates.</h3>
          <p>We will inform you when the new application period opens up.</p>
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

  // Render the candidate application form
  const renderApplicationForm = () => {
    // If status is not "unsent", show status message
    if (status !== 'unsent') {
      return renderStatusMessage();
    }

    return (
      <>
        {error && <div className="error-container"><p>{error}</p></div>}
        <form>
          <div className="form-group">
            <label>Select Area of Expertise</label>
            <select className="form-control" value={selectedCompetence} onChange={onCompetenceChange}>
              <option value="">Select Expertise</option>
              {competences.map((competence) => (
                <option key={competence.competence_id} value={competence.competence_id}>
                  {competence.name}
                </option>
              ))}
            </select>
            <input
              type="number"
              className="form-control"
              placeholder="Years of Experience"
              value={yearsOfExperience}
              onChange={onYearsExperienceChange}
            />
            <button type="button" className="submit-button" onClick={onAddExpertise}>
              Add Expertise
            </button>
          </div>
        </form>

        <div className="expertise-list">
          <h2>Your Expertise</h2>
          <ul>
            {expertise.map((expert, idx) => {
              const compName =
                competences.find((comp) => comp.competence_id === expert.competence_id)?.name ||
                'Competence not found';
              return <li key={idx}>{compName}: {expert.years_of_experience} years</li>;
            })}
          </ul>
        </div>

        <form>
          <div className="form-group">
            <label>Select Availability</label>
            <input
              type="date"
              className="form-control"
              value={fromDate}
              onChange={onFromDateChange}
            />
            <input
              type="date"
              className="form-control"
              value={toDate}
              onChange={onToDateChange}
            />
            <button type="button" className="submit-button" onClick={onAddAvailability}>
              Add Availability
            </button>
          </div>
        </form>

        <div className="availability-list">
          <h2>Your Availability</h2>
          <ul>
            {availability.map((period, idx) => (
              <li key={idx}>
                From: {period.from_date}, To: {period.to_date}
              </li>
            ))}
          </ul>
        </div>

        <div className="button-group">
          <button type="button" className="submit-button" onClick={onSubmit}>
            Submit Application
          </button>
          <button type="button" className="cancel-button" onClick={onCancel}>
            Cancel
          </button>
        </div>
      </>
    );
  };

  return (
    <div className="candidate-form">
      <div className="candidate-header">
        <h1>Welcome, {user.username}!</h1>
      </div>
      {renderApplicationForm()}
    </div>
  );
}
