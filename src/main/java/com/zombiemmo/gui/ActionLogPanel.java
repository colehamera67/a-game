package com.zombiemmo.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel displaying action log and game messages
 */
public class ActionLogPanel extends JPanel {
    private JTextArea logArea;
    private static final int MAX_LINES = 1000;

    public ActionLogPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Action Log",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));
        setPreferredSize(new Dimension(0, 150));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.GREEN);

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);

        logMessage("=== Welcome to Zombie MMO ===");
        logMessage("The world has fallen to a zombie plague.");
        logMessage("Train your skills, fight zombies, and survive!");
        logMessage("");
    }

    public void logMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");

            // Limit number of lines
            String text = logArea.getText();
            String[] lines = text.split("\n");
            if (lines.length > MAX_LINES) {
                int start = text.indexOf('\n', text.length() - MAX_LINES * 50);
                logArea.setText(text.substring(start + 1));
            }

            // Auto-scroll to bottom
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void logSuccess(String message) {
        logMessage("[SUCCESS] " + message);
    }

    public void logError(String message) {
        logMessage("[ERROR] " + message);
    }

    public void logInfo(String message) {
        logMessage("[INFO] " + message);
    }

    public void logCombat(String message) {
        logMessage("[COMBAT] " + message);
    }

    public void logSkill(String message) {
        logMessage("[SKILL] " + message);
    }

    public void clear() {
        logArea.setText("");
    }
}
