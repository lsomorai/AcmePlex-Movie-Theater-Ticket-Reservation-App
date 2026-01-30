import { useQuery } from '@tanstack/react-query';
import { theatresApi } from '@/api';

export const useTheatres = () => {
  return useQuery({
    queryKey: ['theatres'],
    queryFn: async () => {
      const response = await theatresApi.getAll();
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch theatres');
      }
      return response.data || [];
    },
  });
};

export const useTheatre = (id: number) => {
  return useQuery({
    queryKey: ['theatre', id],
    queryFn: async () => {
      const response = await theatresApi.getById(id);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch theatre');
      }
      return response.data;
    },
    enabled: !!id,
  });
};

export const useTheatresByMovie = (movieId: number) => {
  return useQuery({
    queryKey: ['theatres', 'movie', movieId],
    queryFn: async () => {
      const response = await theatresApi.getByMovie(movieId);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch theatres');
      }
      return response.data || [];
    },
    enabled: !!movieId,
  });
};
