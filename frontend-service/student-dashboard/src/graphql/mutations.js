import { gql } from '@apollo/client';

export const ADD_STUDENT = gql`
  mutation AddStudent($input: StudentInput!) {
    addStudent(input: $input) {
      id
      text
      starRating
      status
      grade {
        studentId
        grade
        gpa
      }
    }
  }
`;

export const UPDATE_STUDENT = gql`
  mutation UpdateStudent($input: StudentInput!) {
    updateStudent(input: $input) {
      id
      text
      starRating
      status
      grade {
        studentId
        grade
        gpa
      }
    }
  }
`;