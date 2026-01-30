import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useMovies } from '@/hooks';
import { adminApi } from '@/api';
import { PageLayout } from '@/components';
import { AuthResponse } from '@/types';
import toast from 'react-hot-toast';
import clsx from 'clsx';

export const AdminNotificationsPage: React.FC = () => {
  const { data: movies, isLoading: moviesLoading } = useMovies();
  const [users, setUsers] = useState<AuthResponse[]>([]);
  const [usersLoading, setUsersLoading] = useState(true);

  const [selectedMovie, setSelectedMovie] = useState<number | null>(null);
  const [selectedUsers, setSelectedUsers] = useState<number[]>([]);
  const [message, setMessage] = useState('');
  const [isSending, setIsSending] = useState(false);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await adminApi.getUsers();
        if (response.success && response.data) {
          setUsers(response.data);
        }
      } catch {
        toast.error('Failed to load users');
      } finally {
        setUsersLoading(false);
      }
    };
    fetchUsers();
  }, []);

  const handleSelectAllUsers = () => {
    if (selectedUsers.length === users.length) {
      setSelectedUsers([]);
    } else {
      setSelectedUsers(users.map(u => u.userId));
    }
  };

  const handleUserToggle = (userId: number) => {
    setSelectedUsers(prev =>
      prev.includes(userId)
        ? prev.filter(id => id !== userId)
        : [...prev, userId]
    );
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!selectedMovie) {
      toast.error('Please select a movie');
      return;
    }

    if (selectedUsers.length === 0) {
      toast.error('Please select at least one user');
      return;
    }

    if (!message.trim()) {
      toast.error('Please enter a message');
      return;
    }

    setIsSending(true);

    try {
      const response = await adminApi.sendNotification({
        movieId: selectedMovie,
        userIds: selectedUsers,
        message: message.trim(),
      });

      if (response.success && response.data) {
        toast.success(`Notification sent to ${response.data.sent} users`);
        setSelectedMovie(null);
        setSelectedUsers([]);
        setMessage('');
      } else {
        throw new Error(response.message || 'Failed to send notification');
      }
    } catch (error) {
      toast.error(error instanceof Error ? error.message : 'Failed to send notification');
    } finally {
      setIsSending(false);
    }
  };

  const selectedMovieTitle = movies?.find(m => m.id === selectedMovie)?.title;

  return (
    <PageLayout>
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="flex items-center justify-between mb-8">
          <h1 className="page-title text-gold-gradient mb-0">Send Notifications</h1>
          <Link to="/admin" className="btn btn-secondary text-sm">
            ← Back to Admin
          </Link>
        </div>

        <form onSubmit={handleSubmit} className="space-y-8">
          {/* Movie Selection */}
          <div className="bg-gradient-card border border-gold/20 rounded-xl p-6">
            <h2 className="font-display text-xl text-gold-gradient mb-4">1. Select Movie</h2>

            {moviesLoading ? (
              <div className="flex justify-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-gold"></div>
              </div>
            ) : (
              <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
                {movies?.map(movie => (
                  <button
                    key={movie.id}
                    type="button"
                    onClick={() => setSelectedMovie(movie.id)}
                    className={clsx(
                      'p-3 rounded-lg border-2 text-left transition-all',
                      selectedMovie === movie.id
                        ? 'border-gold bg-gold/10'
                        : 'border-white/10 hover:border-gold/50'
                    )}
                  >
                    <p className="font-medium text-text-primary truncate">{movie.title}</p>
                    <span
                      className={clsx(
                        'text-xs',
                        movie.status === 'NOW_SHOWING' ? 'text-accent-green' : 'text-gold'
                      )}
                    >
                      {movie.status === 'NOW_SHOWING' ? 'Now Showing' : 'Coming Soon'}
                    </span>
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* User Selection */}
          <div className="bg-gradient-card border border-gold/20 rounded-xl p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="font-display text-xl text-gold-gradient">2. Select Users</h2>
              <button
                type="button"
                onClick={handleSelectAllUsers}
                className="btn btn-secondary text-sm py-1 px-3"
              >
                {selectedUsers.length === users.length ? 'Deselect All' : 'Select All'}
              </button>
            </div>

            {usersLoading ? (
              <div className="flex justify-center py-8">
                <div className="animate-spin rounded-full h-8 w-8 border-t-2 border-b-2 border-gold"></div>
              </div>
            ) : users.length === 0 ? (
              <p className="text-text-muted text-center py-8">No registered users with emails found</p>
            ) : (
              <div className="max-h-64 overflow-y-auto space-y-2">
                {users.map(user => (
                  <label
                    key={user.userId}
                    className={clsx(
                      'flex items-center gap-3 p-3 rounded-lg border cursor-pointer transition-all',
                      selectedUsers.includes(user.userId)
                        ? 'border-gold bg-gold/10'
                        : 'border-white/10 hover:border-gold/50'
                    )}
                  >
                    <input
                      type="checkbox"
                      checked={selectedUsers.includes(user.userId)}
                      onChange={() => handleUserToggle(user.userId)}
                      className="w-4 h-4 accent-gold"
                    />
                    <div className="flex-1">
                      <p className="text-text-primary">{user.username}</p>
                      <p className="text-text-muted text-sm">{user.email}</p>
                    </div>
                  </label>
                ))}
              </div>
            )}

            <p className="text-text-muted text-sm mt-3">
              {selectedUsers.length} user(s) selected
            </p>
          </div>

          {/* Message */}
          <div className="bg-gradient-card border border-gold/20 rounded-xl p-6">
            <h2 className="font-display text-xl text-gold-gradient mb-4">3. Compose Message</h2>

            <textarea
              value={message}
              onChange={(e) => setMessage(e.target.value)}
              className="form-input min-h-32 resize-y"
              placeholder={`e.g., Great news! "${selectedMovieTitle || 'Movie'}" is now available for booking. Don't miss out!`}
              required
            />
          </div>

          {/* Preview */}
          {selectedMovie && selectedUsers.length > 0 && message && (
            <div className="bg-bg-card border border-white/10 rounded-xl p-6">
              <h3 className="text-text-muted text-sm uppercase tracking-wide mb-4">Preview</h3>
              <div className="bg-bg-elevated rounded-lg p-4">
                <p className="text-gold font-semibold mb-2">
                  Notification about: {selectedMovieTitle}
                </p>
                <p className="text-text-primary">{message}</p>
                <p className="text-text-muted text-sm mt-4">
                  Will be sent to {selectedUsers.length} user(s)
                </p>
              </div>
            </div>
          )}

          {/* Submit */}
          <div className="flex justify-end">
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isSending || !selectedMovie || selectedUsers.length === 0 || !message.trim()}
            >
              {isSending ? 'Sending...' : 'Send Notification'}
            </button>
          </div>
        </form>
      </div>
    </PageLayout>
  );
};

export default AdminNotificationsPage;
