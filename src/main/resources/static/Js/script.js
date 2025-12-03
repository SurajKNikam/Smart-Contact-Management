console.log("Script loaded..");

document.addEventListener("DOMContentLoaded", () => {
  // Apply saved theme on page load
  applyTheme(getTheme());
});

function changeTheme() {
  const currentTheme = getTheme();
  const newTheme = currentTheme === "light" ? "dark" : "light";
  setTheme(newTheme);
  applyTheme(newTheme);
}

// Apply theme by toggling the "dark" class on <html>
function applyTheme(theme) {
  const label = document.getElementById("themeLabel");
  if (theme === "dark") {
    document.documentElement.classList.add("dark");
    if (label) label.textContent = "Dark";
  } else {
    document.documentElement.classList.remove("dark");
    if (label) label.textContent = "Light";
  }
}


// Save theme to localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Get theme from localStorage (default = light)
function getTheme() {
  const theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}
