package br.ufes.presenterState;

import br.ufes.presenter.ProdutoPresenter;

public abstract class ProdutoPresenterState {
    protected final ProdutoPresenter presenter;

    public ProdutoPresenterState(ProdutoPresenter presenter) {
        this.presenter = presenter;
    }

    public void salvar() {
        throw new RuntimeException("Não é possível salvar neste estado.");
    }

    public void editar() {
        throw new RuntimeException("Não é possível editar neste estado.");
    }

    public void fechar() {
        throw new RuntimeException("Não é possível fechar neste estado.");
    }

    public void cancelar() {
        throw new RuntimeException("Não é possível cancelar neste estado.");
    }

    public void visualizarHistorico() {
        throw new RuntimeException("Não é possível visualizar histórico neste estado.");
    }
}
