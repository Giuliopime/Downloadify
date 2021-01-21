package views.graphicUtils;

import views.Views;
import views.ViewsManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Styler {
    public static void styleView(JPanel rootPnl, JPanel navbarPnl, JPanel titlePnl, JPanel contentPnl, JPanel footerPnl, JLabel logoLbl, JLabel titleLbl, JLabel footerLbl) {
        // Colors
        rootPnl.setBackground(Colors.primaryBG);
        navbarPnl.setBackground(Colors.secondaryBG);
        contentPnl.setBackground(Colors.primaryBG);
        if(titlePnl != null) titlePnl.setBackground(Colors.primaryBG);
        footerPnl.setBackground(Colors.secondaryBG);

        logoLbl.setForeground(Colors.text);
        if(titleLbl != null) titleLbl.setForeground(Colors.text);
        footerLbl.setForeground(Colors.text);

        // Hover events and click events on labels
        JLabel[] labels = new JLabel[] { logoLbl, footerLbl };
        for(JLabel label: labels) {
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (Desktop.isDesktopSupported() && label == footerLbl) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://github.com/Giuliopime/Downloadify"));
                        } catch (IOException | URISyntaxException ignored) { }
                    }
                    else
                        ViewsManager.getInstance().changeView(Views.HOME);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    label.getRootPane().setCursor(new Cursor(Cursor.HAND_CURSOR));
                    label.setForeground(Colors.primary);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    label.setForeground(Colors.text);
                }
            });
        }
    }

    public static void styleCard(JLabel headerLbl, JPanel contentWrapperPnl, JPanel cardWrapperPnl, JPanel cardPnl, JPanel labelsPnl, JPanel inpFieldPnl, JPanel downloadBtnPnl, JLabel inpFieldLbl, JLabel questionLbl, JTextField URLInpField, JCheckBox audioOnlyCb, JLabel downloadLbl) {
        JPanel[] contentPanels = new JPanel[] {contentWrapperPnl, cardWrapperPnl, cardPnl, labelsPnl, inpFieldPnl, downloadBtnPnl };
        for(JPanel panel: contentPanels)
            panel.setBackground(Colors.primaryBG);

        JLabel[] labels = new JLabel[] { headerLbl, inpFieldLbl };
        for(JLabel label: labels)
            label.setForeground(Colors.text);

        headerLbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                headerLbl.setIcon(new ImageIcon(getClass().getResource("/back_arrow_hover.png")));
                headerLbl.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                headerLbl.setIcon(new ImageIcon(getClass().getResource("/back_arrow.png")));
                headerLbl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        if(audioOnlyCb != null)
            audioOnlyCb.setForeground(Colors.text);

        cardPnl.setBorder(new RoundedBorder(Colors.cardBorder, 12));

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

        URLInpField.setBackground(Colors.primaryBG);
        URLInpField.setBorder(new RoundedBorder(Colors.inputBorder, 12));
        URLInpField.setCaretColor(Colors.text);
        URLInpField.setForeground(Colors.text);

        URLInpField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                URLInpField.setBorder(new RoundedBorder(Colors.inputFocusedBorder, 12));
            }

            @Override
            public void focusLost(FocusEvent e) {
                URLInpField.setBorder(new RoundedBorder(Colors.inputBorder, 12));
            }
        });

        downloadLbl.setForeground(Colors.text);

        downloadBtnPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                downloadLbl.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                downloadLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                downloadBtnPnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                downloadLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                downloadBtnPnl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                downloadLbl.setForeground(Colors.text);
            }
        });
    }

    public static void styleErrorCard(JPanel contentPnl, JPanel infoPnl, JPanel menuPnl, JPanel okPnl, JLabel infoLbl, JLabel okLbl) {
        JPanel[] panels = new JPanel[] { contentPnl, infoPnl, menuPnl };
        for(JPanel panel: panels) {
            panel.setBackground(Colors.errorBG);
        }

        infoLbl.setForeground(Colors.text);

        okPnl.setBackground(Colors.errorBG);
        okPnl.setBorder(new RoundedBorder(Colors.text, 12));
        okPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                okPnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                okPnl.setBorder(new RoundedBorder(Colors.primaryBG, 12));
                okLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                okPnl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                okPnl.setBorder(new RoundedBorder(Colors.text, 12));
                okLbl.setForeground(Colors.text);
            }
        });

        okLbl.setForeground(Colors.text);
    }

    public static void styleInfoCard(JPanel contentPnl, JPanel infoPnl, JPanel menuPnl, JPanel okPnl, JLabel infoLbl, JLabel okLbl) {
        JPanel[] panels = new JPanel[] { contentPnl, infoPnl, menuPnl };
        for(JPanel panel: panels) {
            panel.setBackground(Colors.infoBG);
        }

        infoLbl.setForeground(Colors.text);

        okPnl.setBackground(Colors.infoBG);
        okPnl.setBorder(new RoundedBorder(Colors.text, 12));
        okPnl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                okPnl.setCursor(new Cursor(Cursor.HAND_CURSOR));
                okPnl.setBorder(new RoundedBorder(Colors.primaryBG, 12));
                okLbl.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                okPnl.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                okPnl.setBorder(new RoundedBorder(Colors.text, 12));
                okLbl.setForeground(Colors.text);
            }
        });

        okLbl.setForeground(Colors.text);
    }

    public static void removeMouseListenersFromDownloadBtn(JPanel panel, JLabel label) {
        label.setForeground(Colors.text);
        for(MouseListener mouseListener: panel.getMouseListeners())
            panel.removeMouseListener(mouseListener);

        for(MouseMotionListener mouseMotionListener: panel.getMouseMotionListeners())
            panel.removeMouseMotionListener(mouseMotionListener);
    }

    public static void addMouseListenersToDownloadBtn(JPanel panel, JLabel label) {
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                label.setForeground(Color.WHITE);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                label.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                label.setForeground(Colors.primaryBG);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                label.setForeground(Colors.text);
            }
        });
    }
}
