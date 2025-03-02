import React, { useState } from 'react';
import ForgotPasswordView from '../View/ForgotPasswordView';
import { ForgotPasswordFormModel, validateForgotPasswordForm, requestResetCode, resetPassword } from '../Model/ForgotPasswordModel';

const ForgotPasswordPresenter = () => {
    const [formData, setFormData] = useState(ForgotPasswordFormModel);
    const [requestError, setRequestError] = useState('');
    const [requestSuccess, setRequestSuccess] = useState('');
    const [codeSent, setCodeSent] = useState(false);
    const [resetError, setResetError] = useState('');
    const [resetSuccess, setResetSuccess] = useState('');

    const handleRequestCode = async (e) => {
        e.preventDefault();
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

    const handleResetPassword = async (e) => {
        e.preventDefault();
        setResetError('');
        setResetSuccess('');

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
            email={formData.email}
            setEmail={(value) => setFormData({ ...formData, email: value })}
            requestError={requestError}
            requestSuccess={requestSuccess}
            handleRequestCode={handleRequestCode}
            codeSent={codeSent}
            resetEmail={formData.resetEmail}
            setResetEmail={(value) => setFormData({ ...formData, resetEmail: value })}
            username={formData.username}
            setUsername={(value) => setFormData({ ...formData, username: value })}
            newPassword={formData.newPassword}
            setNewPassword={(value) => setFormData({ ...formData, newPassword: value })}
            resetCode={formData.resetCode}
            setResetCode={(value) => setFormData({ ...formData, resetCode: value })}
            resetError={resetError}
            resetSuccess={resetSuccess}
            handleResetPassword={handleResetPassword}
        />
    );
};

export default ForgotPasswordPresenter;
