import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authApi } from '@/api';
import { setTokens, clearTokens, getAccessToken } from '@/api/client';
import { User, LoginRequest, RegisterRequest, UserType } from '@/types';

interface AuthContextType {
  user: User | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  isGuest: boolean;
  login: (data: LoginRequest) => Promise<void>;
  register: (data: RegisterRequest) => Promise<void>;
  logout: () => void;
  continueAsGuest: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: React.ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isGuest, setIsGuest] = useState(false);

  const fetchUser = useCallback(async () => {
    const token = getAccessToken();
    if (!token) {
      setIsLoading(false);
      return;
    }

    try {
      const response = await authApi.me();
      if (response.success && response.data) {
        setUser({
          id: response.data.userId,
          username: response.data.username,
          userType: response.data.userType,
          email: response.data.email,
        });
      } else {
        clearTokens();
      }
    } catch {
      clearTokens();
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchUser();
  }, [fetchUser]);

  const login = async (data: LoginRequest): Promise<void> => {
    const response = await authApi.login(data);
    if (!response.success) {
      throw new Error(response.message || 'Login failed');
    }
    if (response.data) {
      setTokens(response.data.accessToken!, response.data.refreshToken!);
      setUser({
        id: response.data.userId,
        username: response.data.username,
        userType: response.data.userType,
        email: response.data.email,
      });
      setIsGuest(false);
    }
  };

  const register = async (data: RegisterRequest): Promise<void> => {
    const response = await authApi.register(data);
    if (!response.success) {
      throw new Error(response.message || 'Registration failed');
    }
    if (response.data) {
      setTokens(response.data.accessToken!, response.data.refreshToken!);
      setUser({
        id: response.data.userId,
        username: response.data.username,
        userType: response.data.userType,
        email: response.data.email,
      });
      setIsGuest(false);
    }
  };

  const logout = () => {
    clearTokens();
    setUser(null);
    setIsGuest(false);
  };

  const continueAsGuest = () => {
    clearTokens();
    setUser({
      id: 0,
      username: 'Guest',
      userType: 'GUEST' as UserType,
      email: '',
    });
    setIsGuest(true);
  };

  const value: AuthContextType = {
    user,
    isLoading,
    isAuthenticated: !!user && !isGuest,
    isGuest,
    login,
    register,
    logout,
    continueAsGuest,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
