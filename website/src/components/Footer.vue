<template>
  <div id="footer">
    <a href="https://github.com/Giuliopime/Downloadify" target="_blank" id="author">
      <svg id="author-icon" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="code" viewBox="0 0 16 16"><path d="M6.47435 14.1581L10.4743 2.15812L9.52566 1.84189L5.52566 13.8419L6.47435 14.1581Z" class="icon-fill-section"></path><path d="M3.62373 3.67075L0.181352 7.6049C-0.016587 7.83111 -0.0165865 8.16889 0.181352 8.3951L3.62373 12.3293L4.37631 11.6707L1.16441 8L4.37631 4.32925L3.62373 3.67075Z" class="icon-fill-section"></path><path d="M12.3763 3.67075L15.8187 7.6049C16.0166 7.83111 16.0166 8.16889 15.8187 8.3951L12.3763 12.3293L11.6237 11.6707L14.8357 8L11.6237 4.32925L12.3763 3.67075Z" class="icon-fill-section"></path></svg>
      <span id="author-name">BY GIULIOPIME</span>
    </a>
  </div>
</template>

<script>
export default {
  name: "Footer"
};
</script>

<style scoped>
#footer {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 100%;
  text-align: center;
  font-size: 0.62rem;
  letter-spacing: 0.1rem;
  background-color: var(--color-footer-bg);
  transition: background-color var(--transition-speed);
  -webkit-transition: background-color var(--transition-speed);
  -moz-transition: background-color var(--transition-speed);
  -o-transition: background-color var(--transition-speed);
}

#author {
  display: flex;
  align-items: center;
  justify-content: center;
}
#author:hover {
  color: var(--color-primary);
}
#author:hover .icon-fill-section, #author:hover #author-name {
  color: var(--color-primary);
  fill: var(--color-primary);
}

#author-name {
  color: var(--color-text);
}

#author-icon {
  height: 0.87rem;
  margin-right: 0.625rem;
}
.icon-fill-section {
  fill: var(--color-text);
}
</style>
