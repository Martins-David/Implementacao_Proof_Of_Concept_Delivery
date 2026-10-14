package br.ufes.presenterState.CategoriaPresenterState;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Categoria;
import br.ufes.presenter.CategoriaPresenter;

import javax.swing.JOptionPane;

public class VisualizacaoCategoriaPresenterState
        extends CategoriaPresenterState {

    public VisualizacaoCategoriaPresenterState(
            CategoriaPresenter presenter) {

        super(presenter);

        this.presenter.getView().getPnlCategoriaDetalhes()
                .setBorder(
                        javax.swing.BorderFactory.createTitledBorder(
                                "Detalhes de Categoria - Modo: Visualização"
                        )
                );

        this.presenter.getView().getTxtNomeCategoria()
                .setEditable(false);

        this.presenter.getView().getTxtPercentualLucro()
                .setEditable(false);

        this.presenter.getView().getBtnNovo().setEnabled(true);

        boolean temSelecao
                = this.presenter.getCategoriaSelecionada() != null;

        this.presenter.getView().getBtnEditar()
                .setEnabled(temSelecao);

        this.presenter.getView().getBtnExcluir()
                .setEnabled(temSelecao);

        this.presenter.getView().getBtnFechar().setEnabled(true);
        this.presenter.getView().getBtnSalvar().setEnabled(false);
        this.presenter.getView().getBtnCancelar().setEnabled(false);
    }

    @Override
    public void novo() {
        this.presenter.setCategoriaSelecionada(null);
        this.presenter.limparCampos();

        this.presenter.setEstado(
                new InclusaoCategoriaPresenterState(
                        this.presenter
                )
        );
    }

    @Override
    public void editar() {
        if (this.presenter.getCategoriaSelecionada() == null) {
            throw new RegraNegocioException(
                    "Selecione uma categoria para editar."
            );
        }

        this.presenter.setEstado(
                new EdicaoCategoriaPresenterState(
                        this.presenter
                )
        );
    }

    @Override
    public void excluir() {
        Categoria selecionada
                = this.presenter.getCategoriaSelecionada();

        if (selecionada == null) {
            throw new RegraNegocioException(
                    "Selecione uma categoria para excluir."
            );
        }

        int opcao = JOptionPane.showConfirmDialog(
                this.presenter.getView(),
                "Deseja realmente excluir a categoria '"
                + selecionada.getNome() + "'?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcao != JOptionPane.YES_OPTION) {
            return;
        }

        this.presenter.excluirCategoriaSelecionada();

        JOptionPane.showMessageDialog(
                this.presenter.getView(),
                "Categoria excluída com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void fechar() {
        this.presenter.getView().dispose();
    }
}
