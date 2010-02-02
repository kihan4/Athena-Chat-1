/****************************************************
 * Athena: Encrypted Messaging Application v.0.0.1
 * By: 	
 * 			Gregory LeBlanc
 * 			Norm Maclennan 
 * 			Stephen Failla
 * 
 * This program allows a user to send encrypted messages over a fully standardized messaging architecture. It uses RSA with (x) bit keys and SHA-256 to 
 * hash the keys on the server side. It also supports fully encrypted emails using a standardized email address. The user can also send "one-off" emails
 * using a randomly generated email address
 * 
 * File: Client.java
 * 
 * This is where the detailed description of the file will go.
 *
 ****************************************************/
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;


public class Client extends Panel implements Runnable
{
	//Global username variabl
	String username;
	
	// Need to include authentication 
	// Need to re-do GUI 
	
	// Components for the visual display of the chat windows
	private TextField tf = new TextField();
	private TextArea ta = new TextArea();
	
	
	// The socket connecting us to the server
	public Socket socket;
	
	// The streams we communicate to the server; these come
	// from the socket
	private DataOutputStream dout;
	private DataInputStream din;
	
	// Constructor
	public Client( String host, int port ) {
		
		// Set up the screen
		setLayout( new BorderLayout() );
		add( "North", tf );
		add( "Center", ta );
		
		// We want to receive messages when someone types a line
		// and hits return, using an anonymous class as a callback
		tf.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				processMessage( e.getActionCommand() );
			}
		} );
		// Connect to the server
		try {
			// Initiate the connection
			socket = new Socket( host, port );
			
			username = JOptionPane.showInputDialog("Please enter your username"); // Get username from the user
			String password = JOptionPane.showInputDialog("Please enter your password"); // Get Password from the user
						
			// We got a connection! Tell the world
			System.out.println( "connected to "+socket );
			// Let's grab the streams and create DataInput/Output streams
			// from them
			din = new DataInputStream( socket.getInputStream() );
			dout = new DataOutputStream( socket.getOutputStream() );
			
			//Sending user information over the socket to the Server
			dout.writeUTF(username); //Sending Username
			dout.writeUTF(password); //Sending Password
			
			// Start a background thread for receiving messages
			new Thread( this ).start();
		} catch( IOException ie ) { System.out.println( ie ); }
	}
	// Gets called when the user types something
	private void processMessage( String message ) {
		try {
			// Send it to the server
			dout.writeUTF( message );
			
			// Append own message to IM window
			ta.append(username + ": " + message + "\n");
			
			// Clear out text input field
			tf.setText( "" );
		} catch( IOException ie ) { System.out.println( ie ); }
	}
	// Background thread runs this: show messages from other window
	public void run() {
		try {
			//
			String toUser = JOptionPane.showInputDialog("Please input the user you want to talk to!");			
			dout.writeUTF(toUser);
			
			// Receive messages one-by-one, forever
			while (true) {
				// Get the from user 
				String fromUser = din.readUTF();
				// Get the message
				String message = din.readUTF();
				// Print it to our text window
				ta.append( fromUser + ": " + message+"\n" );
			}
		} catch( IOException ie ) { System.out.println( ie ); }
	}
}
