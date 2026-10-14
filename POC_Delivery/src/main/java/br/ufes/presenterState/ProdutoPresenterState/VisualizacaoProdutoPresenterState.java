package br.ufes.presenterState.ProdutoPresenterState;

import br.ufes.presenter.HistoricoPrecoPresenter;
import br.ufes.presenter.ProdutoPresenter;

public class VisualizacaoProdutoPresenterState extends ProdutoPresenterState {
    public VisualizacaoProdutoPresenterState(ProdutoPresenter presenter) {
        super(presenter);
        this.presenter.getView().getTxtNomeProduto().setEditable(false);
        this.presenter.getView().getTxtPrecoCusto().setEditable(false);
        this.presenter.getView().getCmbCategoriaProduto().setEnabled(false);
        this.presenter.getView().getTxtMargemLucro().setEditable(false);
        this.presenter.getView().getTxtPrecoVenda().setEditable(false);

        this.presenter.getView().getBtnSalvar().setVisible(false);
        this.presenter.getView().getBtnCancelar().setVisible(false);
        this.presenter.getView().getBtnEditar().setVisible(true);
        this.presenter.getView().getBtnFechar().setVisible(true);
        this.presenter.getView().getBtnVisualizarHistoricoPrecos().setVisible(true);
    }

    @Override
    public void editar() {
        this.presenter.setEstado(new EdicaoProdutoPresenterState(this.presenter));
    }

    @Override
    public void fechar() {
        this.presenter.getView().dispose();
    }

    @Override
    public void visualizarHistorico() {
        if (this.presenter.getProduto() != null) {
            new HistoricoPrecoPresenter(this.presenter.getHistoricoRepository(), this.presenter.getProduto());
        }
    }
}
