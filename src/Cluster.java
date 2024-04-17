import java.util.ArrayList;
import java.util.List;

public class Cluster {
    public final int id;
    private static int nextId = 1;
    public Point centroid;
    public final List<Point> points;

    public Cluster(Point centroid) {
        this.id = nextId++;
        this.centroid = centroid;
        this.points = new ArrayList<>();
    }
    public int getId() {
        return id;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(Point centroid) {
        this.centroid = centroid;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void clearPoints() {
        points.clear();
    }

}
