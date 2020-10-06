package sincroprocessos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ManipuladorArquivo {

	public static String leitor(String path) throws IOException {
            BufferedReader buffRead = new BufferedReader(new FileReader(path));
            String conteudo = "";
            String linha = "";
            while (true) {
            	if (linha != null) {
                    conteudo += linha;
		} else
                    break;
                    linha = buffRead.readLine();
            }
            buffRead.close();
            return conteudo;
        }
        
        public static void escritor(String path, String conteudo) throws IOException {
		FileWriter fw = new FileWriter(path, true);
                fw.write(conteudo);
                fw.close();
	}
        
        public static void sincronizar(String path) throws IOException {
            String conteudo = leitor(path);
            
            sinc("src/arquivos/arquivo1.txt", conteudo);
            sinc("src/arquivos/arquivo2.txt", conteudo);
            sinc("src/arquivos/arquivo3.txt", conteudo);
        }
        
    public static void sinc(String path, String conteudo) throws IOException {
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
        buffWrite.append(conteudo);
        buffWrite.close();
    }
    
    public static void limparArquivos() throws IOException {
        PrintWriter pw1 = new PrintWriter(new FileWriter("src/arquivos/arquivo1.txt"));
        PrintWriter pw2 = new PrintWriter(new FileWriter("src/arquivos/arquivo2.txt"));
        PrintWriter pw3 = new PrintWriter(new FileWriter("src/arquivos/arquivo3.txt"));
        pw1.flush();
        pw1.close();
        pw2.flush();
        pw2.close();
        pw3.flush();
        pw3.close();
    }
}
