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


public class KeyGeneratorDialog extends KeyManipDialog{

	/**
	 * Construct a Jdialog
	 */
	JButton generateBtn =new JButton("generate");
    public KeyGeneratorDialog()
	{
    	super();
    	   c.weightx=0;
    	    c.fill = GridBagConstraints.HORIZONTAL;
    	    c.ipadx = 0;
    	    c.ipady = 0;       //reset to default
    	    c.gridheight=0;
    	    c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    	    c.gridx = 2;       //aligned with button 2
    	    c.gridy = 5;       //third row
    	    add(generateBtn, c);
	}
	public void generateKeyFormats()
	{
	    this.generateBtn.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent arg0)
	        { 
	        	
	    		try {
	    			int i = 0;
	    			if(!accountsArray.isEmpty()){
	    			while(i<accountsArray.size()){
	    				i++;
	    			}
	    			
	    			keyPair = kp.generateKeyPair();
	    			}
	    			System.out.println("La taille de l'array List est :"+accountsArray.size());
	    			 for(String acc: accountsArray)
	    	              
	    	        	{
//	    			    	CheckBoxNode acc = accountsArray.get(i);
	    			    	System.out.println("============\n+"+acc+"=====================\n");
	    	        	
	    	        		
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
	    			 
	    		} catch (OtrCryptoException e) {
	    			// TODO Auto-generated catch block
	    			System.out.print("Error in key generation");
	    			e.printStackTrace();
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

}

//

