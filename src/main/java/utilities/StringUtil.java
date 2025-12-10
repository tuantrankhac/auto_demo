package utilities;

public class StringUtil {
    public static String parseStringToObjectV2(String xpathOption, String option1, String option2) {
        return xpathOption.replaceAll("&option1", option1).replaceAll("&option2", option2);
    }
    public static String parseStringToObject(String xpathOption, String option) {
        return xpathOption.replaceAll("&option", option);
    }
}
