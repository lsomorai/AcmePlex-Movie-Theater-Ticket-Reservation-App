/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'bg-dark': '#0a0a0f',
        'bg-medium': '#12121a',
        'bg-card': '#1a1a24',
        'bg-elevated': '#222230',
        'gold': {
          DEFAULT: '#d4af37',
          light: '#f4d03f',
          dark: '#aa8c2c',
          glow: 'rgba(212, 175, 55, 0.3)',
        },
        'accent': {
          red: '#e63946',
          green: '#2ec4b6',
          blue: '#4361ee',
        },
        'text': {
          primary: '#f5f5f7',
          secondary: '#a1a1aa',
          muted: '#71717a',
        },
      },
      fontFamily: {
        display: ['Playfair Display', 'Georgia', 'serif'],
        body: ['DM Sans', '-apple-system', 'BlinkMacSystemFont', 'sans-serif'],
        mono: ['JetBrains Mono', 'Fira Code', 'monospace'],
      },
      backgroundImage: {
        'gradient-gold': 'linear-gradient(135deg, #d4af37 0%, #f4d03f 50%, #d4af37 100%)',
        'gradient-dark': 'linear-gradient(180deg, #12121a 0%, #0a0a0f 100%)',
        'gradient-card': 'linear-gradient(145deg, #1a1a24 0%, #12121a 100%)',
        'gradient-spotlight': 'radial-gradient(ellipse at top center, rgba(212, 175, 55, 0.15) 0%, transparent 60%)',
      },
      boxShadow: {
        'sm': '0 2px 8px rgba(0, 0, 0, 0.3)',
        'md': '0 4px 20px rgba(0, 0, 0, 0.4)',
        'lg': '0 8px 40px rgba(0, 0, 0, 0.5)',
        'gold': '0 0 30px rgba(212, 175, 55, 0.2)',
        'glow': '0 0 60px rgba(212, 175, 55, 0.15)',
        'gold-hover': '0 6px 30px rgba(212, 175, 55, 0.4)',
      },
      borderRadius: {
        'sm': '4px',
        'md': '8px',
        'lg': '12px',
        'xl': '20px',
      },
      transitionTimingFunction: {
        'bounce': 'cubic-bezier(0.34, 1.56, 0.64, 1)',
      },
      animation: {
        'fade-in': 'fadeIn 0.5s ease-out',
        'fade-in-up': 'fadeInUp 0.5s ease-out',
        'pulse-slow': 'pulse 2s infinite',
        'shimmer': 'shimmer 2s infinite',
      },
      keyframes: {
        fadeIn: {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        fadeInUp: {
          '0%': { opacity: '0', transform: 'translateY(20px)' },
          '100%': { opacity: '1', transform: 'translateY(0)' },
        },
        shimmer: {
          '0%': { backgroundPosition: '-200% 0' },
          '100%': { backgroundPosition: '200% 0' },
        },
      },
    },
  },
  plugins: [],
}
