import { GET_ALL_STUDENTS, GET_STUDENTS_BY_UNIVERSITY, ADD_STUDENT, UPDATE_STUDENT } from '../graphql/queries';

// Mock student data
export const mockStudent = {
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

// Mock responses for GraphQL operations
export const getAllStudentsMock = {
  request: {
    query: GET_ALL_STUDENTS,
  },
  result: {
    data: {
      allStudents: [mockStudent],
    },
  },
};

export const getStudentsByUniversityMock = {
  request: {
    query: GET_STUDENTS_BY_UNIVERSITY,
    variables: { universityId: 'uni1' },
  },
  result: {
    data: {
      student: [mockStudent],
    },
  },
};

export const addStudentMock = {
  request: {
    query: ADD_STUDENT,
    variables: {
      input: {
        id: '1',
        text: 'John Doe',
        starRating: 4,
        universityId: 'uni1',
        status: 'A',
        grade: {
          studentId: '1',
          grade: 'A',
          gpa: 3.8
        }
      }
    }
  },
  result: {
    data: {
      addStudent: mockStudent,
    },
  },
};

export const updateStudentMock = {
  request: {
    query: UPDATE_STUDENT,
    variables: {
      input: {
        id: '1',
        text: 'John Doe Updated',
        starRating: 5,
        universityId: 'uni1',
        status: 'A',
        grade: {
          studentId: '1',
          grade: 'A',
          gpa: 4.0
        }
      }
    }
  },
  result: {
    data: {
      updateStudent: {
        ...mockStudent,
        text: 'John Doe Updated',
        starRating: 5,
        grade: {
          ...mockStudent.grade,
          gpa: 4.0
        }
      },
    },
  },
};

export const errorMock = {
  request: {
    query: GET_ALL_STUDENTS,
  },
  error: new Error('Something went wrong'),
};

// Add this to the bottom of each empty test file
test('placeholder', () => {
  expect(true).toBe(true);
});