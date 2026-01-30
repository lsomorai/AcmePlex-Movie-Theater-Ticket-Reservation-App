import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import clsx from 'clsx';

export const Navbar: React.FC = () => {
  const { user, isAuthenticated, isGuest, logout } = useAuth();
  const navigate = useNavigate();
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link to="/dashboard" className="navbar-brand">
            AcmePlex
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden md:flex items-center space-x-8">
            <Link to="/movies" className="nav-link">
              Movies
            </Link>
            <Link to="/theatres" className="nav-link">
              Theatres
            </Link>
            {isAuthenticated && (
              <Link to="/showtimes" className="nav-link">
                Showtimes
              </Link>
            )}
            <Link to="/cancellation" className="nav-link">
              Cancellation
            </Link>
            {user?.userType === 'ADMIN' && (
              <Link to="/admin" className="nav-link">
                Admin
              </Link>
            )}
          </div>

          {/* User Info */}
          <div className="hidden md:flex items-center space-x-4">
            {(isAuthenticated || isGuest) && user && (
              <span className="px-4 py-1.5 bg-bg-elevated rounded-full text-text-secondary text-sm border border-white/10">
                {isGuest ? 'Guest' : user.username}
              </span>
            )}
            {isAuthenticated ? (
              <button onClick={handleLogout} className="btn btn-outline text-sm py-2 px-4">
                Logout
              </button>
            ) : isGuest ? (
              <Link to="/login" className="btn btn-primary text-sm py-2 px-4">
                Sign In
              </Link>
            ) : (
              <Link to="/login" className="btn btn-primary text-sm py-2 px-4">
                Sign In
              </Link>
            )}
          </div>

          {/* Mobile menu button */}
          <button
            className="md:hidden p-2 rounded-md text-text-secondary hover:text-gold"
            onClick={() => setIsMobileMenuOpen(!isMobileMenuOpen)}
          >
            <svg
              className="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              {isMobileMenuOpen ? (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M6 18L18 6M6 6l12 12"
                />
              ) : (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M4 6h16M4 12h16M4 18h16"
                />
              )}
            </svg>
          </button>
        </div>

        {/* Mobile menu */}
        <div
          className={clsx(
            'md:hidden transition-all duration-300 overflow-hidden',
            isMobileMenuOpen ? 'max-h-96 pb-4' : 'max-h-0'
          )}
        >
          <div className="flex flex-col space-y-2 pt-4">
            <Link
              to="/movies"
              className="nav-link py-2"
              onClick={() => setIsMobileMenuOpen(false)}
            >
              Movies
            </Link>
            <Link
              to="/theatres"
              className="nav-link py-2"
              onClick={() => setIsMobileMenuOpen(false)}
            >
              Theatres
            </Link>
            {isAuthenticated && (
              <Link
                to="/showtimes"
                className="nav-link py-2"
                onClick={() => setIsMobileMenuOpen(false)}
              >
                Showtimes
              </Link>
            )}
            <Link
              to="/cancellation"
              className="nav-link py-2"
              onClick={() => setIsMobileMenuOpen(false)}
            >
              Cancellation
            </Link>
            {user?.userType === 'ADMIN' && (
              <Link
                to="/admin"
                className="nav-link py-2"
                onClick={() => setIsMobileMenuOpen(false)}
              >
                Admin
              </Link>
            )}
            <div className="pt-4 border-t border-white/10">
              {isAuthenticated ? (
                <button
                  onClick={() => {
                    handleLogout();
                    setIsMobileMenuOpen(false);
                  }}
                  className="btn btn-outline w-full"
                >
                  Logout
                </button>
              ) : (
                <Link
                  to="/login"
                  className="btn btn-primary w-full block text-center"
                  onClick={() => setIsMobileMenuOpen(false)}
                >
                  Sign In
                </Link>
              )}
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
