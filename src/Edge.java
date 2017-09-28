public class Edge
{
    private double weight;
    private int either;
    private int other;

    public Edge(int either, int other)
    {
        this.either = either;
        this.other = other;
    }

    public double getWeight() {
        return weight;
    }

    public int getEither() {
        return either;
    }

    public int getOther() {
        return other;
    }

    public void setEither(int either) {
        this.either = either;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
