
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class MyLoginFrame extends JFrame {

    private JPanel row1, row2, row3;
    private JButton login, exit, showall, new_user;
    private JTextField jusername, jpassword;
    private JLabel username, password;

    public MyLoginFrame() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        row1 = new JPanel();
        username = new JLabel("Username:");
        jusername = new JTextField(15);

        row2 = new JPanel();
        password = new JLabel("Password:");
        jpassword = new JPasswordField(15);

        row3 = new JPanel();
        showall = new JButton("Show all users");
        new_user = new JButton("New User");
        exit = new JButton("Exit");
        login = new JButton("Login");

        Container pane = getContentPane();

        GridLayout layout = new GridLayout(3, 1);
        pane.setLayout(layout);
        FlowLayout flowlayout = new FlowLayout();

        row1.setLayout(flowlayout);
        row1.add(username);
        row1.add(jusername);
        pane.add(row1);

        row2.setLayout(flowlayout);
        row2.add(password);
        row2.add(jpassword);
        pane.add(row2);

        row3.setLayout(flowlayout);
        row3.add(exit);
        //row3.add(showall);
        row3.add(new_user);
        row3.add(login);
        pane.add(row3);

        setContentPane(pane);
        setLocationRelativeTo(null);
        pack();

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = jusername.getText();
                String pass = jpassword.getText();

                Connection conn = null;
                try {
                    String url = "jdbc:sqlite:DB/MyDB.db";
                    conn = DriverManager.getConnection(url);
                    System.out.println("Connection to DB has been established.");
                    Statement stmt = conn.createStatement();

                    if (name.equals("") || pass.equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill both fields", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE username = '" + name + "'");

                        if (rs.next() == false) {
                            JOptionPane.showMessageDialog(null, "User '" + name + "' not found", "Info", JOptionPane.INFORMATION_MESSAGE);
                            jpassword.setText("");
                            jusername.setText("");
                        } else {
                            rs = stmt.executeQuery("SELECT password, admin_rights FROM Users WHERE username = '" + name + "'");

                            String salt = rs.getString("password").substring(0, 30);
                            String pass_no_salt = rs.getString("password").substring(30);
                            boolean passwordMatch = Salt.verifyUserPassword(pass, pass_no_salt, salt);

                            if (passwordMatch) {
                                JOptionPane.showMessageDialog(null, "Success", "Info", JOptionPane.INFORMATION_MESSAGE);
                                dispose();

                                User_Data user = new User_Data(name, rs.getInt("admin_rights"));
                                conn.close();
                                MainFrame a = new MainFrame(user);
                            } else {
                                JOptionPane.showMessageDialog(null, "Wrong Password", "Error", JOptionPane.WARNING_MESSAGE);
                                jpassword.setText("");
                            }
                        }
                    }
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }
        );
        /*
        showall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection conn = null;

                    String url = "jdbc:sqlite:DB/MyDB.db";
                    conn = DriverManager.getConnection(url);
                    System.out.println("Connection to DB has been established.");
                    Statement stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
                    Integer count = 0;
                    while (rs.next()) {
                        System.out.println(count + ". " + String.format("%1$-" + 20 + "s", rs.getString(1)) + "Admin Rights: " + rs.getString(3));
                        count++;
                    }
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(MyLoginFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        );*/
        new_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewUserFrame(new User_Data("temp_user", 0), 0);
                dispose();
            }
        }
        );
    }
}
