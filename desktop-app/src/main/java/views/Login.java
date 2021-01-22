package views;

import apiConsumer.AuthManager;
import dialogs.ErrorDialog;
import dialogs.InfoDialog;
import views.graphicUtils.Colors;
import views.graphicUtils.RoundedBorder;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class Login {
    private JPanel rootPnl;
    private JPanel footerPnl;
    private JLabel footerLbl;
    private JPanel inpFieldPanle;
    private JLabel logoLbl;
    private JTextField tokenInpField;
    private JPanel headerPnl;
    private JPanel titlePnl;
    private JLabel titleLbl;
    private JPanel contentPnl;
    private JPanel cardPnl;
    private JPanel labelsPnl;
    private JLabel inpFieldLbl;
    private JLabel questionLbl;
    private JPanel signInPnl;
    private JLabel signInLbl;

    private final Views redirect;

    public Login(Views redirect) {
        this.redirect = redirect;
        $$$setupUI$$$();
        stylePanel();
        initEvents();
    }

    private void stylePanel() {
        JPanel[] panels = new JPanel[]{headerPnl, footerPnl, rootPnl, cardPnl, contentPnl, labelsPnl, titlePnl, inpFieldPanle, signInPnl};
        for (JPanel panel : panels)
            panel.setBackground(Colors.primaryBG);

        footerLbl.setForeground(Colors.text);
        footerLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/Giuliopime/Downloadify"));
                    } catch (IOException | URISyntaxException ignored) {
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                footerLbl.getRootPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
                footerLbl.setForeground(Colors.primary);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                footerLbl.getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                footerLbl.setForeground(Colors.text);
            }
        });

        cardPnl.setBorder(new RoundedBorder(Colors.cardBorder, 12));

        titleLbl.setForeground(Colors.text);
        inpFieldLbl.setForeground(Colors.text);

        questionLbl.setForeground(Colors.textLink);
        questionLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                questionLbl.setForeground(Colors.textLinkHover);
                questionLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                questionLbl.setForeground(Colors.textLink);
                questionLbl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        tokenInpField.setBackground(Colors.primaryBG);
        tokenInpField.setBorder(new RoundedBorder(Colors.inputBorder, 12));
        tokenInpField.setCaretColor(Colors.text);
        tokenInpField.setForeground(Colors.text);

        tokenInpField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                tokenInpField.setBorder(new RoundedBorder(Colors.inputFocusedBorder, 12));
            }

            @Override
            public void focusLost(FocusEvent e) {
                tokenInpField.setBorder(new RoundedBorder(Colors.inputBorder, 12));
            }
        });

        signInLbl.setForeground(Colors.text);
        signInPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                signInLbl.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                signInLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                signInPnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                signInLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signInPnl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                signInLbl.setForeground(Colors.text);
            }
        });
    }

    private void initEvents() {
        rootPnl.registerKeyboardAction(e -> logIn(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addMouseListenersToSignInBtn();

        questionLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new InfoDialog("Contact the developer to get your token back.");
            }
        });
    }

    private void logIn() {
        removeMouseListenersFromSignInBtn();
        String token = tokenInpField.getText();

        if (token.isBlank()) {
            new ErrorDialog("Invalid Token.");
            addMouseListenersToSignInBtn();
        }
        else {
            signInLbl.setText("Signing you in...");
            if (AuthManager.getInstance().authenticate(token))
                ViewsManager.getInstance().changeView(redirect);
            else {
                signInLbl.setText("Sign in");
                new ErrorDialog("Invalid Token.");
                addMouseListenersToSignInBtn();
            }
        }
    }

    private void removeMouseListenersFromSignInBtn() {
        for(MouseListener mouseListener: signInPnl.getMouseListeners())
            signInPnl.removeMouseListener(mouseListener);
    }

    private void addMouseListenersToSignInBtn() {
        signInPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                logIn();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                signInLbl.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                signInLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                signInPnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                signInLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signInPnl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                signInLbl.setForeground(Colors.text);
            }
        });
    }

    public JPanel getRootPnl() {
        return rootPnl;
    }

    private void createUIComponents() {
        signInPnl = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(12, 12);
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


                //Draws the rounded panel with borders.
                graphics.setColor(Colors.primary);
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
        rootPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        headerPnl = new JPanel();
        headerPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(32, 0, 24, 0), -1, -1));
        rootPnl.add(headerPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        logoLbl = new JLabel();
        logoLbl.setIcon(new ImageIcon(getClass().getResource("/logo.png")));
        logoLbl.setText("");
        headerPnl.add(logoLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        titlePnl = new JPanel();
        titlePnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 20, 0), -1, -1));
        rootPnl.add(titlePnl, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        titleLbl = new JLabel();
        Font titleLblFont = this.$$$getFont$$$("Avenir", -1, 28, titleLbl.getFont());
        if (titleLblFont != null) titleLbl.setFont(titleLblFont);
        titleLbl.setText("Sign in to Downloadify");
        titlePnl.add(titleLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPnl = new JPanel();
        contentPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        rootPnl.add(contentPnl, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        contentPnl.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        cardPnl = new JPanel();
        cardPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(20, 20, 20, 20), -1, -1));
        contentPnl.add(cardPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, new Dimension(400, 250), new Dimension(400, 250), new Dimension(400, 250), 0, false));
        labelsPnl = new JPanel();
        labelsPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        cardPnl.add(labelsPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        inpFieldLbl = new JLabel();
        Font inpFieldLblFont = this.$$$getFont$$$("Helvetica", Font.BOLD, 15, inpFieldLbl.getFont());
        if (inpFieldLblFont != null) inpFieldLbl.setFont(inpFieldLblFont);
        inpFieldLbl.setText("Access Token");
        labelsPnl.add(inpFieldLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        questionLbl = new JLabel();
        Font questionLblFont = this.$$$getFont$$$("Helvetica", Font.BOLD, 13, questionLbl.getFont());
        if (questionLblFont != null) questionLbl.setFont(questionLblFont);
        questionLbl.setText("Forgot token?");
        labelsPnl.add(questionLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        labelsPnl.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        inpFieldPanle = new JPanel();
        inpFieldPanle.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        cardPnl.add(inpFieldPanle, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tokenInpField = new JPasswordField();
        inpFieldPanle.add(tokenInpField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        signInPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(8, 16, 8, 16), -1, -1));
        cardPnl.add(signInPnl, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        signInLbl = new JLabel();
        Font signInLblFont = this.$$$getFont$$$(null, Font.BOLD, 14, signInLbl.getFont());
        if (signInLblFont != null) signInLbl.setFont(signInLblFont);
        signInLbl.setText("Sign in");
        signInPnl.add(signInLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        cardPnl.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 30), new Dimension(-1, 30), new Dimension(-1, 30), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        cardPnl.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        contentPnl.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        contentPnl.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        footerPnl = new JPanel();
        footerPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(5, 5, 5, 5), -1, -1));
        footerPnl.setEnabled(false);
        rootPnl.add(footerPnl, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 40), new Dimension(-1, 40), new Dimension(-1, 40), 0, false));
        footerLbl = new JLabel();
        Font footerLblFont = this.$$$getFont$$$("Roboto Light", -1, 13, footerLbl.getFont());
        if (footerLblFont != null) footerLbl.setFont(footerLblFont);
        footerLbl.setHorizontalAlignment(0);
        footerLbl.setHorizontalTextPosition(0);
        footerLbl.setText("GitHub");
        footerPnl.add(footerLbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
