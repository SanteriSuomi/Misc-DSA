import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DynamicConnectivity {
    public static void main(String[] args) {
        DynamicConnectivity dc = new DynamicConnectivity();
        List<String> sites = ReadInput.asString("dcTiny.txt");
        System.out.println(dc.compute(sites));
    }

    public int compute(List<String> sites) {
        Pattern pattern = Pattern.compile("[0-9]+");
        int n = Integer.parseInt(sites.get(0));
        UnionFind uf = new UnionFind(UnionFind.toValues(n));
        int[] xy = new int[2]; // X, Y
        for (int i = 1; i < sites.size(); i++) {
            parse(sites, pattern, xy, i);
            if (uf.connected(xy[0], xy[1])) continue;
            uf.union(xy[0], xy[1]);
        }
        return uf.count;
    }

    private void parse(List<String> sites, Pattern pattern, int[] xy, int i) {
        String s = sites.get(i);
        Matcher m = pattern.matcher(s);
        for (int j = 0; j < xy.length && m.find(); j++) {
            xy[j] = Integer.parseInt(m.group());
        }
    }
}