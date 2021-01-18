<template>
  <div id="theme-toggler">
    <button class="color-mode-btn light-toggle" aria-label="Toggle light mode" v-on:click="toggleTheme(true)">
      <svg id="sun" viewBox="0 0 24 24" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="5" class="fill-content"></circle>
        <line x1="12" y1="1" x2="12" y2="3"></line>
        <line x1="12" y1="21" x2="12" y2="23"></line>
        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
        <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
        <line x1="1" y1="12" x2="3" y2="12"></line>
        <line x1="21" y1="12" x2="23" y2="12"></line>
        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
        <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
      </svg>
    </button>
    <button class="color-mode-btn dark-toggle" aria-label="Toggle dark mode" v-on:click="toggleTheme(false)">
      <svg id="moon" viewBox="0 0 24 24" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" class="fill-content"></path>
      </svg>
    </button>

  </div>
</template>

<script>
export default {
  name: "ThemeToggler",
  data() {
    return {
      light: true
    }
  },
  methods: {
    toggleTheme(light) {
      document.documentElement.setAttribute("color-mode", light ? "light" : "dark");
      localStorage.setItem("color-mode", light ? "light" : "dark");
    }
  }
}
</script>

<style scoped>
.color-mode-btn {
  cursor: pointer;
  background: none;
  border: none;
  outline: none;
  padding: 0;
}

.color-mode-btn > svg {
  width: 1.56rem;
  height: 1.56rem;
  stroke: var(--color-text);
  transition: all var(--transition-speed);
  -webkit-transition: all var(--transition-speed);
  -moz-transition: all var(--transition-speed);
  -o-transition: all var(--transition-speed);
  fill: var(--color-nav-bg);
}

.fill-content {
  transition: fill var(--transition-speed);
  -webkit-transition: fill var(--transition-speed);
  -moz-transition: fill var(--transition-speed);
  -o-transition: fill var(--transition-speed);
}

.color-mode-btn:hover > svg {
  opacity: 0.8;
}

.color-mode-btn:hover svg .fill-content {
  fill: var(--color-text);
}
</style>