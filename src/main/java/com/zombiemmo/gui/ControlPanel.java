package com.zombiemmo.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Panel with control buttons for player actions
 */
public class ControlPanel extends JPanel {
    private JButton moveNorthButton;
    private JButton moveSouthButton;
    private JButton moveWestButton;
    private JButton moveEastButton;
    private JButton attackButton;
    private JButton gatherButton;
    private JButton restButton;
    private JButton lookAroundButton;

    public ControlPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Controls",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14)
        ));

        // Movement panel
        JPanel movementPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        movementPanel.setBorder(BorderFactory.createTitledBorder("Movement"));

        moveNorthButton = createButton("↑ North (W)");
        moveSouthButton = createButton("↓ South (S)");
        moveWestButton = createButton("← West (A)");
        moveEastButton = createButton("→ East (D)");

        movementPanel.add(new JLabel());
        movementPanel.add(moveNorthButton);
        movementPanel.add(new JLabel());
        movementPanel.add(moveWestButton);
        movementPanel.add(new JLabel());
        movementPanel.add(moveEastButton);
        movementPanel.add(new JLabel());
        movementPanel.add(moveSouthButton);
        movementPanel.add(new JLabel());

        // Actions panel
        JPanel actionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        attackButton = createButton("⚔ Attack Zombie");
        gatherButton = createButton("⛏ Gather Resource");
        restButton = createButton("❤ Rest");
        lookAroundButton = createButton("👁 Look Around");

        actionsPanel.add(attackButton);
        actionsPanel.add(gatherButton);
        actionsPanel.add(restButton);
        actionsPanel.add(lookAroundButton);

        // Add panels
        add(movementPanel, BorderLayout.CENTER);
        add(actionsPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 12));
        button.setFocusPainted(false);
        return button;
    }

    // Getters for buttons
    public JButton getMoveNorthButton() { return moveNorthButton; }
    public JButton getMoveSouthButton() { return moveSouthButton; }
    public JButton getMoveWestButton() { return moveWestButton; }
    public JButton getMoveEastButton() { return moveEastButton; }
    public JButton getAttackButton() { return attackButton; }
    public JButton getGatherButton() { return gatherButton; }
    public JButton getRestButton() { return restButton; }
    public JButton getLookAroundButton() { return lookAroundButton; }

    // Add keyboard shortcuts
    public void setupKeyboardShortcuts(JFrame frame, Runnable onNorth, Runnable onSouth,
                                       Runnable onWest, Runnable onEast) {
        JRootPane rootPane = frame.getRootPane();
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        // W - North
        inputMap.put(KeyStroke.getKeyStroke('w'), "moveNorth");
        actionMap.put("moveNorth", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onNorth.run();
            }
        });

        // S - South
        inputMap.put(KeyStroke.getKeyStroke('s'), "moveSouth");
        actionMap.put("moveSouth", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onSouth.run();
            }
        });

        // A - West
        inputMap.put(KeyStroke.getKeyStroke('a'), "moveWest");
        actionMap.put("moveWest", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onWest.run();
            }
        });

        // D - East
        inputMap.put(KeyStroke.getKeyStroke('d'), "moveEast");
        actionMap.put("moveEast", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onEast.run();
            }
        });
    }
}
