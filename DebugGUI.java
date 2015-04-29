package Debug;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import Login.LoginWindow;
import Shared.Communicator.DatabaseCommunicator;
import Shared.Gradients.GradientButton;
import Shared.Gradients.GradientPanel;

import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
/**
 * Direct user interaction with the database.
 * 
 * @author Samuel Baysting
 * @tester Samuel Baysting
 * @debugger Samuel Baysting
 * 
 */

public class DebugGUI extends JFrame implements ActionListener{
	
	DatabaseCommunicator d = new DatabaseCommunicator();
	
	private JPanel rootPanel;
	private GradientPanel backgroundPanel;
	private JPanel titlePanel;
	private JLabel titleLabel;
	private JLabel dateAndTime;
	private Timer timer;
	private GradientButton logoutButton;
	private JTextField consoleInput;
	private JPanel consoleInputBackground;
	private JTextArea consoleOutput;
	private JLabel lblMysqlTerminal;
	private JLabel lblConsoleOutput;
	private JLabel lblConsoleInput;
	private JLabel lblMysql;
	private JScrollPane consoleScroll;
	
	public DebugGUI()
	{
		super();
		init();
		
		addWindowListener(new WindowAdapter() // To open main window again if you hit the corner "X"
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                new LoginWindow();
                d.disconnect();
                dispose();
            }
        });
	}
	
	public void init()
	{
		this.setTitle("Debug");
		this.setResizable(true);
		this.setSize(1200,700);
		this.frameManipulation();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		this.setResizable(false);
		getContentPane().add(rootPanel);
		this.setVisible(true);
	}
	
	public void frameManipulation()
	{
		setBackgroundPanel();
		setTitlePanel();
		//setButtonPanel();
		//setStatusPanel();
		//setPaymentPanel();
		//setNumberPad();
		setRootPanel();
	}
	
	private void setRootPanel()
	{
		rootPanel = new JPanel();
		rootPanel.setLayout(null);
		rootPanel.add(titlePanel);
		//rootPanel.add(buttonPanel);
		//rootPanel.add(statusPanel);
		//rootPanel.add(paymentPanel);
		rootPanel.add(backgroundPanel);
	}
	
	private void setBackgroundPanel()
	{
		// Create Background Panel
		backgroundPanel = new GradientPanel();
		backgroundPanel.setGradient(Color.white,new Color(46,184,0));
		//backgroundPanel.setBrightness(backgroundPanel.getColor2(),1);
		backgroundPanel.setLayout(null);
		backgroundPanel.setBounds(0,0,1200,700);
		
		logoutButton = new GradientButton("LOGOUT");
		logoutButton.setBounds(187, 550, 825, 66);
		logoutButton.setFocusPainted(false);
		logoutButton.setFont(logoutButton.getFont().deriveFont(16.0f));
		backgroundPanel.add(logoutButton);
		
		consoleOutput = new JTextArea();
		consoleOutput.setLineWrap(true);
		consoleOutput.setBounds(85, 298, 347, 177);
		consoleOutput.setBorder(BorderFactory.createLineBorder(Color.black));
		consoleOutput.setBackground(Color.WHITE);
		consoleOutput.setEditable(false);
		
		consoleScroll = new JScrollPane(consoleOutput);
	    consoleScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    consoleScroll.getVerticalScrollBar().setUnitIncrement(16);
	    consoleScroll.setBounds(new Rectangle(85, 236, 484, 270));
	    consoleScroll.setBackground(Color.white);
	    consoleScroll.setBorder(BorderFactory.createLineBorder(Color.black));
		backgroundPanel.add(consoleScroll);
		
		lblMysqlTerminal = new JLabel("MySQL Terminal");
		lblMysqlTerminal.setHorizontalAlignment(SwingConstants.CENTER);
		lblMysqlTerminal.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblMysqlTerminal.setBounds(85, 82, 484, 39);
		backgroundPanel.add(lblMysqlTerminal);
		
		lblConsoleOutput = new JLabel("Console Output:");
		lblConsoleOutput.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblConsoleOutput.setBounds(85, 200, 347, 39);
		backgroundPanel.add(lblConsoleOutput);
		
		lblConsoleInput = new JLabel("Console Input:");
		lblConsoleInput.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblConsoleInput.setBounds(85, 118, 347, 30);
		backgroundPanel.add(lblConsoleInput);
		
		lblMysql = new JLabel("mysql > ");
		lblMysql.setBackground(Color.WHITE);
		lblMysql.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMysql.setBounds(85, 150, 73, 39);
		backgroundPanel.add(lblMysql);
		
		consoleInput = new JTextField();
		consoleInput.setBorder(BorderFactory.createEmptyBorder());
		consoleInput.setBounds(168, 150, 401, 39);
		consoleInput.setColumns(10);
		consoleInput.setOpaque(false);
		consoleInput.addKeyListener(new KeyListener(){
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==10){
					String terminalInput = consoleInput.getText();
					consoleInput.setText("");
					sqlMessage(terminalInput);
					consoleScroll.getVerticalScrollBar().setValue(0);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}	
		});
		backgroundPanel.add(consoleInput);
		
		consoleInputBackground = new JPanel();
		consoleInputBackground.setBorder(new LineBorder(new Color(0, 0, 0)));
		consoleInputBackground.setBackground(Color.WHITE);
		consoleInputBackground.setBounds(85, 150, 484, 39);
		backgroundPanel.add(consoleInputBackground);
		logoutButton.addActionListener(this);
		
		backgroundPanel.setVisible(true);
		
		if(d.connect("admin","gradMay17")!=0){
			consoleOutput.setText("Database connection failed!");
			return;
		}
		else{
			consoleOutput.setText("Database connection successful! Ready to use terminal.");
		}
	}
	
	private void setTitlePanel()
	{
		// Create Title Panel
		titlePanel = new JPanel();
		titlePanel.setLayout(null);
		titlePanel.setOpaque(false);
		titlePanel.setBounds(new Rectangle(0, 0, 1200, 56));
		// Set Title
		titleLabel = new JLabel("Debug Interface");
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setFont(titleLabel.getFont().deriveFont(38f));
		titleLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		titleLabel.setBounds(new Rectangle(0, 0, 793, 56));
		// Create a timer to update the clock
		timer = new Timer(500,this);
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.addActionListener(this);
        timer.start();

		// Add components to Title Panel
		titlePanel.add(titleLabel);
		// Set Date and Time
		dateAndTime = new JLabel();
		dateAndTime.setBounds(792, 0, 402, 56);
		titlePanel.add(dateAndTime);
		dateAndTime.setHorizontalAlignment(JLabel.CENTER);
		dateAndTime.setFont(dateAndTime.getFont().deriveFont(28f));
		dateAndTime.setBorder(BorderFactory.createLineBorder(Color.black));
		
		titlePanel.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object a = e.getSource();
		
		if(a == timer)
		{
			updateClock();
		}
		if(a == logoutButton)
		{
			d.disconnect();
			new LoginWindow();
			dispose();
		}
		
	}
	
	/**
	 * This is the function that updates the clock. Is called every half of a second by
	 * the action listener
	 * 
	 * @returns none
	 * 
	 **/
	
	private void updateClock() {
        dateAndTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
    }

	
	private void sqlMessage(String input)
	{
		if(input.toUpperCase().contains("INSERT") || input.toUpperCase().contains("DELETE") || input.toUpperCase().contains("UPDATE") ||
				input.toUpperCase().contains("DROP") || input.toUpperCase().contains("CREATE") || input.toUpperCase().contains("ALTER")){
		LinkedList l = d.updateGetStrings(input);
		int rs = (Integer) l.get(0);
		String s = (String) l.get(1);
		
		consoleOutput.setText("");
		
		consoleOutput.setText(consoleOutput.getText() + "mysql > " + input + "\n\n");
		
			consoleOutput.setText(consoleOutput.getText() + "Return code: " + rs + "\n");
			consoleOutput.setText(consoleOutput.getText() + s);		
		
		}
		else{
			LinkedList l = d.tellGetStrings(input);
			ResultSet rs = (ResultSet) l.get(0);
			String s = (String) l.get(1);
			
			consoleOutput.setText("");
			
			consoleOutput.setText(consoleOutput.getText() + "mysql > " + input + "\n\n");
			
			try{
				
				if(rs == null){
					consoleOutput.setText(consoleOutput.getText() + s);
					return;
				}
				
				ResultSetMetaData rsd = rs.getMetaData();
			    int colsize = rsd.getColumnCount();
			    
			    do{
			    	for(int i = 1; i <= colsize; i++){
			    		String r = rs.getString(i);
			    		consoleOutput.setText(consoleOutput.getText() + rsd.getColumnName(i) + "  ");
			    		consoleOutput.setText(consoleOutput.getText() + r + "  \n");
			    	}
			    	consoleOutput.setText(consoleOutput.getText() + "\n"); 
			    }while(rs.next());
			    
				}
			    catch(SQLException sqlEx){
			    	 //Error Handler
			    	consoleOutput.setText(consoleOutput.getText() + "SQLException: " + sqlEx.getMessage() + "\n");
			    	consoleOutput.setText(consoleOutput.getText() + "SQLState: " + sqlEx.getSQLState() + "\n");
			    	consoleOutput.setText(consoleOutput.getText() + "VendorError: " + sqlEx.getErrorCode() + "\n");
			    }
		}
	}
}
