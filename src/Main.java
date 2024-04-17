import java.io.*;
import java.util.*;

public class Main {

    public static final String path = "Data/train.txt";

    public static void main(String[] args) throws IOException {
        List<Point> points = getPoints(path);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter k value: ");
        int k = scanner.nextInt();

        List<Cluster> clusters = createClusters(points, k); //Create clusters

        double sumOfDistances = 0;
        boolean enough = false;
        int iterationCount = 0;
        while (!enough) {
            addPointsToCluster(points, clusters);
            updateCluster(clusters);
            double newSumOfDistances = sumOfDistance(clusters);
            if (Math.abs(sumOfDistances - newSumOfDistances) < 0.01) {
                enough = true;
            }
            sumOfDistances = newSumOfDistances;
            iterationCount++;
            System.out.println("Iteration " + iterationCount);
            System.out.println("Sum of distances: " + sumOfDistances);
            printObservations(clusters);
            System.out.println();
        }
    }

    public static List<Point> getPoints(String path) throws IOException {
        List<Point> points = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null){
            String[] split = line.split(",");
            double[] values = new double[split.length - 1];
            for (int i = 0; i < values.length; i++){
                values[i] = Double.parseDouble(split[i]);
            }
            String name = split[split.length - 1];
            points.add(new Point(values, name));
        }
        br.close();
        return points;
    }

    public static List<Cluster> createClusters(List<Point> points, int k){
        List<Cluster> clusters = new ArrayList<>();
        Random random = new Random();
        for (int i=0; i < k; i++){
            Point centroid = points.get(random.nextInt(points.size()));
            clusters.add(new Cluster(centroid));
        }
        return clusters;
    }
    public static double euclidianDistance(Point p1, Point p2){
        double[] values1 = p1.getValues();
        double[] values2 = p2.getValues();
        double sum = 0;

        for (int i = 0; i < values2.length; i++){
            sum += Math.pow(values1[i] - values2[i], 2);
        }
        return Math.sqrt(sum);
    }

    public static void addPointsToCluster(List<Point> points, List<Cluster> clusters){
        for(Cluster cluster : clusters){
            cluster.clearPoints();
        }
        for(Point point : points){ //get point
            Cluster near = null;
            double minDist = Double.MAX_VALUE;
            for(Cluster cluster : clusters) { //get cluster
                double distance = euclidianDistance(point, cluster.getCentroid());
                if(distance < minDist){ //find smallest distance between
                    minDist = distance; //point and cluster and
                    near = cluster;     //add point to cluster with min dist
                }
            }
            near.addPoint(point);
        }
    }

    public static void updateCluster(List<Cluster> clusters){
        for (Cluster cluster : clusters){
            double[] newCentroid = new double[cluster.getCentroid().getValues().length];
            for (Point point : cluster.getPoints()){
                double[] pointValues = point.getValues();
                for (int i = 0; i< newCentroid.length; i++){
                    newCentroid[i] += pointValues[i];
                }
            }
            for (int i = 0; i < newCentroid.length; i++){
                newCentroid[i] /= cluster.getPoints().size();
            }
            cluster.setCentroid(new Point(newCentroid, ""));
        }
    }
    public static double sumOfDistance(List<Cluster> clusters){
        double sum = 0;
        for(Cluster cluster : clusters){
            Point centroid = cluster.getCentroid();
            for (Point point : cluster.getPoints()){
                sum += euclidianDistance(point, centroid);
            }
        }
        return sum;
    }

    public static void printObservations(List<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            System.out.print("Cluster " + cluster.getId() + ": ");

            List<Point> points = cluster.getPoints();
            Map<String, Integer> nameCounts = new HashMap<>();
            int totalPoints = points.size();
            for (Point point : points) {
                String name = point.getName();
                if (nameCounts.containsKey(name)) {
                    nameCounts.put(name, nameCounts.get(name) + 1);
                } else {
                    nameCounts.put(name, 1);
                }
            }
            for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
                double purity = (double) entry.getValue() / totalPoints;
                System.out.print(entry.getKey() + " (" + Math.round(purity * 100) + "%), " );
            }
            System.out.print("Points: " + cluster.getPoints().size() + "\n");
        }
    }
}