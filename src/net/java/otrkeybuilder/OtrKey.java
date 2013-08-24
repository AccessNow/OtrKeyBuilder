package net.java.otrkeybuilder;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import net.java.otr4j.crypto.OtrCryptoEngineImpl;
import net.java.otr4j.crypto.OtrCryptoException;

import org.apache.commons.codec.binary.Base64;


public class OtrKey {
	
	static KeyPair keyPair;
	private String jitsiFormat = "";
	private String pidginFormat ="";
	Map<String, String> typeAccount = new HashMap<String, String>();
	
	public void Key()
	{
	}
	public KeyPair getKeyPair()
	{
		return keyPair ;
	}
	public KeyPair construct(String privK,String pubK)
	{
		// Load Private Key.
		byte[] privKey = privK.getBytes(); 
		
	    // Load Private Key.
	    byte[] b64PrivKey = Base64.decodeBase64(privKey);
	    if (b64PrivKey == null)
	        return null;
	    
        PKCS8EncodedKeySpec privateKeySpec =
            new PKCS8EncodedKeySpec(b64PrivKey);

        // Load Public Key.
		byte[] pubKey = pubK.getBytes(); 
		byte[] b64PubKey = Base64.decodeBase64(pubKey);
		
		if (b64PubKey == null)
	        return null;


        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64PubKey);

        PublicKey publicKey;
        PrivateKey privateKey;

        // Generate KeyPair.
        KeyFactory keyFactory;
        try
        {
            keyFactory = KeyFactory.getInstance("DSA");
            publicKey = keyFactory.generatePublic(publicKeySpec);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
            return null;
        }
        keyPair =new KeyPair(publicKey, privateKey);
        return new KeyPair(publicKey, privateKey);
	}
	//fingerprint
	public String getLocalFingerprint() throws InvalidKeySpecException, IOException
	{

	    PublicKey pubKey = keyPair.getPublic();

	    try
	    {
	        return new OtrCryptoEngineImpl().getFingerprint(pubKey);
	    }
	    catch (OtrCryptoException e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public HashMap<String, String> parameterParser()
	{
		HashMap<String, String> parm = new HashMap<String, String>();
	    HashMap<String, String>  parmPriv = filtrer(keyPair.getPrivate());
		HashMap<String, String>  parmPub= filtrer(keyPair.getPublic());
		
		if ((parmPriv.get("p").equals(parmPub.get("p")))
				&&(parmPriv.get("q").equals(parmPub.get("q")))
				&&(parmPriv.get("g").equals(parmPub.get("g"))))
		{
//	    		System.out.println("--------------");
//	    		System.out.println("cohérence entre la clé publique et privée");
//	    		System.out.println("--------------");
//	        	System.out.println(" p:  #"+parmPriv.get("p")+"#");
//	        	System.out.println(" p:  #"+ parmPub.get("p")+"#");
//	        	System.out.println("--------------");
//	        	System.out.println(" q:  #"+parmPriv.get("q")+"#");
//	        	System.out.println(" q:  #"+ parmPub.get("q")+"#");
//	        	System.out.println("--------------");
//	        	System.out.println(" g:  #"+parmPriv.get("g")+"#");
//	        	System.out.println(" g:  #"+ parmPub.get("g")+"#");
//	        	System.out.println("--------------");
//	        	System.out.println(" y:  #"+ parmPub.get("y")+"#");
//	        	System.out.println(" x:  #"+ parmPriv.get("x")+"#");
			//---
			parm.put("p",parmPriv.get("p"));
			parm.put("q",parmPriv.get("q"));
			parm.put("g",parmPriv.get("g"));
			parm.put("x",parmPriv.get("x"));
			parm.put("y",parmPub.get("y"));
		}

	  return parm ; 
	}

	public HashMap<String, byte[]> encodeJitsi ()
	{
	    // Store Public Key.
	    PublicKey pubKey = keyPair.getPublic();
	    X509EncodedKeySpec x509EncodedKeySpec =
	        new X509EncodedKeySpec(pubKey.getEncoded());
	    
	    PrivateKey privKey = keyPair.getPrivate();
	    PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
	        new PKCS8EncodedKeySpec(privKey.getEncoded());
	    
	    //net.java.sip.communicator.plugin.otr.Google_Talk_tabkram_gmail_com_gmail_com_public
	    HashMap<String, byte[]> parm = new HashMap<String, byte[]>();
	    parm.put("pub", x509EncodedKeySpec.getEncoded());
	    parm.put("priv",pkcs8EncodedKeySpec.getEncoded());
	    
	    return parm;
	}
	
	public static  HashMap<String, String> filtrer(PrivateKey privateKey)
	{
	System.out.println("Filtering private key parameters...");
	//StringBuffer keyBuffer = new StringBuffer(key); 
	//s.toString().split("\n");
	String keyLn[] = privateKey.toString().split("\n");
	int i=0;
	String p="";
	String q="";
	String g="";
	String x="";
	HashMap<String, String> param = new HashMap<String, String>();
	do
	{
		System.out.println("Search line by line...");
		if (keyLn[i].contains("p:"))
		{
			System.out.println("P parameter found...");
			i++;
			while(!keyLn[i].contains("q:")){
				p = p + keyLn[i];
				i++;
			}
			p=p.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("P parameter is : "+p+"\n");
			param.put("p",p);
			i++;
			System.out.println("Q parameter found...");
			while(!keyLn[i].contains("g:")){
				q = q + keyLn[i];
				i++;
			}
			q=q.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("Q parameter is :"+q+"\n");
			param.put("q",q);
			i++;
			System.out.println("G parameter found...");
			while(!keyLn[i].contains("x:")){
				g = g + keyLn[i];
				i++;
			}
			g=g.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("G parameter is :"+g+"\n");
			param.put("g",g);
			System.out.println("X parameter found...");
			while(i!=keyLn.length){
				x= x + keyLn[i];
				i++;
			}
			x=x.replaceAll("[\\s\t\r\n]+", "");
			x= x.substring(2);
			System.out.print("X parameter is :"+x+"\n");
			param.put("x",x);
		}
		else
		{
		i++;
		}
	}while(i!=keyLn.length);
	System.out.println("end...");
	return param ;
	}

	public static  HashMap<String, String> filtrer(PublicKey publicKey)
	{
	System.out.println("Filtering public key parameters");
	//StringBuffer keyBuffer = new StringBuffer(key); 
	//s.toString().split("\n");
	String keyLn[] = publicKey.toString().split("\n");
	int i=0;
	String p="";
	String q="";
	String g="";
	String y="";
	HashMap<String, String> param = new HashMap<String, String>();
	do
	{
		System.out.println("Search line by line...");
		if (keyLn[i].contains("p:"))
		{
			System.out.println("P parameter found...");
			i++;
			while(!keyLn[i].contains("q:")){
				p = p + keyLn[i];
				i++;
			}
			p=p.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("P parameter is : "+p+"\n");
			param.put("p",p);
			System.out.println("Q parameter found...");
			i++;
			while(!keyLn[i].contains("g:")){
				q = q + keyLn[i];
				i++;
			}
			q=q.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("Q parameter is : "+q+"\n");
			param.put("q",q);
			i++;
			System.out.println("G parameter found...");
			while(!keyLn[i].contains("y:")){
				g = g + keyLn[i];
				i++;
			}
			g=g.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("G parameter is : "+g+"\n");
			param.put("g",g);
			i++;
			System.out.println("Y parameter found...");
			while(i!=keyLn.length){
				y = y + keyLn[i];
				i++;
			}
			y=y.replaceAll("[\\s\t\r\n]+", "");
			System.out.print("Y parameter is : "+y+"\n");
			param.put("y",y);
		}
		else
		{
		i++;
		}
	}while(i!=keyLn.length);
	System.out.println("end...");
	return param ;
	}

	public KeyPair generateKeyPair() throws OtrCryptoException
	{
	    
//		if (accountID == null)
//	        return null;
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

	public void setJitsiFormat(String jf){
		jitsiFormat =  jf ;
	}
	public void setPidginFormat(String pf){
		pidginFormat =  pf ;
	}
	public String getJitsiFormat()
	{
		return jitsiFormat ;
	}
	public String getPidginFormat()
	{
		return pidginFormat ;
	}
	public void setAccountArray(Map<String, String> ta)
	{
		typeAccount = ta ;
	}
	public Map<String, String> getAccountArray()
	{
		return typeAccount;
	}
//	public PrivateKey loadPrivateKey() throws IOException
//	{
//		byte[] privKey = PublicKey.getBytes(); 
//		
//	    // Load Private Key.
//	    byte[] b64PrivKey = Base64.decodeBase64(privKey);
//	    if (b64PrivKey == null)
//	        return null;
//
//	    PKCS8EncodedKeySpec privateKeySpec =
//	        new PKCS8EncodedKeySpec(b64PrivKey);
//
//	    PrivateKey privateKey;
//
//	    // Generate KeyPair.
//	    KeyFactory keyFactory;
//	    try
//	    {
//	        keyFactory = KeyFactory.getInstance("DSA");
//	        privateKey = keyFactory.generatePrivate(privateKeySpec);
//	    }
//	    catch (NoSuchAlgorithmException e)
//	    {
//	        e.printStackTrace();
//	        return null;
//	    }
//	    catch (InvalidKeySpecException e)
//	    {
//	        e.printStackTrace();
//	        return null;
//	    }
//
//	    return privateKey;
//	}
//	public static PublicKey loadPublicKey() throws IOException, InvalidKeySpecException
//	{
//		
//		byte[] pubKey = puKey.getBytes(); 
//		byte[] b64PubKey = Base64.decodeBase64(pubKey);
//	    
//		//byte[] b64PubKey = "MIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAD7g3/6w6634/sRsm+HR22EmfQQz/Tfs6SQC6z9cLGE8Pa3xgb/0No9X9PW2G0YS5LGsdqmXGfe9Dz3ZcTlatTxKZohjALKq2spyKa5JvYpEDwaGxKNWtOr9IdTI04FNwRJvB+lcEElaB6LNxeFJKw7UEVj++E1yCG08LEZsAS9U\\=".getBytes();
//		
//		if (b64PubKey == null)
//	        return null;
//
//	    
//	    X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(b64PubKey);
//
//	    // Generate KeyPair.
//	    KeyFactory keyFactory;
//	    try
//	    {
//	        keyFactory = KeyFactory.getInstance("DSA");
//	        return keyFactory.generatePublic(publicKeySpec);
//	    }
//	    catch (NoSuchAlgorithmException e)
//	    {
//	        e.printStackTrace();
//	        return null;
//	    }
//	}
	
}
