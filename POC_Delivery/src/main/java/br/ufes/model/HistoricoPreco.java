package br.ufes.model;

import java.time.LocalDate;
import java.util.Objects;

public class HistoricoPreco {
    private LocalDate data;
    private Produto produto;
    private double percentualLucroUtilizado;
    private double precoVendaResultante;
    
    public HistoricoPreco(LocalDate data, Produto produto, double percentualLucroUtilizado, double precoVendaResultante ){
        
        Objects.requireNonNull(data, "A data deve ser não nula");
        Objects.requireNonNull(produto, "O produto deve ser não nulo.");
        
        
        if(percentualLucroUtilizado <= 0 || percentualLucroUtilizado == Double.NaN){
            throw new IllegalArgumentException("O percentual de lucro deve ser um valor positivo");
        }
        
        
        if(precoVendaResultante <= 0 || precoVendaResultante == Double.NaN){
            throw new IllegalArgumentException("O preco de venda deve ser um valor positivo");
        }
        
        this.data = data;
        this.produto = produto;
        this.percentualLucroUtilizado = percentualLucroUtilizado;
        this.precoVendaResultante = precoVendaResultante;
    }

    public LocalDate getData() {
        return this.data;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public double getPercentualLucroUtilizado() {
        return this.percentualLucroUtilizado;
    }

    public double getPrecoVendaResultante() {
        return this.precoVendaResultante;
    }

    @Override
    public String toString() {
        return "HistoricoPreco{" + "data=" + data + ", produto=" + produto + ", percentualLucroUtilizado=" + percentualLucroUtilizado + ", precoVendaResultante=" + precoVendaResultante + '}';
    }
    
    
    
    
    
    
}
