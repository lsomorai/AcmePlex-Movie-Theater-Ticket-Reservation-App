import { useQuery } from '@tanstack/react-query';
import { moviesApi } from '@/api';

export const useMovies = () => {
  return useQuery({
    queryKey: ['movies'],
    queryFn: async () => {
      const response = await moviesApi.getAll();
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch movies');
      }
      return response.data || [];
    },
  });
};

export const useMovie = (id: number) => {
  return useQuery({
    queryKey: ['movie', id],
    queryFn: async () => {
      const response = await moviesApi.getById(id);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch movie');
      }
      return response.data;
    },
    enabled: !!id,
  });
};

export const useMovieSearch = (query: string) => {
  return useQuery({
    queryKey: ['movies', 'search', query],
    queryFn: async () => {
      const response = await moviesApi.search(query);
      if (!response.success) {
        throw new Error(response.message || 'Failed to search movies');
      }
      return response.data || [];
    },
    enabled: query.length >= 2,
  });
};

export const useMoviesByTheatre = (theatreId: number) => {
  return useQuery({
    queryKey: ['movies', 'theatre', theatreId],
    queryFn: async () => {
      const response = await moviesApi.getByTheatre(theatreId);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch movies');
      }
      return response.data || [];
    },
    enabled: !!theatreId,
  });
};
