import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import SigninView from '../View/SigninView';
import { SignInFormModel, validateSignInForm, getRedirectPathByRole } from '../Model/AuthModel';
import { useAuth } from '../Model/AuthContext';

export default function SigninPresenter() {
    const [formData, setFormData] = useState(SignInFormModel);
    const [errors, setErrors] = useState({});
    const [submissionError, setSubmissionError] = useState('');
    const { user, login, loading } = useAuth();
    const navigate = useNavigate();

    // Auto-redirect if user is authenticated and loading is finished
    useEffect(() => {
        if (!loading && user) {
            const redirectPath = getRedirectPathByRole(user.role);
            navigate(redirectPath);
        }
    }, [user, loading, navigate]);

    const onChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        setSubmissionError('');
        
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
