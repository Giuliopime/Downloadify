<template>
  <div id="navbar">
    <router-link to="/">
      <div class="logo-item">
        <img src="../assets/images/logo.png" alt="logo" class="logo-image">
        <span class="logo-name">Downloadify</span>
      </div>
    </router-link>
    <LightSwitch id="light-switch"/>
  </div>
</template>

<script>
import LightSwitch from "@/components/ThemeToggler";
export default {
name: "NavBar",
  components: {LightSwitch}
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Exo+2:wght@800&family=Press+Start+2P&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Exo+2:wght@800&display=swap');

#navbar {
  width: 100%;
  background-color: var(--color-nav-bg);
  border-bottom: var(--card-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem 1.25rem;
  margin-bottom: 5vh;
  transition: all var(--transition-speed);
  -webkit-transition: all var(--transition-speed);
  -moz-transition: all var(--transition-speed);
  -o-transition: all var(--transition-speed);
}

.logo-item {
  display: flex;
  align-items: center;
}


.logo-image {
  height: 3rem;
  margin-right: 0.7rem;
}

.logo-name {
  display: inline-block;
  font-family: 'Exo 2', 'Press Start 2P', cursive;
  font-size: 1.5rem;
  text-transform: uppercase;
  text-decoration: none;
  color: var(--color-text)
}
.logo-name::after {
  content: '';
  width: 0;
  height: 0.5vh;
  display: block;
  background: #623CEA;
  transition: var(--transition-speed);
  -webkit-transition: var(--transition-speed);
  -moz-transition: var(--transition-speed);
  -o-transition: var(--transition-speed);
}

#navbar a:hover .logo-name::after {
  width: 100%;
}
</style>
