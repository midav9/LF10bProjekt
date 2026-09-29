package de.jbg.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TextFieldBackground {
    public static void addBackground(JTextField name, String text) {
        name.setText(text);
        name.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (name.getText().equals(text)) {
                    name.setText("");
                    name.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (name.getText().isEmpty()) {
                    name.setForeground(Color.GRAY);
                    name.setText(text);
                }
            }
        });
    }

}
