import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { ProtectedRoute } from '@/routes/ProtectedRoute';
import {
  LoginPage,
  RegisterPage,
  PaymentPage,
  DashboardPage,
  TheatresPage,
  MoviesPage,
  ShowtimesPage,
  SeatsPage,
  TicketConfirmationPage,
  TicketPaymentPage,
  BookingSuccessPage,
  CancellationPage,
  AdminDashboardPage,
  AdminNotificationsPage,
} from '@/pages';

const App: React.FC = () => {
  return (
    <Routes>
      {/* Public Auth Routes */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/payment" element={<PaymentPage />} />

      {/* Public Routes (accessible to all, including guests) */}
      <Route path="/dashboard" element={<DashboardPage />} />
      <Route path="/theatres" element={<TheatresPage />} />
      <Route path="/theatres/:theatreId/movies" element={<MoviesPage />} />
      <Route path="/movies" element={<MoviesPage />} />
      <Route path="/movies/:movieId/theatres" element={<TheatresPage />} />
      <Route path="/cancellation" element={<CancellationPage />} />

      {/* Routes that require user or guest */}
      <Route
        path="/theatres/:theatreId/movies/:movieId/showtimes"
        element={
          <ProtectedRoute allowGuest>
            <ShowtimesPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/showtimes"
        element={
          <ProtectedRoute allowGuest>
            <ShowtimesPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/seats"
        element={
          <ProtectedRoute allowGuest>
            <SeatsPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/ticket-confirmation"
        element={
          <ProtectedRoute allowGuest>
            <TicketConfirmationPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/ticket-payment"
        element={
          <ProtectedRoute allowGuest>
            <TicketPaymentPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/booking-success"
        element={
          <ProtectedRoute allowGuest>
            <BookingSuccessPage />
          </ProtectedRoute>
        }
      />

      {/* Admin Routes */}
      <Route
        path="/admin"
        element={
          <ProtectedRoute requireAdmin>
            <AdminDashboardPage />
          </ProtectedRoute>
        }
      />
      <Route
        path="/admin/notifications"
        element={
          <ProtectedRoute requireAdmin>
            <AdminNotificationsPage />
          </ProtectedRoute>
        }
      />

      {/* Redirects */}
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="*" element={<Navigate to="/dashboard" replace />} />
    </Routes>
  );
};

export default App;
