public class Hash {
    private int operador;
    private Lista[] vetor;

    public Hash(int o) {
        this.operador = o;

        vetor = new Lista[operador];

        for (int i = 0; i < operador; i++) {
            vetor[i] = new Lista();
        }
    }

    public void inserir(Palavra p) {
        int chave = p.getHash() % operador;
        vetor[chave].inserir(p);
    }

    public void remover(String palavra) {
        int chave = gerarHash(palavra) % operador;
        int hash = gerarHash(palavra);
        vetor[chave].remover(hash, palavra);
    }

    public Boolean buscar(String palavra) {
        int busca = gerarHash(palavra);
        return vetor[busca % operador].buscar(busca, palavra);
    }

    public int gerarHash(String p) {
        int hash = 0;
        for (int i = 0; i < p.length(); i++) {
            char c = Character.toLowerCase(p.charAt(i));
            hash += c;
        }
        return hash;
    }

    public String toString() {
        String out = "";
        for (int i = 0; i < operador; i++) {
            out += "" + i + ": ";
            out += (vetor[i % operador]) + "\n";
        }
        return out;
    }
}
