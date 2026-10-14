package br.ufes.repository;

import br.ufes.model.Categoria;
import br.ufes.service.ValidadorAtributos;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CategoriaRepository implements ICategoriaRepository {

    private List<Categoria> categorias;
    // private ProdutoRepository produtos;

    public CategoriaRepository(List<Categoria> categorias) {
        ValidadorAtributos.validarCategoria(categorias);

        this.categorias = categorias;
    }

    @Override
    public void salvar(List<Categoria> categorias) {
        ValidadorAtributos.validarCategoria(categorias);
        this.categorias.addAll(categorias);

    }

    @Override
    public void atualizar(Categoria categoriaAtualizada) {
        Objects.requireNonNull(categoriaAtualizada,
                "A categoria não pode ser nula.");

        for (int i = 0; i < categorias.size(); i++) {
            Categoria categoria = categorias.get(i);

            if (categoria.getIdCategoria()
                    == categoriaAtualizada.getIdCategoria()) {

                categorias.set(i, categoriaAtualizada);
                return;
            }
        }

        throw new IllegalArgumentException(
                "Categoria não encontrada para atualização."
        );
    }

    @Override
    public Categoria buscarCategoriaPorID(Long idCategoria) {
        ValidadorAtributos.validarId(idCategoria);

        for (Categoria categoria : categorias) {
            if (Objects.equals(categoria.getIdCategoria(), idCategoria)) {
                return categoria;
            }
        }

        throw new IllegalArgumentException("Não foi encontrado nenhuma categoria com o id informado.");

    }

    @Override
    public Categoria buscarCategoriaPorNome(String nomeCategoria) {

        ValidadorAtributos.validarNome(nomeCategoria);

        for (Categoria categoria : categorias) {
            if (Objects.equals(categoria.getNome().trim().toLowerCase(), nomeCategoria.trim().toLowerCase())) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Não foi encontrado nenhuma categoria com o nome informado.");

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

}
