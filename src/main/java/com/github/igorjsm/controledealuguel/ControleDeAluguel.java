package com.github.igorjsm.controledealuguel;

import javax.swing.SwingUtilities;
import com.github.igorjsm.controledealuguel.view.Login;

public class ControleDeAluguel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Login login = new Login();
                login.setVisible(true);
            }
        });
    }
}
