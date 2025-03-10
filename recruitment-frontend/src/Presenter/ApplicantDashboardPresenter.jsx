import React, { useState, useEffect } from 'react';
import Header from '../Components/Header.jsx'; // Ensure this path is correct
import ApplicantDashboardView from '../View/ApplicantDashboardView.jsx';
import { fetchCompetences, submitApplication } from '../Model/model.jsx';

export default function ApplicantDashboardPresenter() {
  const [competences, setCompetences] = useState([]);
  const [user, setUser] = useState(null);
  const [status, setStatus] = useState(null);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  // Form state
  const [selectedCompetence, setSelectedCompetence] = useState('');
  const [yearsOfExperience, setYearsOfExperience] = useState('');
  const [expertise, setExpertise] = useState([]);
  const [fromDate, setFromDate] = useState('');
  const [toDate, setToDate] = useState('');
  const [availability, setAvailability] = useState([]);

  // Fetch competences from the back-end on mount
  useEffect(() => {
    fetchCompetences()
      .then((data) => setCompetences(data))
      .catch((err) => setError(err.message));
  }, []);

  // Get user info from localStorage on mount
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      setUser(parsedUser);
      setStatus(parsedUser.application_status || 'unsent');
    }
    setIsLoading(false);
  }, []);

  if (isLoading) return <div>Loading...</div>;
  if (!user) {
    return (
      <div className="error-container">
        You must be logged in to access this page. Please log in first!
      </div>
    );
  }

  // Handlers for form state
  const handleCompetenceChange = (e) => setSelectedCompetence(e.target.value);
  const handleYearsExperienceChange = (e) => setYearsOfExperience(e.target.value);
  const handleFromDateChange = (e) => setFromDate(e.target.value);
  const handleToDateChange = (e) => setToDate(e.target.value);

  const handleAddExpertise = () => {
    if (!selectedCompetence || !yearsOfExperience) {
      setError('Please select a competence and provide years of experience.');
      return;
    }
    const experience = parseFloat(yearsOfExperience);
    if (isNaN(experience) || experience < 0 || experience > 99) {
      setError('Please enter a valid number of years between 0 and 99.');
      return;
    }
    // Convert selectedCompetence to a number, as the back-end expects a numeric competence_id
    const competenceId = parseInt(selectedCompetence, 10);
    setExpertise([
      ...expertise,
      { competence_id: competenceId, years_of_experience: experience },
    ]);
    setYearsOfExperience('');
    setError('');
  };

  const handleAddAvailability = () => {
    if (!fromDate || !toDate) {
      setError('Please provide both start and end dates.');
      return;
    }
    const today = new Date().toISOString().split('T')[0];
    if (new Date(fromDate) < new Date(today) || new Date(toDate) < new Date(today)) {
      setError('Dates cannot be in the past.');
      return;
    }
    if (new Date(fromDate) > new Date(toDate)) {
      setError('Start date cannot be later than end date.');
      return;
    }
    setAvailability([...availability, { from_date: fromDate, to_date: toDate }]);
    setFromDate('');
    setToDate('');
    setError('');
  };

  const handleSubmit = async () => {
    if (!user) {
      setError('User not found.');
      return;
    }
    if (expertise.length === 0 || availability.length === 0) {
      setError('Please add at least one expertise and one availability period before submitting.');
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

  const handleCancel = () => {
    setSelectedCompetence('');
    setYearsOfExperience('');
    setExpertise([]);
    setFromDate('');
    setToDate('');
    setAvailability([]);
    setStatus('unsent');
    setError('');
  };

  return (
    <>
      <Header />
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
    </>
  );
}
