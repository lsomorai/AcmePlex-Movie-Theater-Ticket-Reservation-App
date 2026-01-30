import React from 'react';
import { Navbar } from './Navbar';

interface PageLayoutProps {
  children: React.ReactNode;
  showNavbar?: boolean;
  className?: string;
}

export const PageLayout: React.FC<PageLayoutProps> = ({
  children,
  showNavbar = true,
  className = '',
}) => {
  return (
    <div className="min-h-screen bg-bg-dark">
      {showNavbar && <Navbar />}
      <main className={className}>{children}</main>
    </div>
  );
};

export default PageLayout;
