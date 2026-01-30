import React, { useState } from 'react';
import { useLocation, Link, Navigate } from 'react-router-dom';
import { PageLayout } from '@/components';
import { ShowtimeResponse, SeatResponse } from '@/types';
import toast from 'react-hot-toast';

interface LocationState {
  referenceNumber: string;
  showtime: ShowtimeResponse;
  selectedSeats: SeatResponse[];
  totalPrice: number;
  email?: string;
}

export const BookingSuccessPage: React.FC = () => {
  const location = useLocation();
  const state = location.state as LocationState | null;
  const [copied, setCopied] = useState(false);

  if (!state || !state.referenceNumber) {
    return <Navigate to="/dashboard" replace />;
  }

  const { referenceNumber, showtime, selectedSeats, totalPrice, email } = state;

  const handleCopyReference = async () => {
    try {
      await navigator.clipboard.writeText(referenceNumber);
      setCopied(true);
      toast.success('Reference number copied!');
      setTimeout(() => setCopied(false), 2000);
    } catch {
      toast.error('Failed to copy');
    }
  };

  const formatDate = (dateStr: string): string => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  return (
    <PageLayout>
      <div className="max-w-2xl mx-auto px-4 py-12">
        {/* Success Container */}
        <div className="bg-gradient-card border border-accent-green/30 rounded-xl p-8 text-center shadow-lg">
          {/* Success Icon */}
          <div className="text-6xl text-accent-green mb-6 animate-pulse-slow">
            ✓
          </div>

          <h1 className="text-3xl font-display text-accent-green mb-2">
            Booking Confirmed!
          </h1>
          <p className="text-text-secondary text-lg mb-8">
            Your tickets have been booked successfully
          </p>

          {/* Reference Number */}
          <div className="bg-bg-elevated border border-accent-green/30 rounded-lg p-6 mb-8">
            <p className="text-text-muted text-sm uppercase tracking-wider mb-2">
              Reference Number
            </p>
            <div className="flex items-center justify-center gap-4">
              <span className="text-2xl font-mono text-accent-green font-bold">
                {referenceNumber}
              </span>
              <button
                onClick={handleCopyReference}
                className="p-2 hover:bg-white/5 rounded transition-colors"
                title="Copy to clipboard"
              >
                {copied ? (
                  <span className="text-accent-green">✓</span>
                ) : (
                  <svg
                    className="w-5 h-5 text-text-secondary hover:text-gold"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-6 12h8a2 2 0 002-2v-8a2 2 0 00-2-2h-8a2 2 0 00-2 2v8a2 2 0 002 2z"
                    />
                  </svg>
                )}
              </button>
            </div>
            <p className="text-text-muted text-sm mt-2">
              Save this number for ticket cancellation
            </p>
          </div>

          {/* Booking Details */}
          <div className="bg-bg-elevated rounded-lg p-6 text-left mb-8">
            <h3 className="font-display text-lg text-text-primary mb-4 text-center">
              Booking Details
            </h3>
            <div className="space-y-3">
              <div className="flex justify-between">
                <span className="text-text-muted">Movie</span>
                <span className="text-text-primary font-medium">{showtime.movie.title}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-text-muted">Theatre</span>
                <span className="text-text-primary">{showtime.theatre.name}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-text-muted">Date</span>
                <span className="text-text-primary">{formatDate(showtime.date)}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-text-muted">Time</span>
                <span className="text-text-primary">{showtime.sessionTime}</span>
              </div>
              <div className="flex justify-between">
                <span className="text-text-muted">Seats</span>
                <span className="text-text-primary">
                  {selectedSeats.map(s => s.seatLabel).join(', ')}
                </span>
              </div>
              <div className="border-t border-white/10 pt-3 flex justify-between">
                <span className="text-text-muted font-semibold">Total Paid</span>
                <span className="text-gold font-bold">${totalPrice.toFixed(2)}</span>
              </div>
            </div>
          </div>

          {/* Email Confirmation */}
          {email && (
            <div className="bg-accent-green/10 border border-accent-green/30 rounded-lg p-4 mb-8">
              <p className="text-accent-green">
                Confirmation email sent to <strong>{email}</strong>
              </p>
            </div>
          )}

          {/* Actions */}
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/dashboard" className="btn btn-primary">
              Back to Home
            </Link>
            <Link to="/movies" className="btn btn-secondary">
              Book More Tickets
            </Link>
          </div>
        </div>

        {/* Cancellation Info */}
        <div className="mt-8 p-4 bg-bg-card/50 rounded-lg border border-white/5 text-center">
          <p className="text-text-muted text-sm">
            Need to cancel? Visit the{' '}
            <Link to="/cancellation" className="text-gold hover:text-gold-light underline">
              Cancellation page
            </Link>{' '}
            with your reference number. Cancellations must be made at least 72 hours before showtime.
          </p>
        </div>
      </div>
    </PageLayout>
  );
};

export default BookingSuccessPage;
