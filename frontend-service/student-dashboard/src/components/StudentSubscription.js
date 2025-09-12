import React, { useEffect, useState } from 'react';
import { useSubscription } from '@apollo/client';
import { STUDENT_UPDATED } from '../graphql/subscriptions';
import StudentDetailView from './StudentDetailView';

const StudentSubscription = ({ studentId }) => {
  const [latestUpdate, setLatestUpdate] = useState(null);
  const [updateCount, setUpdateCount] = useState(0);
  const [isSubscribed, setIsSubscribed] = useState(true);
  
  const { data, error } = useSubscription(STUDENT_UPDATED, {
    variables: { studentId },
    skip: !studentId || !isSubscribed
  });

  useEffect(() => {
    if (data) {
      setLatestUpdate(data.studentUpdated);
      setUpdateCount(prev => prev + 1);
    }
  }, [data]);

  const toggleSubscription = () => {
    setIsSubscribed(!isSubscribed);
  };

  if (error) return <div className="error">Error in subscription: {error.message}</div>;

  return (
    <div className="student-subscription">
      <div className="subscription-header">
        <h3>Real-time Updates for Student {studentId}</h3>
        <button 
          onClick={toggleSubscription} 
          className={`subscription-toggle ${isSubscribed ? 'subscribed' : 'unsubscribed'}`}
        >
          {isSubscribed ? 'Stop Subscription' : 'Start Subscription'}
        </button>
      </div>
      
      <p>Update count: {updateCount}</p>
      <p>Status: {isSubscribed ? 'Subscribed' : 'Not Subscribed'}</p>
      
      {latestUpdate ? (
        <div className="subscription-update">
          <h4>Latest Update:</h4>
          <StudentDetailView student={latestUpdate} />
          <p><strong>Updated at:</strong> {new Date().toLocaleTimeString()}</p>
        </div>
      ) : (
        <p>No updates received yet.</p>
      )}
    </div>
  );
};

export default StudentSubscription;