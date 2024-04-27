package game.ui.dialogs;

import game.Game;
import game.util.managers.FontManager;
import services.server.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RegisterDialog extends JDialog {
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel verifyPasswordLabel;
    private JLabel  birthdayLabel;
    private JLabel firstNameLabel;
    private JLabel lastNameLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField verifyPasswordField;
    private JTextField birthdayField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JButton registerButton;
    private JButton cancelButton;
    private boolean isPasswordVerified = false;

    private static final Color BUTTON_DEFAULT_COLOR = new Color(36, 12, 28);

    private static final Color BUTTON_SELECTED_COLOR = new Color(102, 33, 55);

    public RegisterDialog(Frame owner) {
        super(owner, "Register", true);

        this.setUndecorated(true);
        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setupComponents();
        styleComponents();
        setupButtonListeners();
        addComponentsToPanel(panel);
        panel.setBackground(new Color(22, 0, 10));
        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(300, 350);

        // Center the dialog on the screen
        setLocationRelativeTo(null);
    }

    private void setupComponents() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        verifyPasswordLabel = new JLabel("Verify Password:");
        verifyPasswordField = new JPasswordField(20);
        birthdayLabel = new JLabel("Birthday: ");
        birthdayField = new JFormattedTextField(format);
        firstNameLabel = new JLabel("First Name: ");
        firstNameField = new JTextField(20);
        lastNameLabel = new JLabel("Last Name: ");
        lastNameField = new JTextField(20);
        registerButton = new JButton("REGISTER");
        cancelButton = new JButton("CANCEL");
    }

    private void addComponentsToPanel(JPanel panel) {
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(verifyPasswordLabel);
        panel.add(verifyPasswordField);
        panel.add(birthdayLabel);
        panel.add(birthdayField);
        panel.add(cancelButton);
        panel.add(registerButton);
    }

    private void styleComponents() {
        setupFont();
        setupLabelTextColor();
        verifyPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                updateVerifyPasswordFieldBackground();
            }
        });
        setupButton(registerButton);
        setupButton(cancelButton);
    }

    private void setupFont() {
        Font font = FontManager.getInstance().getFont("Pixellari", 16f);

        usernameLabel.setFont(font);
        passwordLabel.setFont(font);
        usernameField.setFont(font);
        verifyPasswordLabel.setFont(font);
        birthdayLabel.setFont(font);
        birthdayField.setFont(font);
        firstNameLabel.setFont(font);
        firstNameField.setFont(font);
        lastNameLabel.setFont(font);
        lastNameField.setFont(font);
        registerButton.setFont(font);
        cancelButton.setFont(font);
    }

    private void setupLabelTextColor() {
        Color textColor = Color.white;
        usernameLabel.setForeground(textColor);
        passwordLabel.setForeground(textColor);
        verifyPasswordLabel.setForeground(textColor);
        birthdayLabel.setForeground(textColor);
        firstNameLabel.setForeground(textColor);
        lastNameLabel.setForeground(textColor);
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
        registerButton.addActionListener(v -> {
            handleRegister();
        });

        cancelButton.addActionListener(v -> {
            destroy();
        });
    }

    private void updateVerifyPasswordFieldBackground() {
        boolean isPasswordEmpty = isPasswordFieldEmpty(passwordField);
        boolean isVerifyEmpty = isPasswordFieldEmpty(verifyPasswordField);
        if (!isPasswordEmpty && !isVerifyEmpty) {
            if (checkIfPasswordsMatching(passwordField, verifyPasswordField)) {
                verifyPasswordField.setBackground(new Color(137, 202, 120));
                isPasswordVerified = true;
            } else {
                verifyPasswordField.setBackground(new Color(117, 34, 37));
                isPasswordVerified = false;
            }
        } else {
            verifyPasswordField.setBackground(Color.white);
            isPasswordVerified = false;
        }
    }

    private boolean checkIfPasswordsMatching(JPasswordField password, JPasswordField verifyPassword) {
        char[] passwordChars = password.getPassword();
        char[] verifyPasswordChars = verifyPassword.getPassword();

        if (passwordChars.length != verifyPasswordChars.length) {
            return false;
        }

        for (int i = 0; i < passwordChars.length; i++) {
            if (passwordChars[i] != verifyPasswordChars[i]) {
                return false;
            }
        }

        return true;
    }

    private boolean isPasswordFieldEmpty(JPasswordField passwordField) {
        return passwordField.getPassword().length == 0;
    }

    private void handleRegister() {
        if(!checkValidInputs()) {
            return;
        }

        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String birthday = birthdayField.getText();
        if(registerUser(username, password, firstName, lastName, birthday)) {
            showOKOptionPane("Welcome to hell, " + firstName + "!", "Registration successful");
            destroy();
        } else {
            showErrorOptionPane("Failed to register: username already exists!", "Registration failed");
        }
    }

    private boolean checkValidInputs() {
        if (checkTextFieldEmpty(firstNameField, "First Name")) {
            return false;
        }
        if (checkTextFieldEmpty(lastNameField, "Last Name")) {
            return false;
        }
        if (checkTextFieldEmpty(usernameField, "Username")) {
            return false;
        }
        if (checkPasswordFieldEmpty(passwordField, "Password")) {
            return false;
        }
        if (checkPasswordFieldEmpty(verifyPasswordField, "Verify Password")) {
            return false;
        }
        if (checkTextFieldEmpty(birthdayField, "Birthday")) {
            return false;
        }
        if (!isPasswordVerified) {
            showErrorOptionPane("Password is not verified!","Invalid Password");
            return false;
        }
        return true;
    }

    private boolean checkPasswordFieldEmpty(JPasswordField passwordField, String fieldName) {
        if (passwordField.getPassword().length == 0) {
            showErrorOptionPane(fieldName + " is required!", "Missing " + fieldName);
            return true;
        }
        return false;
    }

    private boolean checkTextFieldEmpty(JTextField textField, String fieldName) {
        if (textField.getText().isEmpty()) {
            showErrorOptionPane(fieldName + " is required!", "Missing " + fieldName);
            return true;
        }
        return false;
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

    private boolean registerUser(String username, char[] password, String firstName, String lastName, String birthday) {
        DBConnection db = new DBConnection();
        try {
            return db.getUserDML().addUser(username, password, firstName, lastName, birthday);
        } finally {
            db.close();
        }
    }
}
