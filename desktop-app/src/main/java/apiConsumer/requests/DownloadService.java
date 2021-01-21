package apiConsumer.requests;

import apiConsumer.objects.DownloadID;
import apiConsumer.objects.DownloadInfo;
import apiConsumer.objects.SpotifyDownloadRequest;
import apiConsumer.objects.YouTubeDownloadRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface DownloadService {
    @POST("youtube")
    Call<DownloadID> requestYouTubeDownload(
            @Header("Authorization") String token,
            @Body YouTubeDownloadRequest youTubeDownloadRequest
            );

    @POST("spotify")
    Call<DownloadID> requestSpotifyDownload(
            @Header("Authorization") String token,
            @Body SpotifyDownloadRequest spotifyDownloadRequest
            );

    @GET("download-info/{id}")
    Call<DownloadInfo> getDownloadInfo(@Header("Authorization") String token, @Path("id") String downloadID);

    @GET("download-data/{id}")
    @Streaming
    Call<ResponseBody> getDownloadData(@Header("Authorization") String token, @Path("id") String downloadID);
}
