import React, { useState, useEffect } from 'react';
import ProfileView from '../View/ProfileView';
import { useAuth } from '../Model/AuthContext.jsx';
import {
  ProfileFormModel,
  validateProfileForm,
  updateProfile
} from '../Model/ProfileModel';

export default function ProfilePresenter() {
  const { user,checkSession } = useAuth();
  const [formData, setFormData] = useState(ProfileFormModel);
  const [updateErrors, setUpdateErrors] = useState([]);
  const [updateSuccess, setUpdateSuccess] = useState('');

  // Prefill the form once user data is available
  useEffect(() => {
    if (user) {
      setFormData({
        ...ProfileFormModel,
        username: user.username || '',
        firstName: user.firstName || '',
        lastName: user.lastName || '',
        personNumber: user.personNumber || '',
        email: user.email || '',
      });
    }
  }, [user]);

  // personNumber is only editable if the user doesn’t already have one
  const personNumberEditable = !user?.personNumber;
  const personNumberValue = user?.personNumber || formData.personNumber;
  const setPersonNumberProp = personNumberEditable
    ? (value) => setFormData({ ...formData, personNumber: value })
    : () => {};

  /**
   * Handles the form submission to update the profile.
   * Validates email format, checks for blank first/last names, 
   * and only calls updateProfile if no errors.
   */
  const handleUpdateProfile = async (event) => {
    event.preventDefault();
    setUpdateErrors([]);
    setUpdateSuccess('');

    // 1) Validate email if provided
    const { errors } = validateProfileForm(formData);

    // 2) If user originally had a first name but now clearing it out
    if (user?.firstName?.trim() && formData.firstName.trim() === '') {
      errors.firstName = 'First name cannot be blank if you had one already.';
    }

    // If user originally had a last name but now clearing it out
    if (user?.lastName?.trim() && formData.lastName.trim() === '') {
      errors.lastName = 'Last name cannot be blank if you had one already.';
    }

    // If both first and last name are blank now
    if (formData.firstName.trim() === '' && formData.lastName.trim() === '') {
      errors.blankNames = 'First name and last name cannot be blank';
    }
    

    // If any errors, display them and exit (do not update)
    if (Object.keys(errors).length > 0) {
      setUpdateErrors(Object.values(errors));
      return;
    }

    // If personNumber is not editable, we either nullify it or leave it
    if (!personNumberEditable || formData.personNumber.trim() === '') {
      formData.personNumber = null;
    }

    // 3) Attempt to updateProfile
    try {
      await updateProfile(formData);
      await checkSession();
      setUpdateSuccess('Profile updated successfully.');
    } catch (error) {
      setUpdateErrors([error.message]);
    }
  };

  return (
    <ProfileView
      firstName={formData.firstName}
      setFirstName={(val) => setFormData({ ...formData, firstName: val })}
      lastName={formData.lastName}
      setLastName={(val) => setFormData({ ...formData, lastName: val })}
      email={formData.email}
      setEmail={(val) => setFormData({ ...formData, email: val })}
      personNumber={personNumberValue}
      setPersonNumber={setPersonNumberProp}
      personNumberEditable={personNumberEditable}
      updateErrors={updateErrors}
      updateSuccess={updateSuccess}
      handleUpdateProfile={handleUpdateProfile}
    />
  );
}
