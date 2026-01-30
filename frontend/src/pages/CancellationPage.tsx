import React, { useState } from 'react';
import { useAuth } from '@/contexts/AuthContext';
import { ticketsApi } from '@/api';
import { PageLayout } from '@/components';
import { TicketResponse } from '@/types';
import toast from 'react-hot-toast';

export const CancellationPage: React.FC = () => {
  const { isAuthenticated, isGuest } = useAuth();

  const [referenceNumber, setReferenceNumber] = useState('');
  const [email, setEmail] = useState('');
  const [tickets, setTickets] = useState<TicketResponse[] | null>(null);
  const [isSearching, setIsSearching] = useState(false);
  const [isCancelling, setIsCancelling] = useState(false);
  const [cancellationResult, setCancellationResult] = useState<{
    refundAmount: number;
    creditCode?: string;
  } | null>(null);

  const handleSearch = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSearching(true);
    setTickets(null);
    setCancellationResult(null);

    try {
      const response = await ticketsApi.getByReference(referenceNumber);
      if (response.success && response.data && response.data.length > 0) {
        setTickets(response.data);
      } else {
        toast.error('No tickets found with this reference number');
      }
    } catch {
      toast.error('Failed to find tickets');
    } finally {
      setIsSearching(false);
    }
  };

  const handleCancel = async () => {
    if (!tickets || tickets.length === 0) return;

    setIsCancelling(true);

    try {
      const response = await ticketsApi.cancel(
        referenceNumber,
        isGuest ? email : undefined
      );

      if (response.success && response.data) {
        setCancellationResult(response.data);
        setTickets(null);
        toast.success('Tickets cancelled successfully');
      } else {
        throw new Error(response.message || 'Cancellation failed');
      }
    } catch (error) {
      toast.error(error instanceof Error ? error.message : 'Cancellation failed');
    } finally {
      setIsCancelling(false);
    }
  };

  const isRefundable = tickets?.every(t => t.refundable);
  const isAlreadyCancelled = tickets?.some(t => t.status === 'CANCELLED');

  return (
    <PageLayout>
      <div className="max-w-2xl mx-auto px-4 py-8">
        <h1 className="page-title text-gold-gradient">Ticket Cancellation</h1>

        {/* Info Banner */}
        <div className="alert alert-info mb-8">
          <h3 className="font-semibold mb-2">Cancellation Policy</h3>
          <ul className="text-sm space-y-1">
            <li>• Cancellations must be made at least 72 hours before showtime</li>
            <li>• Registered members: 15% cancellation fee</li>
            <li>• Guest users: 15% cancellation fee + $5 admin fee</li>
            <li>• Refunds are issued as credit codes for future bookings</li>
          </ul>
        </div>

        {/* Search Form */}
        {!cancellationResult && (
          <div className="bg-gradient-card border border-gold/20 rounded-xl p-6 shadow-lg">
            <h2 className="font-display text-xl text-gold-gradient mb-6 text-center">
              Find Your Booking
            </h2>

            <form onSubmit={handleSearch} className="space-y-4">
              <div>
                <label className="form-label block">Reference Number</label>
                <input
                  type="text"
                  value={referenceNumber}
                  onChange={(e) => setReferenceNumber(e.target.value.toUpperCase())}
                  className="form-input font-mono"
                  placeholder="Enter your reference number"
                  required
                />
              </div>

              {!isAuthenticated && (
                <div>
                  <label className="form-label block">Email Address</label>
                  <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    className="form-input"
                    placeholder="Email used during booking"
                    required={!isAuthenticated}
                  />
                </div>
              )}

              <button
                type="submit"
                className="btn btn-primary w-full"
                disabled={isSearching}
              >
                {isSearching ? 'Searching...' : 'Find Tickets'}
              </button>
            </form>
          </div>
        )}

        {/* Tickets Found */}
        {tickets && tickets.length > 0 && !cancellationResult && (
          <div className="mt-8 bg-bg-card border border-white/5 rounded-xl p-6">
            <h3 className="font-display text-xl text-text-primary mb-4">
              Booking Found
            </h3>

            <div className="space-y-4">
              {tickets.map((ticket) => (
                <div
                  key={ticket.id}
                  className="bg-bg-elevated rounded-lg p-4 border border-white/5"
                >
                  <div className="flex justify-between items-start">
                    <div>
                      <p className="text-text-primary font-medium">
                        Ticket #{ticket.id}
                      </p>
                      <p className="text-text-muted text-sm">
                        Seat ID: {ticket.seatId}
                      </p>
                      <p className="text-text-muted text-sm">
                        Purchased: {new Date(ticket.purchaseDate).toLocaleDateString()}
                      </p>
                    </div>
                    <span
                      className={`badge ${
                        ticket.status === 'ACTIVE'
                          ? 'badge-success'
                          : ticket.status === 'CANCELLED'
                          ? 'badge-danger'
                          : 'badge-secondary'
                      }`}
                    >
                      {ticket.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>

            {/* Cancellation Action */}
            {isAlreadyCancelled ? (
              <div className="mt-6 p-4 bg-accent-red/10 border border-accent-red/30 rounded-lg text-center">
                <p className="text-accent-red">These tickets have already been cancelled</p>
              </div>
            ) : !isRefundable ? (
              <div className="mt-6 p-4 bg-gold/10 border border-gold/30 rounded-lg text-center">
                <p className="text-gold">
                  These tickets are not refundable (less than 72 hours before showtime)
                </p>
              </div>
            ) : (
              <div className="mt-6">
                <div className="p-4 bg-bg-elevated rounded-lg mb-4">
                  <p className="text-text-muted text-sm">
                    Refund will be issued as a credit code after applying cancellation fee
                  </p>
                </div>
                <button
                  onClick={handleCancel}
                  className="btn btn-danger w-full"
                  disabled={isCancelling}
                >
                  {isCancelling ? 'Processing...' : 'Cancel Tickets'}
                </button>
              </div>
            )}
          </div>
        )}

        {/* Cancellation Result */}
        {cancellationResult && (
          <div className="mt-8 bg-gradient-card border border-accent-green/30 rounded-xl p-8 text-center">
            <div className="text-5xl text-accent-green mb-4">✓</div>
            <h3 className="font-display text-2xl text-accent-green mb-4">
              Cancellation Complete
            </h3>

            <div className="bg-bg-elevated rounded-lg p-6 mb-6">
              <p className="text-text-muted text-sm mb-2">Refund Amount</p>
              <p className="text-3xl font-display text-gold-gradient font-bold">
                ${cancellationResult.refundAmount.toFixed(2)}
              </p>
            </div>

            {cancellationResult.creditCode && (
              <div className="bg-bg-elevated border border-gold/30 rounded-lg p-6">
                <p className="text-text-muted text-sm mb-2">Credit Code</p>
                <p className="text-2xl font-mono text-gold font-bold">
                  {cancellationResult.creditCode}
                </p>
                <p className="text-text-muted text-sm mt-2">
                  Use this code on your next booking
                </p>
              </div>
            )}

            <button
              onClick={() => {
                setCancellationResult(null);
                setReferenceNumber('');
                setEmail('');
              }}
              className="btn btn-secondary mt-6"
            >
              Cancel Another Booking
            </button>
          </div>
        )}
      </div>
    </PageLayout>
  );
};

export default CancellationPage;
