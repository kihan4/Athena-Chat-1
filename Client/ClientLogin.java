/****************************************************
 * Athena: Encrypted Messaging Application v.0.0.2
 * By: 	
 * 			Gregory LeBlanc
 * 			Norm Maclennan 
 * 			Stephen Failla
 * 
 * This program allows a user to send encrypted messages over a fully standardized messaging architecture. It uses RSA with (x) bit keys and SHA-256 to 
 * hash the keys on the server side. It also supports fully encrypted emails using a standardized email address. The user can also send "one-off" emails
 * using a randomly generated email address
 * 
 * File: ClientApplet.java
 * 
 * Creates the window for the client and sets connection variables.
 *
 ****************************************************/
/**
 * @author OlmypuSoft
 *
 */
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientLogin extends JFrame { 

	//Components for the visual display of the login window
	public JFrame login;
	public JPanel contentPane = new JPanel();
	public JTextField username = new JTextField();
	public JPasswordField password = new JPasswordField();
	public JLabel usernameLabel = new JLabel("Username");
	public JLabel passwordLabel = new JLabel("Password");
	public JButton connect = new JButton("Connect");
	public JButton cancel = new JButton("Cancel");
	
	//Constructor | Here's where the fun begins
	ClientLogin() { 
			//Initialize Login window
			login = new JFrame("Athena Chat Application");
			login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			login.setSize(200,300);
			login.setResizable(false);
			contentPane.setLayout(null);
			
			//Adjust font sizes
			connect.setFont(new Font("Dialog", 1, 10));
			cancel.setFont(new Font("Dialog", 1, 10));
			usernameLabel.setFont(new Font("Dialog", 1, 10));
			passwordLabel.setFont(new Font("Dialog", 1, 10));
			
			//Size the components
			usernameLabel.setBounds(50,75,100,25);
			username.setBounds(50,100,100,25);
			passwordLabel.setBounds(50,125,100,25);
			password.setBounds(50,150,100,25);
			cancel.setBounds(10,200,75,30);
			connect.setBounds(105,200,75,30);
			
			//Let the "Action Begin"
			
			//ActionListener to make the connect menu item connect
			connect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event){
					Client.connect(username.getText(), password.getPassword());
					login.setVisible(false);
				}
			});
			
			//Add the components to the Frame
			contentPane.add(usernameLabel);
			contentPane.add(username);
			contentPane.add(passwordLabel);
			contentPane.add(password);
			contentPane.add(connect);
			contentPane.add(cancel);
			
			//Initialize Frame
			login.setContentPane(contentPane);
			login.setVisible(true);
			}
	
}
