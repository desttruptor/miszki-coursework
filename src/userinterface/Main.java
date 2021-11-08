package userinterface;

import report.Rffff;

import java.io.IOException;

public class Main {
    private static boolean isRunning = true;

    public static void main(String[] args) throws InterruptedException {
//        InetAddress address = InetAddress.getByName("8.8.8.8");
//        boolean reachable = address.isReachable(5000);
//        System.out.println(reachable);
//        StringBuilder out = new StringBuilder();
//        Runtime r = Runtime.getRuntime();
//        Process p = r.exec("cd %userprofile%");
//        p.waitFor();
//        Process p1 = r.exec("%SYSTEMROOT%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe -Command \"Get-MpComputerStatus | select AntivirusEnabled\"");
//        p1.waitFor();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
//        String line = "";
//        while ((line = reader.readLine()) != null) {
//            out.append(line).append("\n");
//        }
//        System.out.println(out);

        for (int i = 0; i<2; i++) {
            System.out.print("\r" + i);
            Thread.sleep(1000);
        }

        Menu menu = new Menu();
    }
}
