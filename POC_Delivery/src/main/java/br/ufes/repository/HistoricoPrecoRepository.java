package br.ufes.repository;

import br.ufes.model.HistoricoPreco;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HistoricoPrecoRepository implements IHistoricoPrecoRepository {

    private List<HistoricoPreco> historicos;

    public HistoricoPrecoRepository(List<HistoricoPreco> historicos) {
        validarListaHistoricoPreco(historicos);
        this.historicos = historicos;
    }

    @Override
    public void salvar(List<HistoricoPreco> historicos) {
        validarListaHistoricoPreco(historicos);

        this.historicos.addAll(historicos);
    }

    @Override
    public List<HistoricoPreco> buscarPorIdProduto(Long idProduto) {
       validarIdProduto(idProduto);
       
       
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

    private void validarListaHistoricoPreco(List<HistoricoPreco> historicoPrecos) {
        Objects.requireNonNull(historicoPrecos, "Informe uma lista não nula de historico de preços.");
        if (historicoPrecos.isEmpty()) {
            throw new IllegalArgumentException("Informe uma lista com pelo menos um historico de preço.");
        }
    }

    private void validarIdProduto(Long idProduto) {
        Objects.requireNonNull(idProduto, "Informe um id não nulo.");
        if (idProduto <= 0) {
            throw new IllegalArgumentException("O id do produto deve ser maior que zero.");
        }
    }

    private void validarNome(String nome) {
        Objects.requireNonNull(nome, "Informe um nome não nulo.");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Informe um nome não vazio.");
        }
    }

}
