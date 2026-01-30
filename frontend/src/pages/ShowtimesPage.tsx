import React from 'react';
import { Link, useParams } from 'react-router-dom';
import { useShowtimes, useShowtimesByTheatreAndMovie, useMovie, useTheatre } from '@/hooks';
import { PageLayout } from '@/components';
import { useAuth } from '@/contexts/AuthContext';
import clsx from 'clsx';

const getSessionColor = (session: number): string => {
  switch (session) {
    case 1:
      return 'bg-accent-blue';
    case 2:
      return 'bg-gold';
    case 3:
      return 'bg-accent-red';
    default:
      return 'bg-bg-elevated';
  }
};

const formatDate = (dateStr: string): string => {
  const date = new Date(dateStr);
  return date.toLocaleDateString('en-US', {
    weekday: 'short',
    month: 'short',
    day: 'numeric',
  });
};

export const ShowtimesPage: React.FC = () => {
  const { theatreId, movieId } = useParams<{ theatreId?: string; movieId?: string }>();
  const { isAuthenticated, isGuest } = useAuth();

  const parsedTheatreId = theatreId ? parseInt(theatreId) : 0;
  const parsedMovieId = movieId ? parseInt(movieId) : 0;

  const { data: allShowtimes, isLoading: loadingAll, error: errorAll } = useShowtimes();
  const { data: filteredShowtimes, isLoading: loadingFiltered, error: errorFiltered } = useShowtimesByTheatreAndMovie(
    parsedTheatreId,
    parsedMovieId
  );
  const { data: movie } = useMovie(parsedMovieId);
  const { data: theatre } = useTheatre(parsedTheatreId);

  const showtimes = (theatreId && movieId) ? filteredShowtimes : allShowtimes;
  const isLoading = (theatreId && movieId) ? loadingFiltered : loadingAll;
  const error = (theatreId && movieId) ? errorFiltered : errorAll;

  // Group showtimes by date
  const groupedShowtimes = showtimes?.reduce((acc, showtime) => {
    const date = showtime.date;
    if (!acc[date]) {
      acc[date] = [];
    }
    acc[date].push(showtime);
    return acc;
  }, {} as Record<string, typeof showtimes>);

  return (
    <PageLayout>
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Movie/Theatre Info Header */}
        {(movie || theatre) && (
          <div className="bg-bg-card border border-white/5 rounded-xl p-6 mb-8 text-center">
            {movie && (
              <h2 className="font-display text-2xl text-gold-gradient mb-2">
                {movie.title}
              </h2>
            )}
            {theatre && (
              <p className="text-text-secondary">at {theatre.name}</p>
            )}
            {movie?.status === 'COMING_SOON' && (
              <div className="mt-4">
                <span className="badge badge-warning">Coming Soon - Early Access</span>
                {!isAuthenticated && !isGuest && (
                  <p className="text-text-muted text-sm mt-2">
                    Only registered members can book early access tickets
                  </p>
                )}
              </div>
            )}
          </div>
        )}

        <h1 className="page-title text-gold-gradient">
          {theatreId && movieId ? 'Select Showtime' : 'All Showtimes'}
        </h1>

        {isLoading && (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-gold"></div>
          </div>
        )}

        {error && (
          <div className="alert alert-danger text-center">
            Failed to load showtimes. Please try again later.
          </div>
        )}

        {groupedShowtimes && Object.keys(groupedShowtimes).length > 0 && (
          <div className="space-y-8">
            {Object.entries(groupedShowtimes)
              .sort(([a], [b]) => a.localeCompare(b))
              .map(([date, dateShowtimes]) => (
                <div key={date}>
                  <h3 className="font-display text-xl text-text-primary mb-4 border-b border-white/10 pb-2">
                    {formatDate(date)}
                  </h3>
                  <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
                    {dateShowtimes?.map((showtime, index) => (
                      <Link
                        key={showtime.id}
                        to={`/seats?showtimeId=${showtime.id}`}
                        className="card p-4 text-center group animate-fade-in-up"
                        style={{ animationDelay: `${index * 0.05}s` }}
                      >
                        <div className="mb-3">
                          <span
                            className={clsx(
                              'badge text-white',
                              getSessionColor(showtime.session)
                            )}
                          >
                            Session {showtime.session}
                          </span>
                        </div>
                        <p className="text-2xl font-semibold text-text-primary group-hover:text-gold transition-colors">
                          {showtime.sessionTime}
                        </p>
                        {!theatreId && (
                          <p className="text-text-muted text-sm mt-2">
                            {showtime.movie.title}
                          </p>
                        )}
                        {!theatreId && (
                          <p className="text-text-secondary text-sm">
                            {showtime.theatre.name}
                          </p>
                        )}
                        <p className="text-gold text-sm mt-3">
                          Select Seats →
                        </p>
                      </Link>
                    ))}
                  </div>
                </div>
              ))}
          </div>
        )}

        {groupedShowtimes && Object.keys(groupedShowtimes).length === 0 && (
          <div className="text-center py-12">
            <p className="text-text-muted">No showtimes available.</p>
          </div>
        )}

        {/* Session Legend */}
        <div className="mt-12 flex flex-wrap justify-center gap-6">
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded bg-accent-blue"></div>
            <span className="text-text-secondary text-sm">Morning (10:00 AM)</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded bg-gold"></div>
            <span className="text-text-secondary text-sm">Afternoon (2:00 PM)</span>
          </div>
          <div className="flex items-center gap-2">
            <div className="w-4 h-4 rounded bg-accent-red"></div>
            <span className="text-text-secondary text-sm">Evening (7:00 PM)</span>
          </div>
        </div>

        {/* Back Link */}
        {theatreId && movieId && (
          <div className="mt-8 text-center">
            <Link to={`/theatres/${theatreId}/movies`} className="btn btn-outline">
              ← Back to Movies
            </Link>
          </div>
        )}
      </div>
    </PageLayout>
  );
};

export default ShowtimesPage;
