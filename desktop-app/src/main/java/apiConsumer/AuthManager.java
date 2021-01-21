package apiConsumer;

import dataManager.DataManager;
import dataManager.objects.TokenData;

import java.io.IOException;

public class AuthManager {
    private static AuthManager instance;
    private boolean authenticated;

    public static AuthManager getInstance() {
        if(instance == null)
            instance = new AuthManager();

        return instance;
    }

    public AuthManager() {
        try {
            TokenData tokenData = DataManager.getTokenData();
            authenticated = APIConsumer.getInstance().logIn(tokenData.getToken());
        }
        catch (ClassNotFoundException e) {
            System.out.println("The project is missing the UserData class");
        }
        catch (IOException e) {
            System.out.println("An error occurred when trying to read the userData .dat file:\n" + e.getMessage());
        }
    }

    public boolean authenticate(String token) {
        try {
            authenticated = APIConsumer.getInstance().logIn(token);

            if(authenticated)
                DataManager.setTokenData(new TokenData(token));

            return authenticated;
        }
        catch (IOException e) {
            System.out.println("An error occurred when trying to authenticate into Downloadify API:\n" + e.getMessage());
            return false;
        }
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
