import { gql } from '@apollo/client';

export const STUDENT_UPDATED = gql`
  subscription OnStudentUpdated($studentId: ID!) {
    studentUpdated(studentId: $studentId) {
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