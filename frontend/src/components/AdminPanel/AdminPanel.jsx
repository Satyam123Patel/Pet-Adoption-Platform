//1
import React from 'react';
import AdminNavbar from "./AdminNavbar";
import AdminFooter from "./AdminFooter";
import AdminScreen from './AdminScreen';

const AdminPanel = () => {
  return (
    <div className="d-flex flex-column min-vh-100 bg-light">
      <AdminNavbar/>
      <main className="flex-grow-1 py-3">
        <div className="container-fluid px-4">
          <AdminScreen/>
        </div>
      </main>
      <AdminFooter/>
    </div>
  );
};

export default AdminPanel;

// import React, { useEffect } from 'react';
// import { useNavigate } from 'react-router-dom';
// import { useAuthContext } from '../../hooks/useAuthContext';

// import AdminNavbar from './AdminNavbar';
// import AdminFooter from './AdminFooter';
// import AdminScreen from './AdminScreen';

// const AdminPanel = () => {
//   const { user } = useAuthContext();
//   const navigate = useNavigate();

//   // ================= AUTH GUARD =================
//   useEffect(() => {
//     // Redirect if not logged in or not admin
//     if (!user || user.role !== 'ADMIN') {
//       navigate('/');
//     }
//   }, [user, navigate]);

//   // Prevent rendering until auth is confirmed
//   if (!user || user.role !== 'ADMIN') {
//     return null; // or a spinner/loading component
//   }

//   return (
//     <div className="d-flex flex-column min-vh-100 bg-light">
//       <AdminNavbar />

//       <main className="flex-grow-1 py-3">
//         <div className="container-fluid px-4">
//           <AdminScreen />
//         </div>
//       </main>

//       <AdminFooter />
//     </div>
//   );
// };

// export default AdminPanel;
