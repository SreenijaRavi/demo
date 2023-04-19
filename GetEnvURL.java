package APIAutomation_HopStream.Utility;

public class GetEnvURL {


    static String URL;
    static String Environment;
    public static String GetBaseURL2()
//public static void main(String[] args)
    {

        Environment = System.getProperty("EnvironmentType");
        System.out.println(Environment);

        if (Environment == null) {
            URL = "https://aps-pliny-api.default.abattery.appbattery.nss1.tn.akamai.com";
        } else {

            switch (Environment) {
                case "Staging":
                    URL = "";
                    break;
                case "Lab":
                    URL = "https://aps-pliny-api.default.abattery.appbattery.nss1.tn.akamai.com";
                    break;


                default:
                    URL = "https://aps-hopstream-api.default.abattery.appbattery.nss1.tn.akamai.com";
            }
            System.out.println(URL);

        }
        return URL;
    }


    public static String GetBaseURL()
//public static void main(String[] args)
    {

        Environment = System.getProperty("EnvironmentType");
        System.out.println(Environment);

        if (Environment == null) {
            URL = "https://aps-hopstream-api.default.abattery.appbattery.nss1.tn.akamai.com";
        } else {

            switch (Environment) {
                case "Staging":
                    URL = "";
                    break;
                case "Lab":
                    URL = "https://aps-hopstream-api.default.abattery.appbattery.nss1.tn.akamai.com";
                    break;


                default:
                    URL = "https://aps-hopstream-api.default.abattery.appbattery.nss1.tn.akamai.com";
            }
            System.out.println(URL);

        }
        return URL;
    }
}
