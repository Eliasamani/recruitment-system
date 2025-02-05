import React, { useState } from 'react';
import SigninView from '../View/SinginView.jsx';
import { SignInFormModel } from '../model.jsx';

  export default function SigninPresenter () {
   
    const [formData, setFormData] = useState(SignInFormModel);
    const [errors, setErrors] = useState({});
    const [submissionError, setSubmissionError] = useState('');
  
    const onChange = (e) => {
      const { name, value } = e.target;
      setFormData(prev => ({
        ...prev,
        [name]: value
      }));
    };
  
    const validate = () => {
      const newErrors={};
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
        const response = await fetch('/api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            username: formData.username,
            password: formData.password
          })
        });
  
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || 'Login failed');
        }
  
        console.log('Login successful');
        // Handle successful login (e.g., redirect, store token, etc.)
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
      />
    );
  };
  
  