package app.myapp.model.user.method;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public abstract class Method {
    private final String username;
    private final String encryptedPassword;

    //Constructor
    Method(String username, String password) {
        this.username = username;
        this.encryptedPassword = md5Hash(password);
    }

    //Check if username exist
    public abstract boolean isExist() throws SQLException;

    //Getters
    String getUsername() { return username; }
    String getEncryptedPassword() { return encryptedPassword; }

    //Password Encryption
    String md5Hash(String password){
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(password.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}








// Public - For all
// Protected- For main class and subclass only
// Private- For main class only