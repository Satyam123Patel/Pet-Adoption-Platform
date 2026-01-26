//1
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuthContext } from '../hooks/useAuthContext';

const ProtectedRoute = ({ children, adminOnly = false }) => {
  const { user } = useAuthContext();

  // Not logged in at all -> redirect to user auth
  if (!user) {
    return <Navigate to="/auth" replace />;
  }

  // Route requires admin, but user is not admin -> redirect to home
  if (adminOnly && user.role !== 'ADMIN') {
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;

// import { Navigate } from 'react-router-dom';
// import { useAuthContext } from '../hooks/useAuthContext';

// export const ProtectedRoute = ({ children, requireAdmin = false }) => {
//   const { user } = useAuthContext();

//   // User not logged in → redirect to auth page
//   if (!user) {
//     return <Navigate to="/auth" replace />;
//   }

//   // Admin access required but user is not admin → redirect home
//   if (requireAdmin && user.role !== 'ADMIN') {
//     return <Navigate to="/" replace />;
//   }

//   return children;
// };

// export default ProtectedRoute;
