package apiConsumer.objects;

public class YouTubeDownloadRequest {
    private String youtubeURL;
    private boolean audioOnly;

    public YouTubeDownloadRequest(String youtubeURL, boolean audioOnly) {
        this.youtubeURL = youtubeURL;
        this.audioOnly = audioOnly;
    }

    public String getYoutubeURL() {
        return youtubeURL;
    }

    public void setYoutubeURL(String youtubeURL) {
        this.youtubeURL = youtubeURL;
    }

    public boolean isAudioOnly() {
        return audioOnly;
    }

    public void setAudioOnly(boolean audioOnly) {
        this.audioOnly = audioOnly;
    }
}
