package apiConsumer.objects;

public class SpotifyDownloadRequest {
    private String spotifyURL;

    public SpotifyDownloadRequest(String spotifyURL) {
        this.spotifyURL = spotifyURL;
    }

    public String getSpotifyURL() {
        return spotifyURL;
    }

    public void setSpotifyURL(String spotifyURL) {
        this.spotifyURL = spotifyURL;
    }
}
