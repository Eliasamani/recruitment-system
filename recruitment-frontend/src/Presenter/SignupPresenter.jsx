import React, { useState } from 'react';
import SignupView from '../View/SignupView.jsx'; 
import { SignupFormModel } from '../model.jsx';

export default function SignupPresenter() {
  const [formData, setFormData] = useState(SignupFormModel);
  const [errors, setErrors] = useState({});
  const [submissionError, setSubmissionError] = useState('');
  const [loading, setLoading] = useState(false); // Added loading state

  // onChange handler
  const onChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.firstName.trim()) newErrors.firstName = 'First name is required.';
    if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required.';
    if (!formData.personnummer.trim()) newErrors.personnummer = 'Personnummer is required.';
    if (!formData.username.trim()) newErrors.username = 'Username is required.';
    if (!formData.email.trim()) newErrors.email = 'Email is required.';
    if (!formData.password.trim()) newErrors.password = 'Password is required.';

    // Personnummer validation
    const personnummerRegex = /^\d{6}-\d{4}$/;
    if (formData.personnummer && !personnummerRegex.test(formData.personnummer)) {
      newErrors.personnummer = 'Invalid format (expected YYMMDD-XXXX)';
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (formData.email && !emailRegex.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true); // Start loading
    setSubmissionError(''); // Reset submission error

    try {
      const response = await fetch(process.env.REACT_APP_API_URL + '/api/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          firstname: formData.firstName,
          lastname: formData.lastName,         // Changed to formData.lastName
          personNumber: formData.personnummer,   // Changed to formData.personnummer
          username: formData.username,
          email: formData.email,
          password: formData.password
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
      }
    } catch (error) {
      console.error('Registration error:', error);
      setSubmissionError(
        error instanceof Error ? error.message : 'An unexpected error occurred'
      );
    } finally {
      setLoading(false); // Stop loading
    }
  };

  return (
    <SignupView
      formData={formData}
      errors={errors}
      submissionError={submissionError}
      loading={loading} // Pass loading state to the View
      onChange={onChange}
      onSubmit={onSubmit}
    />
  );
}
