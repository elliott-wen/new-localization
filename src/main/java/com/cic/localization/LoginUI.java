package com.cic.localization;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class LoginUI extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JTextField jTextField1;
	private JButton jButton1;
	private JButton jButton2;
	private JLabel jLabel2;
	private JTextField jTextField2;
	private JLabel Password;
	private JLabel jLabel1;

	/**
	* Auto-generated main method to display this JFrame
	*/
	
	
	public LoginUI() {
		super();
		initGUI();
		setCloseListener();
	}
	
	private void setCloseListener()
	{
		addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent e) {
			    int a = JOptionPane.showConfirmDialog(null, "Are you going to quit？", "Warning",
			      JOptionPane.YES_NO_OPTION);
			    if (a == 0) {  
			     System.exit(0);  //关闭
			    }
			   } 
			  });
	}
	
	private void initGUI() {
		try {
			getContentPane().setLayout(null);
			this.setMinimumSize(new java.awt.Dimension(640, 480));
			this.setMaximumSize(new java.awt.Dimension(640, 480));
			this.setTitle("Localization System");
			{
				jTextField1 = new JTextField();
				getContentPane().add(jTextField1);
				jTextField1.setBounds(230, 208, 285, 29);
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText("Username");
				jLabel1.setBounds(118, 213, 64, 18);
			}
			{
				Password = new JLabel();
				getContentPane().add(Password);
				Password.setText("Password");
				Password.setBounds(118, 282, 64, 18);
			}
			{
				jTextField2 = new JTextField();
				getContentPane().add(jTextField2);
				jTextField2.setBounds(230, 277, 285, 29);
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1);
				jButton1.setText("Login");
				jButton1.setBounds(128, 358, 153, 37);
				jButton1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						
						dispose();
						MainUI mainUI=new MainUI();
						mainUI.setVisible(true);
					}
				});
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2);
				jLabel2.setBounds(0, 0, 632, 177);
				jLabel2.setIcon(new ImageIcon(getClass().getClassLoader().getResource("loginbg.jpg")));
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2);
				jButton2.setText("Reset");
				jButton2.setBounds(319, 358, 153, 37);
			}
			pack();
			this.setSize(640, 480);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
