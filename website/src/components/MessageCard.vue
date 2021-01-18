<template>
  <div class="msg-card hidden">
    <div class="msg-card-content">
      {{ msg }}
      <button
          class="msg-card-btn"
          type="button"
          aria-label="Dismiss this message"
          v-on:click="hideMsgCard"
      >
        <svg
            class="msg-icon"
            viewBox="0 0 16 16"
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
    hideMsgCard () {
      document.getElementsByClassName('msg-card')[0].classList.add('hidden')
    },
  }
}
</script>

<style scoped>
/* Error card */
.msg-card {
  width: 100%;
  background-color: var(--color-info-bg);
  padding: 0.625rem 1.25rem;
  margin-bottom: 1rem;
  font-size: 0.81rem;
  border: 1px var(--color-info-border) solid;
  border-radius: 5px;
}

.msg-card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.msg-icon {
  fill: var(--color-info-icon);
  width: 1rem;
  height: 1rem;
}

.msg-card-btn {
  background: none;
  border: none;
  margin-top: 0.1rem;
}

.msg-icon:hover {
  cursor: pointer;
  opacity: 0.7;
}

.msg-card-btn:focus {
  outline: none;
  box-shadow: none;
}
</style>