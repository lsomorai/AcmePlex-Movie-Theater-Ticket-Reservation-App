import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { authApi } from '@/api';
import toast from 'react-hot-toast';

export const RegisterPage: React.FC = () => {
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [isLoading, setIsLoading] = useState(false);
  const [usernameAvailable, setUsernameAvailable] = useState<boolean | null>(null);
  const [checkingUsername, setCheckingUsername] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  // Check username availability with debounce
  useEffect(() => {
    if (formData.username.length < 5) {
      setUsernameAvailable(null);
      return;
    }

    const timeoutId = setTimeout(async () => {
      setCheckingUsername(true);
      try {
        const response = await authApi.verifyUsername(formData.username);
        setUsernameAvailable(response.data?.available ?? false);
      } catch {
        setUsernameAvailable(null);
      } finally {
        setCheckingUsername(false);
      }
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [formData.username]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (formData.password !== formData.confirmPassword) {
      toast.error('Passwords do not match');
      return;
    }

    if (!usernameAvailable) {
      toast.error('Username is not available');
      return;
    }

    setIsLoading(true);

    try {
      await register({
        firstName: formData.firstName,
        lastName: formData.lastName,
        username: formData.username,
        email: formData.email,
        password: formData.password,
      });
      toast.success('Registration successful! Complete payment to activate membership.');
      navigate('/payment');
    } catch (error) {
      toast.error(error instanceof Error ? error.message : 'Registration failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center p-6 relative">
      {/* Background effects */}
      <div className="absolute inset-0 pointer-events-none">
        <div className="absolute top-1/4 left-1/4 w-96 h-96 bg-gold/10 rounded-full blur-3xl"></div>
        <div className="absolute bottom-1/4 right-1/4 w-64 h-64 bg-accent-blue/10 rounded-full blur-3xl"></div>
      </div>

      <div className="relative z-10 w-full max-w-md">
        {/* Logo */}
        <div className="text-center mb-8">
          <h1 className="text-gold-gradient text-4xl font-display font-bold tracking-wider uppercase">
            AcmePlex
          </h1>
          <p className="text-text-secondary mt-2">Join Our Premium Members</p>
        </div>

        {/* Register Card */}
        <div className="bg-gradient-card border border-gold/20 rounded-xl p-8 shadow-lg shadow-glow relative overflow-hidden">
          {/* Gold top bar */}
          <div className="absolute top-0 left-0 right-0 h-1 bg-gradient-gold"></div>

          <h2 className="text-gold-gradient text-2xl font-display text-center mb-8">
            Create Account
          </h2>

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="form-label block">First Name</label>
                <input
                  type="text"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="John"
                  required
                />
              </div>
              <div>
                <label className="form-label block">Last Name</label>
                <input
                  type="text"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="Doe"
                  required
                />
              </div>
            </div>

            <div>
              <label className="form-label block">Username</label>
              <div className="relative">
                <input
                  type="text"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  className="form-input pr-10"
                  placeholder="Choose a username"
                  required
                  minLength={5}
                />
                {formData.username.length >= 5 && (
                  <span className="absolute right-3 top-1/2 -translate-y-1/2">
                    {checkingUsername ? (
                      <span className="text-text-muted">...</span>
                    ) : usernameAvailable ? (
                      <span className="text-accent-green">✓</span>
                    ) : (
                      <span className="text-accent-red">✗</span>
                    )}
                  </span>
                )}
              </div>
              {formData.username.length >= 5 && !checkingUsername && usernameAvailable === false && (
                <p className="text-accent-red text-sm mt-1">Username is already taken</p>
              )}
            </div>

            <div>
              <label className="form-label block">Email</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="form-input"
                placeholder="john@example.com"
                required
              />
            </div>

            <div>
              <label className="form-label block">Password</label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                className="form-input"
                placeholder="Min. 5 characters"
                required
                minLength={5}
              />
            </div>

            <div>
              <label className="form-label block">Confirm Password</label>
              <input
                type="password"
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                className="form-input"
                placeholder="Re-enter your password"
                required
                minLength={5}
              />
            </div>

            <div className="pt-4">
              <button
                type="submit"
                className="btn btn-primary w-full"
                disabled={isLoading || !usernameAvailable}
              >
                {isLoading ? 'Creating Account...' : 'Create Account'}
              </button>
            </div>
          </form>

          <div className="mt-8 text-center">
            <p className="text-text-secondary">
              Already have an account?{' '}
              <Link to="/login" className="text-gold hover:text-gold-light transition-colors">
                Sign in here
              </Link>
            </p>
          </div>
        </div>

        {/* Membership Info */}
        <div className="mt-6 p-4 bg-bg-card/50 rounded-lg border border-white/5 text-center">
          <p className="text-text-muted text-sm">
            Membership fee: <span className="text-gold font-semibold">$20/year</span>
          </p>
          <p className="text-text-muted text-xs mt-1">
            Unlocks early booking access and premium seats
          </p>
        </div>
      </div>
    </div>
  );
};

export default RegisterPage;
