import Vue from "vue";
import VueRouter from "vue-router";

// APIs interaction
const axios = require('axios');
const { BASEURL } = require('../../config.json');

// Components
import Home from "../views/Home";
import Login from "../views/Login.vue";
import Spotify from "@/views/Spotify";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: "/login",
    name: "Login",
    component: Login
  },
  {
    path: "/spotify",
    name: 'spotify',
    component: Spotify,
    meta: {
      requiresAuth: true
    }
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

// Check if user is logged in and redirect to login page if he's not
axios.defaults.withCredentials = true

router.beforeEach((to, from, next) => {
  if(to.matched.some(record => record.meta.requiresAuth)) {
    try {
      axios(BASEURL + 'user')
          .then(res => {
            if(res.status !== 200)
              next({name: 'Login'})
            else
              next()
          })
          .catch(() => next({name: 'Login'}))
    } catch {
      next({name: 'Login'});
    }
  }
  next();
})

export default router;
