package proyecto_energias;

import com.formdev.flatlaf.intellijthemes.*;


public class Main {

    public static void main(String[] args) {
        FlatOneDarkIJTheme.setup();
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphicsMain().setVisible(true);
            }
        });

    }

}
