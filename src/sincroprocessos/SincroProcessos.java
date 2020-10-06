package sincroprocessos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import model.Escritor;
import model.Leitor;

/**
 *
 * @author mathe
 */
public class SincroProcessos {
    
    static int contLeitores = 0; //Contador de todos os leitores que já existiram
    static int contEscritores = 0; //Contador de todos os escritores que já existiram
    
    static int contLeiEx = 0; //Contador da quantidade de leitores que já leram
    static int contEscEx = 0; //Contador da quantidade de escritores que já escreveram
    
    static boolean isEscrevendo = false; //Indica se tem alguém escrevendo
    static boolean isLendo = false; //Indica se tem alguém lendo
    static boolean isSincronizando = false; //Indica se está sincronizando
    
    static int ultimoModificado; //Guarda o último arquivo modificado
    
    static ArrayList<Escritor> listaEscritores = new ArrayList(); //Lista de "espera" de escritores
    static ArrayList<Leitor> listaLeitores = new ArrayList(); //Lista de "espera" de leitores
    
    static Random aleatorio = new Random(); //Variável usada para gerar valores aleatórios
    
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        
        ManipuladorArquivo.limparArquivos(); //Limpa os três arquivos ao iniciar
        
        while(true) { //Repete infinitamente
            new Thread().sleep(5000); //Determina 5 segundos entre cada execução das threads
            new Thread(gerarEscritor).start(); //Gera um novo escritor
            new Thread(gerarLeitor).start(); //Gera um novo leitor
            new Thread(escrevendo).start(); //Gera um processo de escrita no arquivo
            new Thread(lendo).start(); //Gera um processo de leitura do arquivo
            new Thread(sincronizar).start(); //Gera um processo sincronização
        }
    }

    public static Runnable gerarEscritor = new Runnable() {
        public void run() {
            int tempo = aleatorio.nextInt(20) + 1; //Define um tempo aleatório de 1 a 20 segundos
            try {
                new Thread().sleep(tempo * 1000); //Espera os segundos gerados anteriormente
            } catch (InterruptedException ex) { }
            
            boolean aux = aleatorio.nextBoolean(); //Gera um valor verdadeiro ou falso
            
            if(aux) { //Se o valor gerado for verdadeiro, um novo escritor é criado
                //Incrementa o contador de escritores
                contEscritores++;
                //Gera um novo escritor, escolhendo aleatoriamente o arquivo, e o adiciona na lista
                listaEscritores.add(new Escritor(contEscritores, aleatorio.nextInt(3) + 1, "Nova mensagem "));
            }
        }
    };
    
    public static Runnable gerarLeitor = new Runnable() {
        public void run() {
            int tempo = aleatorio.nextInt(20) + 1; //Define um tempo aleatório de 1 a 20 segundos
            try {
                new Thread().sleep(tempo * 1000); //Espera os segundos gerados anteriormente
            } catch (InterruptedException ex) { }
            
            boolean aux = aleatorio.nextBoolean(); //Gera um valor verdadeiro ou falso
        
            if(aux) { //Se o valor gerado for verdadeiro, um novo leitor é criado
                //Incrementa o contador de leitores
                contLeitores++;
                //Cria um novo leitor, escolhendo aleatoriamente o arquivo, e o adiciona na lista
                listaLeitores.add(new Leitor(contLeitores, aleatorio.nextInt(3) + 1));
            }
        }
    };
    
    private static Runnable escrevendo = new Runnable() {
        @Override
        public void run() {
            if(listaEscritores.size() > 0) { //Verifica se existem escritores
                if(!isEscrevendo && !isLendo && !isSincronizando) { //Verifica se não tem alguém já escrevendo, lendo ou sincronizando
                    isEscrevendo = true; // Define que tem alguém escrevendo
                    Escritor aux = listaEscritores.get(0); //Recebe o próximo escritor
                    try {
                        System.out.println("\nEscritor " + aux.getId() + " escrevendo no arquivo " + aux.getArquivo());
                        escrever(aux); //Escreve no arquivo
                        ultimoModificado = aux.getArquivo(); //Guarda o útlimo arquivo modificado
                        System.out.println("Escrita concluída!\n");
                    } catch (IOException ex) { }
                    listaEscritores.remove(0); //Remove o escritor que escreveu da lista
                    contEscEx++; //Incrementa o contador de escritores excluídos
                    isEscrevendo = false; //Avisa que não está mais escrevendo
                } else //Se houver alguém sincronizando, lendo ou escrevendo, avisa que não foi possível escrever
                    System.out.println("Não foi possível escrever");
            } else //Se não houver escritor, avisa
                System.out.println("Não há escritores");
        }
    };
    
    private static Runnable lendo = new Runnable() {
        @Override
        public void run() {
            if(listaLeitores.size() > 0) { //Verifica se existem leitores esperando
                if(!isEscrevendo && !isSincronizando) { //Verifica se tem alguém escrevendo ou sincronizando
                    isLendo = true; //Determina que está lendo
                    if(listaLeitores.size() > 1) { //Verifica se tem mais de 1 leitor esperando
                        int a = listaLeitores.size(); //Se sim, pega a quantidade
                        try {
                            System.out.println("\n" + a + " leitores estão lendo arquivos"); //Avisa quantos leitores leram
                            while(listaLeitores.size() > 0) { //Lê e remove os leitores da lista de uma só vez
                                Leitor aux = listaLeitores.get(0);
                                ler(aux);
                                listaLeitores.remove(0);
                                contLeiEx++; //Incrementa a quantidade de leitores que já leram
                            }
                            System.out.println("Leitura concluída!");
                        } catch (IOException ex) { }
                    } else { //Se só tiver um leitor esperando
                        Leitor aux = listaLeitores.get(0); //Recebe esse leitor
                        try {
                            System.out.println("\nLeitor " + aux.getId() + " lendo arquivo " + aux.getArquivo());
                            ler(aux); //Executa a leitura
                            System.out.println("Leitura concluída!");
                        } catch (IOException ex) { }
                        listaLeitores.remove(0); //Remove o leitor da lista
                        contLeiEx++; //Incrementa o contador de leitores que já leram
                    }
                    isLendo = false; //Determina que não está mais lendo
                } else //Se houver alguém escrevendo ou sincronizando, avisa que não foi possível ler
                    System.out.println("Não foi possível ler");
            }  else //Se não houverem leitores, avisa
                    System.out.println("Não há leitores");
        }
    };
    
    /**
     * Executa o processo de sincronização
     */
    private static Runnable sincronizar = new Runnable() {
        @Override
        public void run() {
            int tempo = aleatorio.nextInt(11) + 1; //Gera um tempo aleatório entre 1 e 12 segundos
            try {
                new Thread().sleep(tempo * 1000); //Espera a quantidade de segundos sorteada
            } catch (InterruptedException ex) { }
            
            if(contEscEx > 0) { //Verifica se algum escritor já escreveu
                isSincronizando = true; //Determina que está sincronizando
                String caminho = getCaminho(ultimoModificado); //Recebe o caminho do último arquivo modificado
                
                try {
                    System.out.println("\nSincronizando...");
                        ManipuladorArquivo.sincronizar(caminho); //Executa a sincronização
                        System.out.println("Sincronização concluída!");
                    } catch (IOException ex) { }
                isSincronizando = false; //Avisa que acabou a sincronização
            } else //Se nenhum escritor tiver escrito ainda, avisa
                System.out.println("Sincronizador: Nenhum arquivo foi modificado ainda");
        }
    };
    
    /**
     * Executa a escrita
     * @param e
     * @throws IOException 
     */
    public static void escrever(Escritor e) throws IOException {
        String caminho = getCaminho(e.getArquivo());
        
        ManipuladorArquivo.escritor(caminho, e.getConteudo());
    }
    
    /**
     * Executa a leitura
     * @param l
     * @throws IOException 
     */
    public static void ler(Leitor l) throws IOException {
        String conteudo = ManipuladorArquivo.leitor(getCaminho(l.getArquivo()));
        System.out.println(conteudo);
    }
    
    /**
     * Retorna o caminho do arquivo que será lido ou escrito
     * @param arq
     * @return 
     */
    private static String getCaminho(int arq) {
        String caminho;
        
        if(arq == 1)
            caminho = "src/arquivos/arquivo1.txt";
        else if (arq == 2)
            caminho = "src/arquivos/arquivo2.txt";
        else
            caminho = "src/arquivos/arquivo3.txt";
        
        return caminho;
    }
}