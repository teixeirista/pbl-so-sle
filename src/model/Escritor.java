package model;

/**
 *
 * @author mathe
 */
public class Escritor {
    private int id;
    private int arquivo;
    private String conteudo;
    
    public Escritor(int id, int arq, String cont) {
        this.id = id;
        arquivo = arq;
        conteudo = cont;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArquivo() {
        return arquivo;
    }

    public void setArquivo(int arquivo) {
        this.arquivo = arquivo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
