import React, { useState, useEffect} from 'react';
import SigninView from '../View/SigninView.jsx';
import { SignInFormModel } from '../model.jsx';

export default function SigninPresenter() {
  const [formData, setFormData] = useState(SignInFormModel);
  const [errors, setErrors] = useState({});
  const [submissionError, setSubmissionError] = useState("");
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check if user is logged in when the page loads
  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const response = await fetch(process.env.REACT_APP_API_URL+"/api/session", { credentials: "include" });
      if (response.ok) {
        setIsAuthenticated(true); // User is logged in
      } else {
        setIsAuthenticated(false); // User is not logged in
      }
    } catch (error) {
      setIsAuthenticated(false);
    }
  };

  const onChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.username.trim()) newErrors.username = "Username is required";
    if (!formData.password.trim()) newErrors.password = "Password is required";
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setSubmissionError("");

    if (!validate()) return;

    try {
      const response = await fetch(process.env.REACT_APP_API_URL+'/api/login', {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: formData.username,
          password: formData.password,
        }),
        credentials: "include", // Include cookies
      });

      if (!response.ok) {
        let errorMessage = "Login failed";
        try {
          const errorData = await response.clone().json();
          errorMessage = errorData.error || "Login failed";
        } catch {
          errorMessage = await response.text();
        }
        throw new Error(errorMessage);
      }

      console.log("Login successful");
      setIsAuthenticated(true); // Update the state
    } catch (error) {
      console.error("Login error:", error);
      setSubmissionError(
        error instanceof Error ? error.message : "An unexpected error occurred"
      );
    }
  };

  if (isAuthenticated) {
    window.location.href = process.env.REACT_APP_API_URL+"/api/protected";
    return null;
}

  return (
    <SigninView
      formData={formData}
      errors={errors}
      submissionError={submissionError}
      onChange={onChange}
      onSubmit={onSubmit}
    />
  );
}
