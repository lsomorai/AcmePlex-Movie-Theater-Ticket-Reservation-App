import React, { useState } from 'react';
import { useLocation, useNavigate, Navigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { paymentApi } from '@/api';
import { PageLayout } from '@/components';
import { ShowtimeResponse, SeatResponse } from '@/types';
import toast from 'react-hot-toast';

interface LocationState {
  showtimeId: number;
  seatIds: number[];
  showtime: ShowtimeResponse;
  selectedSeats: SeatResponse[];
  totalPrice: number;
}

export const TicketPaymentPage: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, isAuthenticated, isGuest } = useAuth();

  const state = location.state as LocationState | null;

  const [formData, setFormData] = useState({
    cardNumber: '',
    expiryDate: '',
    cvv: '',
    cardholderName: '',
    creditCode: '',
    email: '',
  });
  const [isLoading, setIsLoading] = useState(false);

  if (!state || !state.showtime || !state.selectedSeats) {
    return <Navigate to="/dashboard" replace />;
  }

  const { showtimeId, seatIds, showtime, selectedSeats, totalPrice } = state;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value;

    if (e.target.name === 'cardNumber') {
      value = value.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').trim();
      if (value.length > 19) return;
    }

    if (e.target.name === 'expiryDate') {
      value = value.replace(/\D/g, '');
      if (value.length >= 2) {
        value = value.slice(0, 2) + '/' + value.slice(2, 4);
      }
      if (value.length > 5) return;
    }

    if (e.target.name === 'cvv') {
      value = value.replace(/\D/g, '');
      if (value.length > 4) return;
    }

    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const response = await paymentApi.processTicketPayment({
        showtimeId,
        seatIds,
        cardNumber: formData.cardNumber.replace(/\s/g, ''),
        expiryDate: formData.expiryDate,
        cvv: formData.cvv,
        cardholderName: formData.cardholderName,
        amount: totalPrice,
        creditCode: formData.creditCode || undefined,
        userId: isAuthenticated ? user?.id : undefined,
      });

      if (response.success && response.data) {
        toast.success('Payment successful!');
        navigate('/booking-success', {
          state: {
            referenceNumber: response.data.referenceNumber,
            showtime,
            selectedSeats,
            totalPrice,
            email: isGuest ? formData.email : user?.email,
          },
        });
      } else {
        throw new Error(response.message || 'Payment failed');
      }
    } catch (error) {
      toast.error(error instanceof Error ? error.message : 'Payment failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <PageLayout>
      <div className="max-w-lg mx-auto px-4 py-8">
        <h1 className="page-title text-gold-gradient">Payment</h1>

        {/* Order Summary */}
        <div className="bg-bg-card border border-white/5 rounded-xl p-6 mb-8">
          <h3 className="font-display text-lg text-text-primary mb-4">Order Summary</h3>
          <div className="space-y-2 text-sm">
            <div className="flex justify-between">
              <span className="text-text-secondary">{showtime.movie.title}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-text-muted">{selectedSeats.length} ticket(s)</span>
              <span className="text-text-secondary">
                {selectedSeats.map(s => s.seatLabel).join(', ')}
              </span>
            </div>
            <div className="border-t border-white/10 pt-2 mt-2 flex justify-between font-semibold">
              <span className="text-text-primary">Total</span>
              <span className="text-gold">${totalPrice.toFixed(2)}</span>
            </div>
          </div>
        </div>

        {/* Payment Form */}
        <div className="bg-gradient-card border border-gold/20 rounded-xl p-6 shadow-lg">
          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Email for guests */}
            {isGuest && (
              <div>
                <label className="form-label block">Email Address</label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="your@email.com"
                  required
                />
                <p className="text-text-muted text-xs mt-1">
                  Confirmation will be sent to this email
                </p>
              </div>
            )}

            <div>
              <label className="form-label block">Cardholder Name</label>
              <input
                type="text"
                name="cardholderName"
                value={formData.cardholderName}
                onChange={handleChange}
                className="form-input"
                placeholder="Name on card"
                required
              />
            </div>

            <div>
              <label className="form-label block">Card Number</label>
              <input
                type="text"
                name="cardNumber"
                value={formData.cardNumber}
                onChange={handleChange}
                className="form-input font-mono"
                placeholder="1234 5678 9012 3456"
                required
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="form-label block">Expiry Date</label>
                <input
                  type="text"
                  name="expiryDate"
                  value={formData.expiryDate}
                  onChange={handleChange}
                  className="form-input font-mono"
                  placeholder="MM/YY"
                  required
                />
              </div>
              <div>
                <label className="form-label block">CVV</label>
                <input
                  type="text"
                  name="cvv"
                  value={formData.cvv}
                  onChange={handleChange}
                  className="form-input font-mono"
                  placeholder="123"
                  required
                />
              </div>
            </div>

            {/* Credit Code */}
            <div>
              <label className="form-label block">
                Credit Code <span className="text-text-muted">(Optional)</span>
              </label>
              <input
                type="text"
                name="creditCode"
                value={formData.creditCode}
                onChange={handleChange}
                className="form-input font-mono"
                placeholder="Enter credit code if you have one"
              />
            </div>

            <div className="pt-4 space-y-4">
              <button
                type="submit"
                className="btn btn-primary w-full"
                disabled={isLoading}
              >
                {isLoading ? 'Processing...' : `Pay $${totalPrice.toFixed(2)}`}
              </button>

              <button
                type="button"
                onClick={() => navigate(-1)}
                className="btn btn-secondary w-full"
                disabled={isLoading}
              >
                ← Back
              </button>
            </div>
          </form>
        </div>

        {/* Security Notice */}
        <div className="mt-6 text-center text-text-muted text-sm">
          <p>Your payment information is secure and encrypted</p>
        </div>
      </div>
    </PageLayout>
  );
};

export default TicketPaymentPage;
