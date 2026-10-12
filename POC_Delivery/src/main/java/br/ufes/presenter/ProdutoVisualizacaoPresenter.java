package br.ufes.presenter;

import model.Categoria;
import model.HistoricoPreco;
import model.Produto;
import repositorio.ICategoriaRepository;
import repositorio.IHistoricoPrecoRepository;
import view.AdicionaProdutoView;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProdutoVisualizacaoPresenter {

    private AdicionaProdutoView view;
    private Produto produto;
    private MainPresenter mainPresenter;
    private ICategoriaRepository categoriaRepository;
    private IHistoricoPrecoRepository historicoPrecoRepository;

    public ProdutoVisualizacaoPresenter(MainPresenter mainPresenter, Produto produto, 
                                        ICategoriaRepository categoriaRepository, 
                                        IHistoricoPrecoRepository historicoPrecoRepository) {
        this.mainPresenter = mainPresenter;
        this.produto = produto;
        this.categoriaRepository = categoriaRepository;
        this.historicoPrecoRepository = historicoPrecoRepository;
        this.view = new AdicionaProdutoView();
        configuraView();
    }

    private void configuraView() {
        view.setVisible(false);

        // Popula as categorias no JComboBox antes de selecionar e bloquear os campos
        carregarComboCategorias();

        // Desabilita os campos editáveis para garantir modo somente leitura
        bloquearCampos();
        carregarDadosProduto();

        // Botão Salvar fica oculto/desabilitado na visualização
        view.getBtnSalvar().setVisible(false);

        // Botão Fechar
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
        
        if (categoriaRepository != null) {
            List<Categoria> categorias = categoriaRepository.listar();
            for (Categoria c : categorias) {
                model.addElement(c.getNome());
            }
        }
        
        view.getCmbCategoriaProduto().setModel(model);
    }

    private void bloquearCampos() {
        view.getTxtNomeProduto().setEditable(false);
        view.getTxtPrecoCusto().setEditable(false);
        view.getCmbCategoriaProduto().setEnabled(false);
        view.getTxtMargemLucro().setEditable(false);
        view.getTxtPrecoVenda().setEditable(false);
    }

    private void carregarDadosProduto() {
        view.getTxtNomeProduto().setText(produto.getNome());
        view.getTxtPrecoCusto().setText(String.format("%.2f", produto.getPrecoCusto()));
        
        if (produto.getCategoria() != null && produto.getCategoria().getNome() != null) {
            view.getCmbCategoriaProduto().setSelectedItem(produto.getCategoria().getNome());
        }

        // Busca o histórico do produto para exibir a margem e o preço do último cálculo
        List<HistoricoPreco> historicos = historicoPrecoRepository.buscarPorProdutoId(produto.getId());
        if (!historicos.isEmpty()) {
            HistoricoPreco ultimo = historicos.get(0);
            view.getTxtMargemLucro().setText(String.format("%.2f", ultimo.getPercentualLucro()));
            view.getTxtPrecoVenda().setText(String.format("%.2f", ultimo.getPrecoVenda()));
        } else {
            view.getTxtMargemLucro().setText("-");
            view.getTxtPrecoVenda().setText("-");
        }
    }

    private void fechar() {
        view.dispose();
    }
}