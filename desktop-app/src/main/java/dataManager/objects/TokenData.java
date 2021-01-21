package dataManager.objects;

import java.io.Serializable;

public class TokenData implements Serializable {
    private String token;

    public TokenData(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
