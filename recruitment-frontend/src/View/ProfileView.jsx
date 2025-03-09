import React from 'react';
import Header from '../Components/Header';

/**
 * ProfileView component.
 *
 * This component renders the UI for updating a user's profile. It includes input fields
 * for first name, last name, username, email, and password. It displays error and success messages,
 * and provides a link for contacting support to reset the person number.
 *
 * @constant
 * @param {string} firstName - The user's first name.
 * @param {function} setFirstName - Function to update the first name.
 * @param {string} lastName - The user's last name.
 * @param {function} setLastName - Function to update the last name.
 * @param {string} username - The user's username.
 * @param {function} setUsername - Function to update the username.
 * @param {string} email - The user's email address.
 * @param {function} setEmail - Function to update the email address.
 * @param {string} password - The user's password.
 * @param {function} setPassword - Function to update the password.
 * @param {string} updateError - Error message to display if profile update fails.
 * @param {string} updateSuccess - Success message to display if profile update succeeds.
 * @param {function} handleUpdateProfile - Function to handle the profile form submission.
 * @param {string} personNumberResetLink - A mailto link for contacting support to reset the person number.
 * @returns {JSX.Element} The ProfileView component.
 */
const ProfileView = ({
  firstName,
  setFirstName,
  lastName,
  setLastName,
  username,
  setUsername,
  email,
  setEmail,
  password,
  setPassword,
  updateError,
  updateSuccess,
  handleUpdateProfile,
  personNumberResetLink
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
          <label>Username</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
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
        <div>
          <label>Password</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        {updateError && <p style={{ color: 'red' }}>{updateError}</p>}
        {updateSuccess && <p style={{ color: 'green' }}>{updateSuccess}</p>}
        <button type="submit">Update Profile</button>
      </form>
      <hr />
      <p>
        To reset your person number, please{' '}
        <a href={personNumberResetLink}>contact support</a>.
      </p>
    </div>
  </>
);

export default ProfileView;
