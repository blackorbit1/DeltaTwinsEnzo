import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PersoRendu {
    // dans le cas pratique, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> histoire semble un peu plus rapide
    //private HashMap<Integer, HashMap<Integer, HashSet<Integer>>> histoire = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> histoire = new HashMap<>();
    private HashMap<Integer, HashMap<Integer, TreeMap<Integer, Boolean>>> g_index = new HashMap<>();
    private HashMap<Integer, TreeSet<Integer>> interest_t = new HashMap<>();
    private HashSet<Integer> allPoints = new HashSet<>();
    private int MAX_T = Integer.MIN_VALUE;
    private int MIN_T = Integer.MAX_VALUE;

    public void init(String filePath) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(filePath));
        String s = r.readLine();
        while (s != null) {
            String[] points = s.replaceAll("\n", "").split(" ");
            Integer x_tmp = Integer.parseInt(points[0]);
            Integer y_tmp = Integer.parseInt(points[1]);
            Integer t = Integer.parseInt(points[2]);
            if (t > MAX_T) MAX_T = t;
            if (t < MIN_T) MIN_T = t;
            Integer x = Integer.min(x_tmp, y_tmp);
            Integer y = Integer.max(x_tmp, y_tmp);

            if (!histoire.containsKey(t)) histoire.put(t, new HashMap<>());
            if (!histoire.get(t).containsKey(x)) histoire.get(t).put(x, new ArrayList<>());
            if (!histoire.get(t).containsKey(y)) histoire.get(t).put(y, new ArrayList<>());
            histoire.get(t).get(x).add(y);
            histoire.get(t).get(y).add(x);

            allPoints.add(x);
            allPoints.add(y);

            s = r.readLine();
        }
    }

    public ArrayList<DeltaEdge> DeltaTwinsEnzo(String filePath, Integer delta) {
        try { // Construction de la structure de données de base
            init(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long startTime = System.nanoTime();

        HashSet<Integer> seen_points = new HashSet<>();
        for (Integer t : histoire.keySet()) { // il y a pas mal de coupures dans le temps
            for (Integer a : histoire.get(t).keySet()) {
                for (Integer b : histoire.get(t).get(a)) {
                    if (!seen_points.contains(a) || !seen_points.contains(b)) {
                        TreeSet<Integer> points2see = new TreeSet<>();
                        points2see.add(a);
                        points2see.add(b);
                        points2see.addAll(histoire.get(t).get(a));
                        points2see.addAll(histoire.get(t).get(b));

                        for(Integer x : points2see) {
                            if (!interest_t.containsKey(x)) interest_t.put(x, new TreeSet<>());
                            interest_t.get(x).add(t);
                        }

                        for (Integer x : points2see) {
                            for (Integer y : points2see) {
                                if(x < y){
                                    boolean etat = false;

                                    if (isTwins(x, y, t)) {
                                        etat = true;
                                    }

                                    if (!g_index.containsKey(x)) g_index.put(x, new HashMap<>());
                                    if (!g_index.get(x).containsKey(y)) g_index.get(x).put(y, new TreeMap<>());
                                    g_index.get(x).get(y).put(t, etat);
                                }
                            }
                        }
                        seen_points.addAll(points2see);
                    }
                }
            }
            seen_points.clear();
        }

        System.out.println("\n===========");
        System.out.println("1e boucle : " + (System.nanoTime() - startTime));
        startTime = System.nanoTime();

        // On trie pour éviter de faire des couples doublons
        List<Integer> allPointsSorted = new ArrayList<>(allPoints);
        Collections.sort(allPointsSorted);

        System.out.println("2e boucle : " + (System.nanoTime() - startTime));
        startTime = System.nanoTime();

        ArrayList<DeltaEdge> result = new ArrayList<>();

        for (Integer u : allPointsSorted) {
            for (Integer v : allPointsSorted) {
                if (u < v) {
                    if(interest_t.containsKey(u) || interest_t.containsKey(v)){
                        ArrayList<Integer> all_interest_t = new ArrayList();
                        if(interest_t.containsKey(u)) all_interest_t.addAll(interest_t.get(u));
                        if(interest_t.containsKey(v)) all_interest_t.addAll(interest_t.get(v));
                        Collections.sort(all_interest_t);
                        // TODO : il y a masse doublons dans all_interest_t (gain en temps ?)

                        //if(u == 6 && v == 7)
                        //    System.out.println("debug");

                        int last_t = MIN_T - 1;
                        for (Integer t : all_interest_t) {
                            if(
                                    // Si pour tout t un des points a des voisins mais pas l'autre, il ne peuvent pas etre jumeaux
                                    //(!g_index.containsKey(u) || !g_index.containsKey(v)) <--- TODO : en fait le test !g_index.containsKey(v) sert à rien nn ? (retire delta twins valides)
                                    !g_index.containsKey(u)
                                    // Si à ce t un des points a des voisins mais pas l'autre, il ne peuvent pas etre jumeaux
                                    || (!g_index.get(u).containsKey(v))
                                    // Si aucune ref du couple au moment t mais que 1 des pts est ds un autre couple, il ne peuvent pas etre jumeaux
                                    || (!g_index.get(u).get(v).containsKey(t))
                                    // Si le couple n'est pas jumeaux
                                    || (!g_index.get(u).get(v).get(t))
                            ){
                                if ((t - last_t) >= delta + 1)
                                    result.add(new DeltaEdge(last_t + 1, t - 1, new Vertex(u), new Vertex(v)));
                                last_t = t;
                            }
                        }
                        if ((MAX_T - last_t) >= delta) result.add(new DeltaEdge(last_t + 1, MAX_T, new Vertex(u), new Vertex(v)));
                    } else {
                        result.add(new DeltaEdge(MIN_T, MAX_T, new Vertex(u), new Vertex(v)));
                    }
                }
            }
        }

        System.out.println("3e boucle : " + (System.nanoTime() - startTime));
        System.out.println("===========");

        return result;
    }


    /*
    sortAllVoisins(histoire);
    setDeepHashCode(histoire);

        histoire.get(t = 3).get(point = 5).stockTonDeepHashCode()

        class ArrayListPerso extends ArrayList {
            public deepHashCode
            stockTonDeepHashCode(){
                deepHashCode = deepHashCode()
            }
        }

    if(histoire.get(t).get(x).deepHashCode == histoire.get(t).get(y).deepHashCode){
        // faire la vérif pour etre sur à 100%
    } else {
        return false;
    }

    // O(n log n) pour chaque liste voisins --> très rapide
    // O(n) pour deepHashCode
     */


    private boolean isTwins(Integer u, Integer v, Integer t){
        // Si la différence de taille des liste de voisins est trop grande, ils ne peuvent pas être jumeaux
        // TODO : comme le graphe est pas orianté, dès 1 de diff ça passe plus nn ?
        if(Math.abs(histoire.get(t).get(u).size() - histoire.get(t).get(v).size()) >= 2) return false;

        // Ils sont tous les deux dans la meme situation
        if(!histoire.containsKey(t)
                || (!histoire.get(t).containsKey(u)
                && !histoire.get(t).containsKey(v))
        ) return true;

        // Un a des voisins que l'autre n'a pas
        if(!histoire.get(t).containsKey(u)) return false;
        if(!histoire.get(t).containsKey(v)) return false;

        int x = (histoire.get(t).get(u).size() > histoire.get(t).get(v).size()) ? u : v;
        int y = (histoire.get(t).get(u).size() < histoire.get(t).get(v).size()) ? u : v;
        for(Integer voisin_x : histoire.get(t).get(x)){
            // Si c'est un splitter
            if(voisin_x != v && !histoire.get(t).get(y).contains(voisin_x)){
                return false;
            }
        }

        return true;
    }
}