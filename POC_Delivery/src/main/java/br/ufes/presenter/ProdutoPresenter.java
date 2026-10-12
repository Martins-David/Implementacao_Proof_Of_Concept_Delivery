package br.ufes.presenter;

import br.ufes.exception.RegraNegocioException;
import br.ufes.model.Categoria;
import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;
import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.view.ProdutoView;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProdutoPresenter {

    private ProdutoView view;
    private ProdutoService produtoService;
    private ICategoriaRepository categoriaRepository;
    private IHistoricoPrecoRepository historicoPrecoRepository;
    private MainPresenter mainPresenter;

    private Produto produtoEmEdicao;
    private boolean modoEdicao = false;

    public ProdutoPresenter(MainPresenter mainPresenter, ProdutoService produtoService,
                            ICategoriaRepository categoriaRepository, IHistoricoPrecoRepository historicoPrecoRepository) {
        this.mainPresenter = mainPresenter;
        this.produtoService = produtoService;
        this.categoriaRepository = categoriaRepository;
        this.historicoPrecoRepository = historicoPrecoRepository;
        this.modoEdicao = false;
        this.produtoEmEdicao = null;

        this.view = new ProdutoView();
        configuraView();
    }

    public ProdutoPresenter(MainPresenter mainPresenter, ProdutoService produtoService,
                            ICategoriaRepository categoriaRepository, IHistoricoPrecoRepository historicoPrecoRepository,
                            Produto produtoParaEditar) {
        this.mainPresenter = mainPresenter;
        this.produtoService = produtoService;
        this.categoriaRepository = categoriaRepository;
        this.historicoPrecoRepository = historicoPrecoRepository;
        this.modoEdicao = true;
        this.produtoEmEdicao = produtoParaEditar;

        this.view = new ProdutoView();
        configuraView();
    }

    private void configuraView() {
        view.setVisible(false);

        view.getTxtMargemLucro().setEditable(false);
        view.getTxtPrecoVenda().setEditable(false);

        carregarComboCategorias();

        if (modoEdicao && produtoEmEdicao != null) {
            preencherCamposParaEdicao();
        } else {
            limparCampos();
        }

        view.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    salvar();
                } catch (RegraNegocioException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(view, "Informe um preço de custo válido.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fechar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });

        view.setVisible(true);
    }

    private void carregarComboCategorias() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Categoria categoria : categoriaRepository.listarTodos()) {
            model.addElement(categoria.getNome());
        }
        view.getCmbCategoriaProduto().setModel(model);
    }

    private void preencherCamposParaEdicao() {
        view.getTxtNomeProduto().setText(produtoEmEdicao.getNome());
        view.getTxtPrecoCusto().setText(String.format("%.2f", produtoEmEdicao.getPrecoCusto()));

        if (produtoEmEdicao.getCategoria() != null) {
            view.getCmbCategoriaProduto().setSelectedItem(produtoEmEdicao.getCategoria().getNome());
        }

        List<HistoricoPreco> historicos = historicoPrecoRepository.buscarPorIdProduto(produtoEmEdicao.getIdProduto());
        if (!historicos.isEmpty()) {
            HistoricoPreco ultimo = historicos.get(0);
            view.getTxtMargemLucro().setText(String.format("%.2f", ultimo.getPercentualLucroUtilizado()));
            view.getTxtPrecoVenda().setText(String.format("%.2f", ultimo.getPrecoVendaResultante()));
        } else {
            view.getTxtMargemLucro().setText("-");
            view.getTxtPrecoVenda().setText("-");
        }
    }

    private void salvar() {
        String nome = view.getTxtNomeProduto().getText();

        String textoPreco = view.getTxtPrecoCusto().getText();
        if (textoPreco == null || textoPreco.trim().isEmpty()) {
            throw new RegraNegocioException("O preço de custo é obrigatório.");
        }

        Double precoCusto = Double.parseDouble(textoPreco.replace(",", "."));
        String nomeCategoria = (String) view.getCmbCategoriaProduto().getSelectedItem();

        Categoria categoriaSelecionada = buscarCategoriaPorNome(nomeCategoria);

        if (produtoEmEdicao == null) {
            // MODO INCLUSÃO
            produtoService.cadastrar(nome, precoCusto, categoriaSelecionada);
        } else {
            // MODO EDIÇÃO
            produtoService.atualizar(produtoEmEdicao.getId(), nome, precoCusto, categoriaSelecionada);
        }

        JOptionPane.showMessageDialog(view, "Produto salvo com sucesso!");
        fechar();
    }

    private Categoria buscarCategoriaPorNome(String nomeCategoria) {
        if (nomeCategoria == null) {
            return null;
        }

        for (Categoria c : categoriaRepository.listar()) {
            if (c.getNome() != null && c.getNome().equalsIgnoreCase(nomeCategoria)) {
                return c;
            }
        }
        return null;
    }

    private void limparCampos() {
        view.getTxtNomeProduto().setText("");
        view.getTxtPrecoCusto().setText("");
        view.getTxtMargemLucro().setText("");
        view.getTxtPrecoVenda().setText("");
    }

    private void fechar() {
        view.dispose();
    }
}