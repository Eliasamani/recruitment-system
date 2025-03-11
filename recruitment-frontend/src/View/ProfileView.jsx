/**
 * ProfileView - The View component for displaying and updating the user profile.
 * 
 * - This component is responsible for rendering the profile update form.
 * - All user interactions are passed to the Presenter via props.
 */

import React from 'react';
import Header from '../Components/Header';
import '../App.css';

const ProfileView = ({
  firstName,
  setFirstName,
  lastName,
  setLastName,
  email,
  setEmail,
  personNumber,
  setPersonNumber,
  personNumberEditable,
  updateErrors,
  updateSuccess,
  handleUpdateProfile,
  username
}) => {
  return (
    <>
      {/* Header component for consistent navigation */}
      <Header />

      {/* Profile update section */}
      <div>
        <h2>Update Profile</h2>

        {/* Profile update form */}
        <form onSubmit={handleUpdateProfile}>
          
          {/* First Name Field */}
          <div>
            <label>First Name</label>
            <input
              type="text"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
          </div>

          {/* Last Name Field */}
          <div>
            <label>Last Name</label>
            <input
              type="text"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </div>

          {/* Person Number Field (Editable only if allowed) */}
          <div>
            <label>Person Number</label>
            <input
              type="text"
              value={personNumber}
              onChange={(e) => setPersonNumber(e.target.value)}
              disabled={!personNumberEditable}
            />
          </div>

          {/* Email Field */}
          <div>
            <label>Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>

          {/* Display validation errors if any */}
          {updateErrors && updateErrors.length > 0 && (
            <ul style={{ color: 'red' }}>
              {updateErrors.map((err, idx) => (
                <li key={idx}>{err}</li>
              ))}
            </ul>
          )}

          {/* Display success message if update is successful */}
          {updateSuccess && (
            <p style={{ color: 'green' }}>{updateSuccess}</p>
          )}

          {/* Submit button to update profile */}
          <button type="submit">Update Profile</button>
        </form>
      </div>
    </>
  );
};

export default ProfileView;
