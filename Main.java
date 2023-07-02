import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Hash hash = new Hash(4);

        hash.inserir(new Palavra("Acabate"));
        hash.inserir(new Palavra("Abacate"));
        hash.inserir(new Palavra("Abactae"));
        hash.inserir(new Palavra("Abcaeta"));
        hash.inserir(new Palavra("Voz"));
        hash.inserir(new Palavra("Pessoa"));
        hash.inserir(new Palavra("Capaz"));

        Scanner ler = new Scanner(System.in);

        // System.out.printf("Digite o código desejado: ");
        // String valorASerBuscado = ler.next();

        // Palavra teste = hash.buscar(valorASerBuscado);

        // System.out.println("==========================================\n");
        // System.out.print(hash);
        // System.out.println("\n==========================================\n");
    }
}