package usecases;

/**
 * Консольные команды, выполняемые приложением
 */
public class ConsoleCommands {
    public static final String PING_GOOGLE = "ping 8.8.8.8";
    public static final String FIREWALL_STATE = "netsh advfirewall show allprofiles state";
    public static final String TASKLIST = "tasklist.exe";
    public static final String DEFENDER_EXISTS = "IF EXIST \"C:\\ProgramData\\Microsoft\\Windows Defender\\\" ECHO true";
}
