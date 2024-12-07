//seleccionar elementos do DOM
const toggleNav = document.querySelector('.toggle-nav'); 
const sidebarOverlay = document.querySelector('.sidebar-overlay'); 
const sidebar = document.querySelector('.sidebar'); 
const sidebarClose = document.querySelector('.sidebar-close'); 

//función para amosar/ocultar a sidebar
const toggleSidebar = () => {
  
  if (sidebar.style.left === '-300px' || !sidebar.style.left) {
    sidebar.style.left = '0';
    sidebarOverlay.style.display = 'block';
  } else {
    
    sidebar.style.left = '-300px';
    sidebarOverlay.style.display = 'none';
  }
};

//engadir eventos aos botones
toggleNav.addEventListener('click', toggleSidebar);
sidebarClose.addEventListener('click', toggleSidebar);
sidebarOverlay.addEventListener('click', toggleSidebar); 
