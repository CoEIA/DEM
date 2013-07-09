/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.coeia.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author wajdyessam
 */
public class Splasher {

    public static void imageSplashScreen() {
        // Sleep a while, then the splash screen will hide automaticlly
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
            }
        }
        
        ApplicationLauncher.lunch();
    }

    public static void splashScreenWithProgressBar() {
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("No Splash Screen");
            return;
        }

        final Image img = Toolkit.getDefaultToolkit().getImage(splash.getImageURL());
        final JFrame splashFrame = new JFrame();
        splashFrame.setUndecorated(true);

        final JPanel splashPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(img, 0, 0, null);
            }
        };

        final JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);

        splashPanel.setLayout(new BorderLayout());
        splashPanel.add(progressBar, BorderLayout.SOUTH);

        splashFrame.add(splashPanel);
        splashFrame.setBounds(splash.getBounds());
        splashFrame.setVisible(true);

        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    for (int i = 0; i <100; i++) {
                        publish(i);
                        Thread.sleep(50);
                    }
                } catch (InterruptedException e) {
                }

                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                for (Integer chunk : chunks) {
                    progressBar.setString("Loading module " + chunk);
                    progressBar.setValue(chunk);
                    splashPanel.repaint();
                }
            }

            @Override
            protected void done() {
                ApplicationLauncher.lunch();
                splashFrame.setVisible(false);
            }
        }.execute();
    }

    public static void drawOnSplashScreen() {
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("No Splash Screen");
            return;
        }

        try {
            for (int i = 0; i <= 100; i++) {
                drawOnSplash(i);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
        }
        
        ApplicationLauncher.lunch();
    }

    private static void drawOnSplash(int percent) {
        SplashScreen splash = SplashScreen.getSplashScreen();
        if (splash == null) {
            System.out.println("No Splash Screen");
            return;
        }

        Rectangle bounds = splash.getBounds();
        Graphics2D g = splash.createGraphics();

        int height = 20;
        int x = 2;
        int y = bounds.height - height - 2;
        int width = bounds.width - 4;

        Color brightPurple = new Color(76, 36, 121);
        g.setColor(brightPurple);
        g.fillRect(x, y, width * percent / 100, height);
        splash.update();
    }

    private Splasher() {
        throw new AssertionError();
    }
}
