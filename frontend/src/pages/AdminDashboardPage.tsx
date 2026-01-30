import React from 'react';
import { Link } from 'react-router-dom';
import { PageLayout } from '@/components';

export const AdminDashboardPage: React.FC = () => {
  const adminActions = [
    {
      title: 'Send Notifications',
      description: 'Send movie notifications to registered users',
      link: '/admin/notifications',
      icon: (
        <svg className="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
          <path d="M12 22c1.1 0 2-.9 2-2h-4c0 1.1.9 2 2 2zm6-6v-5c0-3.07-1.63-5.64-4.5-6.32V4c0-.83-.67-1.5-1.5-1.5s-1.5.67-1.5 1.5v.68C7.64 5.36 6 7.92 6 11v5l-2 2v1h16v-1l-2-2zm-2 1H8v-6c0-2.48 1.51-4.5 4-4.5s4 2.02 4 4.5v6z" />
        </svg>
      ),
      available: true,
    },
    {
      title: 'Manage Movies',
      description: 'Add, edit, or remove movies from the catalog',
      link: '/admin/movies',
      icon: (
        <svg className="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
          <path d="M18 4l2 4h-3l-2-4h-2l2 4h-3l-2-4H8l2 4H7L5 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V4h-4z" />
        </svg>
      ),
      available: false,
    },
    {
      title: 'Manage Theatres',
      description: 'Configure theatre information and seating',
      link: '/admin/theatres',
      icon: (
        <svg className="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
          <path d="M18 3v2h-2V3H8v2H6V3H4v18h2v-2h2v2h8v-2h2v2h2V3h-2zM8 17H6v-2h2v2zm0-4H6v-2h2v2zm0-4H6V7h2v2zm10 8h-2v-2h2v2zm0-4h-2v-2h2v2zm0-4h-2V7h2v2z" />
        </svg>
      ),
      available: false,
    },
    {
      title: 'View Reports',
      description: 'View sales and booking analytics',
      link: '/admin/reports',
      icon: (
        <svg className="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
          <path d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zM9 17H7v-7h2v7zm4 0h-2V7h2v10zm4 0h-2v-4h2v4z" />
        </svg>
      ),
      available: false,
    },
  ];

  return (
    <PageLayout>
      <div className="max-w-5xl mx-auto px-4 py-8">
        <h1 className="page-title text-gold-gradient">Admin Dashboard</h1>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-8">
          {adminActions.map((action) => (
            <div
              key={action.title}
              className={`card p-6 ${!action.available ? 'opacity-60' : ''}`}
            >
              <div className="flex items-start gap-4">
                <div className="text-gold">{action.icon}</div>
                <div className="flex-1">
                  <h3 className="font-display text-xl text-text-primary mb-2">
                    {action.title}
                  </h3>
                  <p className="text-text-secondary text-sm mb-4">
                    {action.description}
                  </p>
                  {action.available ? (
                    <Link to={action.link} className="btn btn-primary text-sm py-2 px-4">
                      Open
                    </Link>
                  ) : (
                    <span className="badge badge-secondary">Coming Soon</span>
                  )}
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Quick Stats */}
        <div className="mt-12">
          <h2 className="font-display text-2xl text-text-primary mb-6">Quick Stats</h2>
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            <div className="bg-bg-card border border-white/5 rounded-lg p-4 text-center">
              <p className="text-3xl font-display text-gold">--</p>
              <p className="text-text-muted text-sm">Total Movies</p>
            </div>
            <div className="bg-bg-card border border-white/5 rounded-lg p-4 text-center">
              <p className="text-3xl font-display text-gold">--</p>
              <p className="text-text-muted text-sm">Active Users</p>
            </div>
            <div className="bg-bg-card border border-white/5 rounded-lg p-4 text-center">
              <p className="text-3xl font-display text-gold">--</p>
              <p className="text-text-muted text-sm">Today's Bookings</p>
            </div>
            <div className="bg-bg-card border border-white/5 rounded-lg p-4 text-center">
              <p className="text-3xl font-display text-gold">--</p>
              <p className="text-text-muted text-sm">Revenue</p>
            </div>
          </div>
        </div>
      </div>
    </PageLayout>
  );
};

export default AdminDashboardPage;
