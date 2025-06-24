import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TICTACTOE extends JFrame implements ActionListener {
    JButton[] buttons = new JButton[9];
    int count = 0;
    boolean player1Turn = true;
    JLabel statusLabel;

    public TICTACTOE() {
        setTitle("Tic Tac Toe");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));

        // Status panel
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(new Color(70, 130, 180));
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setPreferredSize(new Dimension(600, 50));
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.NORTH);

        // Game board panel
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(3, 3, 5, 5));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gamePanel.setBackground(new Color(70, 130, 180));

        initializeButtons(gamePanel);
        add(gamePanel, BorderLayout.CENTER);

        // Reset button
        JButton resetButton = new JButton("Reset Game");
        resetButton.setFont(new Font("Arial", Font.BOLD, 18));
        resetButton.setBackground(new Color(220, 220, 220));
        resetButton.addActionListener(e -> resetGame());
        add(resetButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void initializeButtons(JPanel panel) {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.BOLD, 80));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }
    }

    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        if (clickedButton.getText().equals("")) {
            if (player1Turn) {
                clickedButton.setText("X");
                clickedButton.setForeground(new Color(0, 100, 0)); // Dark green
            } else {
                clickedButton.setText("O");
                clickedButton.setForeground(new Color(139, 0, 0)); // Dark red
            }
            count++;

            if (checkwin()) {
                String winner = player1Turn ? "X" : "O";
                statusLabel.setText("Player " + winner + " wins!!");
                JOptionPane.showMessageDialog(this, "Player " + winner + " wins!!");
                disableAllButtons();
            } else if (count == 9) {
                statusLabel.setText("It's a draw!");
                JOptionPane.showMessageDialog(this, "It's a draw!!!");
            } else {
                player1Turn = !player1Turn;
                statusLabel.setText("Player " + (player1Turn ? "X" : "O") + "'s Turn");
            }
        }
    }

    private void disableAllButtons() {
        for (JButton button : buttons) {
            button.setEnabled(false);
            button.setBackground(new Color(220, 220, 220));
        }
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setEnabled(true);
            button.setBackground(Color.WHITE);
        }
        count = 0;
        player1Turn = true;
        statusLabel.setText("Player X's Turn");
    }

    public boolean checkwin() {
        return checkrows() || checkcolumns() || checkdiagonals();
    }

    private boolean checkrows() {
        return isLineEqual(0, 1, 2) ||
                isLineEqual(3, 4, 5) ||
                isLineEqual(6, 7, 8);
    }

    private boolean checkcolumns() {
        return isLineEqual(0, 3, 6) ||
                isLineEqual(1, 4, 7) ||
                isLineEqual(2, 5, 8);
    }

    private boolean checkdiagonals() {
        return isLineEqual(0, 4, 8) ||
                isLineEqual(2, 4, 6);
    }

    private boolean isLineEqual(int i1, int i2, int i3) {
        String a = buttons[i1].getText();
        String b = buttons[i2].getText();
        String c = buttons[i3].getText();

        return !a.equals("") && a.equals(b) && a.equals(c);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TICTACTOE();
        });
    }
}