package game.ui.forms;

import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;
import game.util.handlers.ImageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

public class LoginFrame {

    private static final String WINDOW_TITLE = "Bumbo Hell | Login";

    private static final Color BUTTON_DEFAULT_COLOR = new Color(36, 12, 28);

    private static final Color BUTTON_SELECTED_COLOR = new Color(102, 33, 55);

    private final JFrame loginFrame;

    private final JPanel loginPanel;

    private final JPanel inputContainer;

    private final JPanel buttonContainer;

    private final JLabel unameLabel;
    private final JLabel passLabel;

    private final JTextField unameField;

    private final JPasswordField passField;

    private JButton backBtn, submitBtn;

    public LoginFrame(){
        loginFrame = new JFrame();
        loginPanel = new JPanel();
        inputContainer = new JPanel();
        buttonContainer = new JPanel();
        unameLabel = new JLabel("Username");
        passLabel = new JLabel("Password");
        unameField = new JTextField();
        passField = new JPasswordField();

        decorateInputContainer();
        decorateButtonContainer();
        decorateLoginPanel();
        decorateLoginForm();

        loginFrame.add(loginPanel);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private void decorateLoginForm() {
        loginFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.setTitle(WINDOW_TITLE);
        loginFrame.setIconImage(Objects.requireNonNull(ImageHandler.getImageIcon("src/res/icon.png")).getImage());
        loginFrame.setUndecorated(true);
        loginFrame.setFocusable(true);
        double frameCornerArcHeight = 40.0;
        double frameCornerArcWidth = 40.0;
        loginFrame.setShape(new RoundRectangle2D.Double(
                0,
                0,
                300,
                300,
                frameCornerArcWidth,
                frameCornerArcHeight
        ));
        loginFrame.setLayout(new BorderLayout());
        loginFrame.requestFocusInWindow();
    }

    private void decorateLoginPanel() {
        loginPanel.setOpaque(false);
        loginPanel.setBackground(Color.black);
        loginPanel.setPreferredSize(new Dimension(300, 300));
        loginPanel.setLayout(new BorderLayout());

        loginPanel.add(inputContainer, BorderLayout.CENTER);
        loginPanel.add(buttonContainer, BorderLayout.SOUTH);
    }

    private void decorateInputContainer() {
        inputContainer.setPreferredSize(new Dimension(300, 50));
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.Y_AXIS));

        decorateLabel(unameLabel);
        decorateLabel(passLabel);
        decorateField(unameField);
        decorateField(passField);

        unameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        unameField.setAlignmentY(Component.CENTER_ALIGNMENT);
        passLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        passField.setAlignmentY(Component.CENTER_ALIGNMENT);

        inputContainer.add(unameLabel);
        inputContainer.add(unameField);
        inputContainer.add(passLabel);
        inputContainer.add(passField);
    }

    private void decorateButtonContainer() {
        buttonContainer.setLayout(new FlowLayout());
        backBtn = new JButton("Back");
        submitBtn = new JButton("Submit");

        decorateButton(backBtn);
        decorateButton(submitBtn);

        buttonContainer.add(backBtn);
        buttonContainer.add(submitBtn);
    }

    private void decorateLabel(JLabel label) {
        Font font32;

        font32 = FontHandler.getFont("font-1.ttf", 32f);

        label.setFont(font32);
        addEmptyBorder(label, 10, 50, 10, 50);
    }

    private void decorateField(JTextField textField) {
        if(!(textField instanceof JPasswordField)) {
            Font font24;

            font24 = FontHandler.getFont("font-2.ttf", 24f);

            textField.setFont(font24);
        }


        textField.setForeground(Color.black);
        textField.setPreferredSize(new Dimension(300, 50));
        addEmptyBorder(textField, 10, 10, 20, 10);
    }

    private void decorateButton(JButton button) {
        Font font24;

        font24 = FontHandler.getFont("font-1.ttf", 24f);

        button.setFont(font24);
        button.setForeground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBackground(BUTTON_DEFAULT_COLOR);

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

    private void addEmptyBorder(JComponent jComponent, int top, int left, int bottom, int right) {
        jComponent.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(top, left, bottom, right),
                jComponent.getBorder()));
    }

    public void destroy() {
        loginFrame.dispose();
    }

    public JTextField getUnameField() {
        return unameField;
    }

    public JPasswordField getPassField() {
        return passField;
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public JButton getSubmitBtn() {
        return submitBtn;
    }
}
