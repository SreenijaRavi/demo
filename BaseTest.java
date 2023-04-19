package APIAutomation_HopStream.Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    protected static Properties prop;
    protected static FileInputStream input;
    public static String workingDir;

    public String getPropertyFromConfig(String propertyName) {
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

    public String executeSQL2Command(String command) {
        String queryOutput = null;

        try {
            BaseTest BaseTest = new BaseTest();
            sshHelper ssh = new sshHelper();
            queryOutput = ssh.execRemoteCommand(BaseTest.getPropertyFromConfig("stagingmachine"), command, BaseTest.getPropertyFromConfig("userName"),
                    BaseTest.getPropertyFromConfig("sshPassphrase"), "Internal");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return queryOutput;
    }



    public void VerifyQueryOutput(String[] Expected, String[] Actual, String queryName) {
        try {
            int exp_count;
            String ExpectedText,ActualText;
       if (Expected.length == Actual.length)
       { boolean flag = false;
           System.out.println("Data count is correct for " + queryName);
           for( int act_count =0;act_count<Actual.length;act_count++)
           {
            for(  exp_count =0;exp_count<Expected.length;exp_count++)
            {
                if(Expected[exp_count].equalsIgnoreCase(Actual[act_count]))
                {
                    flag = true;
                     break;
                }

            }
               if(flag)
                   System.out.println("EXPECTED  VALUE : " + Expected[exp_count] + "is present in response for query "+queryName);
               else
                   System.out.println("EXPECTED  VALUE : " + Expected[exp_count] + "is NOT present in response for query "+queryName);
           }



       }else
       {
           System.out.println("Some data might be missing for " + queryName);
       }

        }catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}
