public class HighestSum {
    public static void main(String[] args) {
        HighestSum sw = new HighestSum();
        System.out.println(sw.compute(new int[] {1, 5, 2, 3, 7, 1}, 3)); // 12
        System.out.println(sw.compute(new int[] {5, -3, 7, -6, 8}, 2)); // 4
        System.out.println(sw.compute(new int[] {5, -3, 7, -6, 8}, 3)); // 9
    }

    /**
     * Given an array of integers and a number k. Return the highest sum of any k consecutive elements in the array.
     */
    public int compute(int[] arr, int k) {
        if (arr.length < k) return 0;
        int currentSum = 0;
        int startIndex;
        for (startIndex = 0; startIndex < k; startIndex++) {
            currentSum += arr[startIndex];
        }
        int highestSum = currentSum;
        int left = 0;
        int right = startIndex;
        while (right < arr.length) {
            currentSum -= arr[left];
            currentSum += arr[right];
            if (currentSum > highestSum) {
                highestSum = currentSum;
            }
            left++;
            right++;
        }
        return highestSum;
    }
}