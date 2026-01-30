import apiClient from './client';
import {
  ApiResponse,
  AuthResponse,
  LoginRequest,
  RegisterRequest,
  MovieResponse,
  TheatreResponse,
  ShowtimeResponse,
  SeatResponse,
  TicketResponse,
  MovieSearchResult,
} from '@/types';

// Auth API
export const authApi = {
  login: async (data: LoginRequest): Promise<ApiResponse<AuthResponse>> => {
    const response = await apiClient.post<ApiResponse<AuthResponse>>('/auth/login', data);
    return response.data;
  },

  register: async (data: RegisterRequest): Promise<ApiResponse<AuthResponse>> => {
    const response = await apiClient.post<ApiResponse<AuthResponse>>('/auth/register', data);
    return response.data;
  },

  refresh: async (refreshToken: string): Promise<ApiResponse<{ accessToken: string }>> => {
    const response = await apiClient.post<ApiResponse<{ accessToken: string }>>('/auth/refresh', { refreshToken });
    return response.data;
  },

  me: async (): Promise<ApiResponse<AuthResponse>> => {
    const response = await apiClient.get<ApiResponse<AuthResponse>>('/auth/me');
    return response.data;
  },

  verifyUsername: async (username: string): Promise<ApiResponse<{ available: boolean }>> => {
    const response = await apiClient.get<ApiResponse<{ available: boolean }>>('/auth/verify-username', {
      params: { username },
    });
    return response.data;
  },
};

// Movies API
export const moviesApi = {
  getAll: async (): Promise<ApiResponse<MovieResponse[]>> => {
    const response = await apiClient.get<ApiResponse<MovieResponse[]>>('/movies');
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<MovieResponse>> => {
    const response = await apiClient.get<ApiResponse<MovieResponse>>(`/movies/${id}`);
    return response.data;
  },

  search: async (query: string): Promise<ApiResponse<MovieSearchResult[]>> => {
    const response = await apiClient.get<ApiResponse<MovieSearchResult[]>>('/movies/search', {
      params: { q: query },
    });
    return response.data;
  },

  getByTheatre: async (theatreId: number): Promise<ApiResponse<MovieResponse[]>> => {
    const response = await apiClient.get<ApiResponse<MovieResponse[]>>(`/movies/theatre/${theatreId}`);
    return response.data;
  },
};

// Theatres API
export const theatresApi = {
  getAll: async (): Promise<ApiResponse<TheatreResponse[]>> => {
    const response = await apiClient.get<ApiResponse<TheatreResponse[]>>('/theatres');
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<TheatreResponse>> => {
    const response = await apiClient.get<ApiResponse<TheatreResponse>>(`/theatres/${id}`);
    return response.data;
  },

  getByMovie: async (movieId: number): Promise<ApiResponse<TheatreResponse[]>> => {
    const response = await apiClient.get<ApiResponse<TheatreResponse[]>>(`/movies/${movieId}/theatres`);
    return response.data;
  },
};

// Showtimes API
export const showtimesApi = {
  getAll: async (): Promise<ApiResponse<ShowtimeResponse[]>> => {
    const response = await apiClient.get<ApiResponse<ShowtimeResponse[]>>('/showtimes');
    return response.data;
  },

  getById: async (id: number): Promise<ApiResponse<ShowtimeResponse>> => {
    const response = await apiClient.get<ApiResponse<ShowtimeResponse>>(`/showtimes/${id}`);
    return response.data;
  },

  getByTheatreAndMovie: async (theatreId: number, movieId: number): Promise<ApiResponse<ShowtimeResponse[]>> => {
    const response = await apiClient.get<ApiResponse<ShowtimeResponse[]>>(
      `/showtimes/theatre/${theatreId}/movie/${movieId}`
    );
    return response.data;
  },
};

// Seats API
export const seatsApi = {
  getByShowtime: async (showtimeId: number): Promise<ApiResponse<SeatResponse[]>> => {
    const response = await apiClient.get<ApiResponse<SeatResponse[]>>(`/showtimes/${showtimeId}/seats`);
    return response.data;
  },
};

// Tickets API
export const ticketsApi = {
  create: async (data: {
    showtimeId: number;
    seatIds: number[];
    userId?: number;
    email?: string;
  }): Promise<ApiResponse<{ referenceNumber: string; tickets: TicketResponse[] }>> => {
    const response = await apiClient.post<ApiResponse<{ referenceNumber: string; tickets: TicketResponse[] }>>(
      '/tickets/purchase',
      data
    );
    return response.data;
  },

  getByReference: async (referenceNumber: string): Promise<ApiResponse<TicketResponse[]>> => {
    const response = await apiClient.get<ApiResponse<TicketResponse[]>>(`/tickets/${referenceNumber}`);
    return response.data;
  },

  cancel: async (referenceNumber: string): Promise<ApiResponse<string>> => {
    const response = await apiClient.post<ApiResponse<string>>(
      `/tickets/${referenceNumber}/cancel`
    );
    return response.data;
  },

  getDetails: async (referenceNumber: string, userId: number): Promise<ApiResponse<Record<string, unknown>>> => {
    const response = await apiClient.get<ApiResponse<Record<string, unknown>>>(
      `/tickets/${referenceNumber}/details?userId=${userId}`
    );
    return response.data;
  },
};

// Payment API
export const paymentApi = {
  processTicketPayment: async (data: {
    showtimeId: number;
    seatIds: number[];
    cardNumber: string;
    expiryDate: string;
    cvv: string;
    cardholderName: string;
    amount: number;
    creditCode?: string;
    creditAmount?: number;
    userId?: number;
  }): Promise<ApiResponse<{ referenceNumber: string }>> => {
    // Transform to backend format
    const backendPayload = {
      showtimeId: data.showtimeId,
      selectedSeats: data.seatIds.join(','),
      cardnumber: data.cardNumber,
      expirydate: data.expiryDate,
      cvv: data.cvv,
      cardname: data.cardholderName,
      amount: data.amount,
      appliedCreditCode: data.creditCode,
      appliedCreditAmount: data.creditAmount,
    };
    const response = await apiClient.post<ApiResponse<{ referenceNumber: string }>>(
      `/tickets/purchase?userId=${data.userId || 0}`,
      backendPayload
    );
    return response.data;
  },

  processRegistrationPayment: async (data: {
    cardNumber: string;
    expiryDate: string;
    cvv: string;
    cardholderName: string;
    userId: number;
  }): Promise<ApiResponse<{ success: boolean }>> => {
    const response = await apiClient.post<ApiResponse<{ success: boolean }>>('/payment/registration', data);
    return response.data;
  },
};

// Admin API
export const adminApi = {
  sendNotification: async (data: {
    movieId: number;
    userIds: number[];
    message: string;
  }): Promise<ApiResponse<{ sent: number }>> => {
    const response = await apiClient.post<ApiResponse<{ sent: number }>>('/admin/notifications', data);
    return response.data;
  },

  getUsers: async (): Promise<ApiResponse<AuthResponse[]>> => {
    const response = await apiClient.get<ApiResponse<AuthResponse[]>>('/admin/users');
    return response.data;
  },
};
