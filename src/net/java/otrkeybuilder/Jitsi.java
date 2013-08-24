package net.java.otrkeybuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class Jitsi {

private static String getXmlFriendlyString(String s)
{
    if (s == null || s.length() < 1)
        return s;

    // XML Tags are not allowed to start with digits,
    // insert a dummy "p" char.
    if (Character.isDigit(s.charAt(0)))
        s = "p" + s;

    char[] cId = new char[s.length()];

    for (int i = 0; i < cId.length; i++)
    {
        char c = s.charAt(i);

        cId[i] = Character.isLetterOrDigit(c) ? c : '_';
    }

    return new String(cId);
}
/**
 * 
 * @param publicKey
 * @param privateKey
 * @param accountID
 */
String format;
	public String storeGeneratedKey(byte[] publicKey ,byte[] privateKey , String network ,String accountID)
	{
		System.out.println("writing into Jitsi format...");
		if (accountID == null)
	        return "";
		
		String account = getXmlFriendlyString(accountID);
		System.out.println(account);

		try {
			 
			String JitsiHeader = "net.java.sip.communicator.plugin.otr.";

			File file = new File("./keys/jitsi/sip-communicator.properties");
			file.getParentFile().mkdirs();
			//File file = new File("/home/akram/.jitsi/sip-communicator.properties");
			
			String publicToStore = new String(Base64.encodeBase64(publicKey));
	        String privateToStore = new String(Base64.encodeBase64(privateKey));
	     

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			if(network=="Google_Talk")
			{
			bw.write("\nnet.java.sip.communicator.impl.protocol.jabber.acc9999999999999.ACCOUNT_UID=Google Talk\\:"+accountID+"@gmail.com\n");
			bw.write(JitsiHeader + "Google_Talk_" + account +"_gmail_com"+ "_publicKey=" + publicToStore.toString()+"\n");
			bw.write(JitsiHeader + "Google_Talk_" + account +"_gmail_com"+ "_privateKey=" + privateToStore.toString()+"\n");
			bw.write(JitsiHeader + "Google_Talk_" + account +"_talk_google_com"+ "_publicKey=" + publicToStore.toString()+"\n");
			bw.write(JitsiHeader + "Google_Talk_" + account +"_talk_google_com"+ "_privateKey=" + privateToStore.toString());
			}
			else if(network=="Facebook")
			{
				bw.write("\nnet.java.sip.communicator.impl.protocol.jabber.acc9999999999999.ACCOUNT_UID=Facebook\\:"+accountID+"@chat.facebook.com@chat.facebook.com\n");
				bw.write(JitsiHeader + "Facebook_" + account +"_chat_facebook_com_chat_facebook_com"+ "_publicKey=" + publicToStore.toString()+"\n");
				bw.write(JitsiHeader + "Facebook_" + account +"_chat_facebook_com_chat_facebook_com"+ "_privateKey=" + privateToStore.toString());
			}
			else if(network=="ICQ")
			{
				bw.write("\nnet.java.sip.communicator.impl.protocol.icq.acc9999999999999.ACCOUNT_UID=ICQ\\:"+accountID+"@icq.com\n");
				bw.write(JitsiHeader + "ICQ_" + accountID +"_icq_com"+ "_publicKey=" + publicToStore.toString()+"\n");
				bw.write(JitsiHeader + "ICQ_" + accountID +"_icq_com"+ "_privateKey=" + privateToStore.toString());
			}
			else if(network=="Yahoo")
			{
				if (accountID.contains("@yahoo.com"))
				{
				bw.write("\nnet.java.sip.communicator.impl.protocol.yahoo.acc9999999999999.ACCOUNT_UID=Yahoo\\!\\:"+accountID+"\n");
				bw.write(JitsiHeader + "Yahoo__" + account + "_publicKey=" + publicToStore.toString()+"\n");
				bw.write(JitsiHeader + "Yahoo__" + account + "_privateKey=" + privateToStore.toString());
				}
				else
				{
					bw.write("\nnet.java.sip.communicator.impl.protocol.yahoo.acc9999999999999.ACCOUNT_UID=Yahoo\\!\\:"+accountID+"@yahoo.com\n");
					bw.write(JitsiHeader + "Yahoo__" + account +"_yahoo_com"+ "_publicKey=" + publicToStore.toString()+"\n");
					bw.write(JitsiHeader + "Yahoo__" + account +"_yahoo_com"+ "_privateKey=" + privateToStore.toString());
				}
			}

			bw.close();

			if(network=="Google_Talk")
				{
				format = "net.java.sip.communicator.impl.protocol.jabber.acc9999999999999.ACCOUNT_UID=Google Talk\\:"+accountID+"@gmail.com\n";
				format = format + JitsiHeader + "Google_Talk_" + account +"_gmail_com"+ "_publicKey=" + publicToStore.toString()+"\n";
				format = format + JitsiHeader + "Google_Talk_" + account +"_gmail_com"+ "_privateKey=" + privateToStore.toString()+"\n";
				format = format + JitsiHeader + "Google_Talk_" + account +"_talk_google_com"+ "_publicKey=" + publicToStore.toString()+"\n";
				format = format + JitsiHeader + "Google_Talk_" + account +"_talk_google_com"+ "_privateKey=" + privateToStore.toString();
				}
				else if(network=="Facebook")
				{
					format = "net.java.sip.communicator.impl.protocol.jabber.acc9999999999999.ACCOUNT_UID=Facebook\\:"+accountID+"@chat.facebook.com@chat.facebook.com\n";
					format = format + JitsiHeader + "Facebook_" + account +"_chat_facebook_com_chat_facebook_com"+ "_publicKey=" + publicToStore.toString()+"\n";
					format = format + JitsiHeader + "Facebook_" + account +"_chat_facebook_com_chat_facebook_com"+ "_privateKey=" + privateToStore.toString();
				}
				else if(network=="ICQ")
				{
					format = "net.java.sip.communicator.impl.protocol.icq.acc9999999999999.ACCOUNT_UID=ICQ\\:"+accountID+"@icq.com\n";
					format = format + JitsiHeader + "ICQ_" + accountID +"_icq_com"+ "_publicKey=" + publicToStore.toString()+"\n";
					format = format + JitsiHeader + "ICQ_" + accountID +"_icq_com"+ "_privateKey=" + privateToStore.toString();
				}
				else if(network=="Yahoo")
				{
					if (accountID.contains("@yahoo.com"))
					{
				    format = "net.java.sip.communicator.impl.protocol.yahoo.acc9999999999999.ACCOUNT_UID=Yahoo\\!\\:"+accountID+"\n";
				    format = format + JitsiHeader + "Yahoo__" + account + "_publicKey=" + publicToStore.toString()+"\n";
				    format = format + JitsiHeader + "Yahoo__" + account + "_privateKey=" + privateToStore.toString();
					}
					else
					{
						format = "net.java.sip.communicator.impl.protocol.yahoo.acc9999999999999.ACCOUNT_UID=Yahoo\\!\\:"+accountID+"@yahoo.com\n";
						format = format + JitsiHeader + "Yahoo__" + account +"_yahoo_com"+ "_publicKey=" + publicToStore.toString()+"\n";
						format = format + JitsiHeader + "Yahoo__" + account +"_yahoo_com"+ "_privateKey=" + privateToStore.toString();
					}
				}
	
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return format ;
	}
	
}
