<template>
  <div id="spotify">
    <NavBar />
    <div class="main">
      <div class="main-header">
        <h1>Spotify Downloader</h1>
      </div>
      <div class="main-content">
        <ErrorCard  msg="An error occurred, make sure the URL is correct."/>

        <div class="card">
          <label for="url-field" class="input-label">
            Spotify Song / Album URL
            <a class="label-link" href="https://support.symdistro.com/hc/en-us/articles/360039036711-Spotify-How-to-obtain-a-URI-URL" target="_blank">How to get it?</a>
          </label>
          <input
              type="text"
              placeholder="https://open.spotify.com/..."
              name="URL field"
              id="url-field"
              class="input-field"
              autocapitalize="off"
          />
          <button
              class="btn"
              id="download-btn"
              v-on:click="sendDownloadRequest"
          >
            <svg id="dl-svg" aria-hidden="true" focusable="false" data-prefix="fas" data-icon="arrow-circle-down" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path fill="white" d="M504 256c0 137-111 248-248 248S8 393 8 256 119 8 256 8s248 111 248 248zm-143.6-28.9L288 302.6V120c0-13.3-10.7-24-24-24h-16c-13.3 0-24 10.7-24 24v182.6l-72.4-75.5c-9.3-9.7-24.8-9.9-34.3-.4l-10.9 11c-9.4 9.4-9.4 24.6 0 33.9L239 404.3c9.4 9.4 24.6 9.4 33.9 0l132.7-132.7c9.4-9.4 9.4-24.6 0-33.9l-10.9-11c-9.5-9.5-25-9.3-34.3.4z"></path></svg>
            Download
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

export default {
  name: "Spotify",
  components: {ErrorCard, NavBar},
  methods: {
    showErrorCard() {
      document.getElementsByClassName('error-card')[0].classList.remove('hidden')
    },
    sendDownloadRequest() {
      const spotifyLink = document.getElementById('url-field').value;

      if(!spotifyLink)
        return this.showErrorCard();

      const dlBtn = document.getElementById('download-btn');
      dlBtn.disabled = true;
      dlBtn.innerText = 'Downloading, please be patient...';

      axios.post(BASEURL + 'spotify', { spotifyURL: spotifyLink, responseType: 'blob' })
          .then(async res => {
            // If the request wasn't successful show the error card
            if(res.status !== 200)
              this.showErrorCard();
            else {
              const binaryData = [];
              binaryData.push(res.data);
              const url = window.URL.createObjectURL(new Blob(binaryData, {type: 'application/octet-stream'}));
              const a = document.getElementById('download-a-el');
              a.href = url;

              a.download = 'test.zip';
              document.body.appendChild(a);
              a.click();
              window.URL.revokeObjectURL(url);
              alert('your file has downloaded!'); // or you know, something with better UX...
            }
            restoreDlBtn()
          })
          .catch(e => {
            console.log(e)
            restoreDlBtn()
            this.showErrorCard();
          });


      function restoreDlBtn() {
        dlBtn.disabled = false;
        dlBtn.innerText = "Download";
      }
    }
  }
}
</script>

<style scoped>
.main-content {
  width: 372px;
}

.input-field {
  margin: 5px 0 30px 0;
}

.btn {
  background-color: var(--color-spotify-darker);
  transition: all 0.3s;
}

#dl-svg {
  height: 16px;
  margin-right: 8px;
}
</style>