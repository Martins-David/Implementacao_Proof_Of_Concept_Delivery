package br.ufes.service;

import br.ufes.exception.RegraNegocioException;

import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;

import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.repository.IProdutoRepository;


import static br.ufes.service.PrazoCalculoServiceMock.FORMATO_DATA;
import static br.ufes.service.PrazoCalculoServiceMock.INTERVALO_MINIMO_DIAS;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CalculadoraPrecoVendaService {

    private IProdutoRepository produtoRepository;
    private ICategoriaRepository categoriaRepository;
    private IHistoricoPrecoRepository historicoPrecoRepository;

    public CalculadoraPrecoVendaService(IProdutoRepository produtoRepository, ICategoriaRepository categoriaRepository, IHistoricoPrecoRepository historicoPrecoRepository) {

        Objects.requireNonNull(categoriaRepository, "O repositório de categorias deve ser não nulo.");
        Objects.requireNonNull(produtoRepository, "O repositório de produtos deve ser não nulo.");
        Objects.requireNonNull(historicoPrecoRepository, "O repositório do histórico de preços deve ser não nulo.");

        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.historicoPrecoRepository = historicoPrecoRepository;
    }

    public List<HistoricoPreco> calcular() {
        return calcular(LocalDate.now());
    }

    public List<HistoricoPreco> calcular(LocalDate dataCalculo) {

        Objects.requireNonNull(dataCalculo, "A data do cálculo é obrigatória.");

        validarIntervalo(dataCalculo);

        List<HistoricoPreco> historicos = gerarHistoricos(dataCalculo);
        
        historicoPrecoRepository.salvar(historicos);
        
        
        return historicos;
    }

    private List<HistoricoPreco> gerarHistoricos(LocalDate dataCalculo) {
        List<HistoricoPreco> historicos = new ArrayList<>();

        for (Produto produto : produtoRepository.listarTodos()) {
            double percentual = produto.getCategoria().getPercentualLucro();
            double precoVenda = produto.getPrecoVendaAtual();
            historicos.add(new HistoricoPreco(dataCalculo, produto, percentual, precoVenda));
        }

        return historicos;
    }
    
    // Datas
    private void validarIntervalo(LocalDate dataCalculo) {
        Optional<LocalDate> ultimoCalculo = buscarDataUltimoCalculo();
        if (ultimoCalculo.isEmpty()) {
            return;
        }

        LocalDate proximaDataPermitida = ultimoCalculo.get().plusDays(INTERVALO_MINIMO_DIAS);
        if (dataCalculo.isBefore(proximaDataPermitida)) {
            throw new RegraNegocioException("O novo cálculo ainda não pode ser realizado. Último cálculo em " + ultimoCalculo.get().format(FORMATO_DATA) + "; próximo permitido a partir de " + proximaDataPermitida.format(FORMATO_DATA) + ".");
        }

    }

    private Optional<LocalDate> buscarDataUltimoCalculo() {

        List<HistoricoPreco> historicos = historicoPrecoRepository.listarTodos();

        if (historicos == null || historicos.isEmpty()) {
            return Optional.empty();
        }

        LocalDate dataMaxima = null;

        for (HistoricoPreco historico : historicos) {
            LocalDate dataAtual = historico.getData();

            if (dataAtual != null) {
                if (dataMaxima == null || dataAtual.isAfter(dataMaxima)) {
                    dataMaxima = dataAtual;
                }
            }
        }

        return Optional.ofNullable(dataMaxima);

    }
}
