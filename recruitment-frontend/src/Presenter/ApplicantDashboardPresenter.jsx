import React, { useState, useEffect } from 'react';
import ApplicantDashboardView from '../View/ApplicantDashboardView';
import { validateApplication, competences, savedApplicationData } from '../Model/ApplicantDashboardModel';
export default function ApplicantDashboardPresenter() {
  // Simulated user state (no back-end call)
  const [user, setUser] = useState(null);
  const [status, setStatus] = useState('unsent');
  const [error, setError] = useState('');

  // Form state
  const [selectedCompetence, setSelectedCompetence] = useState('');
  const [yearsOfExperience, setYearsOfExperience] = useState('');
  const [expertise, setExpertise] = useState([]);
  const [fromDate, setFromDate] = useState('');
  const [toDate, setToDate] = useState('');
  const [availability, setAvailability] = useState([]);

  /**
   * On mount, simulate a dummy user and restore any saved form state from the model.
   */
  useEffect(() => {
    const dummyUser = { username: "Applicant", person_id: 123, application_status: "unsent" };
    setUser(dummyUser);
    setStatus(dummyUser.application_status);

    // Restore any saved form state from the model's in-memory JSON "file"
    setSelectedCompetence(savedApplicationData.selectedCompetence);
    setYearsOfExperience(savedApplicationData.yearsOfExperience);
    setExpertise(savedApplicationData.expertise);
    setFromDate(savedApplicationData.fromDate);
    setToDate(savedApplicationData.toDate);
    setAvailability(savedApplicationData.availability);
  }, []);

  /**
   * Persists form state to the model's in-memory JSON "file" whenever any form state changes.
   */
  useEffect(() => {
    Object.assign(savedApplicationData, {
      selectedCompetence,
      yearsOfExperience,
      expertise,
      fromDate,
      toDate,
      availability
    });
  }, [selectedCompetence, yearsOfExperience, expertise, fromDate, toDate, availability]);

  /**
   * Handles changes in the competence selection.
   *
   * @param {Object} e - The event object.
   */
  const handleCompetenceChange = (e) => setSelectedCompetence(e.target.value);

  /**
   * Handles changes in the years of experience input.
   * Ensures the value is not negative.
   *
   * @param {Object} e - The event object.
   */
  const handleYearsExperienceChange = (e) => {
    const value = e.target.value;
    if (value < 0) {
      setError('Years of experience cannot be negative.');
    } else {
      setError('');
      setYearsOfExperience(value);
    }
  };

  /**
   * Handles changes in the "From" date input.
   *
   * @param {Object} e - The event object.
   */
  const handleFromDateChange = (e) => setFromDate(e.target.value);

  /**
   * Handles changes in the "To" date input.
   *
   * @param {Object} e - The event object.
   */
  const handleToDateChange = (e) => setToDate(e.target.value);

  /**
   * Adds an expertise entry to the expertise list.
   * Validates that a competence is selected and years of experience is provided.
   */
  const handleAddExpertise = () => {
    if (!selectedCompetence || yearsOfExperience === '') {
      setError('Please select a competence and provide years of experience.');
      return;
    }
    setExpertise([
      ...expertise,
      {
        competence_id: parseInt(selectedCompetence, 10),
        years_of_experience: Number(yearsOfExperience)
      }
    ]);
    setYearsOfExperience('');
    setError('');
  };

  /**
   * Adds an availability period to the availability list.
   * Validates that both start and end dates are provided and that the start date is not after the end date.
   */
  const handleAddAvailability = () => {
    if (!fromDate || !toDate) {
      setError('Please provide both start and end dates.');
      return;
    }
    if (new Date(fromDate) > new Date(toDate)) {
      setError('Start date cannot be after end date.');
      return;
    }
    setAvailability([
      ...availability,
      { from_date: fromDate, to_date: toDate }
    ]);
    setFromDate('');
    setToDate('');
    setError('');
  };

  /**
   * Handles the submission of the application.
   * Validates the application data and logs a JSON object representing the application,
   * which includes the applicant's username, date of submission, expertise, and availability.
   */
  const handleSubmit = () => {
    if (!user) {
      setError('User not found.');
      return;
    }
    const validation = validateApplication(expertise, availability);
    if (!validation.isValid) {
      setError(Object.values(validation.errors).join(' '));
      return;
    }

    // Create a JSON object representing the application data
    const applicationData = {
      username: user.username,
      date_of_submission: new Date().toISOString(),
      expertise,
      availability
    };
    console.log("Application Data (to be sent later):", JSON.stringify(applicationData, null, 2));

    // Display confirmation message
    alert('Application submitted, thanks!');

    // Reset the form and the model's in-memory "file"
    Object.assign(savedApplicationData, {
      selectedCompetence: '',
      yearsOfExperience: '',
      expertise: [],
      fromDate: '',
      toDate: '',
      availability: []
    });
    setSelectedCompetence('');
    setYearsOfExperience('');
    setExpertise([]);
    setFromDate('');
    setToDate('');
    setAvailability([]);
    setError('');
    setStatus('submitted');
  };

  /**
   * Placeholder for a cancel action.
   */
  const handleCancel = () => {
    // Not implemented
  };

  return (
    <ApplicantDashboardView
      user={user}
      competences={competences}
      status={status}
      error={error}
      selectedCompetence={selectedCompetence}
      yearsOfExperience={yearsOfExperience}
      expertise={expertise}
      availability={availability}
      fromDate={fromDate}
      toDate={toDate}
      onCompetenceChange={handleCompetenceChange}
      onYearsExperienceChange={handleYearsExperienceChange}
      onFromDateChange={handleFromDateChange}
      onToDateChange={handleToDateChange}
      onAddExpertise={handleAddExpertise}
      onAddAvailability={handleAddAvailability}
      onSubmit={handleSubmit}
      onCancel={handleCancel}
    />
  );
}
