package br.ufes.service;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Categoria;
import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;
import java.util.List;
import java.util.Objects;

public class ValidadorAtributos {

    public static final void validarId(Long id) {
        Objects.requireNonNull(id, "O id é obrigatório.");
        if (id <= 0) {
            throw new RegraNegocioException("O id deve ser maior que zero.");
        }

    }

    public static final void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome é obrigatório.");
        }
    }

    public static final void validarCategoria(List<Categoria> categorias) {

    }

    public static final void validarPercentualLucro(double percentualLucro) {
        Objects.requireNonNull(percentualLucro, "O percentual de lucro é obrigatório.");
        if (!Double.isFinite(percentualLucro) || percentualLucro <= 0) {
            throw new RegraNegocioException("O percentual de lucro deve ser maior que zero.");
        }

    }

    public static final void validarListaProdutos(List<Produto> produtos) {

        Objects.requireNonNull(produtos, "O produto é obrigatório.");
        if (produtos.isEmpty()) {
            throw new IllegalArgumentException("Informe uma lista com pelo menos um produto.");
        }
    }
    
    public static final void validarPrecoCusto(double precoCusto) {

        Objects.requireNonNull(precoCusto, "O preço de custo é obrigatório.");
        if (!Double.isFinite(precoCusto) || precoCusto <= 0) {
            throw new RegraNegocioException("O preço de custo deve ser maior que zero.");
        }

    }

    public static final void validarHistoricoPreco(List<HistoricoPreco> historicos) {
        Objects.requireNonNull(historicos, "O historico de preços é obrigatório.");
        if (historicos.isEmpty()) {
            throw new IllegalArgumentException("Informe uma lista com pelo menos um historico de preço.");
        }
    }
}
