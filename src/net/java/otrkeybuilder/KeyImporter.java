package net.java.otrkeybuilder;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import javax.swing.JFileChooser;
import net.java.otr4j.crypto.OtrCryptoEngineImpl;
import net.java.otr4j.crypto.OtrCryptoException;

public class KeyImporter {
	JFileChooser chooser;
	String importedFile= "";
	static String publicKey = "";
	static String privateKey = "";
	static KeyImporterDialog  kid ;
    OtrKey key = new OtrKey();

	public KeyImporter(){
    	chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
          System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
          importedFile = chooser.getSelectedFile().toString();
          //srcPathLbl.setText(isoPath);
          
        } else {
          System.out.println("No Selection ");
        }
	}
	public void print() throws IOException, InvalidKeySpecException, OtrCryptoException
	{
		String content = readFile(importedFile) ;
		//System.out.println(content);
		content.indexOf("=");
		    key.construct(privateKey, publicKey);
		    String fp = key.getLocalFingerprint();
			System.out.println("fingerprint: \n"+fp);
			
				System.out.println("\n  cle importée : " + new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));
	            
			kid = new KeyImporterDialog(key);
			kid.start();
            kid.generateKeyFormats();
            key = kid.getKey();
            

	}

	public OtrKey getKey()
	{	
		return key;
	}
	public static String readFile(String impFile) throws IOException
	{
	   BufferedReader br = new BufferedReader(new FileReader(impFile));
	try {
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
	    String everything = sb.toString();
	    return everything ;
	} finally {
	    br.close();
	}
	}
}
