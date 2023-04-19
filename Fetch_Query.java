package APIAutomation_HopStream.Fetch;

import APIAutomation_HopStream.Utility.sshHelper;

import java.util.ArrayList;
import java.util.List;

public class Fetch_Query {

    private static String queryOutput;
    private static String[] queryOutputArray;
    private static List<String> Queryvalues = new ArrayList<String>();
    static String signleFile;
    static int Count;
    static String noLinesQuery;
    static sshHelper ssh = new sshHelper();
    static int TotalLineCount = 0;

    //public static void main(String[] args)
    public static int getFileNames(String Location) {

        Queryvalues = ssh.execRemoteCommandFile(sshHelper.getPropertyFromConfig("lsghostName"), Location, sshHelper.getPropertyFromConfig("sshUserName"),
                sshHelper.getPropertyFromConfig("sshPassphrase"), "deployed");

        System.out.println("List output :" + Queryvalues);
        return getFileCount(Queryvalues, Location);

    }

    public static int getFileCount(List L, String location) {
        for (Object a : L) {
            signleFile = a.toString();
            if (signleFile.contains("p_ingest_ingestserver")) {
                Count++;


                String Command = "cd " + location + " && cat " + signleFile + " | wc -l";
                noLinesQuery = ssh.execRemoteCommand(sshHelper.getPropertyFromConfig("lsghostName"),
                        Command, sshHelper.getPropertyFromConfig("sshUserName"),
                        sshHelper.getPropertyFromConfig("sshPassphrase"), "deployed");


                TotalLineCount += Integer.parseInt(noLinesQuery.trim());

            }

        }
        System.out.println(TotalLineCount);
        System.out.println("No of files in the location =" + Count);
        return TotalLineCount;
    }


}
