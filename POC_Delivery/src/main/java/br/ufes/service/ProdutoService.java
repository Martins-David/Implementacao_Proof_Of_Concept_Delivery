/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufes.service;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Categoria;
import br.ufes.model.Produto;
import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IProdutoRepository;
import java.util.List;
import java.util.Objects;

public class ProdutoService {

    private final IProdutoRepository produtoRepository;
    private final ICategoriaRepository categoriaRepository;

    public ProdutoService(IProdutoRepository produtoRepository, ICategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Produto cadastrar(Long idProduto, String nome, Double precoCusto, Categoria categoria) {
        validarIdProduto(idProduto);
        validarNome(nome);
        validarPrecoCusto(precoCusto);
        Categoria categoriaExistente = obterCategoriaExistente(categoria);

        Produto novoProduto = new Produto(idProduto, nome.trim(), precoCusto, categoriaExistente);
        produtoRepository.salvar(List.of(novoProduto));

        return novoProduto;
    }

    public Produto atualizar(Long id, String nome, Double precoCusto, Categoria categoria) {
        if (id == null || id <= 0) {
            throw new RegraNegocioException("Produto não encontrado.");
        }

        Produto produto = buscarProdutoPorId(id);

        validarNome(nome);
        validarPrecoCusto(precoCusto);
        Categoria categoriaExistente = obterCategoriaExistente(categoria);

        produto.setNome(nome.trim());
        produto.setPrecoCusto(precoCusto);
        produto.setCategoria(categoriaExistente);

        produtoRepository.salvar(List.of(produto));

        return produto;
    }

    private Produto buscarProdutoPorId(Long id) {

        validarIdProduto(id);

        throw new RegraNegocioException("Produto não encontrado.");
    }

    private void validarIdProduto(Long idProduto) {
        Objects.requireNonNull(idProduto, "O id do produto não pode ser nulo.");
        if (idProduto <= 0) {
            throw new IllegalArgumentException("O id do produto deve ser maior que zero.");
        }
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("O nome do produto é obrigatório.");
        }
    }

    private void validarPrecoCusto(Double precoCusto) {
        if (precoCusto == null) {
            throw new RegraNegocioException("O preço de custo é obrigatório.");
        }
        if (!Double.isFinite(precoCusto) || precoCusto <= 0) {
            throw new RegraNegocioException("O preço de custo deve ser maior que zero.");
        }
    }

    private Categoria obterCategoriaExistente(Categoria categoria) {
        

        throw new RegraNegocioException("A categoria informada não existe.");
    }
}
