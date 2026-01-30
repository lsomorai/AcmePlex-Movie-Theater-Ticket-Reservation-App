import { useQuery } from '@tanstack/react-query';
import { showtimesApi } from '@/api';

export const useShowtimes = () => {
  return useQuery({
    queryKey: ['showtimes'],
    queryFn: async () => {
      const response = await showtimesApi.getAll();
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch showtimes');
      }
      return response.data || [];
    },
  });
};

export const useShowtime = (id: number) => {
  return useQuery({
    queryKey: ['showtime', id],
    queryFn: async () => {
      const response = await showtimesApi.getById(id);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch showtime');
      }
      return response.data;
    },
    enabled: !!id,
  });
};

export const useShowtimesByTheatreAndMovie = (theatreId: number, movieId: number) => {
  return useQuery({
    queryKey: ['showtimes', 'theatre', theatreId, 'movie', movieId],
    queryFn: async () => {
      const response = await showtimesApi.getByTheatreAndMovie(theatreId, movieId);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch showtimes');
      }
      return response.data || [];
    },
    enabled: !!theatreId && !!movieId,
  });
};
