/**
 * Simple vigenere's cipher encryption algorithm
 */
public class VigenereCipher {
    public static void main(String[] args) {
        VigenereCipher vc = new VigenereCipher();
        String original = "My name is Santeri";
        String key = "KEY";
        String encrypted = vc.encrypt(original, key);
        String decrypted = vc.decrypt(encrypted, key);
        System.out.println(original.equals(decrypted));
    }

    public String encrypt(String s, String key) {
        StringBuilder str = new StringBuilder();
        int keyInd = 0;
        for (int i = 0; i < s.length(); i++) {
            str.append((char)(s.charAt(i) + key.charAt((keyInd++ % key.length()))));
        }
        return str.toString();
    }

    public String decrypt(String s, String key) {
        StringBuilder str = new StringBuilder();
        int keyInd = 0;
        for (int i = 0; i < s.length(); i++) {
            str.append((char)(s.charAt(i) - key.charAt((keyInd++ % key.length()))));
        }
        return str.toString();
    }
}