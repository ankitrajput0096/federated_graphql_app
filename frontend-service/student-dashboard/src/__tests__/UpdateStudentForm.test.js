import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MockedProvider } from '@apollo/client/testing';
import UpdateStudentForm from '../components/UpdateStudentForm';

// Define mock data inside the test file to avoid the jest.mock restriction
const mockStudent = {
  id: '1',
  text: 'John Doe',
  starRating: 4,
  status: 'A',
  grade: {
    studentId: '1',
    grade: 'A',
    gpa: 3.8,
    rating: 4,
    __typename: 'Grade'
  },
  __typename: 'Student',
};

const mockUniversityData = {
  student: [mockStudent]
};

// Mock the useMutation and useLazyQuery hooks
jest.mock('@apollo/client', () => {
  const originalModule = jest.requireActual('@apollo/client');
  return {
    ...originalModule,
    useMutation: () => [
      jest.fn(),
      { loading: false, error: null }
    ],
    useLazyQuery: () => [
      jest.fn().mockImplementation(() => {
        // Return the mock data directly
        return Promise.resolve({ data: mockUniversityData });
      }),
      { data: mockUniversityData, loading: false, error: null }
    ],
  };
});

describe('UpdateStudentForm', () => {
  it('renders university input and load button', () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <UpdateStudentForm />
      </MockedProvider>
    );
    
    expect(screen.getByPlaceholderText(/enter university id/i)).toBeInTheDocument();
    expect(screen.getByText(/load students/i)).toBeInTheDocument();
  });

  it('loads students for a university', async () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <UpdateStudentForm />
      </MockedProvider>
    );
    
    // Enter university ID and load students
    fireEvent.change(screen.getByPlaceholderText(/enter university id/i), {
      target: { value: 'uni1' }
    });
    fireEvent.click(screen.getByText(/load students/i));
    
    // Wait for students to load
    expect(await screen.findByText(/select a student/i)).toBeInTheDocument();
  });

  it('populates form when student is selected', async () => {
    render(
      <MockedProvider mocks={[]} addTypename={false}>
        <UpdateStudentForm />
      </MockedProvider>
    );
    
    // Enter university ID and load students
    fireEvent.change(screen.getByPlaceholderText(/enter university id/i), {
      target: { value: 'uni1' }
    });
    fireEvent.click(screen.getByText(/load students/i));
    
    // Wait for students to load and select one using the ID
    await waitFor(() => {
      fireEvent.change(screen.getByLabelText(/select student/i), {
        target: { value: '1' }
      });
    });
    
    // Check that form is populated
    expect(screen.getByDisplayValue('John Doe')).toBeInTheDocument();
  });
});