import java.util.HashSet;
import java.util.List;

public class Utils {
    public static double differences(List<DeltaEdge> a, List<DeltaEdge> b){
        double result = 0;
        HashSet<Integer> a_hash = new HashSet<>();
        HashSet<Integer> b_hash = new HashSet<>();
        for(DeltaEdge d : a) a_hash.add(d.hashCode());
        for(DeltaEdge d : b) b_hash.add(d.hashCode());
        int total = a_hash.size() + b_hash.size();
        if(total == 0) return 0;
        a_hash.removeAll(b_hash);
        result = a_hash.size();
        for(DeltaEdge d : a) a_hash.add(d.hashCode());
        b_hash.removeAll(a_hash);
        result += b_hash.size();
        for(DeltaEdge d : b) b_hash.add(d.hashCode());

        return (result / (total));// + (Math.abs(a_hash.size() - b_hash.size()) / (total));
    }
}
