package com.treacher.runespan.ui;

import com.treacher.butlerplankmaker.ui.Painter;
import com.treacher.runespan.Runespan;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Michael Treacher
 */

public class GUI extends JFrame {

    private final Container contentPane;
    private JPanel centerPanel;
    private JComboBox logComboBox;
    private Runespan runespan;

    public GUI(Runespan runespan) {
        setTitle("treach3rs runespan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.contentPane = getContentPane();
        this.runespan = runespan;
        buildGUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildGUI() {
        buildStartButton();
        buildCenterPanel();
        buildGameTypeLabel();
        buildGameTypeComboBox();
        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

    private void buildStartButton() {
        final JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String gameType = (String) logComboBox.getSelectedItem();
                Painter.startTime = System.currentTimeMillis();
                runespan.setGameType(gameType);
                runespan.addTasks();
                dispose();
            }
        });
        contentPane.add(startButton, BorderLayout.PAGE_END);
    }

    private void buildCenterPanel() {
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
    }

    private void buildGameTypeLabel() {
        final JLabel logLabel = new JLabel("Game Type: ");
        logLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logLabel);
    }

    private void buildGameTypeComboBox() {
        logComboBox = new JComboBox<String>(new String[]{"P2P", "F2P"});
        logComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(logComboBox);
        centerPanel.add(Box.createVerticalStrut(15));
    }
}
