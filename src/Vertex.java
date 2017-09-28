import java.util.ArrayList;


public class Vertex
{
    private ArrayList<Integer> point;
    private ArrayList<Double> edges;
    private int name;

    public Vertex(int name)
    {
        this.name = name;
    }

    public void setEdges(ArrayList<Double> edges) {
        this.edges = edges;
    }

    public void setPoint(ArrayList<Integer> point) {
        this.point = point;
    }

    public void setName(int name) {
        this.name = name;
    }

    public ArrayList<Double> getEdges() {
        return edges;
    }

    public ArrayList<Integer> getPoint() {
        return point;
    }

    public int getName() {
        return name;
    }
}
