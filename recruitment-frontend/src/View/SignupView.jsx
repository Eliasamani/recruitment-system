import React from 'react';
import { Link } from 'react-router-dom';

const SignupView = ({ formData, errors, submissionError, loading, onChange, onSubmit }) => (
  <>
      <form onSubmit={onSubmit}>
        <h2>Sign Up</h2>
        {submissionError && <div style={{ color: 'red' }}>{submissionError}</div>}
        <div>
          <label>First Name:</label>
          <input
            type="text"
            name="firstname"
            value={formData.firstname}
            onChange={onChange}
            placeholder="First Name"
          />
          {errors.firstname && <p style={{ color: 'red' }}>{errors.firstname}</p>}
        </div>
        <div>
          <label>Last Name:</label>
          <input
            type="text"
            name="lastname"
            value={formData.lastname}
            onChange={onChange}
            placeholder="First Name"
          />
          {errors.lastname && <p style={{ color: 'red' }}>{errors.lastname}</p>}
        </div>

        <div>
          <label>Personnummer</label>
          <input
            type="text"
            name="personNumber"
            value={formData.personNumber}
            onChange={onChange}
            placeholder="YYYYMMDD-XXXX"
          />
          {errors.personNumber && <p style={{ color: 'red' }}>{errors.personNumber}</p>}
        </div>

        <div>
          <label>Username:</label>
          <input
            type="text"
            name="username"
            value={formData.username}
            onChange={onChange}
            placeholder="username"
          />
          {errors.username && <p style={{ color: 'red' }}>{errors.username}</p>}
        </div>

        <div>
          <label>Email:</label>
          <input
            type="text"
            name="email"
            value={formData.email}
            onChange={onChange}
            placeholder="name@mailservice.ex"
          />
          {errors.email && <p style={{ color: 'red' }}>{errors.email}</p>}
        </div>

        <div>
          <label>Password:</label>
          <input
            type="password"  
            name="password"
            value={formData.password}
            onChange={onChange}
            placeholder="password"
          />
          {errors.password && <p style={{ color: 'red' }}>{errors.password}</p>}
        </div>

      {/* terms&Condition text */}
      <div style={{ margin: '20px 0', fontSize: '0.9em' }}>
          By creating an account, you agree to the{' '}
          <a href="/terms-of-service" target="_blank" rel="noopener noreferrer">
            Terms of Service
          </a>. For more information about our privacy practices, see the{' '}
          <a href="/privacy-statement" target="_blank" rel="noopener noreferrer">
            Privacy Statement
          </a>. We'll occasionally send you account-related emails.
        </div>
        <button type="submit" disabled={loading}>
          {loading ? 'Registering...' : 'Register'}
        </button>
      </form>

      <div style={{ marginTop: '1rem' }}>
      <span>Do you have an account already? </span>
      <Link to="/signin">
        <button type="button">Sign In</button>
      </Link>
      </div>
      </>);

export default SignupView;