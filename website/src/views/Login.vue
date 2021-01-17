<template>
  <div id="login">
    <div class="header">
      <div class="header-container">
        <a class="header-logo-link" href="/" aria-label="Homepage">
          <img class="header-logo" src="../assets/images/logo.png" alt="Downloadify Logo">
        </a>
      </div>
    </div>
    <div class="main">
      <div class="main-content">
        <div class="main-header">
          <h1>Sign in to Downloadify</h1>
        </div>
        <ErrorCard msg="Invalid Token."/>
        <form class="card">
          <label for="token-field" class="input-label">
            Access Token
            <router-link class="label-link" to="/token-forgot">Forgot token?</router-link>
          </label>
          <input
            type="password"
            name="tokenField"
            id="token-field"
            class="input-field"
            autocapitalize="off"
            autocomplete="token"
          />
          <button
            class="btn"
            id="submit-btn"
            v-on:click="logIn"
          >
            Sign in
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import ErrorCard from "@/components/ErrorCard";
const axios = require('axios');
const { BASEURL } = require('../../config.json');

export default {
  name: "Login",
  components: {ErrorCard},
  methods: {
    showErrorCard() {
      document.getElementsByClassName('msg-card')[0].classList.remove('hidden')
    },
    async logIn() {
      // Disable the submit button for 0.3 seconds to prevent spam and to show the Signing in... text
      const loginButton = document.getElementById('submit-btn');

      loginButton.disabled = true;
      loginButton.innerText = 'Signing in...'

      setTimeout(() => {
        loginButton.disabled = false;
        loginButton.innerText = "Sign in";
      }, 300);


      const token = document.getElementById('token-field').value;

      // If no token has been entered show error by default
      if(!token)
        return this.showErrorCard()

      // Make get request to the server
      axios({
        url: BASEURL + 'login',
        headers: {
          Authorization: token
        }
      })
      .then(async res => {
        // If the request wasn't successful the token was invalid, so show an error
        if(res.status !== 200)
          this.showErrorCard();
        // Otherwise the token was correct
        else
          await this.$router.push({name: 'Home'});
      })
      .catch(() => {
        this.showErrorCard();
      });
    }
  }
};
</script>

<style scoped>
.header {
  width: 100%;
  padding: 32px 0 24px 0;
}
.header-container {
  text-align: center;
}
.header-logo {
  height: 70px;
}

.main-content {
  width: 372px;
}

.input-field {
  margin: 5px 0 30px 0;
}
</style>
