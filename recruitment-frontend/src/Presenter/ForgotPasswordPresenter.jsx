import React, { useState } from 'react';
import ForgotPasswordView from '../View/ForgotPasswordView';
import {ForgotPasswordFormModel,validateForgotPasswordForm,requestResetCode,resetPassword} from '../Model/ForgotPasswordModel';

export default function ForgotPasswordPresenter () {
    /** State for managing forgot password form data */
    const [formData, setFormData] = useState(ForgotPasswordFormModel);

    /** State for handling reset code request messages */
    const [requestError, setRequestError] = useState('');
    const [requestSuccess, setRequestSuccess] = useState('');
    const [codeSent, setCodeSent] = useState(false);

    /** State for handling password reset messages */
    const [resetError, setResetError] = useState('');
    const [resetSuccess, setResetSuccess] = useState('');

    /**
     * Handles the request to send a password reset code.
     * 
     * @param {Object} event - The event object from the form submission.
     */
    const handleRequestCode = async (event) => {
        event.preventDefault();
        setRequestError('');
        setRequestSuccess('');

        try {
            await requestResetCode(formData.email);
            setRequestSuccess('If this email is registered, a reset code has been sent.');
            setCodeSent(true);
        } catch (error) {
            setRequestError(error.message);
        }
    };

    /**
     * Handles the password reset process.
     * 
     * @param {Object} event - The event object from the form submission.
     */
    const handleResetPassword = async (event) => {
        event.preventDefault();
        setResetError('');
        setResetSuccess('');

        // Validate form data before proceeding
        const validation = validateForgotPasswordForm(formData);
        if (!validation.isValid) {
            return;
        }

        try {
            await resetPassword({
                email: formData.email,
                username: formData.username,
                password: formData.newPassword,
                code: formData.resetCode
            });
            setResetSuccess('Reset successful.');
        } catch (error) {
            setResetError(error.message);
        }
    };

    return (
        <ForgotPasswordView
            /** Props related to email input */
            email={formData.email}
            setEmail={(value) => setFormData({ ...formData, email: value })}
            
            /** Props for handling reset code request feedback */
            requestError={requestError}
            requestSuccess={requestSuccess}
            handleRequestCode={handleRequestCode}
            codeSent={codeSent}

            /** Props related to password reset */
            resetEmail={formData.resetEmail}  // Ensure `resetEmail` is part of the model if needed.
            setResetEmail={(value) => setFormData({ ...formData, resetEmail: value })}
            username={formData.username}
            setUsername={(value) => setFormData({ ...formData, username: value })}
            newPassword={formData.newPassword}
            setNewPassword={(value) => setFormData({ ...formData, newPassword: value })}
            resetCode={formData.resetCode}
            setResetCode={(value) => setFormData({ ...formData, resetCode: value })}

            /** Props for handling password reset feedback */
            resetError={resetError}
            resetSuccess={resetSuccess}
            handleResetPassword={handleResetPassword}
        />
    );
};