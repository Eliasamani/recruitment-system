import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import SigninView from '../View/SigninView';
import { SignInFormModel, validateSignInForm, getRedirectPathByRole } from '../Model/AuthModel';
import { useAuth } from '../Model/AuthContext.jsx';

export default function SigninPresenter() {
    // Form state variables.
    const [formData, setFormData] = useState(SignInFormModel);
    const [errors, setErrors] = useState({});
    const [submissionError, setSubmissionError] = useState('');
    
    // Authentication state and login function from context.
    const { user, login, loading } = useAuth();
    const navigate = useNavigate();

    /**
     * Auto-redirects the user once the authentication process completes.
     */
    useEffect(() => {
        if (!loading && user) {
            const redirectPath = getRedirectPathByRole(user.role);
            navigate(redirectPath);
        }
    }, [user, loading, navigate]);
    
    /**
     * Handles changes to the sign-in form inputs.
     *
     * @param {Object} event - The input change event.
     */
    const onChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    /**
     * Handles the sign-in form submission.
     *
     * @param {Object} event - The form submission event.
     */
    const onSubmit = async (e) => {
        e.preventDefault();
        setSubmissionError('');
        
        // Validate the form data.
        const { errors: validationErrors, isValid } = validateSignInForm(formData);
        setErrors(validationErrors);
        
        if (!isValid) return;
        
        try {
            const success = await login(formData.username, formData.password);
            if (!success) {
                setSubmissionError('Login failed');
            }
        } catch (error) {
            console.error('Login error:', error);
            setSubmissionError(error instanceof Error ? error.message : 'An unexpected error occurred');
        }
    };

    return (
        <SigninView
            formData={formData}
            errors={errors}
            submissionError={submissionError}
            onChange={onChange}
            onSubmit={onSubmit}
            isLoading={loading}
        />
    );
}