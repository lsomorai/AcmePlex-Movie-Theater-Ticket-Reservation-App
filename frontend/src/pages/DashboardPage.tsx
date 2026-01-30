import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { useMovieSearch } from '@/hooks';
import { PageLayout } from '@/components';
import clsx from 'clsx';

export const DashboardPage: React.FC = () => {
  const { user, isAuthenticated, isGuest } = useAuth();
  const [searchQuery, setSearchQuery] = useState('');
  const { data: searchResults, isLoading: isSearching } = useMovieSearch(searchQuery);
  const navigate = useNavigate();

  const displayName = isGuest ? 'Guest' : user?.username || 'User';

  return (
    <PageLayout>
      <div className="max-w-4xl mx-auto px-4 py-12">
        {/* Welcome Section */}
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-display mb-2">
            Welcome, <span className="text-gold-gradient">{displayName}</span>
          </h2>
          <p className="text-text-secondary text-lg">
            What would you like to watch today?
          </p>
        </div>

        {/* Search Section */}
        <div className="max-w-xl mx-auto mb-12">
          <div className="relative">
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="form-input pl-12"
              placeholder="Search for a movie..."
            />
            <svg
              className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-text-muted"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
              />
            </svg>
          </div>

          {/* Search Results */}
          {searchQuery.length >= 2 && (
            <div className="mt-4">
              {isSearching ? (
                <div className="text-center py-4">
                  <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-gold mx-auto"></div>
                </div>
              ) : searchResults && searchResults.length > 0 ? (
                <div className="space-y-4">
                  {searchResults.map((result) => (
                    <div
                      key={result.movie.id}
                      className="card p-4 cursor-pointer hover:border-gold/50"
                      onClick={() => navigate(`/movies/${result.movie.id}/theatres`)}
                    >
                      <div className="flex justify-between items-start">
                        <div>
                          <h3 className="font-display text-lg text-text-primary">
                            {result.movie.title}
                          </h3>
                          <span
                            className={clsx(
                              'badge mt-2',
                              result.movie.status === 'NOW_SHOWING'
                                ? 'badge-success'
                                : 'badge-warning'
                            )}
                          >
                            {result.movie.status === 'NOW_SHOWING' ? 'Now Showing' : 'Coming Soon'}
                          </span>
                        </div>
                        <span className="text-gold">→</span>
                      </div>
                      {result.theatres.length > 0 && (
                        <div className="mt-3 pt-3 border-t border-white/10">
                          <p className="text-text-muted text-sm uppercase tracking-wide mb-2">
                            Available at:
                          </p>
                          <div className="flex flex-wrap gap-2">
                            {result.theatres.map((theatre) => (
                              <span
                                key={theatre.id}
                                className="text-sm text-text-secondary bg-bg-elevated px-2 py-1 rounded"
                              >
                                {theatre.name}
                              </span>
                            ))}
                          </div>
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              ) : (
                <p className="text-text-muted text-center py-4">No movies found</p>
              )}
            </div>
          )}
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-xl mx-auto">
          <Link
            to="/movies"
            className="btn btn-primary text-center py-6 text-lg"
          >
            Browse Movies
          </Link>
          <Link
            to="/theatres"
            className="btn btn-secondary text-center py-6 text-lg"
          >
            Browse Theatres
          </Link>
        </div>

        {/* Guest Banner */}
        {isGuest && (
          <div className="mt-12 p-6 bg-gold/10 border border-gold/30 rounded-xl text-center">
            <h3 className="text-gold font-display text-xl mb-2">
              Unlock Premium Features
            </h3>
            <p className="text-text-secondary mb-4">
              Register to access early bookings, premium seats, and more!
            </p>
            <Link to="/register" className="btn btn-primary">
              Become a Member
            </Link>
          </div>
        )}

        {/* Registered User Benefits */}
        {isAuthenticated && user?.userType === 'REGULAR' && (
          <div className="mt-12 p-6 bg-accent-green/10 border border-accent-green/30 rounded-xl text-center">
            <h3 className="text-accent-green font-display text-xl mb-2">
              Member Benefits Active
            </h3>
            <p className="text-text-secondary">
              You have access to early bookings and premium seating!
            </p>
          </div>
        )}
      </div>
    </PageLayout>
  );
};

export default DashboardPage;
