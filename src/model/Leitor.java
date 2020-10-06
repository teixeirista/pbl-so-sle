package model;

/**
 *
 * @author mathe
 */
public class Leitor {
    private int id;
    private int arquivo;
    
    public Leitor(int id, int arq) {
        this.id = id;
        arquivo = arq;
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
}
