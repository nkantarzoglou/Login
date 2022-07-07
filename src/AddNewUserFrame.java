
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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AddNewUserFrame extends JFrame {

    private JPanel row1, row2, row3, row4, row5;
    private JButton cancel, add;
    private JTextField tffirst, tfsecond, tfusername;
    private JLabel message, first, second, username, Lrights, message2;
    private String[] choices = {"User", "Admin"};
    private JComboBox rights;

    public AddNewUserFrame(User_Data user_info, int admin_or_not) {
        super("Add new user");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        row1 = new JPanel();
        message = new JLabel("Please new user data");

        row2 = new JPanel();
        username = new JLabel("Username:");
        tfusername = new JTextField(15);
        Lrights = new JLabel("Rights:");
        rights = new JComboBox(choices);
        rights.setSelectedIndex(0);

        row3 = new JPanel();
        message2 = new JLabel("Enter credentials twice");

        row4 = new JPanel();
        first = new JLabel("1ST:");
        tffirst = new JPasswordField(15);
        second = new JLabel("2ND:");
        tfsecond = new JPasswordField(15);

        row5 = new JPanel();
        cancel = new JButton("Cancel");
        add = new JButton("Add");

        Container pane = getContentPane();

        GridLayout layout = new GridLayout(5, 1);
        pane.setLayout(layout);
        FlowLayout flowlayout = new FlowLayout();

        if (admin_or_not == 0) {
            rights.setEnabled(false);
        }

        row1.setLayout(flowlayout);
        row1.add(message);
        pane.add(row1);

        row2.setLayout(flowlayout);
        row2.add(username);
        row2.add(tfusername);
        row2.add(Lrights);
        row2.add(rights);
        pane.add(row2);

        row3.setLayout(flowlayout);
        row3.add(message2);
        pane.add(row3);

        row4.setLayout(flowlayout);
        row4.add(first);
        row4.add(tffirst);
        row4.add(second);
        row4.add(tfsecond);
        pane.add(row4);

        row5.setLayout(flowlayout);
        row5.add(cancel);
        row5.add(add);
        pane.add(row5);

        setContentPane(pane);
        setLocationRelativeTo(null);
        pack();

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (admin_or_not == 1) {
                    new MainFrame(user_info);
                } else {
                    new MyLoginFrame();
                }
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = null;
                    String url = "jdbc:sqlite:DB/MyDB.db";
                    conn = DriverManager.getConnection(url);
                    System.out.println("Connection to DB has been established.");
                    Statement stmt = conn.createStatement();

                    if (tffirst.getText().equals("") || tfsecond.getText().equals("") || tfusername.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill all textfields", "Info", JOptionPane.ERROR_MESSAGE);
                    } else if (!tffirst.getText().equals(tfsecond.getText())) {
                        JOptionPane.showMessageDialog(null, "Passwords don't match", "Info", JOptionPane.ERROR_MESSAGE);
                        tffirst.setText("");
                        tfsecond.setText("");
                    } else {
                        ResultSet rs;
                        rs = stmt.executeQuery("SELECT * FROM Users WHERE username = '" + tfusername.getText() + "'");

                        String salt = Salt.getSalt(30);
                        String pass_no_salt = Salt.generateSecurePassword(tffirst.getText(), salt);
                        String salted_pass = salt + pass_no_salt;

                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "Username already exists", "Info", JOptionPane.ERROR_MESSAGE);
                            tfusername.setText("");
                            tfsecond.setText("");
                            tffirst.setText("");
                            conn.close();
                        } else {
                            //add user
                            if (rights.getSelectedIndex() == 0) {
                                //user
                                stmt.executeUpdate("INSERT INTO Users VALUES('" + tfusername.getText() + "', '" + salted_pass + "', " + 0 + ")");
                            } else {
                                stmt.executeUpdate("INSERT INTO Users VALUES('" + tfusername.getText() + "', '" + salted_pass + "', " + 1 + ")");
                            }
                            conn.close();
                            dispose();
                            JOptionPane.showMessageDialog(null, "Added user succesfully", "Info", JOptionPane.INFORMATION_MESSAGE);
                            if (admin_or_not == 1) {
                                new MainFrame(user_info);
                            } else {
                                new MainFrame(new User_Data(tfusername.getText(), 0));
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

    }
}
