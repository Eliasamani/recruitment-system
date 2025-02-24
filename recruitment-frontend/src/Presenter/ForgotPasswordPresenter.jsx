import React, { useState } from 'react';
import Header from '../Components/Header.jsx'; 
import ForgotPasswordView from '../View/ForgotPasswordView';

export default function ForgotPasswordPresenter() {
  // State for requesting the reset code
  const [email, setEmail] = useState('');
  const [requestError, setRequestError] = useState('');
  const [requestSuccess, setRequestSuccess] = useState('');
  const [codeSent, setCodeSent] = useState(false);

  // State for resetting credentials
  const [resetEmail, setResetEmail] = useState('');
  const [username, setUsername] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [resetCode, setResetCode] = useState('');
  const [resetError, setResetError] = useState('');
  const [resetSuccess, setResetSuccess] = useState('');

  // Handler to request a reset code
  const handleRequestCode = async (e) => {
    e.preventDefault();
    setRequestError('');
    setRequestSuccess('');
    try {
      const response = await fetch(process.env.REACT_APP_API_URL + '/api/reset/code', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        // The back-end expects a JSON string with the email.
        body: email
      });
      if (response.ok) {
        setRequestSuccess('If this email is registered, a reset code has been sent.');
        setCodeSent(true);
      } else {
        const data = await response.json();
        setRequestError(data.message || 'Failed to send reset code.');
      }
    } catch (error) {
      setRequestError(error.message);
    }
  };

  // Handler to reset the credentials
  const handleResetPassword = async (e) => {
    e.preventDefault();
    setResetError('');
    setResetSuccess('');
    const payload = {
      email: resetEmail,
      username,       // Optional: update if provided
      password: newPassword, // Optional: update if provided
      code: resetCode  // Should be a numeric value within the specified range (e.g. 100000 - 1000000)
    };
    try {
      const response = await fetch('/api/reset/password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
      });
      if (response.ok) {
        const data = await response.json();
        setResetSuccess(data.message || 'Reset successful.');
      } else {
        const data = await response.json();
        setResetError(data.message || 'Reset failed.');
      }
    } catch (error) {
      setResetError(error.message);
    }
  };

  return (
    <>
      <Header />
      <ForgotPasswordView 
        email={email}
        setEmail={setEmail}
        requestError={requestError}
        requestSuccess={requestSuccess}
        handleRequestCode={handleRequestCode}
        codeSent={codeSent}
        resetEmail={resetEmail}
        setResetEmail={setResetEmail}
        username={username}
        setUsername={setUsername}
        newPassword={newPassword}
        setNewPassword={setNewPassword}
        resetCode={resetCode}
        setResetCode={setResetCode}
        resetError={resetError}
        resetSuccess={resetSuccess}
        handleResetPassword={handleResetPassword}
      />
    </>
  );
}
