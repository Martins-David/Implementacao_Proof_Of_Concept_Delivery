
package br.ufes.model;

import java.util.Objects;

public class Categoria {
    private long idCategoria;
    private String nome;
    private double percentualLucro;

    public Categoria(long idCategoria, String nome, double percentualLucro) {
        
        Objects.requireNonNull(idCategoria, "O código identificador da categoria deve ser não nulo.");
        Objects.requireNonNull(nome, "O nome da categproa deve ser não nulo");
        
        if(nome.isBlank()){
            throw new IllegalArgumentException("Insira um nome válido para a categoria.");
        }
        
        if(percentualLucro <= 0 || percentualLucro == Double.NaN){
            throw new IllegalArgumentException("O percentual de lucro deve ser um valor positivo");
        }
        
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.percentualLucro = percentualLucro;
    }

    public long getIdCategoria() {
        return this.idCategoria;
    }

    public void setIdCategoria(long idCategoria) {
        
        Objects.requireNonNull(idCategoria, "O código identificador da categoria deve ser não nulo.");
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        Objects.requireNonNull(nome, "O nome da categproa deve ser não nulo");
        
        if(nome.isBlank()){
            throw new IllegalArgumentException("Insira um nome válido para a categoria.");
        }
        this.nome = nome;
    }

    public double getPercentualLucro() {
        return this.percentualLucro;
    }

    public void setPercentualLucro(double percentualLucro) {
        if(percentualLucro <= 0){
            throw new IllegalArgumentException("O percentual de lucro deve ser maior do que 0.");
        }
        this.percentualLucro = percentualLucro;
    }
    
    
}
