package Billboard;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class User {
    private String userName;
    private String passWord;
    private ArrayList<String> permission;
    private String privilege;

    public User(String name, String password, ArrayList<String> permission, String privilege) throws Exception{
        userName = name;
        passWord = password;
        this.permission = new ArrayList<>(permission.size());
        this.permission.addAll(permission);
        if(privilege.compareTo("") == 0 || privilege == null){
            throw new Exception("User must have privilege");
        }this.privilege = privilege;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public boolean checkPassword(String enteredPass) {return enteredPass.compareTo(passWord) == 0; }

    public ArrayList<String> getPermission() {
        if(privilege.toUpperCase().compareTo("CREATE BILLBOARDS") == 0){

        }
        else if(privilege.toUpperCase().compareTo("EDIT ALL BILLBOARDS") == 0){

        }
        else if(privilege.toUpperCase().compareTo("SCHEDULE BILLBOARD") == 0){

        }
        else if(privilege.toUpperCase().compareTo("EDIT USER") == 0){

        }
        return permission;
    }

    public void setPermission(ArrayList<String> permission) {
        this.permission = permission;
    }

    public String getPrivilege() {
        return privilege;
    }

    /* To update the user's privilege
    * @param acquired privilege
    */
    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public String saltPassWord(String passWord) throws Exception{
        String algorithm = "MD5";
        byte[] salt = createSalt();
        String saltData = bytesToStringHex(salt);
        return generateHashPass(passWord,algorithm,salt);
    }

    //Change password into salt hash
    private static String generateHash(String passWord, String algorithm) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        byte[] hash = digest.digest(passWord.getBytes());
        return bytesToStringHex(hash);
    }

    //Change password into salt hash
    private static String generateHashPass(String passWord, String algorithm, byte[] salt) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        digest.update(salt);
        byte[] hash = digest.digest(passWord.getBytes());
        return bytesToStringHex(hash);
    }

    private final static char[] hexArray = "0123456789ABCDE".toCharArray();

    public static String bytesToStringHex(byte[] bytes){
        char[] hexChars = new char[bytes.length * 2];
        for(int i = 0; i < bytes.length; i++){
            int j = bytes[i] & 0xFF;
            hexChars[i * 2] = hexArray[j>>>4];
            hexChars[i * 2 + 1] = hexArray[j & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] createSalt(){
        byte[] bytes = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes);
        return bytes;
    }
}
