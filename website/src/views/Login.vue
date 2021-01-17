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
        <div class="error-card hidden">
          <div class="error-card-content">
            Invalid Token.
            <button
              class="error-card-btn"
              type="button"
              aria-label="Dismiss this message"
              v-on:click="hideErrorCard"
            >
              <svg
                class="error-icon"
                viewBox="0 0 16 16"
                width="16"
                height="16"
                aria-hidden="true"
              >
                <path
                  fill-rule="evenodd"
                  d="M3.72 3.72a.75.75 0 011.06 0L8 6.94l3.22-3.22a.75.75 0 111.06 1.06L9.06 8l3.22 3.22a.75.75 0 11-1.06 1.06L8 9.06l-3.22 3.22a.75.75 0 01-1.06-1.06L6.94 8 3.72 4.78a.75.75 0 010-1.06z"
                ></path>
              </svg>
            </button>
          </div>
        </div>
        <form id="login-form">
          <label for="token-field" class="input-label">
            Access Token
            <a class="label-link" href="/token-reset">Forgot token?</a>
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
const axios = require('axios');
const { BASEURL } = require('../../config.json');

export default {
  name: "Login",
  methods: {
    hideErrorCard () {
      document.getElementsByClassName('error-card')[0].classList.add('hidden')
    },
    showErrorCard() {
      document.getElementsByClassName('error-card')[0].classList.remove('hidden')
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
  width: 340px;
}

/* Error card */
.error-card {
  width: 100%;
  background-color: var(--color-error-bg);
  padding: 10px 20px;
  margin-bottom: 15px;
  font-size: 13px;
  border: 1px var(--color-error-border) solid;
  border-radius: 5px;
}

.error-card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.error-icon {
  fill: var(--color-error-icon);
}

.error-card-btn {
  background: none;
  border: none;
  margin-top: 2px;
}

.error-icon:hover {
  cursor: pointer;
  opacity: 0.7;
}

.error-card-btn:focus {
  outline: none;
  box-shadow: none;
}


/* Login form */
#login-form {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  font-size: 14px;
  border: 1px solid var(--color-border);
  border-radius: 5px;
  background-color: var(--color-card-bg);
}

.input-label {
  align-self: flex-start;
  width: 100%;
  font-weight: 600;
  margin-bottom: 7px;
  display: block;
}

.label-link {
  float: right;
  font-size: 12px;
  color: var(--color-text-link);
  text-decoration: none;
  font-weight: 600;
}

.input-field {
  width: 100%;
  margin: 5px 0 30px 0;
  padding: 5px 12px;
  line-height: 20px;
  vertical-align: middle;
  border-radius: 6px;
  border: 1px solid var(--color-input-border);
  box-sizing: border-box;
}
.input-field:focus {
  outline: none !important;
  border-color: var(--color-input-focus-border);
  box-shadow: var(--color-input-focus-shadow);
}
</style>
