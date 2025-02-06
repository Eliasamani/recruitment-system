import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import SignupView from '../scr/View/SignupView';

test('renders signup form correctly', () => {
  render(<SignupView formData={{ firstname: '', lastname: '', personNumber: '', username: '', email: '', password: '' }} errors={{}} onChange={() => {}} onSubmit={() => {}} />);

  expect(screen.getByText(/Sign Up/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/First Name/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/Last Name/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/YYYYMMDD-XXXX/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/username/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/name@mailservice.ex/i)).toBeInTheDocument();
  expect(screen.getByPlaceholderText(/password/i)).toBeInTheDocument();
});

test('calls onSubmit when form is submitted', () => {
  const mockSubmit = jest.fn();
  render(<SignupView formData={{ firstname: 'John', lastname: 'Doe', personNumber: '19990101-1234', username: 'johndoe', email: 'john@example.com', password: 'securePass' }} errors={{}} onChange={() => {}} onSubmit={mockSubmit} />);
  
  fireEvent.submit(screen.getByRole('form'));
  expect(mockSubmit).toHaveBeenCalled();
});

test('shows validation errors when fields are empty', () => {
  render(<SignupView formData={{ firstname: '', lastname: '', personNumber: '', username: '', email: '', password: '' }} errors={{ firstname: 'Required', lastname: 'Required', personNumber: 'Required', username: 'Required', email: 'Required', password: 'Required' }} onChange={() => {}} onSubmit={() => {}} />);
  
  expect(screen.getByText('Required')).toBeInTheDocument();
});
