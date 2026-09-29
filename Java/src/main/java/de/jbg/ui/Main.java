package de.jbg.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) throws Exception, InterruptedException {


        JFrame mainFrame = new JFrame("Spast");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400,400);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        JButton mainButton1 = new JButton("Bilder anzeigen");
        JButton mainButton2 = new JButton("Bilder einfügen");

        mainButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UI.showUI();
            }
        });

        mainButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UI.inputFile();
            }
        });
        mainPanel.add(mainButton1);
        mainPanel.add(mainButton2);
        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        mainPanel.setVisible(true);
    }

}
