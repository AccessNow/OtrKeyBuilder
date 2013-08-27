package net.java.otrkeybuilder;
import java.security.KeyPair;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import net.java.otr4j.crypto.OtrCryptoEngineImpl;
import net.java.otr4j.crypto.OtrCryptoException;
/**
 * The main class for OteKeyBuilder that extends a {@link JFrame}.
 * 
 * @author Mohamed Akram Tabka
 */
@SuppressWarnings("serial")
public class OtrKeyBuilder extends JFrame {
	
		//JFrame app
		static OtrKeyBuilder app;
	    
		// Generator and Importer Dialogs 
		static KeyGeneratorDialog genDialog ; 
		static KeyImporter imp;
		
		//JTree and nodes
		static JTree tree;
		static DefaultMutableTreeNode keysNode = new DefaultMutableTreeNode("Keys");
		public static DefaultMutableTreeNode nNode;
	    public static MutableTreeNode node;
	    public static DefaultTreeModel model;
	    public static TreePath path;
		final static Map <String,DefaultMutableTreeNode> googleTalk =  new HashMap<String,DefaultMutableTreeNode>();
		final static Map <String,DefaultMutableTreeNode> facebook =  new HashMap<String,DefaultMutableTreeNode>();
		final static Map <String,DefaultMutableTreeNode> icq =  new HashMap<String,DefaultMutableTreeNode>();
		final static Map <String,DefaultMutableTreeNode> yahoo =  new HashMap<String,DefaultMutableTreeNode>();
	
		
		//panels
		static JScrollPane treePnl = new JScrollPane(tree);
		static JPanel genImpPnl = new JPanel();
		static JPanel buildPnl = new JPanel();
		static JPanel mainPnl = new JPanel();
    	
		static JButton generateBtn =new JButton("Generate");
		static JButton importBtn =new JButton("Import");
	    static JButton buildBtn =new JButton("Build");
	  
	    private static JButton browseSrcBtn = new JButton("Browse...");
	    private static JButton browseDesBtn = new JButton("Browse...");
	    static JTextField linuxUserFld = new JTextField("amnesia");
	    
	    static String isoPath ="./linuxDist/tails-i386-0.18.iso";
	    static String desPath ="./linuxDist";
	    
	    static private JLabel srcPathLbl = new JLabel("<html><pre>"+isoPath+"</pre></html>");
	    static private JLabel desPathLbl = new JLabel("<html><pre>"+desPath+"</pre></html>");
	    static JLabel statusLabel = new JLabel("Ready");
	    //this is an instance of the basic class OtrKey in which we will get the generated or imported Key.
		static OtrKey key ;
/*
 * Basic constructor of JFrame application
 */
public OtrKeyBuilder()
{
setTitle("OTR key Builder");
setSize(600, 400);
setResizable(false);
setLocationRelativeTo(null);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}		
/*
 * private methods that display JFrame components
 */
private static void setGenImpPnl(){
	genImpPnl.setLayout(new BoxLayout(genImpPnl, BoxLayout.PAGE_AXIS));
	genImpPnl.setBorder(BorderFactory.createRaisedBevelBorder());

	generateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	genImpPnl.add(generateBtn);
	genImpPnl.add(Box.createRigidArea(new Dimension(0,5)));
	importBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
	genImpPnl.add(importBtn);
}
private static void setBuildPnl()
{
	buildPnl.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.ipady = 0;
    c.gridx = 0;
    c.gridy = 0;
    buildPnl.add(new JLabel("Source iso:"),c);
    c.gridx = 1;
    buildPnl.add(srcPathLbl,c);
    c.gridx = 2;
    c.gridy = 0;
    buildPnl.add(browseSrcBtn,c);
    
    c.gridx = 0;
    c.gridy = 1;
    buildPnl.add(new JLabel("Dest path:"),c);
    c.gridx = 1;
    buildPnl.add(desPathLbl,c);     
    c.gridx = 2;
    buildPnl.add(browseDesBtn,c);

    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx= 1;
    c.gridy= 2;
    c.gridwidth = 1;
    buildPnl.add(linuxUserFld,c);
    c.gridwidth= 2 ;
    c.gridx= 2;
    buildPnl.add(buildBtn,c);
    
   c.fill = GridBagConstraints.HORIZONTAL;
	app.add(buildPnl,BorderLayout.SOUTH);
	buildPnl.setEnabled(false);
	buildPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder("Build keys")));
	
}
private static void setStatusPnl(){
	JPanel statusPanel = new JPanel();
	statusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

	app.add(statusPanel, BorderLayout.PAGE_END);
	statusPanel.setPreferredSize(new Dimension(app.getWidth(), 20));
	statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
	statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
	statusPanel.add(statusLabel);
}
private static void setMainPnl(){
	mainPnl.setLayout(new GridBagLayout());
    GridBagConstraints c1 = new GridBagConstraints();
    
    c1.ipady = 0;
    c1.gridx = 0;
    c1.gridy = 0;
    mainPnl.add(genImpPnl,c1);
    c1.fill = GridBagConstraints.HORIZONTAL;
    c1.gridx = 1;
      c1.gridy = 0;
      c1.gridheight=5;
      c1.ipady = 200 ;
      mainPnl.add(treePnl,c1);
    c1.fill = GridBagConstraints.HORIZONTAL;
    c1.gridx = 0;
    c1.gridy = 6;
    c1.ipady = 0 ;
    c1.weightx = 2;
    c1.gridwidth=2;
    
    mainPnl.add(buildPnl,c1);
    app.add(mainPnl,BorderLayout.CENTER);
}
	


	/**
	 * OtrKeyBuilder main function
	 */
    public static void main(final String[] args) throws InvalidKeySpecException, IOException 
    {
    	System.out.println("Starting OTR Key Builder ...");
    	app = new OtrKeyBuilder();
    	generate();
    	importKey();
    	browse();
    	build();
    	createKeysTree();
    	
    	setGenImpPnl();
    	setBuildPnl();
    	setStatusPnl();
    	
		setMainPnl();	
		
    	app.setVisible(true);
    	
    	Runtime.getRuntime().addShutdownHook(new Thread()
    	{
    	    @Override
    	    public void run()
    	    {
    	    	try{
    	        Runtime r1 = Runtime.getRuntime();
    	              System.out.println("revoming key cache...");
    	              statusLabel.setText("removing key cache");
    	              
    	              System.out.print(desPath);
    	              String cmd = "rm -r keys" ;
    	              
    	        
    	              System.out.println("-----------------------\n"+cmd+"\n-----------------------\n");
    	          Process p = r1.exec(cmd);

    	          BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
    	          BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
    	          
    	          
    	          while(stdOutput.readLine()!= null||stdError.readLine()!= null)
    	          {
    	              System.out.println(stdOutput.readLine());
    	          	System.out.println(stdError.readLine());
    	          }
    	    }
    	    catch (Exception e)
    	    {
    	    	System.out.println(e.getMessage());
    	    }
    	    }
    	});
    }
    /**
     * This is the generate event
     */
    public static void generate()
    {
    	 
    	    generateBtn.addActionListener(new ActionListener()
    	    {
    	        public void actionPerformed(ActionEvent arg0)
    	        { 
    	            genDialog =new KeyGeneratorDialog();
    	            genDialog.start();
    	            genDialog.generateKeyFormats(); 
    	            key = genDialog.getKey();
    	     
    	            System.out.println(key.getJitsiFormat());
    	            	System.out.println("--------------------------------------------------");
    	            	System.out.println(key.getPidginFormat());
    	            	System.out.println("--------------------------------------------------");
    	            	
    	            	try {
    	            		System.out.print("Yes returned "+new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic())+"\n");
    	            		updateKeysTree(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));
    	            		key.getAccountArray();
    	            		//----------
    	            		model = (DefaultTreeModel)tree.getModel();

    	            		
   		Set<Entry<String, String>> cles = key.getAccountArray().entrySet();
    	 for(Entry<String, String> entry : cles) {
    		 String cle = entry.getKey();
    		    String valeur = entry.getValue();
    		    // traitements
    		    nNode = new DefaultMutableTreeNode((Object)cle);
		        path = tree.getNextMatch("M", 0, Position.Bias.Forward);
		        
		        //node = (MutableTreeNode)path.getLastPathComponent();
		        
		        if (valeur=="Google_Talk"){node = googleTalk.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic())); }
		        else if (valeur=="Facebook"){node = facebook.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));}
		        else if (valeur=="ICQ"){node = icq.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));}
		        else if (valeur=="Yahoo"){node = yahoo.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));}
		        
		        model.insertNodeInto(nNode, node, node.getChildCount());
		        //--------------------
		        statusLabel.setText("Key generated");
    		}
    	 for (int i = 0; i < tree.getRowCount(); i++) {
             tree.expandRow(i);
    	 }
    	    	         	 
    	        	} catch (OtrCryptoException e) {
    	        		// TODO Auto-generated catch block
    	        		e.printStackTrace();
    	        	}
    	        }
    	    });
    	    
    }
    public static void importKey()
    {
    	 importBtn.addActionListener(new ActionListener()
 	    {
 	        public void actionPerformed(ActionEvent arg0)
 	        {
 	        	imp = new KeyImporter();
       try {
		imp.print();
		key = imp.getKey();
		System.out.println(key.getJitsiFormat());
    	System.out.println("--------------------------------------------------");
    	System.out.println(key.getPidginFormat());
    	System.out.println("--------------------------------------------------");
    	KeyPair kkk =key.getKeyPair() ;
		System.out.print("Yes returned "+new OtrCryptoEngineImpl().getFingerprint(kkk.getPublic())+"\n");
		updateKeysTree(new OtrCryptoEngineImpl().getFingerprint(kkk.getPublic()));
		Set<Entry<String, String>> cles2 = key.getAccountArray().entrySet();
for(Entry<String, String> entry : cles2) {
String cle = entry.getKey();
String valeur = entry.getValue();
// traitements
nNode = new DefaultMutableTreeNode(cle);
path = tree.getNextMatch("M", 0, Position.Bias.Forward);

//node = (MutableTreeNode)path.getLastPathComponent();
if (valeur=="Google_Talk"){node = googleTalk.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic())); }
else if (valeur=="Facebook"){node = facebook.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));}
else if (valeur=="ICQ"){node = icq.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));}
else if (valeur=="Yahoo"){node = yahoo.get(new OtrCryptoEngineImpl().getFingerprint(key.getKeyPair().getPublic()));}

model.insertNodeInto(nNode, node, node.getChildCount());
//--------------------

statusLabel.setText("Key imported");
}
for (int i = 0; i < tree.getRowCount(); i++) {
tree.expandRow(i);
}
	} catch (InvalidKeySpecException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (OtrCryptoException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
				
 	        }
 	    });
    }
    private static void browse(){
    	browseSrcBtn.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent arg0)
	        { 
    	JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
          System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
          isoPath = chooser.getSelectedFile().toString();
          srcPathLbl.setText(isoPath);
          
        } else {
          System.out.println("No Selection ");
        }
	        }
	    });
    	browseDesBtn.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent arg0)
	        { 
    	JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("choosertitle");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
          System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
          System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
          desPath = chooser.getSelectedFile().toString();
         
          desPathLbl.setText(desPath);
          
        } else {
          System.out.println("No Selection ");
        }
	        }
	    });
    }

    public static DefaultMutableTreeNode updateKeysTree(String fingerprint)//, String jitsi,String pidgin)
    {
    	model = (DefaultTreeModel)tree.getModel();

        nNode = new DefaultMutableTreeNode(fingerprint);
        path = tree.getNextMatch("M", 0, Position.Bias.Forward);
        
        node = keysNode;
        googleTalk.put(fingerprint, new DefaultMutableTreeNode("Google_talk"));
        facebook.put(fingerprint, new DefaultMutableTreeNode("Facebook"));
        icq.put(fingerprint, new DefaultMutableTreeNode("icq"));
        yahoo.put(fingerprint, new DefaultMutableTreeNode("yahoo"));
        nNode.add(googleTalk.get(fingerprint));
		  nNode.add(facebook.get(fingerprint));
		  nNode.add(icq.get(fingerprint));
		  nNode.add(yahoo.get(fingerprint));
        model.insertNodeInto(nNode, node, node.getChildCount());
       // accountFld.setText("");
        return nNode;
    }
    
 public static void createKeysTree()
 {
	 tree = new JTree(keysNode);
     tree.setBorder(BorderFactory.createTitledBorder(BorderFactory.createTitledBorder("keys Tree :")));
	  tree.setCellEditor(new CheckBoxNodeEditor(tree));
      tree.setEditable(true);

      treePnl = new JScrollPane(tree);
 }
 
 private static void build()
 {
	 buildBtn.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent arg0)
	        { 
	        	 Runtime r = Runtime.getRuntime();

	            

	       /*
	        * Creating builder.bash to be called 
	        * when we need to build keys ....
	        */
	        	 File file = new File("./script/builder.bash");
		       		file.getParentFile().mkdirs();
	      	  try {
	   			if (!file.exists()) {
	       			file.createNewFile();
	       		}
	   			
		       	FileWriter fw = new FileWriter(file.getAbsoluteFile());
		   		BufferedWriter bw = new BufferedWriter(fw);
		      	
	 			bw.write("#!/bin/bash \nif (($#==5))\nthen\n  pidginPath=\"$1\"\n  jitsiPath=\"$2\"\n  isoPath=\"$3\"\n  linuxUser=\"$4\"\n  desPath=\"$5\"\nelse\necho \"error arguments\" 1>&2\n# exit\nfi\n");
                bw.write("\nmkdir -p BUILD\ncd BUILD/\nmkdir mnt\nmount -o loop ../$isoPath mnt/\nmkdir extract-cd\nrsync --exclude=/live/filesystem.squashfs -a mnt/ extract-cd");
	       	    bw.write("\nmkdir squashfs\nmount -t squashfs -o loop mnt/live/filesystem.squashfs squashfs\nmkdir edit\ncp -a squashfs/* edit/ 2> hello");
		       		  
       	 	    bw.write("\n\ncd ..\nmkdir ./BUILD/edit/home/$linuxUser\nmkdir ./BUILD/edit/home/$linuxUser/.purple ./BUILD/edit/home/$linuxUser/.jitsi");
		        bw.write("\ncp $pidginPath/* ./BUILD/edit/home/$linuxUser/.purple/\ncp $jitsiPath/* ./BUILD/edit/home/$linuxUser/.jitsi/");
		       		  
	 		    bw.write("\nmksquashfs ./BUILD/edit ./BUILD/extract-cd/live/filesystem.squashfs\ncd ./BUILD/extract-cd");
		        bw.write("\ngenisoimage -o ../../$desPath/monimage.iso -r -J -no-emul-boot -boot-load-size 4 -boot-info-table -b isolinux/isolinux.bin -c isolinux/boot.cat ./");
		       		
		        bw.close();
		        file.setExecutable(true);
		        
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.print("Error while generating script files ");
					e1.printStackTrace();
				}
	       		  	  
	      	  /*
	      	   * running builder process by calling the generated builder.bash script
	      	   */
	      	  try
	      	  {  
	      		  System.out.println("building...");
	      		  statusLabel.setText("creating bash script...");
     
	      		  statusLabel.setText("building...(please wait)");
         
	      		  System.out.print(desPath);
	      		  String cmd = "./script/builder.bash ./keys/purple ./keys/jitsi "+ isoPath +" "+ linuxUserFld.getText()+" "+desPath ;    
	      		  Process p = r.exec(cmd);
	      		  System.out.println("-----------------------\n"+cmd+"\n-----------------------\n");
     
	      		  BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	      		  BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	      		  	while(stdOutput.readLine()!= null||stdError.readLine()!= null)
	      		  	{
	      		  		System.out.println(stdOutput.readLine());
	      		  		System.out.println(stdError.readLine());
	      		  	}
	      	  }
	      	  catch (Exception e)
	      	  {
	      		  System.out.println(e.getMessage());
	      	  }
	      	  
	      	  /*
	      	   * creating cleaner.bash script to 
	      	   * be called when the build process finished injecting keys
	      	   */
	          File cleanerScript = new File("./script/cleaner.bash");
	     		cleanerScript.getParentFile().mkdirs();

	          try {
	           		// if file doesnt exists, then create it
	     			if (!cleanerScript.exists()) {
	     				cleanerScript.createNewFile();
	         		}

	         		FileWriter fw = new FileWriter(cleanerScript.getAbsoluteFile());
	         		BufferedWriter bw = new BufferedWriter(fw);
	         	    bw.write("#!/bin/bash \ncd BUILD\numount -l squashfs\numount -l mnt\ncd ..\nrm -r BUILD");
	         	    bw.close();
	         	
	                cleanerScript.setExecutable(true);
	     		} catch (IOException e1) {
	     			// TODO Auto-generated catch block
	     			System.out.print("Error while generating cleaner script files ");
	     			e1.printStackTrace();
	     		}
	          
	          /*
	           * Cleaning BUILD folders 
	           * this process will unmount squashfs & mnt folders 
	           * then delete the whole BUILD folder 
	           */
	          statusLabel.setText("Cleaning Build folder");
	          String cmd1 = "./script/cleaner.bash" ;
	          Process p1;
			try {
				p1 = r.exec(cmd1);
		          //p1.waitFor();
		          BufferedReader stdOutput = new BufferedReader(new InputStreamReader(p1.getInputStream()));
	      		  BufferedReader stdError = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
		          stdOutput = new BufferedReader(new InputStreamReader(p1.getInputStream()));
		          stdError = new BufferedReader(new InputStreamReader(p1.getErrorStream()));
		          while(stdOutput.readLine()!= null||stdError.readLine()!= null)
		          {
		            System.out.println(stdOutput.readLine());
		          	System.out.println(stdError.readLine());
		          }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	                 
		    }
	    });
 }

 }
