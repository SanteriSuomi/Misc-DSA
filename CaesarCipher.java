/**
 * Simple caesar's cipher encryption algorithm
 */
public class CaesarCipher {
    public static void main(String[] args) {
        CaesarCipher cc = new CaesarCipher();
        String original = "My name is Santeri";
        String encrypted = cc.encrypt(original, 3);
        String decrypted = cc.decrypt(encrypted, 3);
        System.out.println(original.equals(decrypted));
        String[] ciphers = cc.bruteBreak(encrypted);
        for (int i = 0; i < ciphers.length; i++) {
            if (ciphers[i].equals(original)) {
                System.out.println(ciphers[i]);
            }
        }
    }

    public String encrypt(String s, int shift) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append((char)(s.charAt(i) + shift));
        }
        return sb.toString();
    }

    public String decrypt(String s, int shift) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append((char)(s.charAt(i) - shift));
        }
        return sb.toString();
    }

    /**
     * Return all possible plaintext versions of the encrypted cipher
     * @param s Cipher
     * @return All possible plaintext versions
     */
    public String[] bruteBreak(String s) {
        String[] ciphers = new String[25];
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 25; i++) {
            for (int j = 0; j < s.length(); j++) {
                sb.append((char)(s.charAt(j) - i));
            }
            ciphers[i - 1] = sb.toString();
            sb.setLength(0);
        }
        return ciphers;
    }
}