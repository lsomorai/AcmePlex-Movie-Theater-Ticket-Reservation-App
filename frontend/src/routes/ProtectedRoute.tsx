import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requireAuth?: boolean;
  requireAdmin?: boolean;
  allowGuest?: boolean;
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({
  children,
  requireAuth = true,
  requireAdmin = false,
  allowGuest = false,
}) => {
  const { user, isLoading, isAuthenticated, isGuest } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-bg-dark">
        <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-gold"></div>
      </div>
    );
  }

  // If route requires admin access
  if (requireAdmin) {
    if (!isAuthenticated || user?.userType !== 'ADMIN') {
      return <Navigate to="/dashboard" replace />;
    }
  }

  // If route requires authentication
  if (requireAuth) {
    if (!isAuthenticated && !isGuest) {
      return <Navigate to="/login" state={{ from: location }} replace />;
    }

    // If guest is trying to access a route that doesn't allow guests
    if (isGuest && !allowGuest) {
      return <Navigate to="/login" state={{ from: location }} replace />;
    }
  }

  return <>{children}</>;
};

export default ProtectedRoute;
