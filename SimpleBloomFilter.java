import java.util.BitSet;

public class SimpleBloomFilter {
    public static void main(String[] args) {
        SimpleBloomFilter sbf = new SimpleBloomFilter(100);
        sbf.insert("santeri");
        System.out.println(sbf.contains("santeri"));
        System.out.println(sbf.contains("santer"));
        System.out.println(sbf.contains("santerii"));
        sbf.insert("pepsi");
        System.out.println(sbf.contains("pepsi"));
        System.out.println(sbf.contains("ispep"));
    }

    private BitSet set;
    private int size;
    private Hash[] hashFuncs; // Hash functions

    public SimpleBloomFilter(int size) {
        set = new BitSet(size);
        this.size = size;
        hashFuncs = new Hash[4];
        hashFuncs[0] = new Hash1();
        hashFuncs[1] = new Hash2();
        hashFuncs[2] = new Hash3();
        hashFuncs[3] = new Hash4();
    }

    public void insert(String s) {
        for (int i = 0; i < hashFuncs.length; i++) {
            set.set(hashFuncs[i].hash(s, size));
        }
    }

    public boolean contains(String s) {
        for (int i = 0; i < hashFuncs.length; i++) {
            if (!set.get(hashFuncs[i].hash(s, size))) return false;
        }
        return true;
    }
}

interface HashFunction {
    public int hash(String s, int n);
}

class Hash implements HashFunction {
    /**
     * Generate a hash from string using string folding hashing
     * @param s String
     * @param n Modulo
     * @return Hash
     */
    public int fold(String s, int n) {
        long sum = 0;
        long mult = 1;
        for (int i = 0; i < s.length(); i++) {
            mult = (i % 4 == 0) ? 1 : mult * 256;
            sum += s.charAt(i) * mult;
        }
        return (int)(Math.abs(sum) % n);
    }

    @Override
    public int hash(String s, int n) {
        // Override
        return 0;
    }
}

class Hash1 extends Hash {
    @Override
    public int hash(String s, int n) {
        return 1 + fold(s, n) % n;
    }
}

class Hash2 extends Hash {
    @Override
    public int hash(String s, int n) {
        return 5 + (int)Math.pow(fold(s, n), 4) % n;
    }
}

class Hash3 extends Hash {
    @Override
    public int hash(String s, int n) {
        return 10 + 3 * fold(s, n) % n;
    }
}

class Hash4 extends Hash {
    @Override
    public int hash(String s, int n) {
        return 10 + 6 * (int)Math.pow(fold(s, n), 2.5) % n;
    }
}