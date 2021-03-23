import java.util.Arrays;

public class Colors {
    public static void main(String[] args) {
        Colors c = new Colors();
        String[] res = new String[] {"red","white","blue","red","blue","white","blue"};
        c.sort(res);
        System.out.println(Arrays.toString(res));
        res = new String[] {"blue","red","blue","red","blue","white","white","red","red","blue"};
        c.sort(res);
        System.out.println(Arrays.toString(res));
    }

    public void sort(String[] c) {
        sortc(c, 0, 1);
        sortc(c, c.length - 1, -1);
    }

    private void sortc(String[] c, int start, int inc) {
        int left = 0;
        int right = c.length - 1;
        int i = start;
        while (i < c.length && i >= 0) {
            char ch = c[i].charAt(0);
            if (ch == 'r') {
                swap(c, i, left);
                left++;
            } else if (ch == 'b') {
                swap(c, i, right);
                right--;
            }
            i += inc;
        }
    }

    private void swap(String[] c, int i, int j) {
        String temp = c[i];
        c[i] = c[j];
        c[j] = temp;
    }
}