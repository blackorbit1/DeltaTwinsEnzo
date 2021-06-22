public class DeltaEdge {

    private int startInstant;
    private int endInstant;
    private Vertex u = null;
    private Vertex v = null;

    public DeltaEdge(int startInstant, int endInstant, Vertex u, Vertex v) {
        this.startInstant = startInstant;
        this.endInstant = endInstant;
        if(u != null) this.u = (u.i < v.i)?u:v;
        if(v != null) this.v = (u.i > v.i)?u:v;
    }

    @Override
    public String toString() {
        return "DeltaEdge{" +
                "startInstant=" + startInstant +
                ", endInstant=" + endInstant +
                ", u=" + u +
                ", v=" + v +
                '}';
    }

    public void setStartInstant(int startInstant) {
        this.startInstant = startInstant;
    }

    public void setEndInstant(int endInstant) {
        this.endInstant = endInstant;
    }

    public void setU(Vertex u) {
        this.u = u;
    }

    public void setV(Vertex v) {
        this.v = v;
    }

    public Integer getStartInstant() {
        return this.startInstant;
    }

    public int getEndInstant() {
        return this.endInstant;
    }

    public Vertex getU() {
        return this.u;
    }

    public Vertex getV() {
        return this.v;
    }

    public boolean isDeltaEdge(int delta) {
        return (this.endInstant - this.startInstant + 1 >= delta);
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj == null) return false;
        DeltaEdge o = (DeltaEdge) obj;
        return u.i == o.getU().i && v.i == o.getV().i && startInstant == o.getStartInstant() && endInstant == o.getEndInstant();
    }

    @Override
    public int hashCode() {
        return ("" + (u.i>v.i?v.i:u.i) + " " + (u.i<v.i?v.i:u.i) + " " + startInstant + " " + endInstant).hashCode();
    }

    public String md5(String str) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
}
