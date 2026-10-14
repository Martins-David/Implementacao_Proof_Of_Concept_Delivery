package br.ufes.presenterState.CategoriaPresenterState;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Categoria;
import br.ufes.presenter.CategoriaPresenter;

import javax.swing.JOptionPane;

public class InclusaoCategoriaPresenterState
        extends CategoriaPresenterState {

    public InclusaoCategoriaPresenterState(
            CategoriaPresenter presenter) {

        super(presenter);

        this.presenter.getView().getPnlCategoriaDetalhes()
                .setBorder(
                        javax.swing.BorderFactory.createTitledBorder(
                                "Detalhes de Categoria - Modo: Inclusão"
                        )
                );

        this.presenter.getView().getTxtNomeCategoria()
                .setEditable(true);

        this.presenter.getView().getTxtPercentualLucro()
                .setEditable(true);

        this.presenter.getView().getBtnNovo().setEnabled(false);
        this.presenter.getView().getBtnEditar().setEnabled(false);
        this.presenter.getView().getBtnExcluir().setEnabled(false);
        this.presenter.getView().getBtnFechar().setEnabled(false);

        this.presenter.getView().getBtnSalvar().setEnabled(true);
        this.presenter.getView().getBtnCancelar().setEnabled(true);
    }

    @Override
    public void salvar() {
        String nome = this.presenter.getView()
                .getTxtNomeCategoria().getText().trim();

        String textoLucro = this.presenter.getView()
                .getTxtPercentualLucro().getText().trim();

        if (nome.isEmpty()) {
            throw new RegraNegocioException(
                    "O nome da categoria é obrigatório."
            );
        }

        if (textoLucro.isEmpty()) {
            throw new RegraNegocioException(
                    "O percentual de lucro é obrigatório."
            );
        }

        double lucro;

        try {
            lucro = Double.parseDouble(
                    textoLucro.replace(",", ".")
            );
        } catch (NumberFormatException ex) {
            throw new RegraNegocioException(
                    "Percentual de lucro inválido."
            );
        }

        if (!Double.isFinite(lucro) || lucro <= 0) {
            throw new RegraNegocioException(
                    "O percentual de lucro deve ser maior que zero."
            );
        }

        for (Categoria categoria
                : this.presenter.getCategoriaRepository().listarTodos()) {

            if (categoria.getNome().trim()
                    .equalsIgnoreCase(nome)) {

                throw new RegraNegocioException(
                        "Já existe uma categoria com esse nome."
                );
            }
        }

        Categoria nova = new Categoria(
                this.presenter.gerarProximoId(),
                nome,
                lucro
        );

        this.presenter.getCategoriaRepository().salvar(
                java.util.Collections.singletonList(nova)
        );

        this.presenter.setCategoriaSelecionada(nova);
        this.presenter.carregarTabela();

        this.presenter.setEstado(
                new VisualizacaoCategoriaPresenterState(
                        this.presenter
                )
        );

        this.presenter.exibirDetalhes(nova);

        JOptionPane.showMessageDialog(
                this.presenter.getView(),
                "Categoria cadastrada com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    public void cancelar() {
        this.presenter.setCategoriaSelecionada(null);
        this.presenter.limparCampos();

        this.presenter.setEstado(
                new VisualizacaoCategoriaPresenterState(
                        this.presenter
                )
        );
    }
}
