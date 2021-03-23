public class RecursiveMultiplication {
    public static void main(String[] args) {
        RecursiveMultiplication rm = new RecursiveMultiplication();
        System.out.println(rm.mult(10, 5));
        System.out.println(rm.mult(3, 5643));
    }

    public int mult(int a, int b) {
        return recMult(a, b, b, 0);
    }

    private int recMult(int a, int b, int t, int res) {
        if (t == 0) return res;
        return recMult(a, b, --t, res + a);
    }
}