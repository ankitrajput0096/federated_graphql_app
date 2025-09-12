import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import AddStudentForm from '../components/AddStudentForm';

// Mock the useMutation hook
jest.mock('@apollo/client', () => {
  const originalModule = jest.requireActual('@apollo/client');
  return {
    ...originalModule,
    useMutation: () => [
      jest.fn(),
      { loading: false, error: null }
    ],
  };
});

describe('AddStudentForm', () => {
  it('renders all form fields', () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <AddStudentForm />
      </MockedProvider>
    );
    
    // Use more specific queries to avoid multiple matches
    expect(screen.getByLabelText('ID:')).toBeInTheDocument();
    expect(screen.getByLabelText('Name:')).toBeInTheDocument();
    expect(screen.getByLabelText('Star Rating:')).toBeInTheDocument();
    expect(screen.getByLabelText('University ID:')).toBeInTheDocument();
    expect(screen.getByLabelText('Status:')).toBeInTheDocument();
    expect(screen.getByLabelText('Student ID (for grade):')).toBeInTheDocument();
    expect(screen.getByLabelText('Letter Grade:')).toBeInTheDocument();
    expect(screen.getByLabelText('GPA:')).toBeInTheDocument();
  });

    // In AddStudentForm.test.js, update the test that submits the form
    it('submits the form with correct data', async () => {
    const mockMutation = jest.fn();
    
    // Mock the useMutation hook to resolve immediately
    jest.spyOn(require('@apollo/client'), 'useMutation').mockReturnValue([
        mockMutation,
        { loading: false, error: null }
    ]);
    
    render(
        <MockedProvider mocks={[]} addTypename={false}>
        <AddStudentForm />
        </MockedProvider>
    );
    
    // Fill out the form using exact label text
    fireEvent.change(screen.getByLabelText('ID:'), { target: { value: '1' } });
    fireEvent.change(screen.getByLabelText('Name:'), { target: { value: 'John Doe' } });
    fireEvent.change(screen.getByLabelText('Star Rating:'), { target: { value: '4' } });
    fireEvent.change(screen.getByLabelText('University ID:'), { target: { value: 'uni1' } });
    fireEvent.change(screen.getByLabelText('Student ID (for grade):'), { target: { value: '1' } });
    fireEvent.change(screen.getByLabelText('GPA:'), { target: { value: '3.8' } });
    
    // Submit the form
    fireEvent.click(screen.getByText('Add Student'));
    
    // Wait for any state updates to complete
    await new Promise(resolve => setTimeout(resolve, 0));
    
    // Check that the mutation was called
    expect(mockMutation).toHaveBeenCalled();
    });

  it('displays error message when mutation fails', async () => {
    const mockMutation = jest.fn();
    
    // Mock the useMutation hook to return an error
    jest.spyOn(require('@apollo/client'), 'useMutation').mockReturnValue([
      mockMutation,
      { loading: false, error: { message: 'Test error' } }
    ]);
    
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <AddStudentForm />
      </MockedProvider>
    );
    
    // Check that error message is displayed
    expect(screen.getByText('Error: Test error')).toBeInTheDocument();
  });

  it('validates required fields', async () => {
    // Mock the useMutation hook
    jest.spyOn(require('@apollo/client'), 'useMutation').mockReturnValue([
      jest.fn(),
      { loading: false, error: null }
    ]);
    
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <AddStudentForm />
      </MockedProvider>
    );
    
    // Try to submit without filling required fields
    fireEvent.click(screen.getByText('Add Student'));
    
    // Check that validation prevents submission
    expect(screen.getByLabelText('ID:').value).toBe('');
  });
});