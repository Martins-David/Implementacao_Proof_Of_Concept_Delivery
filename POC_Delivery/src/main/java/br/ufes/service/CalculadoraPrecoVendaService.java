package br.ufes.service;

import br.ufes.model.Produto;
import java.util.Objects;

public class CalculadoraPrecoVendaService {
    private Produto produto;
    
    public CalculadoraPrecoVendaService(Produto produto){
        Objects.requireNonNull(produto, "O produto deve ser não nulo.");
        
        this.produto = produto;
    }
    
    
    
    public void calcularPrecoVenda(Produto produto){
        
    }
    
    
    
}
