// $Id$
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

public class Server
{
	// The ServerSocket we'll use for accepting new connections
	private ServerSocket ss;
	
	//Define the MySQL connection
	private Connection con = null;
	
	//Define Server Listening port
	private static int listenPort = 7777;
	
	// A mapping from sockets to DataOutputStreams. This will
	// help us avoid having to create a DataOutputStream each time
	// we want to write to a stream.
	private Hashtable outputStreams = new Hashtable();
	// Constructor and while-accept loop all in one.
	public Server( int port ) throws IOException {
		// All we have to do is listen
		listen( port );
	}
	
	private void login ( String userName, String password) { 
		try { 
		//Here we will have to have some mysql code to verify with our Database that their username is correct.
		//JDBC URL for the database
		String url = "jdbc:mysql://athenachat.org/" +
				"mysql?user=CHANGEME&password=CHANGEME";
		//Defining the Statement and ResultSet holders
		Statement stmt;
		ResultSet rs; 
		
		//NO IDEA WHAT THIS IS
		Class.forName("org.gjt.mm.mysql.Driver");
		
		//Here is where the connection is made
		con = DriverManager.getConnection(url);
		}
		catch ( SQLException e) { 
				e.printStackTrace ( );
		}
		catch ( ClassNotFoundException f) { 
				f.printStackTrace();
		}
		finally { 
			if( con != null) { 
				try { con.close( ); }
				catch( Exception e) { } 
			}
		}
	}
		
	
	private void listen( int port ) throws IOException {
		// Create the ServerSocket
		ss = new ServerSocket( port );
		// Tell the world we're ready to go
		System.out.println( "Listening on "+ss );
		
		// Keep accepting connections forever 
		// This is true, but we need to make it such that the connection gets sent to the correct client -> client 
		while (true) {
			// Grab the next incoming connection
			Socket s = ss.accept();
			// Tell the world we've got it
			System.out.println( "Connection from "+s );
			// Create a DataOutputStream for writing data to the
			// other side
			
			// How are we going to send the DataOutputStream to the correct client?
			DataOutputStream dout = new DataOutputStream( s.getOutputStream() );
			
			// Save this stream so we don't need to make it again (Maybe here we can have an extra field in outputStreams 
			//Where we can define the client
			outputStreams.put( s, dout );
			
			// Create a new thread for this connection, and then forget
			// about it
			new ServerThread( this, s );
		}
	}
	// Get an enumeration of all the OutputStreams, one for each client
	// connected to us
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}
	// Send a message to all clients (utility routine)
	void sendToAll( String message ) {
		// We synchronize on this because another thread might be
		// calling removeConnection() and this would screw us up
		// as we tried to walk through the list
		synchronized( outputStreams ) {
			// For each client ...
			for (Enumeration e = getOutputStreams(); e.hasMoreElements(); ) {
				// ... get the output stream ...
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				// ... and send the message
				try {
					dout.writeUTF( message );
				} catch( IOException ie ) { System.out.println( ie ); }
			}
		}
	}
	// Remove a socket, and it's corresponding output stream, from our
	// list. This is usually called by a connection thread that has
	// discovered that the connection to the client is dead.
	void removeConnection( Socket s ) {
		// Synchronize so we don't mess up sendToAll() while it walks
		// down the list of all output streams
		synchronized( outputStreams ) {
			// Tell the world
			System.out.println( "Removing connection to "+s );
			// Remove it from our hashtable/list
			outputStreams.remove( s );
			// Make sure it's closed
			try {
				s.close();
			} catch( IOException ie ) {
				System.out.println( "Error closing "+s );
				ie.printStackTrace();
			}
		}
	}
	// Main routine
	// Usage: java Server <port>
	static public void main( String args[] ) throws Exception {
		// Get the port # from the command line
		int port = listenPort;
		// Create a Server object, which will automatically begin
		// accepting connections.
		new Server( port );
	}
}
