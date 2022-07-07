
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private JPanel row1, row2, row3, row4;
    private JButton change_my_pass, change_my_usnme;
    private JButton delete_user, change_rights, add_user;
    private JButton exit, disconnect;
    private JLabel WELCOME, USER, ADMIN, OTHER;

    public MainFrame(User_Data user_info) {
        super("Main Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        row1 = new JPanel();
        WELCOME = new JLabel("Welcome back, " + user_info.get_username());

        row2 = new JPanel();
        change_my_usnme = new JButton("Change my Username");
        change_my_pass = new JButton("Change my Password");

        row3 = new JPanel();
        delete_user = new JButton("Delete user");
        change_rights = new JButton("Change user rights");
        add_user = new JButton("Add user");

        row4 = new JPanel();
        exit = new JButton("Exit");
        disconnect = new JButton("Disconnect");

        Container pane = getContentPane();

        GridLayout layout = new GridLayout(4, 1);
        pane.setLayout(layout);
        FlowLayout flowlayout = new FlowLayout();

        row1.setLayout(flowlayout);
        row1.add(WELCOME);
        pane.add(row1);

        row2.setLayout(flowlayout);
        row2.add(change_my_usnme);
        row2.add(change_my_pass);
        pane.add(row2);

        row3.setLayout(flowlayout);
        row3.add(delete_user);
        row3.add(change_rights);
        row3.add(add_user);
        pane.add(row3);

        row4.setLayout(flowlayout);
        row4.add(exit);
        row4.add(disconnect);
        pane.add(row4);

        setContentPane(pane);
        setLocationRelativeTo(null);
        pack();

        if (user_info.get_admin_rights() == 0) {
            delete_user.setEnabled(false);
            change_rights.setEnabled(false);
            add_user.setEnabled(false);
        }

        change_my_usnme.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangeUserNameFrame(user_info);
                dispose();
            }
        });

        change_my_pass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangePasswordFrame(user_info);
                dispose();
            }
        });

        delete_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeleteUserFrame(user_info);
                dispose();
            }
        });

        change_rights.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChangeUserRightsFrame(user_info);
                dispose();
            }
        });

        add_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddNewUserFrame(user_info, 1);
                dispose();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MyLoginFrame();
            }
        });

    }

}
