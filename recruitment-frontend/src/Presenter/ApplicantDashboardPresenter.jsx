
import React, { useState, useEffect } from 'react';
import ApplicantDashboardView from '../View/ApplicantDashboardView';
import { fetchCompetences, submitApplication, validateApplication } from '../Model/ApplicantDashboardModel';

export default function ApplicantDashboardPresenter ()  {
    const [competences, setCompetences] = useState([]);
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

    // Fetch competences
    useEffect(() => {
        fetchCompetences()
            .then(setCompetences)
            .catch(err => setError(err.message));
    }, []);

    // Fetch user from localStorage
    useEffect(() => {
        const storedUser = localStorage.getItem('user');
        if (storedUser) {
            const parsedUser = JSON.parse(storedUser);
            setUser(parsedUser);
            setStatus(parsedUser.application_status || 'unsent');
        }
    }, []);

    // Handlers
    const handleCompetenceChange = (e) => setSelectedCompetence(e.target.value);
    const handleYearsExperienceChange = (e) => setYearsOfExperience(e.target.value);
    const handleFromDateChange = (e) => setFromDate(e.target.value);
    const handleToDateChange = (e) => setToDate(e.target.value);

    const handleAddExpertise = () => {
        if (!selectedCompetence || !yearsOfExperience) {
            setError('Please select a competence and provide years of experience.');
            return;
        }
        setExpertise([...expertise, { competence_id: parseInt(selectedCompetence, 10), years_of_experience: yearsOfExperience }]);
        setYearsOfExperience('');
    };

    const handleAddAvailability = () => {
        if (!fromDate || !toDate) {
            setError('Please provide both start and end dates.');
            return;
        }
        setAvailability([...availability, { from_date: fromDate, to_date: toDate }]);
        setFromDate('');
        setToDate('');
    };

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
        />
    );
};

