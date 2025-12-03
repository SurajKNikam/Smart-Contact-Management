/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
	"./src/main/resources/templates/**/*.html",
	 "./src/main/webapp/**/*.html",
	 "./src/main/webapp/**/*.js"
  ],
  theme: {
    extend: {},
  },
  plugins: [],
  darkMode:"class",
  
  safelist: [
    'swal2-confirm', 'swal2-cancel', 'swal2-deny'
  ],

}

