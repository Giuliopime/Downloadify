package views;

import apiConsumer.AuthManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewsManager extends JFrame{
    private static ViewsManager instance;

    public static ViewsManager getInstance() {
        if (instance == null)
            instance = new ViewsManager();

        return instance;
    }

    private ViewsManager() {
        initFrame();
        changeView(Views.HOME);

        setName("Downloadify");
        setFocusable(true);
        setFocusableWindowState(true);
        setSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(800, 600));
        setVisible(true);
        setLocationRelativeTo(null);
    }

    private void initFrame() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }


    public void changeView(Views view) {
        if (view == null) {
            setContentPane(new Home().getRootPnl());
        } else {
            switch (view) {
                case LOGIN -> setContentPane(new Login(Views.HOME).getRootPnl());
                case YOUTUBE -> setContentPane(AuthManager.getInstance().isAuthenticated() ? new YouTube().getRootPnl() : new Login(Views.YOUTUBE).getRootPnl());
                case SPOTIFY -> setContentPane(AuthManager.getInstance().isAuthenticated() ? new Spotify().getRootPnl() : new Login(Views.SPOTIFY).getRootPnl());
                default -> setContentPane(new Home().getRootPnl());
            }
        }
        validate();
    }


    public JFrame getFrame() {
        return this;
    }
}

