package br.ufes.repository;

import br.ufes.model.Categoria;
import br.ufes.model.Produto;
import br.ufes.service.ValidadorAtributos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ProdutoRepository implements IProdutoRepository {

    private List<Produto> produtos;

    public ProdutoRepository(List<Produto> produtos) {
        
        Objects.requireNonNull(produtos, "A lista de poodutos não pode ser nula.");
        this.produtos = produtos;
    }

    @Override
    public void salvar(List<Produto> produtosNovos) {

        ValidadorAtributos.validarListaProdutos(produtosNovos);

        this.produtos.addAll(produtosNovos);

    }

    @Override
    public void atualizar(Produto produtoAtualizado) {
        ValidadorAtributos.validarListaProdutos(List.of(produtoAtualizado));

        for (int i = 0; i < produtos.size(); i++) {
            Produto produto = produtos.get(i);

            if (produto.getIdProduto()
                    == produtoAtualizado.getIdProduto()) {

                produtos.set(i, produtoAtualizado);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Produto não encontrado para atualização."
        );
    }

    @Override
    public Produto buscarProdutoPorId(Long idProduto) {

        ValidadorAtributos.validarId(idProduto);

        for (Produto produto : produtos) {
            if (Objects.equals(produto.getIdProduto(), idProduto)) {
                return produto;
            }
        }

        throw new IllegalArgumentException("Não foi encontrado nenhum produto com o id informado.");
    }

    @Override
    public List<Produto> buscarProdutoPorNome(String nomeProduto) {

        ValidadorAtributos.validarNome(nomeProduto);
        List<Produto> produtosEncontrados = new ArrayList<>();
        for (Produto produto : produtos) {
            if (Objects.equals(produto.getNome().trim().toLowerCase(), nomeProduto.trim().toLowerCase())) {
                produtosEncontrados.add(produto);
            }
            else if (produto.getNome().trim().toLowerCase().contains(nomeProduto.trim().toLowerCase())) {
                produtosEncontrados.add(produto);
            }
        }

        if (!produtosEncontrados.isEmpty()) {
            return produtosEncontrados;
        }

        throw new IllegalArgumentException("Não foi encontrado nenhum produto com o nome informado.");
    }

    @Override
    public List<Produto> buscarProdutoPorCategoria(String nomeCategoria) {

        ValidadorAtributos.validarNome(nomeCategoria);

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

        ValidadorAtributos.validarId(idProduto);

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

        ValidadorAtributos.validarNome(nomeCategoria);

        for (Produto produto : produtos) {
            if (Objects.equals(produto.getCategoria().getNome().trim().toLowerCase(), nomeCategoria.trim().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
