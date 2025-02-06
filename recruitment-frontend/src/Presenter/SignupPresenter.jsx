import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import SignupView from '../View/SignupView.jsx';
import { SignupFormModel } from '../model.jsx';

export default function SignupPresenter() {
  const [formData, setFormData] = useState(SignupFormModel);
  const [errors, setErrors] = useState({});
  const [submissionError, setSubmissionError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // onChange handler
  const onChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.firstname.trim()) newErrors.firstname = 'First name is required.';
    if (!formData.lastname.trim()) newErrors.lastname = 'Last name is required.';
    if (!formData.personNumber.trim()) newErrors.personNumber = 'Personnumber is required.';
    if (!formData.username.trim()) newErrors.username = 'Username is required.';
    if (!formData.email.trim()) newErrors.email = 'Email is required.';
    if (!formData.password.trim()) newErrors.password = 'Password is required.';

    // Personnumber validation
    const personNumberRegex = /^\d{8}-\d{4}$/;
    if (formData.personNumber && !personNumberRegex.test(formData.personNumber.trim())) {
      newErrors.personNumber = 'Invalid format (expected YYYYMMDD-XXXX)';
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
          firstname: formData.firstname,
          lastname: formData.lastname,
          personNumber: formData.personNumber,
          username: formData.username,
          email: formData.email,
          password: formData.password,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Registration failed');
      }

      // Redirect to Signin page with username as a query parameter
      navigate(`/signin?username=${encodeURIComponent(formData.username)}`);
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
      loading={loading}
      onChange={onChange}
      onSubmit={onSubmit}
    />
  );
}