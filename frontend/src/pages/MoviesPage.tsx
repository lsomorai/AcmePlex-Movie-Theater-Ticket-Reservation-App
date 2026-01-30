import React, { useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import { useMovies, useMoviesByTheatre, useTheatre } from '@/hooks';
import { PageLayout } from '@/components';
import clsx from 'clsx';
import { MovieStatus } from '@/types';

export const MoviesPage: React.FC = () => {
  const { theatreId } = useParams<{ theatreId?: string }>();
  const [statusFilter, setStatusFilter] = useState<MovieStatus | 'ALL'>('ALL');

  const { data: allMovies, isLoading: loadingAll, error: errorAll } = useMovies();
  const { data: theatreMovies, isLoading: loadingTheatre, error: errorTheatre } = useMoviesByTheatre(
    theatreId ? parseInt(theatreId) : 0
  );
  const { data: theatre } = useTheatre(theatreId ? parseInt(theatreId) : 0);

  const movies = theatreId ? theatreMovies : allMovies;
  const isLoading = theatreId ? loadingTheatre : loadingAll;
  const error = theatreId ? errorTheatre : errorAll;

  const filteredMovies = movies?.filter((movie) => {
    if (statusFilter === 'ALL') return true;
    return movie.status === statusFilter;
  });

  return (
    <PageLayout>
      <div className="max-w-7xl mx-auto px-4 py-8">
        <h1 className="page-title text-gold-gradient">
          {theatre ? `Movies at ${theatre.name}` : 'All Movies'}
        </h1>

        {/* Filter */}
        <div className="flex justify-center gap-4 mb-8">
          <button
            onClick={() => setStatusFilter('ALL')}
            className={clsx(
              'btn',
              statusFilter === 'ALL' ? 'btn-primary' : 'btn-secondary'
            )}
          >
            All
          </button>
          <button
            onClick={() => setStatusFilter('NOW_SHOWING')}
            className={clsx(
              'btn',
              statusFilter === 'NOW_SHOWING' ? 'btn-primary' : 'btn-secondary'
            )}
          >
            Now Showing
          </button>
          <button
            onClick={() => setStatusFilter('COMING_SOON')}
            className={clsx(
              'btn',
              statusFilter === 'COMING_SOON' ? 'btn-primary' : 'btn-secondary'
            )}
          >
            Coming Soon
          </button>
        </div>

        {isLoading && (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-gold"></div>
          </div>
        )}

        {error && (
          <div className="alert alert-danger text-center">
            Failed to load movies. Please try again later.
          </div>
        )}

        {filteredMovies && filteredMovies.length > 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredMovies.map((movie, index) => (
              <Link
                key={movie.id}
                to={theatreId ? `/theatres/${theatreId}/movies/${movie.id}/showtimes` : `/movies/${movie.id}/theatres`}
                className="card group animate-fade-in-up"
                style={{ animationDelay: `${index * 0.1}s` }}
              >
                {/* Movie Poster Placeholder */}
                <div className="aspect-[2/3] bg-gradient-to-br from-bg-elevated to-bg-medium relative overflow-hidden">
                  <div className="absolute inset-0 flex items-center justify-center">
                    <svg
                      className="w-16 h-16 text-gold/20"
                      fill="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path d="M18 4l2 4h-3l-2-4h-2l2 4h-3l-2-4H8l2 4H7L5 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V4h-4z" />
                    </svg>
                  </div>
                  <div className="absolute inset-0 bg-gradient-to-t from-bg-dark/90 via-transparent to-transparent"></div>

                  {/* Status Badge */}
                  <div className="absolute top-3 right-3">
                    <span
                      className={clsx(
                        'badge',
                        movie.status === 'NOW_SHOWING'
                          ? 'badge-success'
                          : 'badge-warning'
                      )}
                    >
                      {movie.status === 'NOW_SHOWING' ? 'Now Showing' : 'Coming Soon'}
                    </span>
                  </div>
                </div>

                <div className="p-4">
                  <h3 className="font-display text-lg text-text-primary group-hover:text-gold transition-colors line-clamp-2">
                    {movie.title}
                  </h3>
                  <p className="text-text-muted text-sm mt-2">
                    {theatreId ? 'View Showtimes' : 'Find Theatres'} →
                  </p>
                </div>
              </Link>
            ))}
          </div>
        )}

        {filteredMovies && filteredMovies.length === 0 && (
          <div className="text-center py-12">
            <p className="text-text-muted">No movies found matching your filter.</p>
          </div>
        )}

        {/* Back Link */}
        {theatreId && (
          <div className="mt-8 text-center">
            <Link to="/theatres" className="btn btn-outline">
              ← Back to Theatres
            </Link>
          </div>
        )}
      </div>
    </PageLayout>
  );
};

export default MoviesPage;
