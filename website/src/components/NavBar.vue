<template>
  <div id="navbar">
    <router-link to="/">
      <div class="logo-item">
        <img src="../assets/images/logo.png" alt="logo" class="logo-image">
        <span class="logo-name">Downloadify</span>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
name: "NavBar"
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Exo+2:wght@800&family=Press+Start+2P&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Exo+2:wght@800&display=swap');

#navbar {
  width: 100%;
  height: 12vh;
  background-color: var(--color-card-bg);
  border-bottom: var(--card-border);
  display: flex;
  align-items: center;
  padding: 5px 20px;
  margin-bottom: 5vh;
}

.logo-item {
  display: flex;
  align-items: center;
}


.logo-image {
  height: 8vh;
  margin-right: 0.7rem;
}

.logo-name {
  display: inline-block;
  font-family: 'Exo 2', 'Press Start 2P', cursive;
  font-size: 1.5rem;
  text-transform: uppercase;
  text-decoration: none;
}
.logo-name::after {
  content: '';
  width: 0;
  height: 0.5vh;
  display: block;
  background: #623CEA;
  transition: 300ms;
}

#navbar a:hover .logo-name::after {
  width: 100%;
}
</style>
