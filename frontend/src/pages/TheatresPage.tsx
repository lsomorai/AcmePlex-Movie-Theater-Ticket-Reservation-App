import React from 'react';
import { Link } from 'react-router-dom';
import { useTheatres } from '@/hooks';
import { PageLayout } from '@/components';

export const TheatresPage: React.FC = () => {
  const { data: theatres, isLoading, error } = useTheatres();

  return (
    <PageLayout>
      <div className="max-w-7xl mx-auto px-4 py-8">
        <h1 className="page-title text-gold-gradient">Our Theatres</h1>

        {isLoading && (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-gold"></div>
          </div>
        )}

        {error && (
          <div className="alert alert-danger text-center">
            Failed to load theatres. Please try again later.
          </div>
        )}

        {theatres && theatres.length > 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
            {theatres.map((theatre, index) => (
              <Link
                key={theatre.id}
                to={`/theatres/${theatre.id}/movies`}
                className="card group animate-fade-in-up"
                style={{ animationDelay: `${index * 0.1}s` }}
              >
                {/* Theatre Image Placeholder */}
                <div className="h-48 bg-gradient-to-br from-bg-elevated to-bg-medium relative overflow-hidden">
                  <div className="absolute inset-0 flex items-center justify-center">
                    <svg
                      className="w-20 h-20 text-gold/20"
                      fill="currentColor"
                      viewBox="0 0 24 24"
                    >
                      <path d="M18 3v2h-2V3H8v2H6V3H4v18h2v-2h2v2h8v-2h2v2h2V3h-2zM8 17H6v-2h2v2zm0-4H6v-2h2v2zm0-4H6V7h2v2zm10 8h-2v-2h2v2zm0-4h-2v-2h2v2zm0-4h-2V7h2v2z" />
                    </svg>
                  </div>
                  <div className="absolute inset-0 bg-gradient-to-t from-bg-dark/90 to-transparent"></div>
                </div>

                <div className="p-6 relative -mt-12">
                  <h3 className="font-display text-xl text-text-primary group-hover:text-gold transition-colors">
                    {theatre.name}
                  </h3>
                  <p className="text-text-secondary text-sm mt-2">
                    View Movies →
                  </p>
                </div>
              </Link>
            ))}
          </div>
        )}

        {theatres && theatres.length === 0 && (
          <div className="text-center py-12">
            <p className="text-text-muted">No theatres available at the moment.</p>
          </div>
        )}
      </div>
    </PageLayout>
  );
};

export default TheatresPage;
