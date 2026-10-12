package br.ufes.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.repository.IProdutoRepository;
import br.ufes.view.BuscaProdutoView;

public class BuscaProdutoPresenter {

    private final IProdutoRepository produtoRepository;
    private final IHistoricoPrecoRepository historicoPrecoRepository;
    private final BuscaProdutoView view;
    private final MainPresenter mainPresenter;

    public BuscaProdutoPresenter(MainPresenter mainPresenter, IProdutoRepository produtoRepository, IHistoricoPrecoRepository historicoPrecoRepository) {
        this.mainPresenter = mainPresenter;
        this.produtoRepository = produtoRepository;
        this.historicoPrecoRepository = historicoPrecoRepository;
        this.view = new BuscaProdutoView();
        configurarView();
    }

    private void configurarView() {

        view.setVisible(false);

        view.getCmbBoxBuscar().setModel(
                new javax.swing.DefaultComboBoxModel<>(new String[]{"Nome do produto", "Categoria"})
        );

        configurarTabela();
        carregarTodosProdutos();

        // O botão Visualizar inicia desabilitado conforme o Requisito 8.2
        view.getBtnVisualizar().setEnabled(false);

        view.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    buscar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });

        view.getBtnNovo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPresenter.abrirIncluirProdutos();
            }
        });

        // 1. Habilita/Desabilita o botão conforme a seleção na tabela (sem ternário)
        view.getTblProdutos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int linhaSelecionada = view.getTblProdutos().getSelectedRow();

                    if (linhaSelecionada != -1) {
                        view.getBtnVisualizar().setEnabled(true);
                    } else {
                        view.getBtnVisualizar().setEnabled(false);
                    }
                }
            }
        });

        // 2. Ação do botão Visualizar (abrir a tela de visualização do produto)
        view.getBtnVisualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    visualizarProdutoSelecionado();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha ao abrir visualização: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });

        view.setVisible(true);
    }

    private void configurarTabela() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Nome do produto", "Preço de custo", "Categoria", "Margem de lucro (%)", "Preço de venda"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getTblProdutos().setModel(model);
    }

    private void carregarTodosProdutos() {
        popularTabela(produtoRepository.obterTodos());
    }

    private void buscar() {
        String termo = view.getTxtBuscar().getText();
        String criterio = (String) view.getCmbBoxBuscar().getSelectedItem();

        List<Produto> resultado;

        if ("Categoria".equals(criterio)) {
            resultado = produtoRepository.buscarPorCategoria(termo);
        } else {
            resultado = produtoRepository.buscarPorNome(termo);
        }

        popularTabela(resultado);
        
        // Garante o estado correto do botão ao filtrar a tabela
        int linhaSelecionada = view.getTblProdutos().getSelectedRow();
        if (linhaSelecionada != -1) {
            view.getBtnVisualizar().setEnabled(true);
        } else {
            view.getBtnVisualizar().setEnabled(false);
        }
    }

    private void popularTabela(List<Produto> produtos) {
        DefaultTableModel model = (DefaultTableModel) view.getTblProdutos().getModel();
        model.setRowCount(0);

        for (Produto produto : produtos) {
            List<HistoricoPreco> historicos = historicoPrecoRepository.buscarPorProdutoId(produto.getId());

            String margem = "";
            String precoVenda = "";

            if (!historicos.isEmpty()) {
                HistoricoPreco ultimo = historicos.get(0);
                margem = String.format("%.2f", ultimo.getPercentualLucro());
                precoVenda = String.format("%.2f", ultimo.getPrecoVenda());
            }

            String nomeCategoria = "-";
            if (produto.getCategoria() != null && produto.getCategoria().getNome() != null) {
                nomeCategoria = produto.getCategoria().getNome();
            }

            model.addRow(new Object[]{
                produto.getNome(),
                String.format("%.2f", produto.getPrecoCusto()),
                nomeCategoria,
                margem,
                precoVenda
            });
        }
    }

    private void visualizarProdutoSelecionado() {
        int linhaSelecionada = view.getTblProdutos().getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(view, "Selecione um produto na tabela para visualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeProduto = (String) view.getTblProdutos().getValueAt(linhaSelecionada, 0);
        List<Produto> produtos = produtoRepository.buscarPorNome(nomeProduto);

        if (produtos != null) {
            for (Produto p : produtos) {
                if (p.getNome() != null && p.getNome().equalsIgnoreCase(nomeProduto)) {
                    mainPresenter.abrirVisualizarProduto(p);
                    break;
                }
            }
        }
    }
}