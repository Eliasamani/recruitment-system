import React, { useState, useEffect } from 'react';
import CandidatepageView from '../View/CandidatepageView';
import { fetchCompetences, submitApplication } from '../model.jsx';

export default function CandidatepagePresenter() {
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

  // Fetch competences on mount
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
      setError('Please select a competence and years of experience.');
      return;
    }
    const experience = parseInt(yearsOfExperience, 10);
    if (isNaN(experience) || experience < 0 || experience > 99) {
      setError('Please enter a valid number of years of experience between 0 and 99.');
      return;
    }
    setExpertise([
      ...expertise,
      { competence_id: selectedCompetence, years_of_experience: yearsOfExperience },
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
      setError('From date cannot be later than To date.');
      return;
    }
    setAvailability([...availability, { from_date: fromDate, to_date: toDate }]);
    setFromDate('');
    setToDate('');
    setError('');
  };

  const handleSubmit = async () => {
    if (!user) {
      setError('User not found');
      return;
    }
    if (expertise.length === 0 || availability.length === 0) {
      setError('Please add at least one competence and one availability period before submitting.');
      return;
    }
    try {
      await submitApplication(user.person_id, expertise, availability);
      alert('Application submitted successfully');
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
    <CandidatepageView
      user={user}
      // Pass down form data & handlers only
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
