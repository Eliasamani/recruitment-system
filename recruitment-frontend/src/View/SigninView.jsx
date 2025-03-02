import React from 'react';
import { Link } from 'react-router-dom';

export default function SigninView ({ formData, errors, submissionError, onChange, onSubmit }) {
  <>
    <form onSubmit={onSubmit}>
      <h2>Sign In</h2>

      {submissionError && (
        <div style={{ color: 'red', marginBottom: '1rem' }}>{submissionError}</div>
      )}

      <div>
        <label>Username:</label>
        <input
          type="text"
          name="username"
          value={formData.username}
          onChange={onChange}
          placeholder="Enter your username"
        />
        {errors.username && <p style={{ color: 'red' }}>{errors.username}</p>}
      </div>

      <div>
        <label>Password:</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={onChange}
          placeholder="Enter your password"
        />
        {errors.password && <p style={{ color: 'red' }}>{errors.password}</p>}
      </div>

      <div style={{ marginTop: '1rem' }}>
        <button type="submit">Sign In</button>
      </div>

      <div style={{ marginTop: '1rem' }}>
        <Link to="/forgot-password" style={{ fontSize: '0.9em' }}>
          Forgot password?
        </Link>
      </div>
    </form>

    <div style={{ marginTop: '1rem' }}>
      <span>Don't have an account? </span>
      <Link to="/signup">
        <button type="button">Sign Up</button>
      </Link>
    </div>
  </>
};

