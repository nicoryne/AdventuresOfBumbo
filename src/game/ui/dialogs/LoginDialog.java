package game.ui.dialogs;

import game.ui.GamePanel;
import game.ui.titlescreen.TitleScreen;
import game.util.handlers.FontHandler;
import services.LoggerHelper;
import services.server.DBConnection;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {

    private JLabel usernameLabel;

    private JLabel passwordLabel;

    private JTextField usernameField;

    private JPasswordField passwordField;

    private JButton loginButton;

    private JButton cancelButton;

    public LoginDialog(Frame owner) {
        super(owner, "Login", true);

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        // Style components
        Font font = FontHandler.getFont("font-2.ttf", 14f);
        usernameLabel.setFont(font);
        passwordLabel.setFont(font);
        usernameField.setFont(font);
        passwordField.setFont(font);
        loginButton.setFont(font);
        cancelButton.setFont(font);

        // Action listeners for buttons
        loginButton.addActionListener(v -> {
            handleLogin();
        });

        cancelButton.addActionListener(v -> {
            destroy();
        });

        // Add components to the panel
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(cancelButton);

        // Set background color
        panel.setBackground(new Color(49, 54, 63));

        // Add panel to the dialog
        getContentPane().add(panel);

        // Set dialog size
        setSize(300, 150);

        // Center the dialog on the screen
        setLocationRelativeTo(null);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        if(loginUser(username, password)) {
            destroy();
        } else {
            LoggerHelper.logWarning("Failed to login user.");
        }
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
