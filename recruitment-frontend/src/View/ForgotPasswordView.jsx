import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../Components/Header';

const ForgotPasswordView =({
    email,
    setEmail,
    requestError,
    requestSuccess,
    handleRequestCode,
    codeSent,
    resetEmail,
    setResetEmail,
    username,
    setUsername,
    newPassword,
    setNewPassword,
    resetCode,
    setResetCode,
    resetError,
    resetSuccess,
    handleResetPassword,
}) =>(
    <div>
        <Header />
        <div className="applicant-page">
            <div className="applicant-header">
                <h1>Forgot Password</h1>
                <p>Reset your username and/or password</p>
            </div>
            <div className="applicant-body">
                {/* Request Reset Code Section */}
                <section className="form-section">
                    <h2>Request Reset Code</h2>
                    <p className="section-info">
                        Enter your registered email address. If it is registered, a reset code will be sent.
                    </p>
                    {requestError && <div className="error-container"><p>{requestError}</p></div>}
                    {requestSuccess && <div className="success-message"><p>{requestSuccess}</p></div>}
                    <form onSubmit={handleRequestCode}>
                        <div className="form-group">
                            <label>Email:</label>
                            <input 
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                placeholder="Enter your email"
                                required
                            />
                        </div>
                        <button type="submit" className="submit-button">Send Reset Code</button>
                    </form>
                </section>

                {/* Reset Credentials Section (appears once codeSent is true) */}
                {codeSent && (
                    <section className="form-section" style={{ marginTop: '2rem' }}>
                        <h2>Reset Credentials</h2>
                        <p className="section-info">
                            Enter the reset code along with your details. You can update your username, password, or both.
                        </p>
                        {resetError && <div className="error-container"><p>{resetError}</p></div>}
                        {resetSuccess && <div className="success-message"><p>{resetSuccess}</p></div>}
                        <form onSubmit={handleResetPassword}>
                            <div className="form-group">
                                <label>Email:</label>
                                <input 
                                    type="email"
                                    value={resetEmail}
                                    onChange={(e) => setResetEmail(e.target.value)}
                                    placeholder="Enter your email"
                                    required
                                />
                            </div>
                            <div className="form-group">
                                <label>New Username:</label>
                                <input 
                                    type="text"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    placeholder="Enter new username (optional)"
                                />
                            </div>
                            <div className="form-group">
                                <label>New Password:</label>
                                <input 
                                    type="password"
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    placeholder="Enter new password (optional)"
                                />
                            </div>
                            <div className="form-group">
                                <label>Reset Code:</label>
                                <input 
                                    type="text"
                                    value={resetCode}
                                    onChange={(e) => setResetCode(e.target.value)}
                                    placeholder="Enter the reset code"
                                    required
                                />
                            </div>
                            <button type="submit" className="submit-button">Reset</button>
                        </form>
                    </section>
                )}
                <div style={{ marginTop: '1rem', textAlign: 'center' }}>
                    <Link to="/signin">Back to Sign In</Link>
                </div>
            </div>
        </div>
    </div>
);
export default ForgotPasswordView;