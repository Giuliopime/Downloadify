package dataManager;

import dataManager.objects.TokenData;

import java.io.*;
import java.net.URISyntaxException;

public class DataManager {
    private static TokenData tokenData;
    private static File app;

    static {
        try {
            app = new File(DataManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            System.out.println(app.getParentFile().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static TokenData getTokenData() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(app.getParentFile().getPath() + "/token.dat");
        ObjectInputStream ois = new ObjectInputStream(fis);

        TokenData tokenData = null;
        while (fis.available() > 0)
            tokenData = (TokenData) ois.readObject();

        ois.close();
        fis.close();

        DataManager.tokenData = tokenData;

        return tokenData;
    }

    public static void setTokenData(TokenData tokenData) throws IOException {
        FileOutputStream fos = new FileOutputStream(app.getParentFile().getPath() + "/token.dat");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(tokenData);

        oos.close();
        fos.close();

        DataManager.tokenData = tokenData;
    }

    public static TokenData getTokenDataCached() {
        return tokenData;
    }
}
