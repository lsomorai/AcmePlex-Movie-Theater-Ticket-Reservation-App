// Enums
export type MovieStatus = 'NOW_SHOWING' | 'COMING_SOON';
export type TicketStatus = 'ACTIVE' | 'CANCELLED' | 'USED';
export type SeatStatus = 'AVAILABLE' | 'BOOKED';
export type UserType = 'REGULAR' | 'ADMIN' | 'GUEST';

// API Response wrapper
export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
  timestamp: string;
}

// Auth
export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  username: string;
  password: string;
  email: string;
}

export interface AuthResponse {
  userId: number;
  username: string;
  userType: UserType;
  email: string;
  accessToken?: string;
  refreshToken?: string;
}

export interface User {
  id: number;
  username: string;
  userType: UserType;
  email: string;
}

// Theatre
export interface Theatre {
  id: number;
  name: string;
}

export interface TheatreResponse {
  id: number;
  name: string;
}

// Movie
export interface Movie {
  id: number;
  title: string;
  status: MovieStatus;
}

export interface MovieResponse {
  id: number;
  title: string;
  status: MovieStatus;
}

// Showtime
export interface Showtime {
  id: number;
  date: string;
  session: number;
  sessionTime: string;
  movie: MovieResponse;
  theatre: TheatreResponse;
}

export interface ShowtimeResponse {
  id: number;
  date: string;
  session: number;
  sessionTime: string;
  movie: MovieResponse;
  theatre: TheatreResponse;
}

// Seat
export interface Seat {
  id: number;
  seatRow: string;
  seatNumber: number;
  seatLabel: string;
  seatType: string | null;
  price: number;
  status: SeatStatus;
}

export interface SeatResponse {
  id: number;
  seatRow: string;
  seatNumber: number;
  seatLabel: string;
  seatType: string | null;
  price: number;
  status: string;
}

// Ticket
export interface Ticket {
  id: number;
  referenceNumber: string;
  movieId: number;
  seatId: number;
  purchaseDate: string;
  status: TicketStatus;
  refundable: boolean;
}

export interface TicketResponse {
  id: number;
  referenceNumber: string;
  movieId: number;
  seatId: number;
  purchaseDate: string;
  status: TicketStatus;
  refundable: boolean;
}

// Payment
export interface PaymentRequest {
  cardNumber: string;
  expiryDate: string;
  cvv: string;
  cardholderName: string;
  creditCode?: string;
}

// Booking
export interface BookingRequest {
  showtimeId: number;
  seatIds: number[];
  userId?: number;
  email?: string;
}

export interface BookingResponse {
  referenceNumber: string;
  tickets: TicketResponse[];
  totalAmount: number;
}

// Cancellation
export interface CancellationRequest {
  referenceNumber: string;
  email?: string;
}

export interface CancellationResponse {
  ticket: TicketResponse;
  refundAmount: number;
  creditCode?: string;
}

// Search
export interface MovieSearchResult {
  movie: MovieResponse;
  theatres: TheatreResponse[];
}
