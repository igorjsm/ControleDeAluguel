package com.github.igorjsm.controledealuguel.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.github.igorjsm.controledealuguel.dao.ItemDAO;
import com.github.igorjsm.controledealuguel.dao.ReservationDAO;
import com.github.igorjsm.controledealuguel.model.Item;
import com.github.igorjsm.controledealuguel.model.Reservation;
import com.github.igorjsm.controledealuguel.model.User;

public class ControleDeAluguel extends JFrame {
    private JTable tableItems;
    private JTable tableReserves;
    private DefaultTableModel tableModelItems;
    private DefaultTableModel tableModelReserves;
    private ItemDAO itemDAO;
    private ReservationDAO reservationDAO;
    private static User currentUser;

    public ControleDeAluguel(User loggedUser) {
        currentUser = loggedUser;
        itemDAO = new ItemDAO();
        reservationDAO = new ReservationDAO();
        initComponents();
        tableModelItems = (DefaultTableModel) tableItems.getModel();
        tableModelReserves = (DefaultTableModel) tableReserves.getModel();
        loadItemsTableData();
        loadReservationsTableData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JScrollPane scrollPaneItems = new JScrollPane();
        tableItems = new JTable();
        JButton buttonAdicionarItem = new JButton();
        JButton buttonRemoverItem = new JButton();
        JButton buttonEditarItem = new JButton();
        JScrollPane scrollPanelReserves = new JScrollPane();
        tableReserves = new JTable();
        JButton buttonFazerReserva = new JButton();
        JButton buttonRemoverReserva = new JButton();
        JButton buttonEditarReserva = new JButton();
        JButton buttonAdministrarUsuarios = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Controle de aluguel");
        setPreferredSize(new Dimension(1366, 768));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tableItems.setModel(new DefaultTableModel(
                new Object[] { "ID", "Description", "Total Quantity", "Available Quantity" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
        scrollPaneItems.setViewportView(tableItems);
        getContentPane().add(scrollPaneItems, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1130, 369));

        buttonAdicionarItem.setFont(new Font("Noto Sans", Font.BOLD, 18));
        buttonAdicionarItem.setText("Adicionar item");
        buttonAdicionarItem.addActionListener(evt -> addItem());
        getContentPane().add(buttonAdicionarItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 10, 200, 50));

        buttonRemoverItem.setFont(new Font("Noto Sans", Font.BOLD, 18));
        buttonRemoverItem.setText("Remover item");
        buttonRemoverItem.addActionListener(evt -> deleteItem());
        getContentPane().add(buttonRemoverItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 70, 200, 50));

        buttonEditarItem.setFont(new Font("Noto Sans", Font.BOLD, 18));
        buttonEditarItem.setText("Editar item");
        buttonEditarItem.addActionListener(evt -> updateItem());
        getContentPane().add(buttonEditarItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 130, 200, 50));

        tableReserves.setModel(new DefaultTableModel(
                new Object[] { "ID", "Renter Name", "Delivery Date", "Pickup Date", "Address", "Paid", "Picked Up",
                        "Items" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
        scrollPanelReserves.setViewportView(tableReserves);
        getContentPane().add(scrollPanelReserves,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 1130, 369));

        buttonFazerReserva.setFont(new Font("Liberation Sans", Font.BOLD, 18));
        buttonFazerReserva.setText("Fazer reserva");
        buttonFazerReserva.addActionListener(evt -> addReservation());
        getContentPane().add(buttonFazerReserva, new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 390, 200, 50));

        buttonRemoverReserva.setFont(new Font("Liberation Sans", Font.BOLD, 18));
        buttonRemoverReserva.setText("Remover reserva");
        buttonRemoverReserva.addActionListener(evt -> deleteReservation());
        getContentPane().add(buttonRemoverReserva,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 450, 200, 50));

        buttonEditarReserva.setFont(new Font("Liberation Sans", Font.BOLD, 18));
        buttonEditarReserva.setText("Editar reserva");
        buttonEditarReserva.addActionListener(evt -> updateReservation());
        getContentPane().add(buttonEditarReserva,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 510, 200, 50));

        buttonAdministrarUsuarios.setFont(new Font("Liberation Sans", Font.BOLD, 18));
        buttonAdministrarUsuarios.setText("Administrar usuários");
        buttonAdministrarUsuarios.addActionListener(evt -> adminUsers());
        getContentPane().add(buttonAdministrarUsuarios,
                new org.netbeans.lib.awtextra.AbsoluteConstraints(1150, 708, 200, 50));

        if (currentUser != null && currentUser.isAdmin()) {
            buttonAdministrarUsuarios.setVisible(true);
        } else {
            buttonAdministrarUsuarios.setVisible(false);
        }

        pack();
    }

    private void addItem() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField descriptionField = new JTextField(20);
        JTextField totalQuantityField = new JTextField(5);

        panel.add(new JLabel("Descrição do item:"));
        panel.add(descriptionField);
        panel.add(new JLabel("Quantidade total:"));
        panel.add(totalQuantityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Item", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String description = descriptionField.getText();
            int totalQuantity = Integer.parseInt(totalQuantityField.getText());

            Item newItem = new Item();
            newItem.setDescription(description);
            newItem.setTotalQuantity(totalQuantity);
            newItem.setAvailableQuantity(totalQuantity);

            itemDAO.save(newItem);
            loadItemsTableData();
        }
    }

    private void deleteItem() {
        int selectedRow = tableItems.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModelItems.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar o item?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                itemDAO.delete(id);
                loadItemsTableData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para deletar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateItem() {
        int selectedRow = tableItems.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModelItems.getValueAt(selectedRow, 0);
            String description = (String) tableModelItems.getValueAt(selectedRow, 1);
            int totalQuantity = (int) tableModelItems.getValueAt(selectedRow, 2);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JTextField descriptionField = new JTextField(description, 20);
            JTextField totalQuantityField = new JTextField(String.valueOf(totalQuantity), 5);

            panel.add(new JLabel("Descrição do item:"));
            panel.add(descriptionField);
            panel.add(new JLabel("Quantidade total:"));
            panel.add(totalQuantityField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Item", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newDescription = descriptionField.getText();
                int newTotalQuantity = Integer.parseInt(totalQuantityField.getText());

                Item updatedItem = new Item();
                updatedItem.setId(id);
                updatedItem.setDescription(newDescription);
                updatedItem.setTotalQuantity(newTotalQuantity);
                updatedItem.setAvailableQuantity(newTotalQuantity);

                itemDAO.update(updatedItem);
                loadItemsTableData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item para alterar.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addReservation() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField renterNameField = new JTextField(20);
        JTextField deliveryDateField = new JTextField(10);
        JTextField pickupDateField = new JTextField(10);
        JTextField addressField = new JTextField(20);
        JCheckBox paidCheckBox = new JCheckBox("Pago?");
        JTextArea itemsArea = new JTextArea(5, 20);

        panel.add(new JLabel("Nome do Locatário:"));
        panel.add(renterNameField);
        panel.add(new JLabel("Data de Entrega (yyyy-MM-dd):"));
        panel.add(deliveryDateField);
        panel.add(new JLabel("Data de Retirada (yyyy-MM-dd):"));
        panel.add(pickupDateField);
        panel.add(new JLabel("Endereço:"));
        panel.add(addressField);
        panel.add(paidCheckBox);
        panel.add(new JLabel("Itens e Quantidades (ex: id1=2,id2=3):"));
        panel.add(new JScrollPane(itemsArea));

        int result = JOptionPane.showConfirmDialog(this, panel, "Fazer Reserva", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String renterName = renterNameField.getText();
            String deliveryDate = deliveryDateField.getText();
            String pickupDate = pickupDateField.getText();
            String address = addressField.getText();
            boolean isPaid = paidCheckBox.isSelected();
            String itemsText = itemsArea.getText();

            // Converte a string de itens em um array de strings
            String[] itemQuantities = itemsText.split(",");

            // Validação de disponibilidade dos itens
            boolean areItemsAvailable = reservationDAO.areItemsAvailable(itemsText);
            if (areItemsAvailable) {
                Reservation newReservation = new Reservation();
                newReservation.setRenterName(renterName);
                newReservation.setDeliveryDate(LocalDate.parse(deliveryDate, DateTimeFormatter.ISO_DATE));
                newReservation.setPickupDate(LocalDate.parse(pickupDate, DateTimeFormatter.ISO_DATE));
                newReservation.setAddress(address);
                newReservation.setPaid(isPaid);
                newReservation.setPickedUp(false);
                newReservation.setItems(itemsText);

                // Salva a reserva junto com as quantidades de itens
                reservationDAO.save(newReservation, itemQuantities);
                loadReservationsTableData();
            } else {
                JOptionPane.showMessageDialog(this,
                        "A reserva não pôde ser concluída porque alguns itens não estão disponíveis.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteReservation() {
        int selectedRow = tableReserves.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModelReserves.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja deletar a reserva?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                reservationDAO.delete(id);
                loadReservationsTableData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para deletar.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReservation() {
        int selectedRow = tableReserves.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModelReserves.getValueAt(selectedRow, 0);
            String renterName = (String) tableModelReserves.getValueAt(selectedRow, 1);
            String deliveryDate = (String) tableModelReserves.getValueAt(selectedRow, 2);
            String pickupDate = (String) tableModelReserves.getValueAt(selectedRow, 3);
            String address = (String) tableModelReserves.getValueAt(selectedRow, 4);
            boolean isPaid = (boolean) tableModelReserves.getValueAt(selectedRow, 5);
            boolean isPickedUp = (boolean) tableModelReserves.getValueAt(selectedRow, 6);
            String items = (String) tableModelReserves.getValueAt(selectedRow, 7);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            JTextField renterNameField = new JTextField(renterName, 20);
            JTextField deliveryDateField = new JTextField(deliveryDate, 10);
            JTextField pickupDateField = new JTextField(pickupDate, 10);
            JTextField addressField = new JTextField(address, 20);
            JCheckBox paidCheckBox = new JCheckBox("Pago?", isPaid);
            JCheckBox pickedUpCheckBox = new JCheckBox("Retirado?", isPickedUp);
            JTextArea itemsArea = new JTextArea(items, 5, 20);

            panel.add(new JLabel("Nome do Locatário:"));
            panel.add(renterNameField);
            panel.add(new JLabel("Data de Entrega (yyyy-MM-dd):"));
            panel.add(deliveryDateField);
            panel.add(new JLabel("Data de Retirada (yyyy-MM-dd):"));
            panel.add(pickupDateField);
            panel.add(new JLabel("Endereço:"));
            panel.add(addressField);
            panel.add(paidCheckBox);
            panel.add(pickedUpCheckBox);
            panel.add(new JLabel("Itens e Quantidades (ex: id1=2,id2=3):"));
            panel.add(new JScrollPane(itemsArea));

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Reserva", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String newRenterName = renterNameField.getText();
                String newDeliveryDate = deliveryDateField.getText();
                String newPickupDate = pickupDateField.getText();
                String newAddress = addressField.getText();
                boolean newIsPaid = paidCheckBox.isSelected();
                boolean newIsPickedUp = pickedUpCheckBox.isSelected();
                String newItems = itemsArea.getText();

                // Converte a string de novos itens em um array de strings
                String[] newItemQuantities = newItems.split(",");

                // Validação de disponibilidade dos itens se os itens foram alterados
                if (!items.equals(newItems)) {
                    boolean areItemsAvailable = reservationDAO.areItemsAvailable(newItems);
                    if (!areItemsAvailable) {
                        JOptionPane.showMessageDialog(this,
                                "A reserva não pôde ser concluída porque alguns itens não estão disponíveis.", "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                Reservation updatedReservation = new Reservation();
                updatedReservation.setId(id);
                updatedReservation.setRenterName(newRenterName);
                updatedReservation.setDeliveryDate(LocalDate.parse(newDeliveryDate, DateTimeFormatter.ISO_DATE));
                updatedReservation.setPickupDate(LocalDate.parse(newPickupDate, DateTimeFormatter.ISO_DATE));
                updatedReservation.setAddress(newAddress);
                updatedReservation.setPaid(newIsPaid);
                updatedReservation.setPickedUp(newIsPickedUp);
                updatedReservation.setItems(newItems);

                // Atualiza a reserva junto com as novas quantidades de itens
                reservationDAO.update(updatedReservation, newItemQuantities);
                loadReservationsTableData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma reserva para alterar.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adminUsers() {
        AdminUsuarios adminUsuariosFrame = new AdminUsuarios();
        adminUsuariosFrame.setVisible(true);
    }

    private void loadItemsTableData() {
        tableModelItems.setRowCount(0);
        List<Item> items = itemDAO.getAll();
        for (Item item : items) {
            tableModelItems.addRow(new Object[] { item.getId(), item.getDescription(), item.getTotalQuantity(),
                    item.getAvailableQuantity() });
        }
    }

    private void loadReservationsTableData() {
        tableModelReserves.setRowCount(0);
        List<Reservation> reservations = reservationDAO.getAll();
        for (Reservation reservation : reservations) {
            tableModelReserves.addRow(new Object[] { reservation.getId(), reservation.getRenterName(),
                    reservation.getDeliveryDate().format(DateTimeFormatter.ISO_DATE),
                    reservation.getPickupDate().format(DateTimeFormatter.ISO_DATE), reservation.getAddress(),
                    reservation.isPaid(), reservation.isPickedUp(), reservation.getItems() });
        }
    }

    public static void main(String[] args) {
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

        java.awt.EventQueue.invokeLater(() -> new ControleDeAluguel(currentUser).setVisible(true));
    }
}
