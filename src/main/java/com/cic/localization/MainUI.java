package com.cic.localization;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Map;

import com.cloudgarden.layout.AnchorConstraint;
import com.cloudgarden.layout.AnchorLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.SwingUtilities;


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
public class MainUI extends javax.swing.JFrame implements LocationEventListener{
	private JLabel jLabel1;
	private JMenu jMenu1;
	private JMenuBar jMenuBar1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JMenu jMenu3;
	private JMenu jMenu2;
	private DecimalFormat df = new DecimalFormat("#0.00");
	private JTable jTable2;
	private JTextArea jTextArea1;
	private JTable jTable1;
	private LocationChart lChart;
	/**
	* Auto-generated main method to display this JFrame
	*/
		
	public MainUI() {
		super();
		initGUI();
		setCloseListener();
		//initChartPanel();
	}
	private String truncateDouble(double d)
	{
		return df.format(d);
	}
	
	/*private void initChartPanel()
	{
		lChart=new LocationChart();
		jPanel1.add(lChart.getChartPanel());
	}*/
	
	private void setCloseListener()
	{
		addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent e) {
			    int a = JOptionPane.showConfirmDialog(null, "Ready to shutdown?", "Warning",
			      JOptionPane.YES_NO_OPTION);
			    if (a == 0) {  
			     System.exit(0);  //关闭
			    }
			   } 
			  });
	}
	
	private void initGUI() {
		try {
			AnchorLayout thisLayout = new AnchorLayout();
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Localization System");
			{
				lChart=new LocationChart();
				//lChart.getChartPanel();

				//jPanel1 = new JPanel();
				getContentPane().add(lChart.initPanel(), new AnchorConstraint(17, 803, 993, 4, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				//jPanel1.setPreferredSize(new java.awt.Dimension(812, 690));
				//jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				jTextArea1 = new JTextArea();
				getContentPane().add(jTextArea1, new AnchorConstraint(806, 1000, 1000, 815, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jTextArea1.setPreferredSize(new java.awt.Dimension(188, 137));
				jTextArea1.setEditable(false);
				jTextArea1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			}
			{
				TableModel jTable1Model = 
						new DefaultTableModel(
								new String[][] { { "1", "Unknown" }, },
								new String[] { "TagID", "Location" });
				jTable1 = new JTable();
				JScrollPane scrollPane = new JScrollPane(jTable1);
				getContentPane().add(scrollPane, new AnchorConstraint(78, 1000, 365, 815, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jTable1.setModel(jTable1Model);
				
				jTable1.setPreferredSize(new java.awt.Dimension(188, 203));
				jTable1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				jTable1.setUpdateSelectionOnSort(false);
				jTable1.setVerifyInputWhenFocusTarget(false);
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new AnchorConstraint(26, 969, 53, 815, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jLabel1.setText("Current Location");
				jLabel1.setPreferredSize(new java.awt.Dimension(157, 19));
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2, new AnchorConstraint(753, 967, 782, 815, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jLabel2.setText("Message Log");
				jLabel2.setPreferredSize(new java.awt.Dimension(154, 21));
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3, new AnchorConstraint(405, 968, 432, 815, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jLabel3.setText("Temperature");
				jLabel3.setPreferredSize(new java.awt.Dimension(155, 19));
			}
			{
				TableModel jTable2Model = 
						new DefaultTableModel(
								new String[][] { { "1", "Unknown" } },
								new String[] { "TagID", "Temperature" });
				jTable2 = new JTable();
				JScrollPane scrollPane = new JScrollPane(jTable2);
				getContentPane().add(scrollPane, new AnchorConstraint(449, 1000, 736, 815, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL, AnchorConstraint.ANCHOR_REL));
				jTable2.setModel(jTable2Model);
				jTable2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
				jTable2.setPreferredSize(new java.awt.Dimension(188, 203));
			}
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("File");
				}
				{
					jMenu2 = new JMenu();
					jMenuBar1.add(jMenu2);
					jMenu2.setText("Setting");
				}
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("About");
				}
			}
			pack();
			this.setSize(1024, 768);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

	public void onLocationChange(final int id, final Point2D p, final double angle, boolean onDangerousZone) {
		lChart.updateLocation(id, p,onDangerousZone);
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		    	String location=truncateDouble(p.getX())+","+truncateDouble(p.getY())+","+angle;
		    	jTextArea1.append("Location:("+location+") TagID:"+id+"\n");
		    	DefaultTableModel dm = (DefaultTableModel) jTable1.getModel();
		    	int rowCount=dm.getRowCount();
		    	for (int i = 0;i<rowCount;i++) {
		    	    dm.removeRow(i);
		    	}
		    	Object []data={String.valueOf(id),location};
		    	dm.insertRow(0, data);
		    }
		});
		
	}

	

	public void onVoltageChange(double voltage) {
		
		
	}

	public void onTemperatureChange(double temperature) {
		// TODO Auto-generated method stub
		
	}

	


}
