package net.java.otrkeybuilder;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.*;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import net.java.otr4j.crypto.OtrCryptoEngineImpl;
import net.java.otr4j.crypto.OtrCryptoException;


public class KeyImporterDialog extends KeyManipDialog{

	/**
	 * Construct a Jdialog
	 */

	JButton importBtn =new JButton("Import");
	JComboBox fingerPrintCbx = new JComboBox();
    public KeyImporterDialog(OtrKey k1)
	{
    	super();
    	setSize(420, 250);

    	   c.fill = GridBagConstraints.HORIZONTAL;
    	   c.ipadx = 0;
   	    c.ipady = 0; 
    	     c.gridx = 0;
    	     c.gridy = 0;
    	     c.gridwidth=3;
    	     c.gridheight=1;
    	     add(fingerPrintCbx,c);
    	     add(new JLabel("Please select the network\nand write down your account !"), c);
    	   c.weightx=0;
    	    c.fill = GridBagConstraints.HORIZONTAL;
    	    c.ipadx = 0;
    	    c.ipady = 0;       //reset to default
    	    c.gridheight=0;
    	    try {
    	  kp = k1 ;
				fingerPrintCbx.addItem(new OtrCryptoEngineImpl().getFingerprint(kp.getKeyPair().getPublic()));
			} catch (OtrCryptoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	    c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    	    c.gridx = 2;       //aligned with button 2
    	    c.gridy = 5;       //third row
    	    add(importBtn, c);
    	    
    	    
	}
	public void generateKeyFormats()
	{
	    this.importBtn.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent arg0)
	        { 
	        	
	    		int i = 0;
				if(!accountsArray.isEmpty()){
				while(i<accountsArray.size()){
					i++;
				}
//	    			if (accountsArray.get(i).isSelected())
//	    			{
//	    				keyPair = kp.generateKeyPair();
//	    			}
//	    			else
//	    			{
//	    				System.out.println("no selected");
//	    			}
				}
				System.out.println("La taille de l'array List est :"+accountsArray.size());
				 for(String acc: accountsArray)
				      
					{
//	    			    	CheckBoxNode acc = accountsArray.get(i);
				    	System.out.println("============\n+"+acc+"=====================\n");
					
				    	//if (acc.isSelected())
					{
						
					System.out.println("============\n+"+typeAccount.get(acc)+"=====================\n");
						
					// keyList.add(account,typeaccount,KeyPair);
						
						//HashMap<String, String>  parm= parameterParser(keyPair);
					HashMap<String, String>  parm = kp.parameterParser();
						pidginFormat = pidFile.storePrivateKey(parm,typeAccount.get(acc),acc);
						kp.setPidginFormat(pidginFormat);
				//		HashMap<String, byte[]>  parm1 = encodeJitsi(keyPair);
						HashMap<String, byte[]>  parm1 = kp.encodeJitsi();
						jitsiFormat = jitsiFile.storeGeneratedKey(parm1.get("pub"),parm1.get("priv"),typeAccount.get(acc),acc);
						kp.setJitsiFormat(jitsiFormat);
						kp.setAccountArray(typeAccount);
						setVisible(false);
						 dispose();
					}
					}
	    		
//			    System.out.println("--------------");
//				System.out.println(" p:  #"+parm.get("p")+"#");
//				System.out.println(" q:  #"+parm.get("q")+"#");
//				System.out.println(" g:  #"+parm.get("g")+"#");
//				System.out.println(" y:  #"+ parm.get("y")+"#");
//				System.out.println(" x:  #"+ parm.get("x")+"#");
	        	
	        }
	        
	    });
	    
    	
	}

	public static KeyPair generateKeyPair() throws OtrCryptoException
	{
	    
//		if (accountID == null)
//	        return null;
	    
	    KeyPair keyPair;
	    try
	    {
	        keyPair = KeyPairGenerator.getInstance("DSA").genKeyPair();
			System.out.println("\n  cle generee : " + new OtrCryptoEngineImpl().getFingerprint(keyPair.getPublic()));
	        return keyPair ;
	    }
	    catch (NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}

}

//

