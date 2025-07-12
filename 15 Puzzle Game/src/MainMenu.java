import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Game Main Menu");
        setSize(300, 400);
        setLocationRelativeTo(null); // Center the frame on the screen

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("15 Slide puzzle Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.blue);
        JLabel DifficultyLabel = new JLabel("Select your difficulty", SwingConstants.CENTER);
        DifficultyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton EasyButton = new JButton("Easy");
        JButton MediumButton = new JButton("Medium");
        JButton HardButton = new JButton("Hard");

        Dimension buttonSize = new Dimension(150, 30);
        
        ImageIcon icon = new ImageIcon("Images/puzzle.png"); 
        JLabel label = new JLabel(icon);
        getContentPane().add(label, BorderLayout.CENTER);
        
        EasyButton.setPreferredSize(buttonSize);
        MediumButton.setPreferredSize(buttonSize);
        HardButton.setPreferredSize(buttonSize);

        EasyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        MediumButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        HardButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        DifficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        EasyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add code to start the game here
                BoardGUI g = new BoardGUI(50);
                dispose();
                
            }
        });

        MediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BoardGUI g = new BoardGUI(75);
                dispose();
            }
        });

        HardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BoardGUI g = new BoardGUI(150);
                dispose();
            }
        });
        
        
        buttonPanel.add(Box.createVerticalGlue()); 
        buttonPanel.add(label);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(DifficultyLabel);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(EasyButton);
        buttonPanel.add(Box.createVerticalStrut(10));// Add some spacing
        buttonPanel.add(MediumButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Add some spacing
        buttonPanel.add(HardButton);
        buttonPanel.add(Box.createVerticalGlue()); // Add glue to center vertically
        panel.add(buttonPanel, BorderLayout.CENTER);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu();
            }
        });
    }
}
