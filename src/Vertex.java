public class Vertex {
    public Integer i;

    public Vertex(int i){
        this.i = i;
    }
    public Vertex() {
        this(1);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "i=" + i +
                '}';
    }
}
