package net.java.otrkeybuilder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
/**
 * This is an abstract class that inherits from <tt>JDialog<tt> 
 * it provides a general set of two JDialogs : <tt>KeyImporterDialog</tt> and <tt>KeyGeneratorDialog</tt>
 * @see JDialogs
 * @see net.java.otrkeybuilder.KeyImporterDialog
 * @see net.java.otrkeybuilder.KeyGeneratorDialog
 * @author Mohamed Akram Tabka <tabkram@gmail.com>
 */
@SuppressWarnings("serial")
public abstract class KeyManipDialog extends JDialog{
   
    OtrKey kp = new OtrKey();
    pidgin pidFile = new pidgin();
	Jitsi jitsiFile = new Jitsi();

    Map<String, String> accountsMap= new HashMap<String, String>() ;
	protected KeyPair keyPair;
	protected String pidginFormat ;
	protected String jitsiFormat ;

	// display and components declaration
	JButton addAccountBtn =new JButton("add account");
	JComboBox network = new JComboBox();
	JTextField accountFld = new JTextField();
	GridBagConstraints c ;

	// tree variables declaration
    JTree tree;
    DefaultMutableTreeNode accountsNode = new DefaultMutableTreeNode("My Accounts");
	final DefaultMutableTreeNode googleTalk = new DefaultMutableTreeNode("Google_Talk");
	final DefaultMutableTreeNode facebook = new DefaultMutableTreeNode("Facebook");
	final DefaultMutableTreeNode icq = new DefaultMutableTreeNode("ICQ");
    final DefaultMutableTreeNode yahoo = new DefaultMutableTreeNode("Yahoo");	
    JScrollPane treePnl ;
    public static DefaultMutableTreeNode nNode;
    public static MutableTreeNode node;
    public static DefaultTreeModel model;
    public static TreePath path;
    
	/**
	 * Construct a Jdialog
	 */
    public KeyManipDialog()
	{
    	setModal(true);
		network.addItem("Google_Talk");
		network.addItem("Facebook");
		network.addItem("ICQ");
		network.addItem("Yahoo");

//        accountsNode.add(googleTalk);
//		  accountsNode.add(facebook);
//		  accountsNode.add(icq);
//		  accountsNode.add(yahoo);
		  tree = new JTree(accountsNode);

	      //  tree.setRootVisible(false);

	         treePnl = new JScrollPane(tree);
	        //--------------

	//	setVisible(true);
		displayDialog();
	}
	public void start()
	{
		this.addAccountBtn.addActionListener(new ActionListener()
	    {
			
	        public void actionPerformed(ActionEvent arg0)
	        { 
	        	if (!accountFld.getText().equals(""))
	        	{
	        	model = (DefaultTreeModel)tree.getModel();

	          	accountsMap.put(accountFld.getText(), network.getSelectedItem().toString());
	          	
		        nNode = new DefaultMutableTreeNode(accountFld.getText());
		        path = tree.getNextMatch("M", 0, Position.Bias.Forward);  
		        //node = (MutableTreeNode)path.getLastPathComponent();
		        if (network.getSelectedItem().toString()=="Google_Talk"){
		        	if(accountsNode.getIndex(googleTalk)==-1){
		        		accountsNode.add(googleTalk);
		        	}
		        	node = googleTalk;
		   
		        	}
		        else if (network.getSelectedItem().toString()=="Facebook"){
		        	if(accountsNode.getIndex(facebook)==-1){
		        		accountsNode.add(facebook);
		        	}
		        	node = facebook;
		        }
		        else if (network.getSelectedItem().toString()=="ICQ"){
		         	if(accountsNode.getIndex(icq)==-1){
		        		accountsNode.add(icq);
		        	}
		        	node = icq;
		        }
		        else if (network.getSelectedItem().toString()=="Yahoo"){
		        	if(accountsNode.getIndex(yahoo)==-1){
		        		accountsNode.add(yahoo);
		        	}
		        	node = yahoo;
		        }
		        
		        model.insertNodeInto(nNode, node, node.getChildCount());
		        tree.updateUI();
		        for (int i = 0; i < tree.getRowCount(); i++) {
		        	tree.expandRow(i);
		        	} 
		        accountFld.setText("");
	        	}

	        }
	    });	    			
	}

	public OtrKey getKey()
	{
		setVisible(true);
		return kp;
	}
protected void displayDialog(){
	setTitle("Generator"); 
	setSize(420, 225);
	setResizable(false); 
	setLocationRelativeTo(null);
    setLayout(new GridBagLayout());
     c = new GridBagConstraints();
    //c.insets = new Insets(0, 0, 0,0);
    //c.weightx = 0.1;
  
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridwidth=1;
    c.ipady = 30;
    c.gridx = 0;
    c.gridy = 1;
    add(new JLabel("<html><pre>Please select the network\nand write down your account !</pre></html>"), c);

    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.ipady = 0;
    c.gridx= 0;
    c.gridy= 2;
    add(network,c);
    
   c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx= 0;
    c.gridy= 3;
    add(accountFld,c);
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx= 0;
    c.gridy= 4;
    add(addAccountBtn,c);
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.ipady = 130;      //make this component tall
    c.ipadx = 170;
    c.gridx = 2;
    c.gridy = 1;
    c.gridheight=4;
    treePnl.setBorder(BorderFactory.createLoweredBevelBorder());
   add(treePnl, c);
}
}

