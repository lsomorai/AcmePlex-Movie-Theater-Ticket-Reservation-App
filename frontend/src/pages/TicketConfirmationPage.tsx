import React from 'react';
import { useLocation, useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { PageLayout } from '@/components';
import { ShowtimeResponse, SeatResponse } from '@/types';
import clsx from 'clsx';

interface LocationState {
  showtimeId: number;
  seatIds: number[];
  showtime: ShowtimeResponse;
  selectedSeats: SeatResponse[];
  totalPrice: number;
}

export const TicketConfirmationPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, isGuest } = useAuth();

  const state = location.state as LocationState | null;

  if (!state || !state.showtime || !state.selectedSeats) {
    return <Navigate to="/dashboard" replace />;
  }

  const { showtime, selectedSeats, totalPrice, showtimeId, seatIds } = state;

  const handleProceedToPayment = () => {
    navigate('/ticket-payment', {
      state: {
        showtimeId,
        seatIds,
        showtime,
        selectedSeats,
        totalPrice,
      },
    });
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
      <div className="max-w-3xl mx-auto px-4 py-8">
        <h1 className="page-title text-gold-gradient">Confirm Your Booking</h1>

        {/* Movie Ticket Card */}
        <div className="movie-ticket mt-8">
          {/* Header */}
          <div className="border-b-2 border-dashed border-gold/30 pb-6 mb-6">
            <h2 className="text-gold-gradient text-3xl font-display mb-2">
              {showtime.movie.title}
            </h2>
            <p className="text-text-secondary text-lg">
              {showtime.theatre.name}
            </p>
          </div>

          {/* Details Grid */}
          <div className="grid grid-cols-2 gap-6">
            <div>
              <p className="text-text-muted text-xs uppercase tracking-wider mb-1">Date</p>
              <p className="text-text-primary text-lg">{formatDate(showtime.date)}</p>
            </div>
            <div>
              <p className="text-text-muted text-xs uppercase tracking-wider mb-1">Time</p>
              <p className="text-text-primary text-lg">{showtime.sessionTime}</p>
            </div>
            <div>
              <p className="text-text-muted text-xs uppercase tracking-wider mb-1">Session</p>
              <p className="text-text-primary text-lg">Session {showtime.session}</p>
            </div>
            <div>
              <p className="text-text-muted text-xs uppercase tracking-wider mb-1">Tickets</p>
              <p className="text-text-primary text-lg">{selectedSeats.length}</p>
            </div>
          </div>

          {/* Seats Section */}
          <div className="border-t-2 border-dashed border-gold/30 pt-6 mt-6">
            <p className="text-text-muted text-xs uppercase tracking-wider mb-3">Selected Seats</p>
            <div className="flex flex-wrap gap-2">
              {selectedSeats.map(seat => (
                <span
                  key={seat.id}
                  className={clsx(
                    'badge text-sm',
                    seat.seatType === 'special' ? 'badge-warning' : 'badge-primary'
                  )}
                >
                  {seat.seatLabel} - ${seat.price.toFixed(2)}
                </span>
              ))}
            </div>
          </div>

          {/* Total */}
          <div className="border-t-2 border-dashed border-gold/30 pt-6 mt-6 text-right">
            <p className="text-text-muted text-xs uppercase tracking-wider mb-1">Total Amount</p>
            <p className="text-gold-gradient text-4xl font-display font-bold">
              ${totalPrice.toFixed(2)}
            </p>
          </div>
        </div>

        {/* User Info */}
        <div className="mt-6 p-4 bg-bg-card border border-white/5 rounded-lg">
          <p className="text-text-muted text-sm">
            Booking as: <span className="text-text-primary font-medium">{isGuest ? 'Guest' : user?.username}</span>
          </p>
          {isGuest && (
            <p className="text-text-muted text-xs mt-1">
              Note: Guest bookings have a $5 admin fee on cancellations
            </p>
          )}
        </div>

        {/* Actions */}
        <div className="mt-8 flex flex-wrap gap-4 justify-center">
          <button
            onClick={() => navigate(-1)}
            className="btn btn-secondary"
          >
            ← Modify Selection
          </button>
          <button
            onClick={handleProceedToPayment}
            className="btn btn-primary"
          >
            Proceed to Payment
          </button>
        </div>
      </div>
    </PageLayout>
  );
};

export default TicketConfirmationPage;
