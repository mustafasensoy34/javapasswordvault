package sensoy.m.password.vault;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author Mustafa SENSOY | sensoy.m@gmail.com
 */
public class Main {

    private final static String CHARS = "abcdefghijklmnoprstuvyzqwx1234567890ABCDEFGHIJKLMNOPRSTUVYZQWX";
    private final static String SPCHARS = "@%+!#$?:.-_";
    private final static String COMMAND_GENERATE = "generate";
    private final static String COMMAND_DECRYPT = "decrypt";
    private final static String BASE_ENC_KEY = "5H1an97BbQ?fVAj9";

    private final static String BASE_ENC_KEY_F1 = "3H1AJ48KGw?g#Aj9";
    private final static String BASE_ENC_KEY_F2 = "4LS@o55RCc!z?i30";
    private final static String BASE_ENC_KEY_F3 = "5KMa.74SPq*f_Ih8";
    
    private final static String BASE_ENC_KEY_F4 = "3VNANVGVNMSaPMXa";
    private final static String BASE_ENC_KEY_F5 = "Ek%F_2@mx1_pnD5W";
    private final static String BASE_ENC_KEY_F6 = "f0.$b?wHY@DtvnD@";
    
    private final static String BASE_ENC_KEY_F7 = "vDX84Q:61f_5K88l";
    private final static String BASE_ENC_KEY_F8 = ":%k3T569MLw9YT0h";
    private final static String BASE_ENC_KEY_F9 = "V:.ORTWalh+dX1Wf";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        
        long st = System.currentTimeMillis();

        if (args == null || args.length < 2) {
            System.out.println("Usage: ");

            System.out.println("java -jar RandomPasswordGenerator.jar ${COMMAND} ${PASSWORD_LENGTH} ${INCLUDE_SPECIAL_CHARS} ${STORE_NAME} [${USER_NAME} ${ENC_PASSWORD}]");
            System.out.println("java -jar RandomPasswordGenerator.jar " + COMMAND_GENERATE + " 16 true netflix [mustafa 123456]");
            System.out.println("java -jar RandomPasswordGenerator.jar ${COMMAND} ${STORE_NAME} ${ENC_PASSWORD}");
            System.out.println("java -jar RandomPasswordGenerator.jar " + COMMAND_DECRYPT + " netflix [123456]");

            System.out.println("!!! USER_NAME and ENC_PASSWORD is optional, if ENC_PASSWORD is not provided, file will be encrypted using default password!");
            System.exit(0);
        }

        String COMMAND = args[0];
        Main g = new Main();

        if (COMMAND.equalsIgnoreCase(COMMAND_GENERATE)) {

            int PASSWORD_LENGTH = Integer.valueOf(args[1]);
            boolean INCLUDE_SPECIAL_CHARS = Boolean.valueOf(args[2]);
            String STORE_NAME = args[3];

            String USER_NAME = "";
            String ENC_PASSWORD = "";

            if (args.length == 6) {
                USER_NAME = args[4];
                ENC_PASSWORD = args[5];
            } else if (args.length == 5) {
                USER_NAME = args[4];
            }

            ENC_PASSWORD = normalizePassword(ENC_PASSWORD);

            g.generate(PASSWORD_LENGTH, INCLUDE_SPECIAL_CHARS, STORE_NAME, USER_NAME, ENC_PASSWORD);

        } else if (COMMAND.equalsIgnoreCase(COMMAND_DECRYPT)) {

            String STORE_NAME = args[1];
            String ENC_PASSWORD = "";

            if (args.length == 3) {
                ENC_PASSWORD = args[2];
            }

            ENC_PASSWORD = normalizePassword(ENC_PASSWORD);
            g.decrypt(STORE_NAME, ENC_PASSWORD);

        } else {
            System.err.println("[ERR-01] Unknown command: " + COMMAND + " . Available commands: " + COMMAND_GENERATE + ", " + COMMAND_DECRYPT);
        }
        
        System.out.println("Execution Time: " + (System.currentTimeMillis() - st) + "ms");

    }

    private static String normalizePassword(String ENC_PASSWORD) {
        if (ENC_PASSWORD.isEmpty()) {
            ENC_PASSWORD = BASE_ENC_KEY;
        }

        if (ENC_PASSWORD.length() < 16) {
            for (int i = ENC_PASSWORD.length(); i < 16; i++) {
                ENC_PASSWORD += "0";
            }
        }

        if (ENC_PASSWORD.length() > 16) {
            ENC_PASSWORD = ENC_PASSWORD.substring(0, 16);
        }

        return ENC_PASSWORD;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void decrypt(String STORE_NAME, String ENC_PASSWORD) {

        FileReader fr = null;
        BufferedReader br = null;

        try {

            File f = new File(STORE_NAME);
            fr = new FileReader(f);
            br = new BufferedReader(fr);

            String line = br.readLine();
            
            if(line == null) {
                System.out.println("No Content!");
                return;
            }
            
            EncrDecr ed = new EncrDecr();
            
            line = ed.decrypt(line, ENC_PASSWORD);
            
            line = ed.decrypt(line, BASE_ENC_KEY_F9);
            line = ed.decrypt(line, BASE_ENC_KEY_F8);
            line = ed.decrypt(line, BASE_ENC_KEY_F7);
            
            line = ed.decrypt(line, BASE_ENC_KEY_F6);
            line = ed.decrypt(line, BASE_ENC_KEY_F5);
            line = ed.decrypt(line, BASE_ENC_KEY_F4);
            
            line = ed.decrypt(line, BASE_ENC_KEY_F3);
            line = ed.decrypt(line, BASE_ENC_KEY_F2);
            line = ed.decrypt(line, BASE_ENC_KEY_F1);
            
            line = ed.decrypt(line, BASE_ENC_KEY);

            System.out.println(line);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException ex) {
                }
            }

            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                }
            }
        }

    }

    private void generate(int PASSWORD_LENGTH, boolean INCLUDE_SPECIAL_CHARS, String STORE_NAME, String USER_NAME, String ENC_PASSWORD) throws IOException {
        String SEEDSTR = CHARS;
        if (INCLUDE_SPECIAL_CHARS) {
            SEEDSTR += SPCHARS;
        }

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(SEEDSTR.charAt(rnd.nextInt(SEEDSTR.length())));
        }

        StringBuilder sbPrint = new StringBuilder();

        sbPrint.append("username=").append(USER_NAME).append(System.lineSeparator());
        sbPrint.append("password=").append(sb.toString());

        String result = sbPrint.toString();
        EncrDecr ed = new EncrDecr();

        String result_enc = ed.encrypt(result, BASE_ENC_KEY);
        
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F1);
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F2);
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F3);
        
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F4);
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F5);
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F6);
        
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F7);
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F8);
        result_enc = ed.encrypt(result_enc, BASE_ENC_KEY_F9);

        result_enc = ed.encrypt(result_enc, ENC_PASSWORD);

        File f = new File(STORE_NAME);
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(result_enc);
            fw.flush();
        }

        System.out.println("###########################");
        System.out.println(STORE_NAME);
        System.out.println(result);
        System.out.println("###########################");
        System.out.println("ENC:");
        System.out.println(result_enc);
        System.out.println("###########################");

    }

}
