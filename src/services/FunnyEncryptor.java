package services;

public abstract class FunnyEncryptor {

    public static String encrypt(char[] password) {
        StringBuilder encryptedPassword = new StringBuilder();
        for(char c : password) {
            c *= 3;
            c -= 30;
            c += 4;
            encryptedPassword.append(c);
        }

        return encryptedPassword.toString();
    }

    public static String decrypt(String encryptedPassword) {
        StringBuilder decryptedPassword = new StringBuilder();
        for(char c : encryptedPassword.toCharArray()) {
            c -= 4;
            c += 30;
            c /= 3;
            decryptedPassword.append(c);
        }

        return decryptedPassword.toString();
    }
}
