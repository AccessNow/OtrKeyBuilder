package net.java.otrkeybuilder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
public abstract class KeyManipDialog extends JDialog{

	JButton addAccountBtn =new JButton("add account");
	JComboBox network = new JComboBox();
	JTextField accountFld = new JTextField();
    JTree tree;
    DefaultMutableTreeNode accountsNode = new DefaultMutableTreeNode("My Accounts");
    OtrKey kp = new OtrKey();
    pidgin pidFile = new pidgin();
	Jitsi jitsiFile = new Jitsi();
	final DefaultMutableTreeNode googleTalk = new DefaultMutableTreeNode("Google_Talk");
	final DefaultMutableTreeNode facebook = new DefaultMutableTreeNode("Facebook");
	final DefaultMutableTreeNode icq = new DefaultMutableTreeNode("ICQ");
    final DefaultMutableTreeNode yahoo = new DefaultMutableTreeNode("Yahoo");	
    JScrollPane treePnl ;
    ArrayList<String> accountsArray = new ArrayList<String>();
    Map<String, String> typeAccount= new HashMap<String, String>() ;
	protected KeyPair keyPair;
	protected String pidginFormat ;
	protected String jitsiFormat ;
	GridBagConstraints c ;
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

        accountsNode.add(googleTalk);
		  accountsNode.add(facebook);
		  accountsNode.add(icq);
		  accountsNode.add(yahoo);
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

	          	accountsArray.add(accountFld.getText());

	          	typeAccount.put(accountsArray.get(accountsArray.size()-1), network.getSelectedItem().toString());
		        nNode = new DefaultMutableTreeNode(accountsArray.get(accountsArray.size()-1));
		        path = tree.getNextMatch("M", 0, Position.Bias.Forward);
		        
		        //node = (MutableTreeNode)path.getLastPathComponent();
		        if (network.getSelectedItem().toString()=="Google_Talk"){node = googleTalk; }
		        else if (network.getSelectedItem().toString()=="Facebook"){node = facebook;}
		        else if (network.getSelectedItem().toString()=="ICQ"){node = icq;}
		        else if (network.getSelectedItem().toString()=="Yahoo"){node = yahoo;}
		        
		        model.insertNodeInto(nNode, node, node.getChildCount());
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

//

