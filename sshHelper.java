package APIAutomation_HopStream.Utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;


import com.jcraft.jsch.*;
import org.apache.commons.io.IOUtils;


public class sshHelper {


    private static String workingDir;
    private static FileInputStream input;
    private static Properties prop;

    ChannelSftp sftp = null;

    private String theString;
    Session session = null;
     List AllFiles  = new ArrayList();
    public  Session getSession(String host, String sshUserName, String sshPassphrase,String keys) {
        try {
            JSch jsch = new JSch();
            String username = System.getProperty("user.name");
            File privateKey = null;
            System.out.println("Entered");
            if (getPropertyFromConfig("env").equalsIgnoreCase("qa"))
            {
                System.out.println("Host Name: " + host);
                System.out.println("Username: " + sshUserName);
                System.out.println("passPhrase: " + sshPassphrase);


                privateKey = new File(System.getProperty("user.dir")+"/SSH_Keys/"+getPropertyFromConfig("DeployedKey")); //getting SSH key from working directory
                System.out.println(privateKey);
                jsch.addIdentity(privateKey.getPath(), sshPassphrase);
                JSch.setConfig("StrictHostKeyChecking", "no");
                session = jsch.getSession(sshUserName, host, 22);
                System.out.println("Reached");
                session.connect();
                //session.setConfig("PreferredAuthentications", "publickey");
                System.out.println("Connected");
                return session;
            }
            if(getPropertyFromConfig("env").equalsIgnoreCase("prod"))
            {
                System.out.println("Host Name: " + host);
                sshUserName = getPropertyFromConfig("prod_sshUserName");
                sshPassphrase = getPropertyFromConfig("prod_sshPassphrase");
                System.out.println("Username: " + sshUserName);
                System.out.println("passPhrase: " +sshPassphrase );
                // privateKey = new File("/Users/"+ username +"/.ssh/"+keys+"/spathak-"+keys+"-2021-05-06");

                privateKey = new File(getPropertyFromConfig("prod_ssh")); //getting SSH key from working directory
                System.out.println(privateKey);
                jsch.addIdentity(privateKey.getPath(), sshPassphrase);
                JSch.setConfig("StrictHostKeyChecking", "no");
                session = jsch.getSession(sshUserName, host, 22);
                System.out.println("Reached");
                session.connect();
                //session.setConfig("PreferredAuthentications", "publickey");
                System.out.println("Connected");
                return session;
            }
        } catch (JSchException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create Jsch Session object.", e);
        }
        return session;
    }

    public String execRemoteCommand(String host, String command, String sshUserName, String sshPassphrase,String keys) {
        Session session = getSession(host, sshUserName, sshPassphrase,keys);
        if(session !=null){
            try {
                Channel channel = session.openChannel("exec");
                channel.setInputStream(null);
                ((ChannelExec) channel).setCommand(command);
                ((ChannelExec) channel).setErrStream(System.err);
                InputStream in = channel.getInputStream();
                try {
                    Thread.sleep(5000);
                } catch (Exception ee) {
                }
                channel.connect();

                System.out.println("Channel Connected");
                theString = IOUtils.toString(in, "UTF-8");
                //System.out.println("Output command " + theString);

                channel.disconnect();
                session.disconnect();
                return theString;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception in execRemoteCommand ");
                return theString;
            }
        }
        return theString;

    }

    public static String getPropertyFromConfig(String propertyName) {
        try {
            workingDir = System.getProperty("user.dir");
            input = new FileInputStream(workingDir + "//config.properties");
            prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(propertyName);
    }

    public List execRemoteCommandFile(String host, String Location, String sshUserName, String sshPassphrase,String keys) {
        Session session = getSession(host, sshUserName, sshPassphrase, keys);
        if (session != null) {
            try {
                Channel channel = session.openChannel("sftp");
                channel.setInputStream(null);
                channel.connect();
                sftp = (ChannelSftp) channel;
                Vector<ChannelSftp.LsEntry> files = sftp.ls(Location);



                System.out.println("Channel Connected");
                System.out.println("All files in location ");
                for (ChannelSftp.LsEntry a : files) {
                    AllFiles.add(a.getFilename());
                }



                channel.disconnect();
                session.disconnect();
                sftp.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception in execRemoteCommandFile ");

            }
        }
        return AllFiles;

    }

    //public static void main(String[] args) {
        
    

}
