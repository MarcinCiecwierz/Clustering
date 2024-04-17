import java.util.Arrays;

public class Point {

    public final double[] values;
    public final String name;
    public Point(double[] values, String name){
        this.values = values;
        this.name = name;
    }

    public double[] getValues() {
        return values;
    }

    public String getName() {
        return name;
    }

}
