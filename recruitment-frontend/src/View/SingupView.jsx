import React from 'react';
import { SignInFormModel } from '../model.jsx';



const SignupView = ({ formData, errors, onChange, onSubmit }) => (
  <form onSubmit={onSubmit}>
    <h2>Sign Up</h2>

    <div>
      <label>First Name:</label>
      <input
        type="text"
        name="firstName"
        value={formData.firstname}
        onChange={onChange}
        placeholder="firstName"
      />
      {errors.firstName && <p style={{ color: 'red' }}>{errors.firstName}</p>}
    </div>
    <div>
      <label>Last Name:</label>
      <input
        type="text"
        name="lastName"
        value={formData.lastname}
        onChange={onChange}
        placeholder="lastName"

      />
      {errors.lastName && <p style={{ color: 'red' }}>{errors.lastName}</p>}
    </div>

    <div>
      <label>Personnummer:</label>
      <input
        type="text"
        name="personnummer"
        value={formData.personNumber}
        onChange={onChange}
        placeholder="YYMMDD-XXXX"
      />
      {errors.personnummer && <p style={{ color: 'red' }}>{errors.personnummer}</p>}
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

    <button type="submit">Register</button>
  </form>
);

export default SignupView;