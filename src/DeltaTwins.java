import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DeltaTwins {

    //CHANGE DIRECTORY PATH AND VALUE OF GAMMA HERE
    private static String directory = "data/timeprogression";
    private static int delta = 2; // 323

    public static void main(String[] args) {
        ArrayList<String> dataset = new ArrayList<>();
        File folder = new File(directory);
        List<File> listOfFiles = Arrays.stream(folder.listFiles()).filter(f -> {
            try {
                if(str2Int(f.getName()) == null) return false;
                return true;
            } catch (Exception e){
                return false;
            }
        }).collect(Collectors.toList());
        for(File f : listOfFiles.stream().sorted((a, b) -> {
            return str2Int(a.getName()).compareTo(str2Int(b.getName()));
        }).collect(Collectors.toList())){
            if(f.isFile()) dataset.add(f.getPath());
        }

        for (String filepath : dataset) {
            LinkStream ls = initiate(filepath);
            System.out.print(ls.getVertices().size() + "," + ls.getLinks().size() + "," + (ls.getEndInstant() - ls.getStartInstant()) + ",");
            DeltaTwins.compute(ls, filepath);
        }

    }

    public static void compute(LinkStream ls, String filepath) {
        ArrayList<String> output = new ArrayList<>();
        String line = new String();
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();
        ArrayList<DeltaEdge> deltaTwinsPerso = new ArrayList<>();
        long startTime = System.nanoTime();
        deltaTwinsPerso = new PersoRendu().DeltaTwinsEnzo(filepath, delta);
        long interTime = System.nanoTime();
        //deltaTwins = computeDeltaTwinsMLEI(ls, line, delta);
        //long endTime = System.nanoTime();


        /*

        for(DeltaEdge edge : deltaTwins){
            if(edge.getU().i > edge.getV().i){
                Vertex tmp = edge.getU();
                edge.setU(edge.getV());
                edge.setV(tmp);
            }
        }

        Set<DeltaEdge> ad = new HashSet<>(deltaTwins);
        Set<DeltaEdge> bd = new HashSet<>(deltaTwinsPerso);
        ad.removeAll(bd);
        ArrayList<DeltaEdge> diff = new ArrayList<>(ad);

        Collections.sort(deltaTwins, (o1, o2) -> {
            if(o1.getU().i == o2.getU().i){
                if(o1.getStartInstant() == o2.getStartInstant())
                    return 0;
                return o1.getStartInstant() < o2.getStartInstant() ? -1 : 1;
            }
            return o1.getU().i < o2.getU().i ? -1 : 1;
        });


        Collections.sort(deltaTwinsPerso, (o1, o2) -> {
            if(o1.getU().i == o2.getU().i){
                if(o1.getStartInstant() == o2.getStartInstant())
                    return 0;
                return o1.getStartInstant() < o2.getStartInstant() ? -1 : 1;
            }
            return o1.getU().i < o2.getU().i ? -1 : 1;
        });

        Collections.sort(diff, (o1, o2) -> {
            if(o1.getU().i == o2.getU().i){
                if(o1.getStartInstant() == o2.getStartInstant())
                    return 0;
                return o1.getStartInstant() < o2.getStartInstant() ? -1 : 1;
            }
            return o1.getU().i < o2.getU().i ? -1 : 1;
        });

         */

        //System.out.println((interTime - startTime) + ", " + (endTime - interTime) + ", " + String.format("%,.3f", Utils.differences(deltaTwins, deltaTwinsPerso)));
        //System.out.println((endTime - interTime) + ", " + String.format("%,.3f", Utils.differences(deltaTwins, deltaTwinsPerso)));
        System.out.println((interTime - startTime) + ", " + String.format("%,.3f", Utils.differences(deltaTwins, deltaTwinsPerso)));

        output.add(line);
    }

    static private LinkStream initiate(String filePath) {
        FileParser fp = null;
        try {
            fp = new FileParser(filePath);
        } catch (IOException e) {
            System.err.println("C'est cassé");
        }

        LinkStream ls = fp.getLs();
        return ls;

    }

    public static ArrayList<DeltaEdge> computeEternalTwinsNaively(LinkStream ls, String line) {

        System.out.println("COMPUTING ETERNAL TWINS NAIVELY");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();
        try {
            Date startTime = new Date();

            deltaTwins = TwinAlgorithms.naivelyComputeEternalTwins(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(deltaTwins.size() + "," + timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat(",OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " eternal twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeEternalTwinsMEI(LinkStream ls, String line) {

        System.out.println("COMPUTING ETERNAL TWINS USING EDGES ITERATION");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();
        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeEternalTwinsByEdgesIteration(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " eternal twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeEternalTwinsMLEI(LinkStream ls, String line) {

        System.out.println("COMPUTING ETERNAL TWINS USING EDGES ITERATION WITHOUT MATRICES");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeEternalTwinsByEdgesIterationWithoutMatrices(ls);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        System.out.println("We have " + deltaTwins.size() + " eternal twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeDeltaTwinsNaively(LinkStream ls, String line, int delta) {

        System.out.println("COMPUTING " + delta + "-TWINS NAIVELY");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.naivelyComputeDeltaTwins(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(deltaTwins.size() + "," + timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat(",OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " " + delta + "-twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeDeltaTwinsMEI(LinkStream ls, String line, int delta) {

        System.out.println("COMPUTING " + delta + "-TWINS USING EDGES ITERATION");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeDeltaTwinsByEdgesIteration(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",");
        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,");
        }
        System.out.println("We have " + deltaTwins.size() + " " + delta + "-twins");
        return deltaTwins;
    }

    public static ArrayList<DeltaEdge> computeDeltaTwinsMLEI(LinkStream ls, String line, int delta) {

        //System.out.println("COMPUTING " + delta + "-TWINS USING EDGES ITERATION WITHOUT MATRICES");
        ArrayList<DeltaEdge> deltaTwins = new ArrayList<>();

        try {
            Date startTime = new Date();
            deltaTwins = TwinAlgorithms.computeDeltaTwinsByEdgesIterationWithoutMatrices(ls, delta);
            Date endTime = new Date();
            long timeElapsed = endTime.getTime() - startTime.getTime();
            line = line.concat(timeElapsed + ",,");

        } catch (OutOfMemoryError e) {
            System.err.println("Out of memory");
            line = line.concat("OUT OF MEMORY,,");
        }
        //System.out.println("We have " + deltaTwins.size() + " " + delta + "-twins");
        return deltaTwins;
    }

    static public Integer str2Int(String str) {
        Integer result = null;
        if (null == str || 0 == str.length()) {
            return null;
        }
        try {
            result = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            String negativeMode = "";
            if(str.indexOf('-') != -1)
                negativeMode = "-";
            str = str.replaceAll("-", "" );
            if (str.indexOf('.') != -1) {
                str = str.substring(0, str.indexOf('.'));
                if (str.length() == 0) {
                    return (Integer)0;
                }
            }
            String strNum = str.replaceAll("[^\\d]", "" );
            if (0 == strNum.length()) {
                return null;
            }
            result = Integer.parseInt(negativeMode + strNum);
        }
        return result;
    }
}
