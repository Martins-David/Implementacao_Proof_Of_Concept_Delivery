
package br.ufes.service;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Produto;
import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IProdutoRepository;
import java.util.Objects;

public class ExcluirCategoriaService {
    
    private IProdutoRepository produtos;
    private ICategoriaRepository categorias;
    
    public ExcluirCategoriaService(IProdutoRepository produtos,ICategoriaRepository categorias ){
        Objects.requireNonNull(produtos, "Informe um repositório não nulo de produtos.");
        Objects.requireNonNull(categorias, "Informe um repositório não nulo de categorias.");
        
        
        this.produtos = produtos;
        this.categorias = categorias;
    }
    
    public void excluirCategoria(Long idCategoria){
        
        ValidadorAtributos.validarId(idCategoria);
        
        verificarAssociacaoCategoriaProduto(idCategoria);
        
        categorias.excluirCategoria(idCategoria);
        
        
    }

    private void verificarAssociacaoCategoriaProduto(Long idCategoria) {
        for(Produto produto: produtos.listarTodos()){
            if(Objects.equals(produto.getCategoria().getIdCategoria(), idCategoria)){
                throw new RegraNegocioException("O id " + idCategoria +" já está sendo utilizado.");
            }
        }
        
        return;
    
    
    
    }
    
    
    
    
    
    
    
    
    
    
}
