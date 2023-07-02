public class Palavra {
    private String palavra;
    private int hash;

    public Palavra() {
        this.palavra = "";
    }

    public String toString() {
        return "(" + palavra + "," + hash + ")";
    }

    public Palavra(String v) {
        this.palavra = v;
        this.hash = gerarHash(v);
    }

    public int gerarHash(String p) {
        int hash = 0;
        for (int i = 0; i < p.length(); i++) {
            char c = Character.toLowerCase(p.charAt(i));
            hash += c;
        }
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public int getHash() {
        return hash;
    }

    public String getPalavra() {
        return palavra;
    }

}
