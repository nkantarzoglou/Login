
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChangeUserRightsFrame extends JFrame {

    private JPanel row1, row2, row3, row4;
    private JButton cancel, change;
    private JTextField username;
    private JLabel Lmessage, Lusername, Lrights;
    private String[] choices = {"User", "Admin"};
    private JComboBox rights;

    public ChangeUserRightsFrame(User_Data user_info) {
        super("Change User Rigts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        row1 = new JPanel();
        Lmessage = new JLabel("Select user and given rights");

        row2 = new JPanel();
        Lusername = new JLabel("Usename:");
        username = new JTextField(15);
        Lrights = new JLabel("Rights:");
        rights = new JComboBox(choices);
        rights.setSelectedIndex(0);

        row3 = new JPanel();
        cancel = new JButton("Cancel");
        change = new JButton("Change");

        Container pane = getContentPane();

        GridLayout layout = new GridLayout(3, 1);
        pane.setLayout(layout);
        FlowLayout flowlayout = new FlowLayout();

        row1.setLayout(flowlayout);
        row1.add(Lmessage);
        pane.add(row1);

        row2.setLayout(flowlayout);
        row2.add(Lusername);
        row2.add(username);
        row2.add(Lrights);
        row2.add(rights);
        pane.add(row2);

        row3.setLayout(flowlayout);
        row3.add(cancel);
        row3.add(change);
        pane.add(row3);

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

                    if (username.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter a username", "Info", JOptionPane.ERROR_MESSAGE);
                    } else {

                        ResultSet rs;
                        rs = stmt.executeQuery("SELECT * FROM Users WHERE username = '" + username.getText() + "'");

                        if (rs.next()) {
                            if (rights.getSelectedIndex() == 0){
                                //user
                                stmt.executeUpdate("UPDATE Users Set admin_rights = '0' WHERE username = '" + username.getText() + "'");
                            }else{
                                //admin
                                stmt.executeUpdate("UPDATE Users Set admin_rights = '1' WHERE username = '" + username.getText() + "'");
                            }
                            JOptionPane.showMessageDialog(null, "User rights changed succesfully", "Info", JOptionPane.INFORMATION_MESSAGE);
                            conn.close();
                            dispose();
                            new MainFrame(user_info);
                            
                        } else {
                            conn.close();
                            JOptionPane.showMessageDialog(null, "User not found", "Info", JOptionPane.ERROR_MESSAGE);
                            username.setText("");
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }
}
