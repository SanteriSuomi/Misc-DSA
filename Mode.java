import java.util.Arrays;

public class Mode {
    public static void main(String[] args) {
        Mode m = new Mode();
        System.out.println(m.mode(new int[]{2,8,45,89,3,5,7,3,7,3,8,4,3,8,8,8,4,6,8}));
        System.out.println(m.mode(new int[]{6,6,6,6,6,9,5,3,67,2,6,0,5,3,8,4,9,5,8,6,6,9,5,5}));
    }

    public int mode(int[] arr) {
        Arrays.sort(arr);
        int highFreq = 0;
        int currFreq = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                currFreq++;
            } else {
                if (currFreq > highFreq) {
                    highFreq = currFreq;
                }
                currFreq = 0;
            }
        }
        return highFreq + 1;
    }
}