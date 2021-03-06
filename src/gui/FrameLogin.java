package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import app.LoginControlImp;
import app.LoginControlInterface;

/**
 * The login process.
 * 
 * @author Tobias
 */
@SuppressWarnings("serial")
public class FrameLogin extends AbstractFrame {
	private JLabel header;
	private JPanel leftPanel;
	private JPanel centerPanel;
	private JPanel boxdCenterPanel;
	private JLabel user;
	protected JTextField userTextField;
	private JLabel password;
	protected JPasswordField passwordTextField;
	private JPanel boxdleftPanel;
	private JButton btnlogin;

	@Override
	protected void createWidget() {

		// header section
		header = new JLabel("Login Screen");
		header.setPreferredSize(new Dimension(400, 40));
		header.setForeground(Color.WHITE);
		header.setBackground(Color.BLACK);
		header.setOpaque(true);
		header.setHorizontalAlignment(SwingConstants.CENTER);
		header.setFont(header.getFont().deriveFont(Font.BOLD + Font.ITALIC, 30));
		// header section

		leftPanel = new JPanel();
		leftPanel.setLayout(new GridLayout(2, 1, 10, 10));

		user = new JLabel("User ");
		user.setFont(header.getFont().deriveFont(Font.BOLD + Font.ITALIC, 20));

		password = new JLabel("Password ");
		password.setFont(header.getFont().deriveFont(Font.BOLD + Font.ITALIC,
				20));

		boxdleftPanel = new JPanel();
		boxdleftPanel.setLayout(new BoxLayout(boxdleftPanel,
				BoxLayout.PAGE_AXIS));

		centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(2, 1, 10, 10));

		userTextField = new JTextField();

		passwordTextField = new JPasswordField();
		passwordTextField.setFont(header.getFont().deriveFont(Font.BOLD, 20));

		boxdCenterPanel = new JPanel();
		boxdCenterPanel.setLayout(new BoxLayout(boxdCenterPanel,
				BoxLayout.PAGE_AXIS));

		// button
		btnlogin = new JButton("Login");
		btnlogin.setPreferredSize(new Dimension(20, 30));
		btnlogin.setActionCommand("login gedrückt");
		// button
	}

	@Override
	protected void addWidget() {
		getContentPane().setLayout(new BorderLayout(5, 5));
		getContentPane().add(BorderLayout.NORTH, header);
		getContentPane().add(BorderLayout.SOUTH, btnlogin);
		leftPanel.add(user);
		leftPanel.add(password);
		boxdleftPanel.add(leftPanel);
		boxdleftPanel.add(Box.createVerticalGlue());
		getContentPane().add(BorderLayout.WEST, boxdleftPanel);

		centerPanel.add(userTextField);
		centerPanel.add(passwordTextField);
		boxdCenterPanel.add(centerPanel);
		boxdCenterPanel.add(Box.createVerticalGlue());
		getContentPane().add(BorderLayout.CENTER, boxdCenterPanel);
	}

	private void login() {
		String user = userTextField.getText();
		String passText = new String(passwordTextField.getPassword());
		LoginControlInterface tmp = new LoginControlImp();
		try {
			if (tmp.loginDB(user, passText)) {
				new VerwaltungMainFrame().init();
				closeFrame();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			JOptionPane
					.showMessageDialog(
							null,
							"Falsche Benutzer/Passwort-Kombination.\n Bei wiederholtem Fehlschlag überprüfen Sie, ob die DatenBank läuft.");
		}
	}

	@Override
	protected void setupInteractions() {
		passwordTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});

		btnlogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				login();
			}
		});
	}

	public static void main(String[] args) {
		FrameLogin li = new FrameLogin();
		li.init();
	}
}