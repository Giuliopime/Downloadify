<template>
  <div id="spotify">
    <NavBar />
    <div class="main">
      <div class="main-header">
        <router-link to="/">
          <BackArrow />
        </router-link>
        <h1>YouTube Downloader</h1>
        <BackArrow class="hidden-present"/>
      </div>
      <div class="main-content">
        <ErrorCard  err="An error occurred, make sure the URL is correct."/>
        <MessageCard msg="Video / Playlist has been successfully downloaded."/>

        <div class="card">
          <label for="url-field" class="input-label">
            YouTube Song / Playlist URL
            <a class="label-link" href="https://support.google.com/youtube/answer/57793?co=GENIE.Platform%3DDesktop&hl=en" target="_blank">How to get it?</a>
          </label>
          <input
              type="text"
              placeholder="https://www.youtube.com/watch?v=dQw4w9WgXcQ"
              name="URL field"
              id="url-field"
              class="input-field"
              autocapitalize="off"
              autocomplete="off"
          />
          <div class="cb-field">
            <input type="checkbox" name="only-audio" id="only-audio-cb" value="0">
            <label for="only-audio-cb" class="cb-label">audio only</label>
          </div>
          <button
              class="btn"
              id="download-btn"
              v-on:click="sendDownloadRequest"
          >
            <svg class="btn-icon" id="dl-svg" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="arrow-circle-down" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="white" d="M504 256c0 137-111 248-248 248S8 393 8 256 119 8 256 8s248 111 248 248zm-143.6-28.9L288 302.6V120c0-13.3-10.7-24-24-24h-16c-13.3 0-24 10.7-24 24v182.6l-72.4-75.5c-9.3-9.7-24.8-9.9-34.3-.4l-10.9 11c-9.4 9.4-9.4 24.6 0 33.9L239 404.3c9.4 9.4 24.6 9.4 33.9 0l132.7-132.7c9.4-9.4 9.4-24.6 0-33.9l-10.9-11c-9.5-9.5-25-9.3-34.3.4z"></path></svg>
            <svg class="btn-icon hidden" id="loading-svg" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid">
              <g>
                <circle cx="60" cy="50" r="4" fill="#ffffff">
                  <animate attributeName="cx" repeatCount="indefinite" dur="0.819672131147541s" values="95;35" keyTimes="0;1" begin="-0.8174s"></animate>
                  <animate attributeName="fill-opacity" repeatCount="indefinite" dur="0.819672131147541s" values="0;1;1" keyTimes="0;0.2;1" begin="-0.8174s"></animate>
                </circle>
                <circle cx="60" cy="50" r="4" fill="#ffffff">
                  <animate attributeName="cx" repeatCount="indefinite" dur="0.819672131147541s" values="95;35" keyTimes="0;1" begin="-0.4026s"></animate>
                  <animate attributeName="fill-opacity" repeatCount="indefinite" dur="0.819672131147541s" values="0;1;1" keyTimes="0;0.2;1" begin="-0.4026s"></animate>
                </circle>
                <circle cx="60" cy="50" r="4" fill="#ffffff">
                  <animate attributeName="cx" repeatCount="indefinite" dur="0.819672131147541s" values="95;35" keyTimes="0;1" begin="0s"></animate>
                  <animate attributeName="fill-opacity" repeatCount="indefinite" dur="0.819672131147541s" values="0;1;1" keyTimes="0;0.2;1" begin="0s"></animate>
                </circle>
              </g><g transform="translate(-15 0)">
              <path d="M50 50L20 50A30 30 0 0 0 80 50Z" fill="#FFBE0B" transform="rotate(90 50 50)"></path>
              <path d="M50 50L20 50A30 30 0 0 0 80 50Z" fill="#FFBE0B">
                <animateTransform attributeName="transform" type="rotate" repeatCount="indefinite" dur="0.819672131147541s" values="0 50 50;45 50 50;0 50 50" keyTimes="0;0.5;1"></animateTransform>
              </path>
              <path d="M50 50L20 50A30 30 0 0 1 80 50Z" fill="#FFBE0B">
                <animateTransform attributeName="transform" type="rotate" repeatCount="indefinite" dur="0.819672131147541s" values="0 50 50;-45 50 50;0 50 50" keyTimes="0;0.5;1"></animateTransform>
              </path>
            </g>
              Download
            </svg>
            <span id="btn-text">Download</span>
          </button>
        </div>
      </div>

      <a id="download-a-el" class="hidden"></a>
    </div>
  </div>
</template>

<script>
const axios = require('axios');
const { BASEURL } = require('../../config.json');

import NavBar from "@/components/NavBar";
import ErrorCard from "@/components/ErrorCard";
import MessageCard from "@/components/MessageCard";
import BackArrow from "@/components/BackArrow";

export default {
  name: "YouTube",
  components: {ErrorCard, NavBar, MessageCard, BackArrow},
  /*
  Can be added to prevent exiting the page while downloading
  created() {
    window.onbeforeunload = () => {
      return "";
    }
  },
  */
  methods: {
    showErrorCard() {
      document.getElementsByClassName('err-card')[0].classList.remove('hidden')
    },
    showMessageCard() {
      document.getElementsByClassName('msg-card')[0].classList.remove('hidden')
    },
    showDownloadStateBtn() {
      const dlBtn = document.getElementById('download-btn');
      dlBtn.disabled = false;
      document.getElementById('dl-svg').classList.remove('hidden');
      document.getElementById('btn-text').textContent = 'Download';
      document.getElementById('loading-svg').classList.add('hidden');
    },
    showDownloadingStateBtn() {
      const dlBtn = document.getElementById('download-btn');
      dlBtn.disabled = true;
      document.getElementById('dl-svg').classList.add('hidden');
      document.getElementById('btn-text').textContent = 'Downloading, please be patient...';
      document.getElementById('loading-svg').classList.remove('hidden');
    },
    sendDownloadRequest() {
      const youtubeLink = document.getElementById('url-field').value;

      if(!youtubeLink)
        return this.showErrorCard();

      this.showDownloadingStateBtn();


      axios({
        method: 'post',
        url: BASEURL + 'youtube',
        responseType: 'arraybuffer',
        data: {
          youtubeURL: youtubeLink,
          audioOnly: document.getElementById('only-audio-cb').checked
        }
      })
          .then(async res => {
            // If the request wasn't successful show the error card
            if(res.status !== 200)
              this.showErrorCard();
            else {
              const blob = new Blob([res.data], {type: "zip"});
              const url = window.URL.createObjectURL(blob);
              const a = document.getElementById('download-a-el');
              a.href = url;

              a.download = res.headers['zip-file-name'] + '.zip';
              a.click();
              window.URL.revokeObjectURL(url);
              this.showMessageCard();
            }
            this.showDownloadStateBtn();
          })
          .catch(e => {
            console.log(e)
            this.showDownloadStateBtn();
            this.showErrorCard();
          });
    }
  }
}
</script>

<style scoped>
.main-content {
  width: 23.5rem;
}

.arrow-back {
  margin-top: 0.1rem;
}

.input-field {
  margin: 0.31rem 0 0.375rem 0;
}

.cb-field {
  display: flex;
  align-items: center;
  align-self: flex-start;
  margin-bottom: 1.875rem;
  margin-left: 0.25rem;
  font-size: 0.75rem;
}

#only-audio-cb {
  margin-right: 0.31rem;
  border: 1px var(--color-input-border) solid !important;
}

.btn {
  background-color: var(--color-youtube);
  transition: all var(--transition-speed);
}

.btn-icon {
  height: 1rem;
  margin-right: 0.5rem;
}
#loading-svg {
  height: 1.25rem;
  background: none;
  display: block;
  shape-rendering: auto;
}
</style>