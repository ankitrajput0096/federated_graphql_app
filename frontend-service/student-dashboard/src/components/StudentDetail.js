import React from 'react';
import { useMutation } from '@apollo/client';
import { UPDATE_STUDENT } from '../graphql/mutations';

const StudentDetail = ({ student }) => {
  const [updateStudent, { loading }] = useMutation(UPDATE_STUDENT, {
    optimisticResponse: {
      updateStudent: {
        __typename: 'Student',
        id: student.id,
        text: student.text,
        starRating: student.starRating + 1,
        status: student.status,
        universityId: student.universityId || "unknown",
        grade: {
          __typename: 'Grade',
          studentId: student.grade.studentId,
          grade: student.grade.grade,
          gpa: student.grade.gpa
        }
      }
    }
  });

  const handleIncreaseRating = () => {
    updateStudent({
      variables: {
        input: {
          id: student.id,
          text: student.text,
          starRating: student.starRating + 1,
          universityId: student.universityId || "unknown",
          status: student.status,
          grade: {
            studentId: student.grade.studentId,
            grade: student.grade.grade,
            gpa: student.grade.gpa
          }
        }
      }
    });
  };

  return (
    <div className="student-detail">
      <h3>{student.text}</h3>
      <p><strong>ID:</strong> {student.id}</p>
      <p><strong>Star Rating:</strong> {student.starRating}</p>
      <button onClick={handleIncreaseRating} disabled={loading}>
        {loading ? 'Updating...' : 'Increase Rating'}
      </button>
      <p><strong>Status:</strong> {student.status}</p>
      <p><strong>Grade:</strong> {student.grade.grade} (GPA: {student.grade.gpa})</p>
      {student.grade.rating && <p><strong>Rating:</strong> {student.grade.rating}</p>}
    </div>
  );
};

export default StudentDetail;