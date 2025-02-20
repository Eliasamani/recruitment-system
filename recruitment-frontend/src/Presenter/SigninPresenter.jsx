import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import SigninView from '../View/SigninView';
import { SignInFormModel } from '../model';
import { useAuth } from '../AuthContext';
import Header from '../Reusablecomponent/Header';

export default function SigninPresenter() {
    const [formData, setFormData] = useState(SignInFormModel);
    const [errors, setErrors] = useState({});
    const [submissionError, setSubmissionError] = useState('');
    const { user, login, loading } = useAuth();
    const navigate = useNavigate();

    // Auto-redirect if user is authenticated and loading is finished
    useEffect(() => {
        if (!loading && user) {
            if (user.role === 2) {
                navigate('/applicant/dashboard');
            } else if (user.role === 1) {
                navigate('/recruiter/dashboard');
            }
        }
    }, [user, loading, navigate]);

    const onChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const validate = () => {
        const newErrors = {};
        if (!formData.username.trim()) newErrors.username = 'Username is required';
        if (!formData.password.trim()) newErrors.password = 'Password is required';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    const onSubmit = async (e) => {
        e.preventDefault();
        setSubmissionError('');
        if (!validate()) return;
        try {
            const success = await login(formData.username, formData.password);
            if (!success) {
                setSubmissionError('Login failed');
                return;
            }
            // No need to manually redirect here—the useEffect listening to "user" will handle it.
        } catch (error) {
            console.error('Login error:', error);
            setSubmissionError(error instanceof Error ? error.message : 'An unexpected error occurred');
        }
    };

    return (
        <div>
            <Header />
            <SigninView
                formData={formData}
                errors={errors}
                submissionError={submissionError}
                onChange={onChange}
                onSubmit={onSubmit}
            />
        </div>
    );
}
