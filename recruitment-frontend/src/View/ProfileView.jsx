import React from 'react';
import Header from '../Components/Header';

const ProfileView = ({
  firstName,
  setFirstName,
  lastName,
  setLastName,
  email,
  setEmail,
  personNumber,
  setPersonNumber,
  updateError,
  updateSuccess,
  handleUpdateProfile,
  personNumberResetLink,
  personNumberEditable
}) => (
  <>
    <Header />
    <div>
      <h2>Update Profile</h2>
      <form onSubmit={handleUpdateProfile}>
        <div>
          <label>First Name</label>
          <input
            type="text"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
        </div>
        <div>
          <label>Last Name</label>
          <input
            type="text"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
        </div>
      
        <div>
          <label>Person Number</label>
          <input
            type="text"
            value={personNumber}
            onChange={(e) => setPersonNumber(e.target.value)}
            // Disable editing if a person number already exists
            disabled={!personNumberEditable}
          />
        </div>
        <div>
          <label>Email</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
        </div>
        {updateError && <p style={{ color: 'red' }}>{updateError}</p>}
        {updateSuccess && <p style={{ color: 'green' }}>{updateSuccess}</p>}
        <button type="submit">Update Profile</button>
      </form>
    
    </div>
  </>
);

export default ProfileView;
