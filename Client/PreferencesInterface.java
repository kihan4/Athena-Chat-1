import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.UIManager;
import javax.swing.LookAndFeel;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.SwingUtilities;

/****************************************************
 * Athena: Encrypted Messaging Application v.0.0.2
 * By: 	
 * 			Gregory LeBlanc
 * 			Norman Maclennan
 * 			Stephen Failla
 * 
 * This program allows a user to send encrypted messages over a fully standardized messaging architecture. It uses RSA with (x) bit keys and SHA-256 to 
 * hash the keys on the server side. It also supports fully encrypted emails using a standardized email address. The user can also send "one-off" emails
 * using a randomly generated email address
 * 
 * File: ClientPreferences.java
 * 
 * Creates the preferences window invoked from ClientApplet
 *
 ****************************************************/

//Let's make the preferences window
public class PreferencesInterface extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5472264414606126641L;
	//Load preference variables from file into array
	Object[] settingsArray = Athena.clientResource.getCurrentSettingsArray();
	boolean allowSystemTray = Boolean.parseBoolean(settingsArray[0].toString());
	boolean allowESCTab = Boolean.parseBoolean(settingsArray[1].toString());
	boolean enableSpellCheck = Boolean.parseBoolean(settingsArray[2].toString());
	boolean enableSounds = Boolean.parseBoolean(settingsArray[3].toString());
	int setEncryptionType = Integer.parseInt(settingsArray[4].toString());
	String setFontFace = settingsArray[5].toString();
	boolean enableBold = Boolean.parseBoolean(settingsArray[6].toString());
	boolean enableItalic = Boolean.parseBoolean(settingsArray[7].toString());
	boolean enableUnderline = Boolean.parseBoolean(settingsArray[8].toString());
	int setFontSize = Integer.parseInt(settingsArray[9].toString());
	
	
	//Define components
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	//String[] allFontFamilies = ge.getAvailableFontFamilyNames();
	Font[] allFonts = ge.getAllFonts();
	public String[] themeList = {"javax.swing.plaf.metal.MetalLookAndFeel","com.sun.java.swing.plaf.windows.WindowsLookAndFeel","com.sun.java.swing.plaf.gtk.GTKLookAndFeel","com.sun.java.swing.plaf.mac.MacLookAndFeel","com.sun.java.swing.plaf.motif.MotifLookAndFeel","com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"};
	
	public JFrame preferences;
	public JPanel contentPane = new JPanel();
	public JPanel generalPanel, notificationsPanel, encryptionPanel, formattingPanel, themePanel;
	public JPanel prefLabelPaneLeft = new JPanel();
	public JPanel prefLabelPaneRight = new JPanel();
	public JButton apply = new JButton("Apply");
	public JButton cancel = new JButton("Close");
	public Border blackline, whiteline;
	public Color originalColor;
	public TitledBorder generalTitledBorder, notificationsTitledBorder, encryptionTitledBorder, formattingTitledBorder, themeTitleBorder;

	//TODO Create components for each of the preference menu categories
	//Define components for the General Menu Panel
	public JButton generalLabel = new JButton("System", new ImageIcon("images/generalPref.png"));
	public JCheckBox systemTrayCheckBox = new JCheckBox("Show Athena in System Tray", allowSystemTray);
	public JCheckBox allowESCCheckBox = new JCheckBox("Allow ESC Key to Close a Tab", allowESCTab);
	public JCheckBox enableSpellCheckCheckBox = new JCheckBox("Enable Spell Check", enableSpellCheck);
	boolean systemTrayVal;
	boolean allowESCVal;
	boolean enableSCVal;
	boolean systemTrayFlag = false;
	boolean allowESCFlag = false;
	boolean enableSpellCheckFlag = false;
	
	//Define components for the Notifications Menu Panel
	public JButton notificationsLabel = new JButton("Notifications", new ImageIcon("images/notificationsPref.png"));
	public JCheckBox enableSoundsCheckBox = new JCheckBox("Enable Sounds", enableSounds);
	boolean enableSoundsVal;
	boolean enableSoundsFlag = false;

	//Define components for the Encryption Menu Panel
	public JButton encryptionLabel = new JButton("Encryption", new ImageIcon("images/encryptionPref.png"));
	public JLabel generateNewKeyPairJLabel = new JLabel("Generate New Encryption Key Pair");
	public JButton generateNewKeyPairJButton = new JButton("Generate");
	
	//Define components for the Formatting Menu Panel
	public JButton formattingLabel = new JButton("Formatting", new ImageIcon("images/fontPref.png"));
	public JLabel selectFontLabel = new JLabel("Font Type:");
	public JLabel fontSizeLabel = new JLabel("Font Size:");
	public JLabel generalFontLabel = new JLabel("Font Style:");
	public JComboBox selectFontComboBox, fontSizeComboBox;
	public JCheckBox setBoldCheckBox = new JCheckBox("Bold", enableBold);
	public JCheckBox setItalicsCheckBox = new JCheckBox("Italics", enableItalic);
	public JCheckBox setUnderlineCheckBox = new JCheckBox("Underlined", enableUnderline);
	boolean setFontFaceFlag = false;
	boolean setBoldFlag = false;
	boolean setItalicsFlag = false;
	boolean setUnderlineFlag = false;
	boolean setSizeFlag = false;
	String setFontFaceVal = setFontFace;
	boolean setBoldVal = enableBold;
	boolean setItalicsVal = enableItalic;
	boolean setUnderlineVal = enableUnderline;
	int setSizeVal = setFontSize;
	
	//Define components for the Theme Menu Panel
	public JButton themeLabel = new JButton("Appearance", new ImageIcon("images/themePref.png"));
	public JComboBox selectThemeComboBox = new JComboBox(themeList);
	public JLabel selectThemeJLabel = new JLabel("Select Theme");
	public JButton installNewThemeJButton = new JButton("Install");
	public JLabel installNewThemeJLabel = new JLabel("Install New Theme");
	
	//Initialize array to hold current file settings and accept all new setting changes
	public Object[] settingsToWrite = settingsArray;
	
	//Constructor
	PreferencesInterface() {	
		
		//Initialize Preferences Window
		preferences = new JFrame("Preferences");
		preferences.setSize(615,375);
		preferences.setResizable(false);
		contentPane.setLayout(null);
		preferences.setLocationRelativeTo(CommunicationInterface.imContentFrame);
		preferences.setIconImage(Toolkit.getDefaultToolkit().getImage("images/logosmall.png"));
		//Retrieve all available font names and set font combo box
		String[] allFontNames = new String[allFonts.length];
		//Client.clientResource.fontFamilyTable.clear();
		for(int a = 0; a < Athena.clientResource.allFonts.length; a++)
		{
			allFontNames[a] = Athena.clientResource.allFonts[a].getFontName();
		}
		selectFontComboBox = new JComboBox(allFontNames);
		selectFontComboBox.setSelectedItem(setFontFace);
		
		fontSizeComboBox = new JComboBox(new String[] {"8", "10", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48"});
		fontSizeComboBox.setSelectedItem(Integer.toString(setFontSize));
		
		//Initialize borders
		blackline = BorderFactory.createLineBorder(Color.black);
		whiteline = BorderFactory.createLineBorder(Color.white);
		Border labelBorder = BorderFactory.createRaisedBevelBorder();
		Border blackBorder = BorderFactory.createBevelBorder(1, Color.darkGray, Color.black);
		Border extraBorder = BorderFactory.createLoweredBevelBorder();
		Border prefAltBorder = BorderFactory.createCompoundBorder(labelBorder, extraBorder);
		Border prefBorder = BorderFactory.createCompoundBorder(blackBorder, labelBorder);
		generalTitledBorder = BorderFactory.createTitledBorder(
			       prefBorder, "System Options");
		encryptionTitledBorder = BorderFactory.createTitledBorder(
					prefBorder, "Encryption Options");
		formattingTitledBorder = BorderFactory.createTitledBorder(
					prefBorder, "Formatting Options");
		notificationsTitledBorder = BorderFactory.createTitledBorder(
					prefBorder, "Notification Options");
		themeTitleBorder = BorderFactory.createTitledBorder(
					prefBorder, "Appearance Options");
		
		//Size the default components
		prefLabelPaneLeft.setBounds(15, 10, 100, 320);
		prefLabelPaneRight.setBounds(470, 10, 100, 220);
		prefLabelPaneLeft.setBorder(prefAltBorder);
		prefLabelPaneRight.setBorder(prefAltBorder);
		apply.setBounds(470,250,100,30);
		cancel.setBounds(470,290,100,30);

		// Set apply button default to disabled until changes are made
		apply.setEnabled(false);
		
		//Initialize default button action listeners
		apply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				//Apply all changes
				try {
					setGeneralSettings(systemTrayFlag, systemTrayVal, allowESCFlag, allowESCVal, enableSpellCheckFlag, enableSCVal);
					setNotificationSettings(enableSoundsFlag, enableSoundsVal);
					setFormattingSettings(setFontFaceFlag, setBoldFlag, setItalicsFlag, setUnderlineFlag, setSizeFlag,
											setFontFaceVal, setBoldVal, setItalicsVal, setUnderlineVal, setSizeVal);
					writeSavedPreferences(settingsToWrite);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				apply.setEnabled(false);
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
				preferences.dispose();
			}
		});

		//Initialize the JPanels for each of the options
		//General Menu Section
		/*************************************************/		
		generalLabel.setForeground(Color.white);
		originalColor = generalLabel.getBackground();
		generalLabel.setBackground(Color.black);
		generalLabel.setBorder(whiteline);
		generalLabel.setVerticalTextPosition(JLabel.BOTTOM);
		generalLabel.setHorizontalTextPosition(JLabel.CENTER);
		generalLabel.setBounds(30,20,75,75);
		generalLabel.setBorder(labelBorder);
		
		generalPanel = new JPanel();
		generalPanel.setLayout(null);
		generalPanel.setBorder(generalTitledBorder);
		generalPanel.setBounds(140,15,300,300);
		generalPanel.setVisible(true);		
		
		systemTrayCheckBox.setBounds(50,20,200,50);
		allowESCCheckBox.setBounds(50,60,200,50);
		enableSpellCheckCheckBox.setBounds(50,100,200,50);
		
		generalPanel.add(systemTrayCheckBox);
		generalPanel.add(allowESCCheckBox);
		generalPanel.add(enableSpellCheckCheckBox);
		
		systemTrayCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					systemTrayVal = true;
				else
					systemTrayVal = false;
				settingsToWrite[0] = systemTrayVal;
				systemTrayFlag = true;
			}
		});
		allowESCCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					allowESCVal = true;
				else
					allowESCVal = false;
				settingsToWrite[1] = allowESCVal;
				allowESCFlag = true;
			}
		});
		enableSpellCheckCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					enableSCVal = true;
				else
					enableSCVal = false;
				settingsToWrite[2] = enableSCVal;
				enableSpellCheckFlag = true;
			}
		});
		/*************************************************/
		
		//Notification Menu Section
		/*************************************************/	
		notificationsLabel.setForeground(Color.black);
		notificationsLabel.setVerticalTextPosition(JLabel.BOTTOM);
		notificationsLabel.setHorizontalTextPosition(JLabel.CENTER);
		notificationsLabel.setBounds(30,100,75,75);
		notificationsLabel.setBorder(labelBorder);
		
		notificationsPanel = new JPanel();
		notificationsPanel.setLayout(null);
		notificationsPanel.setBorder(notificationsTitledBorder);
		notificationsPanel.setBounds(140,15,300,300);
		notificationsPanel.setVisible(false);
		
		enableSoundsCheckBox.setBounds(50,20,200,50);
		
		notificationsPanel.add(enableSoundsCheckBox);
		
		enableSoundsCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					enableSoundsVal = true;
				else
					enableSoundsVal = false;
				settingsToWrite[3] = enableSoundsVal;
				enableSoundsFlag = true;
			}
		});
		/*************************************************/		
		
		//Encrpytion Menu Selection
		/*************************************************/	
		encryptionLabel.setForeground(Color.black);
		encryptionLabel.setVerticalTextPosition(JLabel.BOTTOM);
		encryptionLabel.setHorizontalTextPosition(JLabel.CENTER);
		encryptionLabel.setBounds(30,180,75,75);
		encryptionLabel.setBorder(labelBorder);
		
		encryptionPanel = new JPanel();
		encryptionPanel.setLayout(null);
		encryptionPanel.setBorder(encryptionTitledBorder);
		encryptionPanel.setBounds(140,15,300,300);
		encryptionPanel.setVisible(false);
		
		generateNewKeyPairJLabel.setBounds(50,20,200,50);
		generateNewKeyPairJButton.setBounds(50,70,100,50);

		encryptionPanel.add(generateNewKeyPairJButton);
		encryptionPanel.add(generateNewKeyPairJLabel);
		
		//This settings array update will go in check box action listeners when implemented as seen above in general settings
		//settingsToWrite[5] = "0";
		/*************************************************/	
		
		//Formatting Menu Selection
		/*************************************************/	
		formattingLabel.setForeground(Color.black);
		formattingLabel.setVerticalTextPosition(JLabel.BOTTOM);
		formattingLabel.setHorizontalTextPosition(JLabel.CENTER);
		formattingLabel.setBounds(485,20,75,75);
		formattingLabel.setBorder(labelBorder);
		
		formattingPanel = new JPanel();
		formattingPanel.setLayout(null);
		formattingPanel.setBorder(formattingTitledBorder);
		formattingPanel.setBounds(140,15,300,300);
		formattingPanel.setVisible(false);
		
		selectFontComboBox.setBounds(50, 55, 200, 30);
		fontSizeComboBox.setBounds(192, 115, 50, 30);
		setBoldCheckBox.setBounds(50,125,100,30);
		setItalicsCheckBox.setBounds(50,155,100,30);
		setUnderlineCheckBox.setBounds(50,185,100,30);
		selectFontLabel.setBounds(50, 30, 100, 20);
		fontSizeLabel.setBounds(190, 95, 100, 20);
		generalFontLabel.setBounds(50, 95, 100, 20);
		
		formattingPanel.add(selectFontComboBox);
		formattingPanel.add(setBoldCheckBox);
		formattingPanel.add(setItalicsCheckBox);
		formattingPanel.add(setUnderlineCheckBox);
		formattingPanel.add(fontSizeComboBox);
		formattingPanel.add(selectFontLabel);
		formattingPanel.add(fontSizeLabel);
		formattingPanel.add(generalFontLabel);
		
		selectFontComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
					apply.setEnabled(true);
					setFontFaceVal = selectFontComboBox.getSelectedItem().toString();
					System.out.println("Retrieved font style: " + setFontFaceVal);
					settingsToWrite[5] = setFontFaceVal;
					setFontFaceFlag = true;
					
					
				}
			});
		
		fontSizeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
					apply.setEnabled(true);
					setSizeVal = Integer.parseInt(fontSizeComboBox.getSelectedItem().toString());
					System.out.println("Retrieved font size: " + setSizeVal);
					settingsToWrite[9] = setSizeVal;
					setSizeFlag = true;
				}
			});
		
		setBoldCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					setBoldVal = true;
				else
					setBoldVal = false;
				settingsToWrite[6] = setBoldVal;
				setBoldFlag = true;
			}
		});
		
		setItalicsCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					setItalicsVal = true;
				else
					setItalicsVal = false;
				settingsToWrite[7] = setItalicsVal;
				setItalicsFlag = true;
			}
		});
		
		setUnderlineCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				apply.setEnabled(true);
				if (e.getStateChange() == ItemEvent.SELECTED)
					setUnderlineVal = true;
				else
					setUnderlineVal = false;
				settingsToWrite[8] = setUnderlineVal;
				setUnderlineFlag = true;
			}
		});
		/*************************************************/	
		
		//Theme Menu Selection
		/*************************************************/	
		themeLabel.setForeground(Color.black);
		themeLabel.setVerticalTextPosition(JLabel.BOTTOM);
		themeLabel.setHorizontalTextPosition(JLabel.CENTER);
		themeLabel.setBounds(485,100,75,75);
		themeLabel.setBorder(labelBorder);
		
		themePanel = new JPanel();
		themePanel.setLayout(null);
		themePanel.setBorder(themeTitleBorder);
		themePanel.setBounds(140,15,300,300);
		themePanel.setVisible(false);
		
		//Define components for the Theme Menu Panel
		selectThemeComboBox.setBounds(50,70,200,50);
		selectThemeJLabel.setBounds(50,20,100,50);
		installNewThemeJButton.setBounds(50,175,100,50);
		installNewThemeJLabel.setBounds(50,125,120,50);
		
		themePanel.add(selectThemeComboBox);
		themePanel.add(selectThemeJLabel);
		themePanel.add(installNewThemeJButton);
		themePanel.add(installNewThemeJLabel);
		
		//This settings array update will go in check box action listeners when implemented as seen above in general settings
		//settingsToWrite[10] = "0";
		/*************************************************/	
	
		//ActionListener to make the connect menu item connect
		selectThemeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event){
			try{
				UIManager.setLookAndFeel(selectThemeComboBox.getSelectedItem().toString());}catch(Exception e){e.printStackTrace();
				SwingUtilities.updateComponentTreeUI(preferences);
				preferences.pack();
				preferences.repaint();
				}
			}
		});
		
		//Mouse Listener for the options
		//MouseListener for the generalPreferences
	    	MouseListener mouseListenerGeneral = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				refreshSettingsView(generalLabel);
		}};
		
		//MouseListener for the notificationsPreferences
			MouseListener mouseListenerNotifications = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				refreshSettingsView(notificationsLabel);

		}};
		
		//MouseListener for the encryptionPreferences
			MouseListener mouseListenerEncryption = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {	
				refreshSettingsView(encryptionLabel);
		}};
		
		//MouseListener for the formattingPreferences
			MouseListener mouseListenerFormatting = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {	
				refreshSettingsView(formattingLabel);
		}};
		
		//MouseListener for the themePreferences
			MouseListener mouseListenerTheme = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				refreshSettingsView(themeLabel);
		}};
		
		
		//Add the mouseListeners to the Labels
		generalLabel.addMouseListener(mouseListenerGeneral);
		notificationsLabel.addMouseListener(mouseListenerNotifications);
		encryptionLabel.addMouseListener(mouseListenerEncryption);
		formattingLabel.addMouseListener(mouseListenerFormatting);
		themeLabel.addMouseListener(mouseListenerTheme);
		
		
		//Add the Drawing Panels to the LabelPane
		prefLabelPaneLeft.add(generalLabel);
		prefLabelPaneLeft.add(notificationsLabel);
		prefLabelPaneLeft.add(encryptionLabel);
		prefLabelPaneRight.add(formattingLabel);
		prefLabelPaneRight.add(themeLabel);
				
		//Add the JPanels to the ContentPane (set to default until Label Image is clicked)
		contentPane.add(notificationsPanel);
		contentPane.add(generalPanel);
		contentPane.add(encryptionPanel);
		contentPane.add(formattingPanel);
		contentPane.add(themePanel);
		contentPane.add(prefLabelPaneLeft);
		contentPane.add(prefLabelPaneRight);
		contentPane.add(apply);
		contentPane.add(cancel);

		
		//Initialize Frame
		preferences.setContentPane(contentPane);
		preferences.setVisible(true);


	}
	
	private void refreshSettingsView(JButton activeButton)
	{
		if(activeButton == generalLabel)
		{
			generalPanel.setVisible(true);
			generalLabel.setBackground(Color.black);
			generalLabel.setForeground(Color.white);
			notificationsPanel.setVisible(false);
			notificationsLabel.setBackground(originalColor);
			notificationsLabel.setForeground(Color.black);
			encryptionPanel.setVisible(false);
			encryptionLabel.setBackground(originalColor);
			encryptionLabel.setForeground(Color.black);
			formattingPanel.setVisible(false);
			formattingLabel.setBackground(originalColor);
			formattingLabel.setForeground(Color.black);
			themePanel.setVisible(false);
			themeLabel.setBackground(originalColor);
			themeLabel.setForeground(Color.black);
		}
		if(activeButton == notificationsLabel)
		{
			generalPanel.setVisible(false);
			generalLabel.setBackground(originalColor);
			generalLabel.setForeground(Color.black);
			notificationsPanel.setVisible(true);
			notificationsLabel.setBackground(Color.black);
			notificationsLabel.setForeground(Color.white);
			encryptionPanel.setVisible(false);
			encryptionLabel.setBackground(originalColor);
			encryptionLabel.setForeground(Color.black);
			formattingPanel.setVisible(false);
			formattingLabel.setBackground(originalColor);
			formattingLabel.setForeground(Color.black);
			themePanel.setVisible(false);
			themeLabel.setBackground(originalColor);
			themeLabel.setForeground(Color.black);
		}
		if(activeButton == encryptionLabel)
		{
			generalPanel.setVisible(false);
			generalLabel.setBackground(originalColor);
			generalLabel.setForeground(Color.black);
			notificationsPanel.setVisible(false);
			notificationsLabel.setBackground(originalColor);
			notificationsLabel.setForeground(Color.black);
			encryptionPanel.setVisible(true);
			encryptionLabel.setBackground(Color.black);
			encryptionLabel.setForeground(Color.white);
			formattingPanel.setVisible(false);
			formattingLabel.setBackground(originalColor);
			formattingLabel.setForeground(Color.black);
			themePanel.setVisible(false);
			themeLabel.setBackground(originalColor);
			themeLabel.setForeground(Color.black);
		}
		if(activeButton == formattingLabel)
		{
			generalPanel.setVisible(false);
			generalLabel.setBackground(originalColor);
			generalLabel.setForeground(Color.black);
			notificationsPanel.setVisible(false);
			notificationsLabel.setBackground(originalColor);
			notificationsLabel.setForeground(Color.black);
			encryptionPanel.setVisible(false);
			encryptionLabel.setBackground(originalColor);
			encryptionLabel.setForeground(Color.black);
			formattingPanel.setVisible(true);
			formattingLabel.setBackground(Color.black);
			formattingLabel.setForeground(Color.white);
			themePanel.setVisible(false);
			themeLabel.setBackground(originalColor);
			themeLabel.setForeground(Color.black);
		}
		if(activeButton == themeLabel)
		{
			generalPanel.setVisible(false);
			generalLabel.setBackground(originalColor);
			generalLabel.setForeground(Color.black);
			notificationsPanel.setVisible(false);
			notificationsLabel.setBackground(originalColor);
			notificationsLabel.setForeground(Color.black);
			encryptionPanel.setVisible(false);
			encryptionLabel.setBackground(originalColor);
			encryptionLabel.setForeground(Color.black);
			formattingPanel.setVisible(false);
			formattingLabel.setBackground(originalColor);
			formattingLabel.setForeground(Color.black);
			themePanel.setVisible(true);
			themeLabel.setBackground(Color.black);
			themeLabel.setForeground(Color.white);
		}
	}
	
	private void setGeneralSettings (boolean systemTrayFlag, boolean systemTrayVal, boolean allowESCFlag,
			boolean allowESCVal, boolean enableSpellCheckFlag, boolean enableSCVal) throws AWTException
	{
		if(systemTrayFlag)
		{	
			if(!(systemTrayVal))
			{
				Athena.clientResource.setSystemTrayIcon(false);
			}
			if(systemTrayVal)
			{
				Athena.clientResource.setSystemTrayIcon(true);
			}
		}
		if (allowESCFlag)
		{
			//Adjust setting
			if(!(allowESCVal))
			{
				Athena.clientResource.closeTabWithESC(false);
			}
			if(allowESCVal)
			{
				Athena.clientResource.closeTabWithESC(true);
			}
		}
		if (enableSpellCheckFlag)
		{
			//Adjust setting
			if(!(enableSCVal))
			{
				Athena.clientResource.setSpellCheck(false);
			}
			if(enableSCVal)
			{
				Athena.clientResource.setSpellCheck(true);
			}
		}
	}
	
	private void setNotificationSettings (boolean enableSoundsFlag, boolean enableSoundsVal)
	{
		if (enableSoundsFlag)
		{
			if(!(enableSoundsVal))
			{
				Athena.setEnableSounds(false);
			}
			if(enableSoundsVal)
			{
				Athena.setEnableSounds(true);
			}
		}
	}
	
	private void setFormattingSettings(boolean setFontFaceFlag, boolean setBoldFlag, boolean setItalicsFlag, boolean setUnderlineFlag, boolean setSizeFlag,
										String setFontFaceVal, boolean setBoldVal, boolean setItalicsVal, boolean setUnderlineVal, int setSizeVal)
	{
		if (setFontFaceFlag || setBoldFlag || setItalicsFlag || setUnderlineFlag || setSizeFlag)
		{
			Athena.clientResource.settingsLoaded = true;
			Athena.print.setTextFont(setFontFaceVal, setBoldVal, setItalicsVal, setUnderlineVal, setSizeVal);
			Athena.clientResource.setNewFontToLoad(setFontFaceVal, setBoldVal, setItalicsVal, setUnderlineVal, setSizeVal);
		}
	}
	
	private void writeSavedPreferences(Object[] settingsToWrite)
	{
		try {
			BufferedWriter outPref = new BufferedWriter(new FileWriter("./users/" + Athena.username + "/athena.conf"));
			
			//Write general settings
			outPref.write("[GENERAL]");
			outPref.newLine();
			outPref.write("allowSystemTray=" + settingsToWrite[0]);
			outPref.newLine();
			outPref.write("allowESCTab=" + settingsToWrite[1]);
			outPref.newLine();
			outPref.write("enableSpellCheck=" + settingsToWrite[2]);
			outPref.newLine();
			outPref.newLine();
			outPref.newLine();

			//Write notification settings
			outPref.write("[NOTIFICATIONS]");
			outPref.newLine();
			outPref.write("enableSounds=" + settingsToWrite[3]);
			outPref.newLine();
			outPref.newLine();
			outPref.newLine();
			
			//Write encryption settings
			outPref.write("[ENCRYPTION]");
			outPref.newLine();
			outPref.write(";");
			outPref.newLine();
			outPref.write(";");
			outPref.newLine();
			outPref.write("encryptionType=" + settingsToWrite[4]);
			outPref.newLine();
			outPref.newLine();
			outPref.newLine();
			
			//Write formatting settings
			outPref.write("[FORMATTING]");
			outPref.newLine();
			outPref.write("fontFace=" + settingsToWrite[5]);
			outPref.newLine();
			outPref.write("fontBold=" + settingsToWrite[6]);
			outPref.newLine();
			outPref.write("fontItalic=" + settingsToWrite[7]);
			outPref.newLine();
			outPref.write("fontUnderline=" + settingsToWrite[8]);
			outPref.newLine();
			outPref.write("fontSize=" + settingsToWrite[9]);
			outPref.newLine();
			outPref.newLine();
			outPref.newLine();
			
			//Write theme settings
			outPref.write("[THEME]");
			outPref.newLine();
			outPref.write("activeTheme=" + settingsToWrite[10]);
			
			outPref.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}