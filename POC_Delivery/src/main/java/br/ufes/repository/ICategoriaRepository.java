
package br.ufes.repository;

import br.ufes.model.Categoria;
import java.util.List;

public interface ICategoriaRepository {
    
   void salvar(List<Categoria> categorias);
   List<Categoria> buscarCategoriaPorID(Long idCategoria);
   List<Categoria> buscarCategoriaPorNome (String nomeCategoria);
   List<Categoria> listarTodos();
   void excluirCategoria(Long idCategoria);
}
