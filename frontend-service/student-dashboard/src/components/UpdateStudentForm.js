import React, { useState, useEffect } from 'react';
import { useLazyQuery, useMutation } from '@apollo/client';
import { GET_STUDENTS_BY_UNIVERSITY, GET_ALL_STUDENTS } from '../graphql/queries';
import { UPDATE_STUDENT } from '../graphql/mutations';

const UpdateStudentForm = () => {
  const [universityId, setUniversityId] = useState('');
  const [selectedStudentId, setSelectedStudentId] = useState('');
  const [formData, setFormData] = useState({
    id: '',
    text: '',
    starRating: 0,
    universityId: '',
    status: 'A',
    grade: {
      studentId: '',
      grade: 'A',
      gpa: 0.0
    }
  });

  // Query to get students by university
  const [getStudents, { data: universityData, loading: loadingStudents, error: universityError }] = useLazyQuery(
    GET_STUDENTS_BY_UNIVERSITY,
    {
      variables: { universityId },
      fetchPolicy: 'network-only'
    }
  );

  // Mutation to update student
  const [updateStudent, { loading: updating, error: updateError }] = useMutation(UPDATE_STUDENT, {
    refetchQueries: [{ query: GET_ALL_STUDENTS }],
    onCompleted: () => {
      // Reset form after successful update
      setFormData({
        id: '',
        text: '',
        starRating: 0,
        universityId: '',
        status: 'A',
        grade: {
          studentId: '',
          grade: 'A',
          gpa: 0.0
        }
      });
      setSelectedStudentId('');
      alert('Student updated successfully!');
    }
  });

  // Effect to populate form when a student is selected
  useEffect(() => {
    if (selectedStudentId && universityData && universityData.student) {
      const student = universityData.student.find(s => s.id === selectedStudentId);
      if (student) {
        setFormData({
          id: student.id,
          text: student.text || '',
          starRating: student.starRating || 0,
          universityId: universityId,
          status: student.status || 'A',
          grade: {
            studentId: student.grade?.studentId || '',
            grade: student.grade?.grade || 'A',
            gpa: student.grade?.gpa || 0.0
          }
        });
      }
    }
  }, [selectedStudentId, universityData, universityId]);

  const handleUniversitySearch = () => {
    if (universityId.trim()) {
      getStudents();
      setSelectedStudentId(''); // Reset selected student when university changes
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    
    if (name.startsWith('grade.')) {
      const gradeField = name.split('.')[1];
      setFormData(prev => ({
        ...prev,
        grade: {
          ...prev.grade,
          [gradeField]: gradeField === 'gpa' ? parseFloat(value) || 0 : value
        }
      }));
    } else {
      setFormData(prev => ({
        ...prev,
        [name]: name === 'starRating' ? parseInt(value) : value
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await updateStudent({ 
        variables: { 
          input: {
            ...formData,
            starRating: parseInt(formData.starRating),
            grade: {
              ...formData.grade,
              gpa: parseFloat(formData.grade.gpa)
            }
          } 
        } 
      });
    } catch (err) {
      console.error('Error updating student:', err);
    }
  };

  return (
    <div className="update-student-form">
      <h2>Update Student Details</h2>
      
      <div className="university-selection">
        <div className="form-group">
          <label htmlFor="universityIdInput">University ID:</label>
          <div className="search-controls">
            <input
              id="universityIdInput"
              type="text"
              value={universityId}
              onChange={(e) => setUniversityId(e.target.value)}
              placeholder="Enter University ID"
            />
            <button 
              onClick={handleUniversitySearch} 
              disabled={loadingStudents || !universityId.trim()}
            >
              {loadingStudents ? 'Loading...' : 'Load Students'}
            </button>
          </div>
        </div>
      </div>

      {universityError && <div className="error">Error: {universityError.message}</div>}

      {universityData && universityData.student && (
        <div className="student-selection">
          <div className="form-group">
            <label htmlFor="studentSelect">Select Student:</label>
            <select
              id="studentSelect"
              value={selectedStudentId}
              onChange={(e) => setSelectedStudentId(e.target.value)}
            >
              <option value="">Select a student</option>
              {universityData.student.map(student => (
                <option key={student.id} value={student.id}>
                  {student.text} (ID: {student.id})
                </option>
              ))}
            </select>
          </div>
        </div>
      )}

      {selectedStudentId && (
        <form onSubmit={handleSubmit} className="student-form">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="studentId">ID:</label>
              <input
                id="studentId"
                type="text"
                name="id"
                value={formData.id}
                onChange={handleChange}
                required
                disabled // ID should not be editable
              />
            </div>
            <div className="form-group">
              <label htmlFor="studentName">Name:</label>
              <input
                id="studentName"
                type="text"
                name="text"
                value={formData.text}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="starRating">Star Rating:</label>
              <input
                id="starRating"
                type="number"
                name="starRating"
                value={formData.starRating}
                onChange={handleChange}
                min="0"
                max="5"
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="universityIdDisplay">University ID:</label>
              <input
                id="universityIdDisplay"
                type="text"
                name="universityId"
                value={formData.universityId}
                onChange={handleChange}
                required
                disabled // University ID should not be editable
              />
            </div>
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="status">Status:</label>
              <select id="status" name="status" value={formData.status} onChange={handleChange}>
                <option value="A">A</option>
                <option value="B">B</option>
                <option value="C">C</option>
              </select>
            </div>
          </div>
          
          <div className="form-section">
            <h3>Grade Information</h3>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="gradeStudentId">Student ID (for grade):</label>
                <input
                  id="gradeStudentId"
                  type="text"
                  name="grade.studentId"
                  value={formData.grade.studentId}
                  onChange={handleChange}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="letterGrade">Letter Grade:</label>
                <select id="letterGrade" name="grade.grade" value={formData.grade.grade} onChange={handleChange}>
                  <option value="A">A</option>
                  <option value="B">B</option>
                  <option value="C">C</option>
                  <option value="D">D</option>
                  <option value="F">F</option>
                </select>
              </div>
            </div>
            
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="gpa">GPA:</label>
                <input
                  id="gpa"
                  type="number"
                  name="grade.gpa"
                  value={formData.grade.gpa}
                  onChange={handleChange}
                  step="0.1"
                  min="0"
                  max="4.0"
                  required
                />
              </div>
            </div>
          </div>
          
          <button type="submit" disabled={updating} className="submit-btn">
            {updating ? 'Updating...' : 'Update Student'}
          </button>
          
          {updateError && <div className="error">Error: {updateError.message}</div>}
        </form>
      )}
    </div>
  );
};

export default UpdateStudentForm;