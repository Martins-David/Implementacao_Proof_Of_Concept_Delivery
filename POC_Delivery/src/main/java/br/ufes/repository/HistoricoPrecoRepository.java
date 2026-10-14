package br.ufes.repository;

import br.ufes.model.HistoricoPreco;
import br.ufes.service.ValidadorAtributos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HistoricoPrecoRepository implements IHistoricoPrecoRepository {

    private List<HistoricoPreco> historicos;

    public HistoricoPrecoRepository(List<HistoricoPreco> historicos) {
        Objects.requireNonNull(historicos, "A lista de historicos não pode ser nula.");
        
        this.historicos = historicos;
    }

    @Override
    public void salvar(List<HistoricoPreco> historicosNovos) {
        ValidadorAtributos.validarHistoricoPreco(historicosNovos);

        this.historicos.addAll(historicosNovos);
    }

    @Override
    public List<HistoricoPreco> buscarPorIdProduto(Long idProduto) {
       ValidadorAtributos.validarId(idProduto);
       
       List<HistoricoPreco> historicosEncontrados = new ArrayList<>();
       for(HistoricoPreco historico : historicos){
           if(Objects.equals(historico.getProduto().getIdProduto(), idProduto)){
               historicosEncontrados.add(historico);
           }
       }
       
       Collections.sort(historicosEncontrados, new Comparator<HistoricoPreco>() {
            @Override
            public int compare(HistoricoPreco h1, HistoricoPreco h2) {
                return h2.getData().compareTo(h1.getData());
            }
        });
       
       return historicosEncontrados;
        
    }

    @Override
    public List<HistoricoPreco> listarTodos() {
        return Collections.unmodifiableList(this.historicos);
    }


}
