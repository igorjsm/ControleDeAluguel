package com.github.igorjsm.controledealuguel.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import com.github.igorjsm.controledealuguel.dao.UserDAO;
import com.github.igorjsm.controledealuguel.model.User;

public class AdminUsuarios extends JFrame {

    private JTable tableUsuarios;
    private DefaultTableModel model;
    private UserDAO userDAO;

    public AdminUsuarios() {
        setTitle("Administrar Usuários");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        userDAO = new UserDAO();
        model = new DefaultTableModel(new String[] { "ID", "Username", "Admin" }, 0);
        tableUsuarios = new JTable(model);

        loadUsers(); // Carregar a lista de usuários na tabela

        JScrollPane scrollPane = new JScrollPane(tableUsuarios);

        JButton btnAdd = new JButton("Adicionar");
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUser();
            }
        });

        JButton btnEdit = new JButton("Editar");
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editUser();
            }
        });

        JButton btnRemove = new JButton("Remover");
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeUser();
            }
        });

        JPanel panelButtons = new JPanel();
        panelButtons.add(btnAdd);
        panelButtons.add(btnEdit);
        panelButtons.add(btnRemove);

        add(scrollPane, BorderLayout.CENTER);
        add(panelButtons, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        model.setRowCount(0); // Limpar a tabela
        List<User> users = userDAO.getAll();
        for (User user : users) {
            model.addRow(new Object[] { user.getId(), user.getUsername(), user.isAdmin() });
        }
    }

    private void addUser() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox adminCheckbox = new JCheckBox("Administrador");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(adminCheckbox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            boolean isAdmin = adminCheckbox.isSelected();

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setAdmin(isAdmin);
            userDAO.save(newUser);
            loadUsers(); // Atualizar a tabela
        }
    }

    private void editUser() {
        int selectedRow = tableUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = (int) model.getValueAt(selectedRow, 0);
        User user = userDAO.getById(userId);

        JTextField usernameField = new JTextField(user.getUsername());
        JPasswordField passwordField = new JPasswordField(user.getPassword());
        JCheckBox adminCheckbox = new JCheckBox("Administrador", user.isAdmin());

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(adminCheckbox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            user.setUsername(usernameField.getText());
            user.setPassword(new String(passwordField.getPassword()));
            user.setAdmin(adminCheckbox.isSelected());
            userDAO.update(user);
            loadUsers(); // Atualizar a tabela
        }
    }

    private void removeUser() {
        int selectedRow = tableUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = (int) model.getValueAt(selectedRow, 0);

        int result = JOptionPane.showConfirmDialog(this,
                "Tem certeza de que deseja remover este usuário?", "Confirmar Remoção",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            userDAO.delete(userId);
            loadUsers(); // Atualizar a tabela
        }
    }
}
