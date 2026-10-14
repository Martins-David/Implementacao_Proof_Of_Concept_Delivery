package br.ufes.presenterState.CategoriaPresenterState;

import br.ufes.presenter.CategoriaPresenter;

public abstract class CategoriaPresenterState {

    protected final CategoriaPresenter presenter;

    public CategoriaPresenterState(CategoriaPresenter presenter) {
        this.presenter = presenter;
    }

    public void novo() {
        throw new IllegalStateException(
                "Não é possível iniciar uma inclusão neste estado."
        );
    }

    public void editar() {
        throw new IllegalStateException(
                "Não é possível editar neste estado."
        );
    }

    public void excluir() {
        throw new IllegalStateException(
                "Não é possível excluir neste estado."
        );
    }

    public void salvar() {
        throw new IllegalStateException(
                "Não é possível salvar neste estado."
        );
    }

    public void cancelar() {
        throw new IllegalStateException(
                "Não é possível cancelar neste estado."
        );
    }

    public void fechar() {
        presenter.getView().dispose();
    }
}
