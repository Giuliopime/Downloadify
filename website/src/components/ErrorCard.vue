<template>
  <div class="error-card hidden">
    <div class="error-card-content">
      {{ msg }}
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
</template>

<script>
export default {
  name: "ErrorCard",
  props: {
    msg: String
  },
  methods: {
    hideErrorCard () {
      document.getElementsByClassName('error-card')[0].classList.add('hidden')
    },
  }
}
</script>

<style scoped>
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
</style>