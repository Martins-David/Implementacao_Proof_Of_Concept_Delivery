package br.ufes.repository;

import br.ufes.model.Categoria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CategoriaRepository implements ICategoriaRepository {

    private List<Categoria> categorias;
    // private ProdutoRepository produtos;

    public CategoriaRepository(List<Categoria> categorias) {
        validarListaDeCategorias(categorias);

        this.categorias = categorias;
    }

    @Override
    public void salvar(List<Categoria> categorias) {
        validarListaDeCategorias(categorias);
        this.categorias.addAll(categorias);

    }

    @Override
    public List<Categoria> buscarCategoriaPorID(Long idCategoria) {
        validarIdCategoria(idCategoria);

        List<Categoria> categoriasEncontradas = new ArrayList<>();
        for (Categoria categoria : categorias) {
            if (Objects.equals(categoria.getIdCategoria(), idCategoria)) {
                categoriasEncontradas.add(categoria);
            }
        }

        return categoriasEncontradas;
    }

    @Override
    public List<Categoria> buscarCategoriaPorNome(String nomeCategoria) {

        validarNome(nomeCategoria);
      
        List<Categoria> categoriasEncontradas = new ArrayList<>();

        for (Categoria categoria : categorias) {
            if (Objects.equals(categoria.getNome().trim().toLowerCase(), nomeCategoria.trim().toLowerCase())) {
                categoriasEncontradas.add(categoria);
            }
        }

        return categoriasEncontradas;
    }

    @Override
    public List<Categoria> listarTodos() {
        return Collections.unmodifiableList(this.categorias);
    }

    @Override
    public void excluirCategoria(Long idCategoria) {
        try {
            verificarExistencia(idCategoria);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Iterator<Categoria> categoriaIterator = categorias.iterator();

        while (categoriaIterator.hasNext()) {
            Categoria categoria = categoriaIterator.next();
            if (categoria.getIdCategoria() == idCategoria) {
                categoriaIterator.remove();
            }
        }
    }

    private void verificarExistencia(long id) throws Exception {
        for (Categoria categoria : categorias) {
            if (categoria.getIdCategoria() == id) {
                return;
            }
        }

        throw new Exception("Categoria não encontrada para o id: " + id);
    }

    private void validarListaDeCategorias(List<Categoria> categorias) {
        Objects.requireNonNull(categorias, "Informe uma lista não nula de categorias.");
        if (categorias.isEmpty()) {
            throw new IllegalArgumentException("Informe uma lista com pelo menos uma categoria.");
        }
    }

    private void validarIdCategoria(Long idCategoria) {
        Objects.requireNonNull(idCategoria, "Informe um codigo identificador não nulo.");
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("O id do produto deve ser maior que zero.");
        }
    }

    private void validarNome(String nome) {
        Objects.requireNonNull(nome, "Informe um nome não nulo.");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Informe um nome não vazio.");
        }
    }

    
    
    /*
    private boolean existeAssociacaoProduto(){
              //    faz algo para verificar se há produtos cadastrados nessa categoria
        }
     */
    
    
    
}
