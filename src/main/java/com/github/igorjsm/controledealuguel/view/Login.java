package com.github.igorjsm.controledealuguel.view;

import javax.swing.*;
import com.github.igorjsm.controledealuguel.model.User;
import com.github.igorjsm.controledealuguel.dao.UserDAO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends javax.swing.JFrame {
    private UserDAO userDAO;

    public Login() {
        userDAO = new UserDAO();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        labelLOGIN = new javax.swing.JLabel();
        textFieldUsername = new javax.swing.JTextField();
        passwordFieldPassword = new javax.swing.JPasswordField();
        buttonEntrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setPreferredSize(new java.awt.Dimension(300, 200));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelLOGIN.setFont(new java.awt.Font("Liberation Sans", 1, 24));
        labelLOGIN.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelLOGIN.setText("LOGIN");
        getContentPane().add(labelLOGIN, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 60));

        textFieldUsername.setForeground(new java.awt.Color(204, 204, 204));
        textFieldUsername.setText("Nome de usuário");
        textFieldUsername.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textFieldUsernameFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                textFieldUsernameFocusLost(evt);
            }
        });
        textFieldUsername.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordFieldPassword.requestFocusInWindow();
                }
            }
        });
        getContentPane().add(textFieldUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 280, 40));

        passwordFieldPassword.setForeground(new java.awt.Color(204, 204, 204));
        passwordFieldPassword.setText("****************");
        passwordFieldPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordFieldPasswordFocusGained(evt);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordFieldPasswordFocusLost(evt);
            }
        });
        passwordFieldPassword.addActionListener(evt -> handleLogin());
        getContentPane().add(passwordFieldPassword,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 280, 40));

        buttonEntrar.setFont(new java.awt.Font("Liberation Sans", 1, 14));
        buttonEntrar.setText("Entrar");
        buttonEntrar.setPreferredSize(new java.awt.Dimension(100, 25));
        buttonEntrar.addActionListener(evt -> handleLogin());
        getContentPane().add(buttonEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 100, 30));

        pack();
    }

    private void textFieldUsernameFocusGained(java.awt.event.FocusEvent evt) {
        if (textFieldUsername.getText().equals("Nome de usuário")) {
            textFieldUsername.setText("");
            textFieldUsername.setForeground(new java.awt.Color(0, 0, 0));
        }
    }

    private void textFieldUsernameFocusLost(java.awt.event.FocusEvent evt) {
        if (textFieldUsername.getText().isEmpty()) {
            textFieldUsername.setForeground(new java.awt.Color(204, 204, 204));
            textFieldUsername.setText("Nome de usuário");
        }
    }

    private void passwordFieldPasswordFocusGained(java.awt.event.FocusEvent evt) {
        if (new String(passwordFieldPassword.getPassword()).equals("****************")) {
            passwordFieldPassword.setText("");
            passwordFieldPassword.setForeground(new java.awt.Color(0, 0, 0));
            passwordFieldPassword.setEchoChar('\u2022');
        }
    }

    private void passwordFieldPasswordFocusLost(java.awt.event.FocusEvent evt) {
        if (new String(passwordFieldPassword.getPassword()).isEmpty()) {
            passwordFieldPassword.setForeground(new java.awt.Color(204, 204, 204));
            passwordFieldPassword.setText("****************");
            passwordFieldPassword.setEchoChar((char) 0);
        }
    }

    private void handleLogin() {
        String username = textFieldUsername.getText();
        String password = new String(passwordFieldPassword.getPassword());

        User loggedUser = validateCredentials(username, password);
        if (loggedUser != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido!");
            dispose();

            // Passando o usuário logado para a classe ControleDeAluguel
            ControleDeAluguel controleDeAluguel = new ControleDeAluguel(loggedUser);
            controleDeAluguel.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Nome de usuário ou senha inválidos", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private User validateCredentials(String username, String password) {
        for (User user : userDAO.getAll()) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Login().setVisible(true));
    }

    private javax.swing.JButton buttonEntrar;
    private javax.swing.JLabel labelLOGIN;
    private javax.swing.JPasswordField passwordFieldPassword;
    private javax.swing.JTextField textFieldUsername;
}
