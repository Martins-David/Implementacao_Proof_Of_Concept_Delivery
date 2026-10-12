
package br.ufes.repository;

import br.ufes.model.HistoricoPreco;
import java.util.List;

public interface IHistoricoPrecoRepository {
    
   void salvar(List<HistoricoPreco> historicos);
   List<HistoricoPreco> buscarPorIdProduto (Long idProduto);
   List<HistoricoPreco> listarTodos();
    
}
