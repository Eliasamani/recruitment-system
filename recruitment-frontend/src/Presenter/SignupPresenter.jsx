import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import SignupView from '../View/SignupView';
import { SignupFormModel, validateSignupForm, registerUser } from '../Model/SignupModel';

export default function SignupPresenter() {
    const [formData, setFormData] = useState(SignupFormModel);
    const [errors, setErrors] = useState({});
    const [submissionError, setSubmissionError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const onChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        
        // Validate the form
        const { errors: validationErrors, isValid } = validateSignupForm(formData);
        setErrors(validationErrors);
        if (!isValid) return;
        
        // Process submission
        setLoading(true);
        setSubmissionError('');
        
        try {
            await registerUser({
                firstname: formData.firstname,
                lastname: formData.lastname,
                personNumber: formData.personNumber,
                username: formData.username,
                email: formData.email,
                password: formData.password,
            });
            
            // Redirect to Signin page with username as a query parameter
            navigate(`/signin?username=${encodeURIComponent(formData.username)}`);
        } catch (error) {
            console.error('Registration error:', error);
            setSubmissionError(
                error instanceof Error ? error.message : 'An unexpected error occurred'
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <SignupView
            formData={formData}
            errors={errors}
            submissionError={submissionError}
            loading={loading}
            onChange={onChange}
            onSubmit={onSubmit}
        />
    );
}