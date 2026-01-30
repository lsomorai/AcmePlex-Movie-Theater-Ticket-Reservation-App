import React, { useState, useMemo } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { useSeats, useShowtime } from '@/hooks';
import { useAuth } from '@/contexts/AuthContext';
import { PageLayout } from '@/components';
import { SeatResponse } from '@/types';
import clsx from 'clsx';
import toast from 'react-hot-toast';

export const SeatsPage: React.FC = () => {
  const [searchParams] = useSearchParams();
  const showtimeId = parseInt(searchParams.get('showtimeId') || '0');
  const navigate = useNavigate();
  const { isAuthenticated, isGuest } = useAuth();

  const { data: seats, isLoading: seatsLoading } = useSeats(showtimeId);
  const { data: showtime, isLoading: showtimeLoading } = useShowtime(showtimeId);

  const [selectedSeats, setSelectedSeats] = useState<number[]>([]);

  const isLoading = seatsLoading || showtimeLoading;
  const isComingSoon = showtime?.movie.status === 'COMING_SOON';
  const isGuestUser = isGuest || !isAuthenticated;

  // Group seats by row
  const seatsByRow = useMemo(() => {
    if (!seats) return {};
    return seats.reduce((acc, seat) => {
      if (!acc[seat.seatRow]) {
        acc[seat.seatRow] = [];
      }
      acc[seat.seatRow].push(seat);
      return acc;
    }, {} as Record<string, SeatResponse[]>);
  }, [seats]);

  // Sort rows alphabetically and seats by number
  const sortedRows = useMemo(() => {
    return Object.keys(seatsByRow).sort().map(row => ({
      row,
      seats: seatsByRow[row].sort((a, b) => a.seatNumber - b.seatNumber),
    }));
  }, [seatsByRow]);

  const canSelectSeat = (seat: SeatResponse): boolean => {
    // Already booked
    if (seat.status === 'BOOKED') return false;

    // Guest users can't select special seats
    if (isGuestUser && seat.seatType === 'special') return false;

    // Coming soon movies: only special seats for registered users
    if (isComingSoon) {
      if (isGuestUser) return false;
      if (seat.seatType !== 'special') return false;
    }

    return true;
  };

  const handleSeatClick = (seat: SeatResponse) => {
    if (!canSelectSeat(seat)) {
      if (seat.status === 'BOOKED') {
        toast.error('This seat is already booked');
      } else if (isGuestUser && seat.seatType === 'special') {
        toast.error('Premium seats are only available for registered members');
      } else if (isComingSoon && isGuestUser) {
        toast.error('Early access booking is only for registered members');
      } else if (isComingSoon && seat.seatType !== 'special') {
        toast.error('Only special seats are available for early booking');
      }
      return;
    }

    setSelectedSeats(prev => {
      if (prev.includes(seat.id)) {
        return prev.filter(id => id !== seat.id);
      }
      return [...prev, seat.id];
    });
  };

  const getSeatClass = (seat: SeatResponse): string => {
    const isSelected = selectedSeats.includes(seat.id);
    const isBooked = seat.status === 'BOOKED';
    const isSpecial = seat.seatType === 'special';
    const isRestricted = !canSelectSeat(seat) && !isBooked;

    if (isBooked) return 'seat seat-disabled';
    if (isSelected) return 'seat seat-selected';
    if (isRestricted) return 'seat opacity-40 cursor-not-allowed';
    if (isSpecial) return 'seat seat-special';
    return 'seat';
  };

  const selectedSeatDetails = useMemo(() => {
    if (!seats) return [];
    return seats.filter(seat => selectedSeats.includes(seat.id));
  }, [seats, selectedSeats]);

  const totalPrice = useMemo(() => {
    return selectedSeatDetails.reduce((sum, seat) => sum + seat.price, 0);
  }, [selectedSeatDetails]);

  const handleProceed = () => {
    if (selectedSeats.length === 0) {
      toast.error('Please select at least one seat');
      return;
    }

    // Navigate to ticket confirmation with selected data
    navigate('/ticket-confirmation', {
      state: {
        showtimeId,
        seatIds: selectedSeats,
        showtime,
        selectedSeats: selectedSeatDetails,
        totalPrice,
      },
    });
  };

  if (!showtimeId) {
    return (
      <PageLayout>
        <div className="flex justify-center items-center min-h-[60vh]">
          <p className="text-text-muted">Invalid showtime. Please select a showtime first.</p>
        </div>
      </PageLayout>
    );
  }

  return (
    <PageLayout>
      <div className="max-w-7xl mx-auto px-4 py-8">
        {/* Movie/Showtime Info */}
        {showtime && (
          <div className="bg-bg-card border border-white/5 rounded-xl p-6 mb-8 text-center">
            <h2 className="font-display text-2xl text-gold-gradient mb-2">
              {showtime.movie.title}
            </h2>
            <p className="text-text-secondary">
              {showtime.theatre.name} • {new Date(showtime.date).toLocaleDateString('en-US', {
                weekday: 'long',
                month: 'long',
                day: 'numeric',
              })} • {showtime.sessionTime}
            </p>
            {isComingSoon && (
              <div className="mt-4 p-4 bg-gold/10 border border-gold/30 rounded-lg">
                <p className="text-gold font-semibold">
                  Early Access Booking
                </p>
                <p className="text-text-secondary text-sm mt-1">
                  Only special seats are available for early booking
                </p>
              </div>
            )}
          </div>
        )}

        <h1 className="page-title text-gold-gradient">Select Your Seats</h1>

        {isLoading && (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-gold"></div>
          </div>
        )}

        {!isLoading && seats && (
          <>
            {/* Seat Selection Area */}
            <div className="flex flex-col items-center gap-6 py-8 overflow-x-auto">
              {/* Screen */}
              <div className="screen mb-8">Screen</div>

              {/* Seats Grid */}
              <div className="space-y-2">
                {sortedRows.map(({ row, seats: rowSeats }) => (
                  <div key={row} className="flex items-center gap-2">
                    <span className="w-8 font-display font-bold text-gold text-center">
                      {row}
                    </span>
                    <div className="flex gap-2">
                      {rowSeats.map(seat => (
                        <button
                          key={seat.id}
                          onClick={() => handleSeatClick(seat)}
                          className={getSeatClass(seat)}
                          disabled={seat.status === 'BOOKED'}
                          title={`${seat.seatLabel} - $${seat.price.toFixed(2)}${seat.seatType === 'special' ? ' (Premium)' : ''}`}
                        >
                          {seat.seatNumber}
                        </button>
                      ))}
                    </div>
                    <span className="w-8 font-display font-bold text-gold text-center">
                      {row}
                    </span>
                  </div>
                ))}
              </div>
            </div>

            {/* Legend */}
            <div className="flex flex-wrap justify-center gap-6 py-6 border-t border-white/10">
              <div className="flex items-center gap-2">
                <div className="w-7 h-7 bg-bg-elevated border-2 border-white/15 rounded"></div>
                <span className="text-text-secondary text-sm">Available</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-7 h-7 bg-gold/15 border-2 border-gold rounded"></div>
                <span className="text-text-secondary text-sm">Premium</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-7 h-7 bg-accent-green border-2 border-accent-green rounded"></div>
                <span className="text-text-secondary text-sm">Selected</span>
              </div>
              <div className="flex items-center gap-2">
                <div className="w-7 h-7 bg-accent-red border-2 border-accent-red rounded opacity-70"></div>
                <span className="text-text-secondary text-sm">Booked</span>
              </div>
            </div>

            {/* Selection Summary */}
            <div className="mt-8 bg-bg-card border border-white/5 rounded-xl p-6">
              <div className="flex flex-wrap items-center justify-between gap-4">
                <div>
                  <p className="text-text-muted text-sm uppercase tracking-wide">Selected Seats</p>
                  <div className="flex flex-wrap gap-2 mt-2">
                    {selectedSeatDetails.length === 0 ? (
                      <span className="text-text-secondary">No seats selected</span>
                    ) : (
                      selectedSeatDetails.map(seat => (
                        <span
                          key={seat.id}
                          className={clsx(
                            'badge',
                            seat.seatType === 'special' ? 'badge-warning' : 'badge-primary'
                          )}
                        >
                          {seat.seatLabel}
                        </span>
                      ))
                    )}
                  </div>
                </div>
                <div className="text-right">
                  <p className="text-text-muted text-sm uppercase tracking-wide">Total</p>
                  <p className="text-gold-gradient text-3xl font-display font-bold">
                    ${totalPrice.toFixed(2)}
                  </p>
                </div>
              </div>

              <div className="mt-6 flex flex-wrap gap-4 justify-end">
                <button
                  onClick={() => navigate(-1)}
                  className="btn btn-secondary"
                >
                  ← Back
                </button>
                <button
                  onClick={handleProceed}
                  className="btn btn-primary"
                  disabled={selectedSeats.length === 0}
                >
                  Continue to Payment ({selectedSeats.length} {selectedSeats.length === 1 ? 'seat' : 'seats'})
                </button>
              </div>
            </div>

            {/* Guest User Notice */}
            {isGuestUser && (
              <div className="mt-6 p-4 bg-gold/10 border border-gold/30 rounded-lg text-center">
                <p className="text-gold font-semibold">Premium seats are members-only</p>
                <p className="text-text-secondary text-sm mt-1">
                  <a href="/register" className="text-gold hover:text-gold-light underline">
                    Register now
                  </a>{' '}
                  to unlock all seats and early access bookings
                </p>
              </div>
            )}
          </>
        )}
      </div>
    </PageLayout>
  );
};

export default SeatsPage;
