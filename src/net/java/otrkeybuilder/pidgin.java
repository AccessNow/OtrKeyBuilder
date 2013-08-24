package net.java.otrkeybuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

//import org.apache.commons.codec.binary.Base64;

public class pidgin {
	public String storePrivateKey(HashMap<String, String> parm, String network,String accountID) 
{
	System.out.println("writing into Pidgin format...");
	if (accountID == null)
        return null;
	String p= "00"+parm.get("p").toUpperCase();
	String q="00"+parm.get("q").toUpperCase();
	String g=parm.get("g").toUpperCase();
	String x=parm.get("x").toUpperCase();
	String y=parm.get("y").toUpperCase();
	String format =new String("");
	try {
		 
	//	String JitsiHeader = "net.java.sip.communicator.plugin.otr.";

		File file = new File("./keys/purple/otr.private_key");
		file.getParentFile().mkdirs();
		//File file = new File("/home/akram/.purple/otr.private_key");
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		   BufferedReader br = new BufferedReader(new FileReader(file));
		   String line = br.readLine();
		   FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
		   if((line != null)&&(line.contains("(privkeys")))
		   {
			   bw.write("(privkeys\n");
				if(network=="Google_Talk"){
					bw.write(" (account\n(name \""+accountID+"/\")\n(protocol prpl-jabber)\n(private-key\n (dsa\n");
					}
					else if(network=="Facebook"){
						bw.write(" (account\n(name \""+accountID+"@chat.facebook.com"+"/\")\n(protocol prpl-jabber)\n(private-key\n (dsa\n");
					}
					else if(network=="ICQ")
					{
						bw.write(" (account\n(name \""+accountID+"\")\n(protocol prpl-icq)\n(private-key\n (dsa\n");
					}
					else if(network=="Yahoo")
					{
						bw.write(" (account\n(name \""+accountID+"\")\n(protocol prpl-yahoo)\n(private-key\n (dsa\n");
					}
					bw.write("  (p #"+p+"#)\n");
					bw.write("  (q #"+q+"#)\n");
					bw.write("  (g #"+g+"#)\n");
					bw.write("  (y #"+y+"#)\n");
					bw.write("  (x #"+x+"#)\n");
					//bw.write("  )\n )\n )\n)");
					bw.write("  )\n )\n )\n");
					
					line = br.readLine();
					while (line != null) {
				    	bw.write(line+"\n");
				        line = br.readLine();
				    }
					
		   }
		   else
		   {
			   bw.write("(privkeys\n");
				if(network=="Google_Talk"){
					bw.write(" (account\n(name \""+accountID+"/\")\n(protocol prpl-jabber)\n(private-key\n (dsa\n");
					}
					else if(network=="Facebook"){
						bw.write(" (account\n(name \""+accountID+"@chat.facebook.com"+"/\")\n(protocol prpl-jabber)\n(private-key\n (dsa\n");
					}
					else if(network=="ICQ")
					{
						bw.write(" (account\n(name \""+accountID+"\")\n(protocol prpl-icq)\n(private-key\n (dsa\n");
					}
					else if(network=="Yahoo")
					{
						bw.write(" (account\n(name \""+accountID+"\")\n(protocol prpl-yahoo)\n(private-key\n (dsa\n");
					}
					bw.write("  (p #"+p+"#)\n");
					bw.write("  (q #"+q+"#)\n");
					bw.write("  (g #"+g+"#)\n");
					bw.write("  (y #"+y+"#)\n");
					bw.write("  (x #"+x+"#)\n");
					//bw.write("  )\n )\n )\n)");
					bw.write("  )\n )\n )\n)"); 
		   }
		
	
		bw.close();
//fin ecriture fichier 
		if(network=="Google_Talk"){
		format = "(privkeys\n (account\n(name \""+accountID+"/\")\n(protocol prpl-jabber)\n(private-key\n (dsa\n";
		}
		else if(network=="Facebook"){
			format ="(privkeys\n (account\n(name \""+accountID+"@chat.facebook.com"+"/\")\n(protocol prpl-jabber)\n(private-key\n (dsa\n";
		}
		else if(network=="ICQ")
		{
			format ="(privkeys\n (account\n(name \""+accountID+"\")\n(protocol prpl-icq)\n(private-key\n (dsa\n";
		}
		else if(network=="Yahoo")
		{
			format ="(privkeys\n (account\n(name \""+accountID+"\")\n(protocol prpl-yahoo)\n(private-key\n (dsa\n";
		}
		format = format + "  (p #"+p+"#)\n";
		format = format +"  (q #"+q+"#)\n";
		format = format +"  (g #"+g+"#)\n";
		format = format +"  (y #"+y+"#)\n";
		format = format +"  (x #"+x+"#)\n";
		format = format +"  )\n )\n )\n)";
		//bw.close();
		System.out.println("Done");

		

	} catch (IOException e) {
		e.printStackTrace();
	}
	return format ;
}
}
