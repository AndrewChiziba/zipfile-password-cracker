import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MainCracker {
    public static void main(String[] args) throws MalformedURLException {
        //String zipFilePath = "protected.zip";
        //String crackedPasswordPath = "crackedpassword.txt";
        String zipFilePath = args[0];
        String crackedPasswordPath = args[1];

        //read password file and brute force zip password
        URL passwordSource = new URL("https://raw.githubusercontent.com/danielmiessler/SecLists/master/Passwords/Common-Credentials/10k-most-common.txt");
        BufferedWriter myWriter;
        BufferedReader myReader;
        try {
            myReader = new BufferedReader(new InputStreamReader(passwordSource.openStream()));
            String myPasswordBuffer;
            //loop until     end of password file
            while((myPasswordBuffer = myReader.readLine()) != null){
                try {
                    // Create a ZipFile object with the path to the ZIP file
                    ZipFile zipFile = new ZipFile(zipFilePath);

                    // Check if the ZIP file is password protected
                    if (zipFile.isEncrypted()) {
                        // Set the password for the ZIP file
                        zipFile.setPassword(myPasswordBuffer.toCharArray());
                    }
                    // Extract the contents of the ZIP file to the destination folder
                    zipFile.extractAll("unzipped");

                    //create file and write password
                    myWriter = new BufferedWriter(new FileWriter(crackedPasswordPath));
                    myWriter.write("The password for the zip file: " + myPasswordBuffer);
                    myWriter.close();

                    System.out.println("ZIP file extracted successfully.");
                    break;
                }
                catch (ZipException e) {
                    //System.out.println("Error extracting ZIP file: " + e.getMessage());
                }
            }
            myReader.close();

            try {
                BufferedReader checkpasswordReader = new BufferedReader(new FileReader(crackedPasswordPath));
            } catch (FileNotFoundException e) {
                System.out.println("Password not found");
            }
        }
        catch (Exception e){
            System.out.println("Unexpected error reading passwords source file");
        }


    }
}

