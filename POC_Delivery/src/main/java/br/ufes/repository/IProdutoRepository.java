
package br.ufes.repository;

import br.ufes.model.Produto;
import java.util.List;

public interface IProdutoRepository {
    
   void salvar(List<Produto> produto);
   Produto buscarProdutoPorId (Long idProduto);
   List<Produto> buscarProdutoPorNome (String nomeProduto);
   List<Produto> buscarProdutoPorCategoria(String nomeCategoria);
   List<Produto> listarTodos();
   
   boolean existePorCategoria(String nomeCategoria);
   
   void excluirProduto (Long idProduto);
    
}
