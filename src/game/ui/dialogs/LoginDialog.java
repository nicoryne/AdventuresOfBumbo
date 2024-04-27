package game.ui.dialogs;

import game.Game;
import game.ui.titlescreen.TitleScreen;
import game.util.managers.FontManager;
import services.LoggerHelper;
import services.server.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginDialog extends JDialog {

    private JLabel usernameLabel;

    private JLabel passwordLabel;

    private JTextField usernameField;

    private JPasswordField passwordField;

    private JButton loginButton;

    private JButton cancelButton;

    private static final Color BUTTON_DEFAULT_COLOR = new Color(36, 12, 28);

    private static final Color BUTTON_SELECTED_COLOR = new Color(102, 33, 55);

    public LoginDialog(Frame owner) {
        super(owner, "Login", true);

        this.setUndecorated(true);
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setupComponents();
        styleComponents();
        setupButtonListeners();
        addComponentsToPanel(panel);
        panel.setBackground(new Color(22, 0, 10));
        getContentPane().add(panel);
        setSize(300, 150);

        setLocationRelativeTo(null);
    }

    private void setupComponents() {
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("LOGIN");
        cancelButton = new JButton("CANCEL");
    }

    private void addComponentsToPanel(JPanel panel) {
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(cancelButton);
        panel.add(loginButton);
    }

    private void styleComponents() {
        setupFont();
        setupLabelTextColor();
        setupButton(loginButton);
        setupButton(cancelButton);
        setupFields();
    }

    private void setupFields() {
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private void setupFont() {
        Font font = FontManager.getInstance().getFont("Pixellari", 16f);

        usernameLabel.setFont(font);
        passwordLabel.setFont(font);
        usernameField.setFont(font);
        loginButton.setFont(font);
        cancelButton.setFont(font);
    }

    private void setupLabelTextColor() {
        Color textColor = Color.white;
        usernameLabel.setForeground(textColor);
        passwordLabel.setForeground(textColor);
    }

    private void setupButton(JButton button) {
        button.setForeground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(BUTTON_DEFAULT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                button.setBackground(BUTTON_SELECTED_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                button.setBackground(BUTTON_DEFAULT_COLOR);
            }
        });
    }

    private void setupButtonListeners() {
        loginButton.addActionListener(v -> {
            handleLogin();
        });

        cancelButton.addActionListener(v -> {
            destroy();
        });
    }

    private void handleLogin() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        if(loginUser(username, password)) {
            TitleScreen.setTitleState(TitleScreen.TitleScreenState.MENU);
            showOKOptionPane("Welcome to hell, " + username + "!", "Login successful");
            destroy();
        } else {
            LoggerHelper.logWarning("Failed to login user.");
            showErrorOptionPane("Username or password is invalid", "Login failed");
        }
    }

    private void showErrorOptionPane(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void showOKOptionPane(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void destroy() {
        this.dispose();
    }

    private boolean loginUser(String username, char[] password) {
        DBConnection db = new DBConnection();
        try {
            return db.getUserDML().loginUser(username, password);
        } finally {
            db.close();
        }
    }
}
