<template>
  <svg class="arrow-back" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="sort-up" role="img" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 320 512"><path class="arrow-back-path" d="M279 224H41c-21.4 0-32.1-25.9-17-41L143 64c9.4-9.4 24.6-9.4 33.9 0l119 119c15.2 15.1 4.5 41-16.9 41z"></path></svg>
</template>

<script>
export default {
  name: "BackArrow"
}
</script>

<style scoped>
.arrow-back {
  height: 2rem;
  width: 2rem;
  transform: rotate(-90deg);
  display: flex;
  justify-content: center;
  align-items: center;
  align-content: center;
}
.arrow-back-path {
  fill: var(--color-text);
  transition: all var(--transition-speed);
  -webkit-transition: all var(--transition-speed);
  -moz-transition: all var(--transition-speed);
  -o-transition: all var(--transition-speed);
}

.arrow-back:hover .arrow-back-path {
  fill: var(--color-primary);
}
</style>