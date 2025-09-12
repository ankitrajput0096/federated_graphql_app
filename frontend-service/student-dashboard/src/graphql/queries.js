import { gql } from '@apollo/client';

export const GET_ALL_STUDENTS = gql`
  query GetAllStudents {
    allStudents {
      id
      text
      starRating
      status
      grade {
        studentId
        grade
        gpa
        rating(filter: 1)
      }
    }
  }
`;

export const GET_STUDENTS_BY_UNIVERSITY = gql`
  query GetStudentsByUniversity($universityId: ID!) {
    student(universityId: $universityId) {
      id
      text
      starRating
      status
      grade {
        studentId
        grade
        gpa
        rating(filter: 1)
      }
    }
  }
`;