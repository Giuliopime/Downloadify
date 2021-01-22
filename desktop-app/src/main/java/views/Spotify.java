package views;

import apiConsumer.APIConsumer;
import apiConsumer.objects.DownloadInfo;
import apiConsumer.objects.SpotifyDownloadRequest;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import dialogs.ErrorDialog;
import dialogs.InfoDialog;
import views.graphicUtils.Colors;
import views.graphicUtils.Styler;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Spotify {
    private JPanel rootPnl;
    private JPanel navbarPnl;
    private JLabel logoLbl;
    private JPanel contentPnl;
    private JPanel footerPnl;
    private JLabel footerLbl;
    private JTextField URLInpField;
    private JLabel headerLbl;
    private JLabel inpFieldLbl;
    private JLabel questionLbl;
    private JPanel contentWrapperPnl;
    private JPanel cardWrapperPnl;
    private JPanel cardPnl;
    private JPanel labelsPnl;
    private JPanel inpFieldPanle;
    private JPanel downloadBtnPnl;
    private JLabel downloadLbl;

    public Spotify() {
        $$$setupUI$$$();
        Styler.styleView(rootPnl, navbarPnl, null, contentPnl, footerPnl, logoLbl, null, footerLbl);
        Styler.styleCard(headerLbl, contentWrapperPnl, cardWrapperPnl, cardPnl, labelsPnl, inpFieldPanle, downloadBtnPnl, inpFieldLbl, questionLbl, URLInpField, null, downloadLbl);

        initListeners();
    }

    private void initListeners() {
        headerLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ViewsManager.getInstance().changeView(Views.HOME);
            }
        });

        questionLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://support.symdistro.com/hc/en-us/articles/360039036711-Spotify-How-to-obtain-a-URI-URL"));
                    } catch (IOException | URISyntaxException exception) {
                        new ErrorDialog("Couldn't open the browser.");
                    }
                } else {
                    new ErrorDialog("Couldn't open the browser.");
                }
            }
        });

        addMouseListenerToDownloadBtn();
    }

    private void addMouseListenerToDownloadBtn() {
        downloadBtnPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                downloadBtnPnl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                Styler.removeMouseListenersFromDownloadBtn(downloadBtnPnl, downloadLbl);
                String URL = URLInpField.getText();

                if (URL.isBlank())
                    handleError();
                else {
                    downloadLbl.setText("Processing...");
                    String downloadID = APIConsumer.getInstance().requestSpotifyDownload(new SpotifyDownloadRequest(URL));
                    if (downloadID == null) {
                        handleError();
                        return;
                    }

                    downloadLbl.setText("Downloading...");
                    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                    Runnable checkDownloadInfo = () -> {
                        DownloadInfo downloadInfo = APIConsumer.getInstance().checkDownloadInfo(downloadID);
                        if (downloadInfo == null || downloadInfo.isErrored()) {
                            executor.shutdown();
                            handleError();
                            return;
                        }

                        if (downloadInfo.isCompleted()) {
                            downloadLbl.setText("Receiving files...");
                            executor.shutdown();

                            if(ViewsManager.getInstance().getFrame().isActive())
                                saveFiles(downloadID);
                            else {
                                ViewsManager.getInstance().getFrame().addWindowFocusListener(new WindowFocusListener() {
                                    @Override
                                    public void windowGainedFocus(WindowEvent e) {
                                        saveFiles(downloadID);
                                    }

                                    @Override
                                    public void windowLostFocus(WindowEvent e) {

                                    }
                                });
                            }
                        }
                    };
                    executor.scheduleAtFixedRate(checkDownloadInfo, 0, 3, TimeUnit.SECONDS);
                }
            }
        });
    }

    private void saveFiles(String downloadID) {
        JFrame frame = ViewsManager.getInstance().getFrame();
        for(WindowFocusListener focusListener: frame.getWindowFocusListeners())
            frame.removeWindowFocusListener(focusListener);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int scelta = fileChooser.showSaveDialog(ViewsManager.getInstance().getFrame());

        if (scelta != JFileChooser.APPROVE_OPTION)
            return;

        CompletableFuture.runAsync(() -> {
            boolean downloadResult = APIConsumer.getInstance().downloadData(downloadID, fileChooser.getSelectedFile().getPath());
            if(downloadResult) {
                addMouseListenerToDownloadBtn();
                Styler.addMouseListenersToDownloadBtn(downloadBtnPnl, downloadLbl);
                downloadLbl.setText("Download");
            }
            else
                handleError();
        });
    }

    private void handleError() {
        downloadLbl.setText("Download");
        Styler.addMouseListenersToDownloadBtn(downloadBtnPnl, downloadLbl);
        addMouseListenerToDownloadBtn();
        new ErrorDialog("An error occurred.\nMake sure the URL is correct and select a valid download folder when prompted.");
    }

    public JPanel getRootPnl() {
        return rootPnl;
    }

    private void createUIComponents() {
        // Rounded download button
        downloadBtnPnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(12, 12);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                //Draws the rounded panel with borders.
                graphics.setColor(Colors.spotifyDarker);
                graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
                graphics.setColor(Colors.cardBorder);
                graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);
            }
        };
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPnl = new JPanel();
        rootPnl.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        navbarPnl = new JPanel();
        navbarPnl.setLayout(new GridLayoutManager(1, 2, new Insets(10, 20, 10, 10), -1, -1));
        rootPnl.add(navbarPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(-1, 100), new Dimension(-1, 120), new Dimension(-1, 140), 0, false));
        logoLbl = new JLabel();
        Font logoLblFont = this.$$$getFont$$$("Krungthep", Font.BOLD, 26, logoLbl.getFont());
        if (logoLblFont != null) logoLbl.setFont(logoLblFont);
        logoLbl.setHorizontalAlignment(10);
        logoLbl.setIcon(new ImageIcon(getClass().getResource("/logo.png")));
        logoLbl.setIconTextGap(10);
        logoLbl.setText("DOWNLOADIFY");
        navbarPnl.add(logoLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        navbarPnl.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        contentPnl = new JPanel();
        contentPnl.setLayout(new GridLayoutManager(1, 1, new Insets(50, 30, 30, 30), -1, -1));
        rootPnl.add(contentPnl, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        contentWrapperPnl = new JPanel();
        contentWrapperPnl.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPnl.add(contentWrapperPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        headerLbl = new JLabel();
        Font headerLblFont = this.$$$getFont$$$("Avenir", -1, 28, headerLbl.getFont());
        if (headerLblFont != null) headerLbl.setFont(headerLblFont);
        headerLbl.setIcon(new ImageIcon(getClass().getResource("/back_arrow.png")));
        headerLbl.setIconTextGap(10);
        headerLbl.setText("Spotify Downloader");
        contentWrapperPnl.add(headerLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        cardWrapperPnl = new JPanel();
        cardWrapperPnl.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        contentWrapperPnl.add(cardWrapperPnl, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        cardPnl = new JPanel();
        cardPnl.setLayout(new GridLayoutManager(5, 1, new Insets(20, 20, 20, 20), -1, -1));
        cardWrapperPnl.add(cardPnl, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 250), new Dimension(400, 250), new Dimension(400, 250), 0, false));
        labelsPnl = new JPanel();
        labelsPnl.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        cardPnl.add(labelsPnl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inpFieldLbl = new JLabel();
        Font inpFieldLblFont = this.$$$getFont$$$("Helvetica", Font.BOLD, 15, inpFieldLbl.getFont());
        if (inpFieldLblFont != null) inpFieldLbl.setFont(inpFieldLblFont);
        inpFieldLbl.setText("Spotify Song / Album URL");
        labelsPnl.add(inpFieldLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        questionLbl = new JLabel();
        Font questionLblFont = this.$$$getFont$$$("Helvetica", Font.BOLD, 13, questionLbl.getFont());
        if (questionLblFont != null) questionLbl.setFont(questionLblFont);
        questionLbl.setText("How to get it?");
        labelsPnl.add(questionLbl, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        labelsPnl.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        inpFieldPanle = new JPanel();
        inpFieldPanle.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        cardPnl.add(inpFieldPanle, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        URLInpField = new JTextField();
        Font URLInpFieldFont = this.$$$getFont$$$(null, -1, 12, URLInpField.getFont());
        if (URLInpFieldFont != null) URLInpField.setFont(URLInpFieldFont);
        URLInpField.setInheritsPopupMenu(false);
        URLInpField.setMargin(new Insets(2, 6, 2, 6));
        URLInpField.setOpaque(false);
        URLInpField.setText("");
        URLInpField.setToolTipText("");
        inpFieldPanle.add(URLInpField, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, new Dimension(340, 30), new Dimension(340, 30), new Dimension(340, 30), 0, false));
        downloadBtnPnl.setLayout(new GridLayoutManager(1, 1, new Insets(8, 16, 8, 16), -1, -1));
        cardPnl.add(downloadBtnPnl, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadLbl = new JLabel();
        Font downloadLblFont = this.$$$getFont$$$(null, Font.BOLD, 14, downloadLbl.getFont());
        if (downloadLblFont != null) downloadLbl.setFont(downloadLblFont);
        downloadLbl.setText("Download");
        downloadBtnPnl.add(downloadLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        cardPnl.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        final Spacer spacer4 = new Spacer();
        cardPnl.add(spacer4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final Spacer spacer5 = new Spacer();
        cardWrapperPnl.add(spacer5, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        cardWrapperPnl.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        cardWrapperPnl.add(spacer7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        contentWrapperPnl.add(spacer8, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        final Spacer spacer9 = new Spacer();
        contentWrapperPnl.add(spacer9, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(42, -1), new Dimension(42, -1), new Dimension(42, -1), 0, false));
        footerPnl = new JPanel();
        footerPnl.setLayout(new GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        footerPnl.setEnabled(false);
        rootPnl.add(footerPnl, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 40), new Dimension(-1, 40), new Dimension(-1, 40), 0, false));
        footerLbl = new JLabel();
        Font footerLblFont = this.$$$getFont$$$("Roboto Light", -1, 13, footerLbl.getFont());
        if (footerLblFont != null) footerLbl.setFont(footerLblFont);
        footerLbl.setHorizontalAlignment(0);
        footerLbl.setHorizontalTextPosition(0);
        footerLbl.setText("GitHub");
        footerPnl.add(footerLbl, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPnl;
    }

}
