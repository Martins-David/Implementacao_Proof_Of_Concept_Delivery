package br.ufes.presenter;

import br.ufes.model.Produto;
import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.repository.IProdutoRepository;
import br.ufes.service.CalculadoraPrecoVendaService;
import br.ufes.view.BuscaProdutoView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class BuscarProdutoPresenter {

    private final BuscaProdutoView view;
    private final IProdutoRepository produtos;
    private final ICategoriaRepository categorias;
    private final IHistoricoPrecoRepository historicos;
    private final CalculadoraPrecoVendaService calculadoraPrecoVendaService;

    private List<Produto> listaAtual = new ArrayList<>();

    public BuscarProdutoPresenter(
            IProdutoRepository produtos,
            ICategoriaRepository categorias,
            IHistoricoPrecoRepository historicos,
            CalculadoraPrecoVendaService calculadoraPrecoVendaService
    ) {

        Objects.requireNonNull(produtos, "Informe um repositório não nulo de produtos.");
        Objects.requireNonNull(categorias, "Informe um repositório não nulo de categorias.");
        Objects.requireNonNull(historicos, "Informe um repositório não nulo de historicos de preço.");
        Objects.requireNonNull(calculadoraPrecoVendaService, "Informe uma calculadora não nula.");

        this.produtos = Objects.requireNonNull(produtos);
        this.categorias = Objects.requireNonNull(categorias);
        this.historicos = Objects.requireNonNull(historicos);
        this.calculadoraPrecoVendaService = calculadoraPrecoVendaService;

        this.view = new BuscaProdutoView();

        configurarListeners();
        carregarTabela("");

        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    private void configurarListeners() {

        this.view.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarTabela(view.getTxtBuscar().getText());
            }
        });

        this.view.getTblProdutos().getSelectionModel()
                .addListSelectionListener(new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            boolean selecionado
                                    = view.getTblProdutos().getSelectedRow() >= 0;

                            view.getBtnVisualizar().setEnabled(selecionado);
                        }
                    }
                });

        this.view.getBtnNovo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProdutoPresenter(
                        produtos,
                        categorias,
                        historicos,
                        calculadoraPrecoVendaService,
                        null,
                        false
                );

                carregarTabela(view.getTxtBuscar().getText());
            }
        });

        this.view.getBtnVisualizar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int linha = view.getTblProdutos().getSelectedRow();

                if (linha >= 0) {
                    Produto produtoSelecionado = listaAtual.get(linha);

                    new ProdutoPresenter(
                            produtos,
                            categorias,
                            historicos,
                            calculadoraPrecoVendaService,
                            produtoSelecionado,
                            true
                    );
                }
            }
        });

        this.view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }

    private void carregarTabela(String termo) {

        String tipo = (String) view.getCmbBoxBuscar().getSelectedItem();

        if (termo == null || termo.trim().isEmpty()) {
            listaAtual = produtos.listarTodos();

        }
        else if ("Categoria".equalsIgnoreCase(tipo)) {
            listaAtual = produtos.buscarProdutoPorCategoria(termo.trim());

        }
        else {
            listaAtual = produtos.buscarProdutoPorNome(termo.trim());
        }

        DefaultTableModel modelo
                = (DefaultTableModel) view.getTblProdutos().getModel();

        modelo.setRowCount(0);

        for (Produto p : listaAtual) {

            String categoria = p.getCategoria() != null
                    ? p.getCategoria().getNome()
                    : "-";

            modelo.addRow(new Object[]{
                p.getIdProduto(),
                p.getNome(),
                String.format("R$ %.2f", p.getPrecoCusto()),
                categoria,
                String.format("%.2f", p.getPercentualLucro()),
                String.format("R$ %.2f", p.getPrecoVendaAtual())
            });
        }

        view.getBtnVisualizar().setEnabled(false);
    }
}
