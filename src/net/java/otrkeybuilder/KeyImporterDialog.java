package net.java.otrkeybuilder;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.*;

import net.java.otr4j.crypto.OtrCryptoEngineImpl;
import net.java.otr4j.crypto.OtrCryptoException;

/**
 * This class inherits from <tt>KeyManipDialog<tt> 
 * @see net.java.otrkeybuilder.KeyManipDialog
 * @author Mohamed Akram Tabka <tabkram@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class KeyImporterDialog extends KeyManipDialog{

	/**
	 * Construct a Jdialog
	 */
	
	JButton importBtn =new JButton("Import");
	JComboBox fingerPrintCbx = new JComboBox();
    public KeyImporterDialog(OtrKey k1)
	{
    	super();
    	setTitle("Importer"); 
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
				if(!accountsMap.isEmpty()){
				while(i<accountsMap.size()){
					i++;
				}
				}
				HashMap<String, String>  parm = kp.parameterParser();
				HashMap<String, byte[]>  parm1 = kp.encodeJitsi();
				System.out.println("key imported for accountsMap.size() accounts : \n");
				//--
				Set<Entry<String, String>> cles = accountsMap.entrySet();
		    	 for(Entry<String, String> entry : cles) {
		    		 String key = entry.getKey();
		    		 String value = entry.getValue();
		    		 
		    		 System.out.println("<"+key+","+value+">\n");
		    		 
					
						pidginFormat = pidFile.storePrivateKey(parm,accountsMap.get(key),key);
						kp.setPidginFormat(pidginFormat);
					
						jitsiFormat = jitsiFile.storeGeneratedKey(parm1.get("pub"),parm1.get("priv"),accountsMap.get(key),key);
						kp.setJitsiFormat(jitsiFormat);
						kp.setAccountArray(accountsMap);
						setVisible(false);
						 dispose();
			
		    	 }
	        	
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

