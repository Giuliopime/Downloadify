package apiConsumer.requests;

import apiConsumer.objects.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserService {
    @GET("user")
    Call<User> getUser(@Header("Authorization") String accessToken);
}
