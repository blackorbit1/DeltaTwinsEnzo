import java.util.ArrayList;
import java.util.Comparator;

class GraphComp implements Comparator<ArrayList<DeltaEdge>> {
    @Override
    public int compare(ArrayList<DeltaEdge> l1, ArrayList<DeltaEdge> l2) {
        // decroissant
        if(l1.size() > l2.size()) return -1;
        else return 1;
    }
}