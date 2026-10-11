
package br.ufes.model;

import java.util.List;
import java.util.Objects;

public class Produto {
    private long idProduto;
    private String nome;
    private double precoCusto;
    private Categoria categoria;

    public Produto(long idProduto, String nome, double precoCusto, Categoria categoria) {
        
        Objects.requireNonNull(idProduto, "O código identificador da categoria deve ser não nulo.");
        Objects.requireNonNull(nome, "O nome da categproa deve ser não nulo");
        
        if(nome.isBlank()){
            throw new IllegalArgumentException("Insira um nome válido para a categoria.");
        }
        
        if(precoCusto <= 0 || precoCusto == Double.NaN){
            throw new IllegalArgumentException("O percentual de lucro deve ser um valor positivo");
        }
        
        Objects.requireNonNull(categoria, "A categoria do produto deve ser não nula");  
        
        
        
        this.idProduto = idProduto;
        this.nome = nome;
        this.precoCusto = precoCusto;
        this.categoria = categoria;
    }

    public long getIdProduto() {
        return this.idProduto;
    }

    public void setIdProduto(long idProduto) {
        Objects.requireNonNull(idProduto, "O código identificador da categoria deve ser não nulo.");
        this.idProduto = idProduto;
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

    public double getPrecoCusto() {
        return this.precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        if(precoCusto <= 0 || precoCusto == Double.NaN){
            throw new IllegalArgumentException("O percentual de lucro deve ser um valor positivo");
        }
        this.precoCusto = precoCusto;
    }

    public Categoria getCategoria() {
        return this.categoria;
    }

    public void setCategoria(Categoria categoria) {
        Objects.requireNonNull(categoria, "A categoria do produto deve ser não nula");  
        this.categoria = categoria;
    }
    
    public double getPercentualLucro(){
        return this.categoria.getPercentualLucro();
    }
    
    
}
