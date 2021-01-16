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
      <div class="mainContent">
        <div class="mainHeader">
          <h1>Sign in to Downloadify</h1>
        </div>
        <div class="errorCard hiddenCard">
          <div class="errorCardContent">
            Invalid Token.
            <button
              class="errorCardBtn"
              type="button"
              aria-label="Dismiss this message"
              v-on:click="hideErrorCard"
            >
              <svg
                class="errorIcon"
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
        <form id="loginForm">
          <label for="tokenField" class="input-label">
            Access Token
            <a class="label-link" href="/token-reset">Forgot token?</a>
          </label>
          <input
            type="password"
            name="tokenField"
            id="tokenField"
            class="inputField"
            autocapitalize="off"
            autocomplete="token"
          />
          <input
            name="login"
            value="Sign In"
            class="btn"
            id="submitBtn"
            data-disable-with="Signing in..."
            v-on:click="logIn"
          />
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
      document.getElementsByClassName('errorCard')[0].classList.add('hiddenCard')
    },
    showErrorCard() {
      document.getElementsByClassName('errorCard')[0].classList.remove('hiddenCard')
    },
    async logIn() {
      const token = document.getElementById('tokenField').value;

      if(!token)
        return this.showErrorCard()


      axios({
        url: BASEURL + 'login',
        headers: {
          Authorization: token
        }
      })
      .then(async res => {
        if(res.status !== 200)
          this.showErrorCard();
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

<style>
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

.main {
  width: 100%;
}

.mainContent {
  width: 340px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  padding: 0 16px 0 16px;
}

.mainHeader {
  width: 100%;
  margin-bottom: 20px;
  text-align: center;
}

h1 {
  font-size: 24px;
  font-weight: 300;
  letter-spacing: -0.5px;
  margin-top: 0;
  margin-bottom: 0;
}

.hiddenCard {
  display: none;
}

/* Error card */
.errorCard {
  width: 100%;
  background-color: var(--color-error-bg);
  padding: 10px 20px;
  margin-bottom: 15px;
  font-size: 13px;
  border: 1px var(--color-error-border) solid;
  border-radius: 5px;
}

.errorCardContent {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.errorIcon {
  fill: var(--color-error-icon);
}

.errorCardBtn {
  background: none;
  border: none;
  margin-top: 2px;
}

.errorIcon:hover {
  cursor: pointer;
  opacity: 0.7;
}

.errorCardBtn:focus {
  outline: none;
  box-shadow: none;
}

#loginForm {
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

.inputField {
  width: 100%;
  margin: 5px 0 30px 0;
  padding: 5px 12px;
  line-height: 20px;
  vertical-align: middle;
  border-radius: 6px;
  border: 1px solid var(--color-input-border);
  box-sizing: border-box;
}
.inputField:focus {
  outline: none !important;
  border-color: var(--color-input-focus-border);
  box-shadow: var(--color-input-focus-shadow);
}

.btn {
  width: 100%;
  text-align: center;
  color: white;
  padding: 5px 16px;
  line-height: 20px;
  font-weight: 500;
  font-size: 14px;
  border: 1px rgba(27, 31, 35, 0.15) solid;
  border-radius: 6px;
  background-color: var(--color-primary);
  transition: all 0.3s;
}
.btn:hover {
  opacity: 0.9;
  cursor: pointer;
}
</style>
