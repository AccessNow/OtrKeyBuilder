package net.java.otrkeybuilder;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
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
	Map<String, String> accountsMap = new HashMap<String, String>();

	// getters
	public KeyPair getKeyPair()
	{
		return keyPair ;
	}
	public String getJitsiFormat()
	{
		return jitsiFormat ;
	}
	public String getPidginFormat()
	{
		return pidginFormat ;
	}
	public Map<String, String> getAccountArray()
	{
		return accountsMap;
	}
	
	//setters
	public void setJitsiFormat(String jf){
		jitsiFormat =  jf ;
	}
	public void setPidginFormat(String pf){
		pidginFormat =  pf ;
	}
	public void setAccountArray(Map<String, String> ta)
	{
		accountsMap = ta ;
	}
	
	//private methods
	/**
	 * filter parameters from private key
	 * @param privateKey
	 * @return {@link HashMap} for private parameters
	 */
	private static  HashMap<String, String> filter(PrivateKey privateKey)
	{
	System.out.print("Filtering private key parameters...");
	DSAPrivateKey DSApriv = (DSAPrivateKey)privateKey ;
	
	HashMap<String, String> param = new HashMap<String, String>();
	
	param.put("p",DSApriv.getParams().getP().toString(16));
	param.put("q",DSApriv.getParams().getQ().toString(16));
	param.put("g",DSApriv.getParams().getG().toString(16));
	param.put("x",DSApriv.getX().toString(16));
	System.out.print("end...\n");
	return param ;
	}
	
	/**
	 * filter parameters from public key
	 * @param publicKey
	 * @return {@link HashMap} for public parameters
	 */
	private static  HashMap<String, String> filter(PublicKey publicKey)
	{
	System.out.print("Filtering public key parameters...");

	DSAPublicKey DSApriv = (DSAPublicKey)publicKey ;
	HashMap<String, String> param = new HashMap<String, String>();
	param.put("p",DSApriv.getParams().getP().toString(16));
	param.put("q",DSApriv.getParams().getQ().toString(16));
	param.put("g",DSApriv.getParams().getG().toString(16));
	param.put("y",DSApriv.getY().toString(16));
	
	System.out.print("end...\n");
	return param ;
	}
	
	
	//public methods

	/**
	 * This is the basic method that generates OTR keys from DSA "Digital Signature Algorithm"
	 * @return the generated {@link KeyPair}  
	 */
	public KeyPair generateKeyPair() throws OtrCryptoException
	{
	    try
	    {
	        keyPair = KeyPairGenerator.getInstance("DSA").genKeyPair();
			System.out.println("\n  Generated key fingerprint : " + new OtrCryptoEngineImpl().getFingerprint(keyPair.getPublic()));
	        
			return keyPair ;
	    }
	    catch (NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}
	/**
	 * This is a basic method that construct OTR {@link keyPair} from given encoded public and private keys
	 * @return the constructed {@link KeyPair} from imported arguments 
	 */
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
	
	/**
	 * Gets the fingerprint from {@link KeyPair}
	 * @return a {@link String} that contains the fingerprint
	 */
	public String getLocalFingerprint()
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
	/**
	 * gets (p,q,g,x,y) parameters from the public and private OTR keys 
	 * through calling filter methods (useful for Pidgin format)
	 * @return a {@link HashMap} that contains parameters exp:(p,value)
	 */
	public HashMap<String, String> parameterParser()
	{
		HashMap<String, String> parm = new HashMap<String, String>();
	    HashMap<String, String>  parmPriv = filter(keyPair.getPrivate());
		HashMap<String, String>  parmPub= filter(keyPair.getPublic());
		
		if ((parmPriv.get("p").equals(parmPub.get("p")))
				&&(parmPriv.get("q").equals(parmPub.get("q")))
				&&(parmPriv.get("g").equals(parmPub.get("g"))))
		{
//			display key parameters
			System.out.println("--------------");
	        	System.out.println(" p:  #"+parmPriv.get("p")+"#");
	      	System.out.println("--------------");
	        	System.out.println(" q:  #"+parmPriv.get("q")+"#");
	        	System.out.println("--------------");
	        	System.out.println(" g:  #"+parmPriv.get("g")+"#");
	        	System.out.println("--------------");
	        	System.out.println(" y:  #"+ parmPub.get("y")+"#");
	        	System.out.println(" x:  #"+ parmPriv.get("x")+"#");
			// puting parameters into Map to be returned
			parm.put("p",parmPriv.get("p"));
			parm.put("q",parmPriv.get("q"));
			parm.put("g",parmPriv.get("g"));
			parm.put("x",parmPriv.get("x"));
			parm.put("y",parmPub.get("y"));
		}

	  return parm ; 
	}
/**
 * Encodes public and private keys to be useful for Jitsi format 
 *  @return a {@link HashMap} that contains encoded public and private
 */
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

}
