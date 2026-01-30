import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { paymentApi } from '@/api';
import toast from 'react-hot-toast';
import { PageLayout } from '@/components';

export const PaymentPage: React.FC = () => {
  const [formData, setFormData] = useState({
    cardNumber: '',
    expiryDate: '',
    cvv: '',
    cardholderName: '',
  });
  const [isLoading, setIsLoading] = useState(false);
  const { user } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    let value = e.target.value;

    // Format card number with spaces
    if (e.target.name === 'cardNumber') {
      value = value.replace(/\D/g, '').replace(/(.{4})/g, '$1 ').trim();
      if (value.length > 19) return;
    }

    // Format expiry date
    if (e.target.name === 'expiryDate') {
      value = value.replace(/\D/g, '');
      if (value.length >= 2) {
        value = value.slice(0, 2) + '/' + value.slice(2, 4);
      }
      if (value.length > 5) return;
    }

    // Limit CVV to 4 digits
    if (e.target.name === 'cvv') {
      value = value.replace(/\D/g, '');
      if (value.length > 4) return;
    }

    setFormData({ ...formData, [e.target.name]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!user) {
      toast.error('Please log in first');
      navigate('/login');
      return;
    }

    setIsLoading(true);

    try {
      const response = await paymentApi.processRegistrationPayment({
        cardNumber: formData.cardNumber.replace(/\s/g, ''),
        expiryDate: formData.expiryDate,
        cvv: formData.cvv,
        cardholderName: formData.cardholderName,
        userId: user.id,
      });

      if (response.success) {
        toast.success('Membership activated successfully!');
        navigate('/dashboard');
      } else {
        throw new Error(response.message || 'Payment failed');
      }
    } catch (error) {
      toast.error(error instanceof Error ? error.message : 'Payment failed');
    } finally {
      setIsLoading(false);
    }
  };

  const handleSkip = () => {
    toast.success('You can complete payment later from your profile');
    navigate('/dashboard');
  };

  return (
    <PageLayout showNavbar={false}>
      <div className="min-h-screen flex flex-col items-center justify-center p-6 relative">
        {/* Background effects */}
        <div className="absolute inset-0 pointer-events-none">
          <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-gold/10 rounded-full blur-3xl"></div>
          <div className="absolute bottom-1/4 right-1/4 w-64 h-64 bg-accent-blue/10 rounded-full blur-3xl"></div>
        </div>

        <div className="relative z-10 w-full max-w-md">
          {/* Payment Card */}
          <div className="bg-gradient-card border border-gold/20 rounded-xl p-8 shadow-lg shadow-glow relative overflow-hidden">
            {/* Gold top bar */}
            <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-gold"></div>

            <h2 className="text-gold-gradient text-2xl font-display text-center mb-2">
              Complete Your Membership
            </h2>
            <p className="text-text-secondary text-center mb-8">
              Annual membership fee: <span className="text-gold font-semibold">$20.00</span>
            </p>

            <form onSubmit={handleSubmit} className="space-y-4">
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

              <div className="space-y-4 pt-4">
                <button
                  type="submit"
                  className="btn btn-primary w-full"
                  disabled={isLoading}
                >
                  {isLoading ? 'Processing...' : 'Pay $20.00'}
                </button>

                <button
                  type="button"
                  onClick={handleSkip}
                  className="btn btn-secondary w-full"
                >
                  Skip for Now
                </button>
              </div>
            </form>
          </div>

          {/* Benefits */}
          <div className="mt-6 p-4 bg-bg-card/50 rounded-lg border border-white/5">
            <h3 className="text-gold font-semibold mb-3">Membership Benefits:</h3>
            <ul className="space-y-2 text-text-secondary text-sm">
              <li className="flex items-center gap-2">
                <span className="text-accent-green">✓</span>
                Early access to movie bookings
              </li>
              <li className="flex items-center gap-2">
                <span className="text-accent-green">✓</span>
                Access to premium seating
              </li>
              <li className="flex items-center gap-2">
                <span className="text-accent-green">✓</span>
                Movie news and notifications
              </li>
              <li className="flex items-center gap-2">
                <span className="text-accent-green">✓</span>
                Only 15% cancellation fee (vs 15% + $5 for guests)
              </li>
            </ul>
          </div>
        </div>
      </div>
    </PageLayout>
  );
};

export default PaymentPage;
