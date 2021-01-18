<template>
  <div id="app">
    <div id="wrapper">
      <router-view />
      <div id="push"></div>
    </div>
    <Footer id="footer"></Footer>
  </div>
</template>

<script>
import Footer from "@/components/Footer";

export default {
  components: {
    Footer
  }
}
</script>

<style>
@import "assets/styles/style.css";

/* Sticky footer setup */
#app {
  height: 100%;
  margin: 0 ;
}
#wrapper {
  min-height: 100%;
  margin-bottom: -3.125rem;
}
#footer,
#push {
  height: 3.125rem;
}
</style>
