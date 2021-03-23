public class SentenceWordReverser {
    public static void main(String[] args) {
        SentenceWordReverser swr = new SentenceWordReverser();
        System.out.println(swr.reverse("My name is Chris")); // Chris is name My
    }

    public String reverse(String s) {
        StringBuilder sb = new StringBuilder();
        String[] words = s.split(" ");
        int left = 0;
        int right = words.length - 1;
        while(left < right) {
            String temp = words[left];
            words[left] = words[right];
            words[right] = temp;
            left++;
            right--;
        }
        for (int i = 0; i < words.length; i++) {
            if (i == words.length - 1) {
                sb.append(words[i]);
            } else {
                sb.append(words[i] + ' ');
            }
        }
        return sb.toString();
    }
}