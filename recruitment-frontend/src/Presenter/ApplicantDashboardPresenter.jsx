import React, { useState, useEffect } from 'react';
import ApplicantDashboardView from '../View/ApplicantDashboardView';
import { submitApplication, validateApplication, competences } from '../Model/ApplicantDashboardModel';

export default function ApplicantDashboardPresenter() {
  // User and application state
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
     * Fetches the user data from the back-end.
     */
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/applicant`, {
          credentials: 'include',
        });
        if (!response.ok) {
          throw new Error('Failed to fetch user data.');
        }
        const userData = await response.json();
        setUser(userData);
        setStatus(userData.application_status || 'unsent');
      } catch (err) {
        setError(err.message);
      }
    };

    fetchUser();
  }, []);

  // Event handler for competence selection changes.
  const handleCompetenceChange = (e) => setSelectedCompetence(e.target.value);
  const handleYearsExperienceChange = (e) => setYearsOfExperience(e.target.value);
  const handleFromDateChange = (e) => setFromDate(e.target.value);
  const handleToDateChange = (e) => setToDate(e.target.value);

  /**
     * Adds an expertise entry to the expertise list.
     */
  const handleAddExpertise = () => {
    if (!selectedCompetence || !yearsOfExperience) {
        setError('Please select a competence and provide years of experience.');
        return;
    }
    setExpertise([
        ...expertise,
        {
            competence_id: parseInt(selectedCompetence, 10),
            years_of_experience: yearsOfExperience
        }
    ]);
    setYearsOfExperience('');
};

    /**
     * Adds an availability period to the availability list.
     */
    const handleAddAvailability = () => {
        if (!fromDate || !toDate) {
            setError('Please provide both start and end dates.');
            return;
        }
        setAvailability([
            ...availability,
            { from_date: fromDate, to_date: toDate }
        ]);
        setFromDate('');
        setToDate('');
    };

    /**
     * Handles the submission of the application.
     */
    const handleSubmit = async () => {
        if (!user) {
            setError('User not found.');
            return;
        }
        const validation = validateApplication(expertise, availability);
        if (!validation.isValid) {
            setError(Object.values(validation.errors).join(' '));
            return;
        }

        try {
            await submitApplication(user.person_id, expertise, availability);
            alert('Application submitted successfully.');
            setExpertise([]);
            setAvailability([]);
            setStatus('unhandled');
            setError('');
        } catch (err) {
            setError(err.message);
        }
    };

    /**
     * Handles the cancel action.
     * Optionally resets the form fields.
     */
    const handleCancel = () => {
        // Optionally implement cancel logic (e.g., reset form fields)
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