package net.java.otrkeybuilder;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	//variables
	JFileChooser chooser;
	String importedFile= "";
	static String publicKey = "";
	static String privateKey = "";
	JButton importBtn =new JButton("Import");
	JComboBox fingerPrintCbx = new JComboBox();

	//getters
	public String getImportedFile(){
		return importedFile;
	}
	
	
    /**
	 * Construct a Jdialog
	 */
	
    public KeyImporterDialog()
	{
    	super();
    	// special components for this inheriting class
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
        c.anchor = GridBagConstraints.PAGE_END; //bottom of space
   	    c.gridx = 2;       //aligned with button 2
   	    c.gridy = 5;       //third row
   	    add(importBtn, c);
   	    setVisible(false);
    	
    	//opening file chooser to import OTR key file
    	chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Import OTR keys file");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileHidingEnabled(false);  
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        	System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
        	System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
        	importedFile = chooser.getSelectedFile().toString();
        } else {
        	System.out.println("No Selection ");
        }
    
	}
    
	public void constructKeyPairs()
	{
		parseKeys(importedFile) ;
		    kp.construct(privateKey, publicKey);
		    String fp;
				fp = kp.getLocalFingerprint();
				System.out.println("fingerprint: \n"+fp);
				String fingerprint;
				try {
					fingerprint = new OtrCryptoEngineImpl().getFingerprint(kp.getKeyPair().getPublic());
					System.out.println("\n  Imported key fingerprint : " + fingerprint);
						fingerPrintCbx.addItem(fingerprint);
						setVisible(true);
				} catch (OtrCryptoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	

	}

	private static void parseKeys(String impFile)
	{
	   BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(impFile));
			 StringBuilder sb = new StringBuilder();
			    String line = br.readLine();

			    while (line != null) {
			    	if(line.contains("privateKey"))
			    			{
			    		privateKey = line.substring(line.indexOf("=")+1);
			    		 System.out.println("Private: \n"+line.substring(line.indexOf("=")+1));
			    			}
			    	else if(line.contains("publicKey"))
			    	{
			    		publicKey = line.substring(line.indexOf("=")+1);
			    		System.out.println("Public: \n" +line.substring(line.indexOf("=")+1));
			    	}
			    	sb.append(line);
			        sb.append("\n");
			        line = br.readLine();
			    }

			    br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

			
		    	 }
					//setVisible(false);
					 dispose();
	        }
	        
	    }); 
    	
	}
}

//

