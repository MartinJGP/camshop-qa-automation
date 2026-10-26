/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        cyber: {
          bg: '#0c1227',
          surface: '#111a31',
          card: '#162446',
          crimson: '#e94560',
          coral: '#ff7189',
          mint: '#4ee0a5'
        }
      }
    },
  },
  plugins: [],
}
