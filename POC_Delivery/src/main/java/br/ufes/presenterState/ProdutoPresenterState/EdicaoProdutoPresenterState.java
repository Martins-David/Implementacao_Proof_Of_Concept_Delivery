package br.ufes.presenterState.ProdutoPresenterState;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Categoria;
import br.ufes.model.Produto;
import br.ufes.presenter.ProdutoPresenter;
import java.util.List;
import javax.swing.JOptionPane;

public class EdicaoProdutoPresenterState extends ProdutoPresenterState {

    public EdicaoProdutoPresenterState(ProdutoPresenter presenter) {
        super(presenter);
        this.presenter.getView().getTxtNomeProduto().setEditable(true);
        this.presenter.getView().getTxtPrecoCusto().setEditable(true);
        this.presenter.getView().getCmbCategoriaProduto().setEnabled(true);
        this.presenter.getView().getTxtMargemLucro().setEditable(false);
        this.presenter.getView().getTxtPrecoVenda().setEditable(false);

        this.presenter.getView().getBtnSalvar().setVisible(true);
        this.presenter.getView().getBtnCancelar().setVisible(true);
        this.presenter.getView().getBtnEditar().setVisible(false);
        this.presenter.getView().getBtnFechar().setVisible(false);
        this.presenter.getView().getBtnVisualizarHistoricoPrecos().setVisible(false);
    }

    @Override
    public void salvar() {
        Produto produto = this.presenter.getProduto();
        if (produto == null) {
            throw new RegraNegocioException("Nenhum produto em edição.");
        }

        String nome = this.presenter.getView().getTxtNomeProduto().getText();
        String textoPreco = this.presenter.getView().getTxtPrecoCusto().getText();
        if (textoPreco == null || textoPreco.trim().isEmpty()) {
            throw new RegraNegocioException("Preço de custo é obrigatório.");
        }
        Double precoCusto;
        try {
            precoCusto = Double.valueOf(Double.parseDouble(textoPreco.replace(",", ".")));
        } catch (NumberFormatException ex) {
            throw new RegraNegocioException("Preço de custo inválido.");
        }

        Categoria categoria = (Categoria) this.presenter.getView().getCmbCategoriaProduto().getSelectedItem();
        if (categoria == null) {
            throw new RegraNegocioException("Categoria é obrigatória.");
        }

        produto.setNome(nome);
        produto.setPrecoCusto(precoCusto);
        produto.setCategoria(categoria);
        
        this.presenter.getProdutoRepository().atualizar(produto);

        JOptionPane.showMessageDialog(this.presenter.getView(), "Item salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        this.presenter.setEstado(new VisualizacaoProdutoPresenterState(this.presenter));
    }

    @Override
    public void cancelar() {
        this.presenter.preencherDadosProduto();
        this.presenter.setEstado(new VisualizacaoProdutoPresenterState(this.presenter));
    }

    @Override
    public void fechar() {
        this.presenter.getView().dispose();
    }
}
