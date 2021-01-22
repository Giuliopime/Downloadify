package apiConsumer;

import apiConsumer.objects.*;
import apiConsumer.requests.DownloadService;
import apiConsumer.requests.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataManager.DataManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import views.ViewsManager;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class APIConsumer {
    private static APIConsumer instance;
    private final UserService userService;
    private final DownloadService downloadService;

    public static APIConsumer getInstance() {
        if(instance == null)
            instance = new APIConsumer();

        return instance;
    }

    public APIConsumer() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://downloadify.giuliopime.dev/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userService= retrofit.create(UserService.class);
        downloadService = retrofit.create(DownloadService.class);
    }

    public boolean logIn(String accessToken) {
        try {
            Call<User> userCall = userService.getUser(accessToken);
            Response<User> userResponse = userCall.execute();

            return userResponse.isSuccessful();
        }
        catch (IOException e) {
            System.out.println("An error occurred while sending a get request:\n" + e.getMessage());
            return false;
        }
    }

    public String requestYouTubeDownload(YouTubeDownloadRequest youTubeDownloadRequest) {
        try {
            Call<DownloadID> requestedYouTubeDownload = downloadService.requestYouTubeDownload(DataManager.getTokenDataCached().getToken(), youTubeDownloadRequest);
            Response<DownloadID> response = requestedYouTubeDownload.execute();

            return response.isSuccessful() ? response.body().getDownloadID() : null;
        }
        catch (IOException e) {
            System.out.println("An error occurred while sending a get request:\n" + e.getMessage());
            return null;
        }
    }

    public String requestSpotifyDownload(SpotifyDownloadRequest spotifyDownloadRequest) {
        try {
            Call<DownloadID> requestSpotifyDownload = downloadService.requestSpotifyDownload(DataManager.getTokenDataCached().getToken(), spotifyDownloadRequest);
            Response<DownloadID> response = requestSpotifyDownload.execute();

            return response.isSuccessful() ? response.body().getDownloadID() : null;
        }
        catch (IOException e) {
            System.out.println("An error occurred while sending a get request:\n" + e.getMessage());
            return null;
        }
    }

    public DownloadInfo checkDownloadInfo(String downloadID) {
        try {
            Call<DownloadInfo> getDownloadInfo = downloadService.getDownloadInfo(DataManager.getTokenDataCached().getToken(), downloadID);
            Response<DownloadInfo> response = getDownloadInfo.execute();

            return response.isSuccessful() ? response.body() : null;
        }
        catch (IOException e) {
            System.out.println("An error occurred while sending a get request:\n" + e.getMessage());
            return null;
        }
    }

    public boolean downloadData(String downloadID, String directoryPath) {
        try {
            Response<ResponseBody> response = downloadService.getDownloadData(DataManager.getTokenDataCached().getToken(), downloadID).execute();
            if(response.isSuccessful()) {
                File file = new File(directoryPath + "/" + response.headers().get("file-name"));
                OutputStream outputStream = new FileOutputStream(file, false);
                response.body().byteStream().transferTo(outputStream);
                outputStream.close();
                return true;
            }
            else
                return false;
        }
        catch (IOException e) {
            System.out.println("An error occurred while receiving download data:\n" + e.getMessage());
            return false;
        }
    }
}
