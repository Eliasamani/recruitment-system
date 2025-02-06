/*
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import SigninView from '../View/SigninView';

describe('SigninView', () => {
  const mockOnChange = jest.fn();
  const mockOnSubmit = jest.fn();

  it('renders the sign-in form correctly', () => {
    render(
      <SigninView
        formData={{ username: '', password: '' }}
        errors={{ username: '', password: '' }}
        submissionError=""
        onChange={mockOnChange}
        onSubmit={mockOnSubmit}
      />
    );

    // Check if the form elements are rendered
    expect(screen.getByText('Sign In')).toBeInTheDocument();
    expect(screen.getByLabelText(/username/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
    expect(screen.getByText('Don\'t have an account?')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /sign up/i })).toBeInTheDocument();
  });

  it('updates form data on input change', () => {
    render(
      <SigninView
        formData={{ username: '', password: '' }}
        errors={{ username: '', password: '' }}
        submissionError=""
        onChange={mockOnChange}
        onSubmit={mockOnSubmit}
      />
    );

    // Simulate typing in the username field
    const usernameInput = screen.getByLabelText(/username/i);
    fireEvent.change(usernameInput, { target: { name: 'username', value: 'testuser' } });

    // Check if the onChange handler was called
    expect(mockOnChange).toHaveBeenCalledWith(expect.objectContaining({ name: 'username', value: 'testuser' }));
  });

  it('submits the form and calls onSubmit', async () => {
    render(
      <SigninView
        formData={{ username: 'testuser', password: 'testpassword' }}
        errors={{ username: '', password: '' }}
        submissionError=""
        onChange={mockOnChange}
        onSubmit={mockOnSubmit}
      />
    );

    // Simulate form submission
    const submitButton = screen.getByRole('button', { name: /sign in/i });
    fireEvent.click(submitButton);

    // Check if the onSubmit handler was called
    await waitFor(() => {
      expect(mockOnSubmit).toHaveBeenCalled();
    });
  });

  it('displays validation errors', () => {
    render(
      <SigninView
        formData={{ username: '', password: '' }}
        errors={{ username: 'Username is required', password: 'Password is required' }}
        submissionError=""
        onChange={mockOnChange}
        onSubmit={mockOnSubmit}
      />
    );

    // Check if error messages are displayed
    expect(screen.getByText('Username is required')).toBeInTheDocument();
    expect(screen.getByText('Password is required')).toBeInTheDocument();
  });

  it('displays submission error', () => {
    render(
      <SigninView
        formData={{ username: '', password: '' }}
        errors={{ username: '', password: '' }}
        submissionError="Login failed"
        onChange={mockOnChange}
        onSubmit={mockOnSubmit}
      />
    );

    // Check if the submission error is displayed
    expect(screen.getByText('Login failed')).toBeInTheDocument();
  });
});
*/