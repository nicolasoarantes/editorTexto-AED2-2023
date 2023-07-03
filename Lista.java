public class Lista {
    private No inicio;
    private int tamanho;

    public Lista() {
        this.inicio = new No();
        this.tamanho = 0;
    }

    public Lista(No i, int t) {
        this.inicio = i;
        this.tamanho = t;
    }

    public void inserir(Palavra p) {
        No no = new No();
        no.setInfo(p);
        no.setProximo(inicio);
        this.inicio = no;
        tamanho++;
    }

    public boolean remover(int hash, String palavra) {
        No anterior = null;
        No atual = inicio;

        while (atual != null) {
            if (atual.getInfo().getHash() == hash && atual.getInfo().getPalavra().intern() == palavra.intern()) {
                if (anterior == null) {
                    // O nó a ser removido é o primeiro nó da lista
                    inicio = atual.getProximo();
                } else {
                    // O nó a ser removido não é o primeiro nó da lista
                    anterior.setProximo(atual.getProximo());
                }
                return true;
            }
            anterior = atual;
            atual = atual.getProximo();
        }

        return false;
    }

    // public Palavra buscar(int hash, String palavra) {
    public Boolean buscar(int hash, String palavra) {
        No no = inicio;
        while (no != null) {
            try {
                if (no.getInfo().getHash() == hash && no.getInfo().getPalavra().intern() == palavra.intern()) {
                    // return no.getInfo();
                    return true;
                }
                no = no.getProximo();

            } catch (Exception e) {
                return false;
            }
        }
        // return null;
        return false;
    }

    public String toString() {
        String out = "";
        No no = inicio;
        while (no != null) {
            out += no.getInfo() + "";
            no = no.getProximo();
        }
        return out;
    }
}
