import apiConsumer.AuthManager;
import views.ViewsManager;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        AuthManager.getInstance();
        ViewsManager.getInstance();
    }
}
