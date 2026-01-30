import { useQuery } from '@tanstack/react-query';
import { seatsApi } from '@/api';

export const useSeats = (showtimeId: number) => {
  return useQuery({
    queryKey: ['seats', 'showtime', showtimeId],
    queryFn: async () => {
      const response = await seatsApi.getByShowtime(showtimeId);
      if (!response.success) {
        throw new Error(response.message || 'Failed to fetch seats');
      }
      return response.data || [];
    },
    enabled: !!showtimeId,
  });
};
