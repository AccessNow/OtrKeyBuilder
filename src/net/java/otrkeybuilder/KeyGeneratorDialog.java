package net.java.otrkeybuilder;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.swing.*;

import net.java.otr4j.crypto.OtrCryptoException;

/**
 * This class inherits from <tt>KeyManipDialog<tt> 
 * @see net.java.otrkeybuilder.KeyManipDialog
 * @author Mohamed Akram Tabka <tabkram@gmail.com>
 *
 */
@SuppressWarnings("serial")
public class KeyGeneratorDialog extends KeyManipDialog{

	/**
	 * Construct a Jdialog
	 */
	JButton generateBtn =new JButton("generate");
    public KeyGeneratorDialog()
	{
    	
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
	    			if(!accountsMap.isEmpty()){
	    			while(i<accountsMap.size()){
	    				i++;
	    			}
	    			keyPair = kp.generateKeyPair();
	    			}
	    			HashMap<String, String>  parm = kp.parameterParser();
	    			HashMap<String, byte[]>  parm1 = kp.encodeJitsi();
	    			
	    			System.out.println("key generated for accountsMap.size() accounts : \n");
	    			
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
			    	 }
	    		} catch (OtrCryptoException e) {
	    			// TODO Auto-generated catch block
	    			System.out.print("Error in key generation");
	    			e.printStackTrace();
	    		}     
	    		dispose();
	    		
	        }
	        
	    });
	    
    	
	}

}

//

