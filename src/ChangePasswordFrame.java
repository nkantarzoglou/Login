
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ChangePasswordFrame extends JFrame {

    private JPanel row1, row2, row3, row4;
    private JButton cancel, change;
    private JTextField tffirst, tfsecond;
    private JLabel message, first, second;

    public ChangePasswordFrame(User_Data user_info) {
        super("Change password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        row1 = new JPanel();
        message = new JLabel("Please enter new password twice");

        row2 = new JPanel();
        first = new JLabel("1ST:");
        tffirst = new JPasswordField(15);

        row3 = new JPanel();
        second = new JLabel("2ND:");
        tfsecond = new JPasswordField(15);

        row4 = new JPanel();
        cancel = new JButton("Cancel");
        change = new JButton("Change");

        Container pane = getContentPane();

        GridLayout layout = new GridLayout(4, 1);
        pane.setLayout(layout);
        FlowLayout flowlayout = new FlowLayout();

        row1.setLayout(flowlayout);
        row1.add(message);
        pane.add(row1);

        row2.setLayout(flowlayout);
        row2.add(first);
        row2.add(tffirst);
        pane.add(row2);

        row3.setLayout(flowlayout);
        row3.add(second);
        row3.add(tfsecond);
        pane.add(row3);

        row4.setLayout(flowlayout);
        row4.add(cancel);
        row4.add(change);
        pane.add(row4);

        setContentPane(pane);
        setLocationRelativeTo(null);
        pack();

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainFrame(user_info);
            }
        });

        change.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = null;
                    String url = "jdbc:sqlite:DB/MyDB.db";
                    conn = DriverManager.getConnection(url);
                    System.out.println("Connection to DB has been established.");
                    Statement stmt = conn.createStatement();

                    if (tffirst.getText().equals("") || tfsecond.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill both fields", "Info", JOptionPane.ERROR_MESSAGE);
                    } else if (!tffirst.getText().equals(tfsecond.getText())) {
                        JOptionPane.showMessageDialog(null, "Data entered don't match", "Info", JOptionPane.ERROR_MESSAGE);
                    } else {
                        
                        String salt = Salt.getSalt(30);
                        String pass_no_salt = Salt.generateSecurePassword(tffirst.getText(), salt);
                        String salted_pass = salt + pass_no_salt;
                        
                        
                        stmt.executeUpdate("UPDATE Users Set password = '" + salted_pass + "' WHERE username = '" + user_info.get_username() + "'");
                        conn.close();
                        dispose();
                        JOptionPane.showMessageDialog(null, "Changed password succesfully", "Info", JOptionPane.INFORMATION_MESSAGE);
                        new MainFrame(user_info);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
}
