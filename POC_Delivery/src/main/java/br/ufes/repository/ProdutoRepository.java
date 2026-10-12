package br.ufes.repository;

import br.ufes.model.Categoria;
import br.ufes.model.Produto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ProdutoRepository implements IProdutoRepository {

    private List<Produto> produtos;

    public ProdutoRepository(List<Produto> produtos) {
        validarListaDeProdutos(produtos);
        this.produtos = produtos;
    }

    @Override
    public void salvar(List<Produto> produto) {

        validarListaDeProdutos(produto);
        this.produtos.addAll(produto);

    }

    @Override
    public List<Produto> buscarProdutoPorId(Long idProduto) {

        validarIdProduto(idProduto);

        List<Produto> produtosEncontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (Objects.equals(produto.getIdProduto(), idProduto)) {
                produtosEncontrados.add(produto);
            }
        }

        return produtosEncontrados;
    }

    @Override
    public List<Produto> buscarProdutoPorNome(String nomeProduto) {
        validarNome(nomeProduto);

        List<Produto> produtosEncontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (Objects.equals(produto.getNome().trim().toLowerCase(), nomeProduto.trim().toLowerCase())) {
                produtosEncontrados.add(produto);
            }
        }

        return produtosEncontrados;
    }

    @Override
    public List<Produto> buscarProdutoPorCategoria(String nomeCategoria) {

        validarNome(nomeCategoria);

        List<Produto> produtosEncontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (Objects.equals(produto.getCategoria().getNome().trim().toLowerCase(), nomeCategoria.trim().toLowerCase())) {
                produtosEncontrados.add(produto);
            }
        }

        return produtosEncontrados;

    }

    @Override
    public List<Produto> listarTodos() {
        return Collections.unmodifiableList(this.produtos);
    }

    @Override
    public void excluirProduto(Long idProduto) {

        validarIdProduto(idProduto);

        Iterator<Produto> produtosIterator = produtos.iterator();

        while (produtosIterator.hasNext()) {
            Produto produto = produtosIterator.next();
            if (produto.getIdProduto() == idProduto) {
                produtosIterator.remove();
            }
        }
    }

    @Override
    public boolean existePorCategoria(String nomeCategoria) {

        validarNome(nomeCategoria);

        for (Produto produto : produtos) {
            if (Objects.equals(produto.getCategoria().getNome().trim().toLowerCase(), nomeCategoria.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void validarListaDeProdutos(List<Produto> produtos) {
        Objects.requireNonNull(produtos, "Informe uma lista não nula de produtos.");
        if (produtos.isEmpty()) {
            throw new IllegalArgumentException("Informe uma lista de produtos com pelo menos um produto.");
        }
    }

    private void validarIdProduto(Long idProduto) {
        Objects.requireNonNull(idProduto, "O id do produto não pode ser nulo.");
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
