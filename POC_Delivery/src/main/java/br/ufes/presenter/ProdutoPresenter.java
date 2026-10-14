package br.ufes.presenter;

import br.ufes.model.Categoria;
import br.ufes.model.Produto;
import br.ufes.presenterState.ProdutoPresenterState.EdicaoProdutoPresenterState;
import br.ufes.presenterState.ProdutoPresenterState.InclusaoProdutoPresenterState;

import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.repository.IProdutoRepository;

import br.ufes.view.ProdutoView;

import br.ufes.presenterState.ProdutoPresenterState.ProdutoPresenterState;
import br.ufes.presenterState.ProdutoPresenterState.VisualizacaoProdutoPresenterState;
import br.ufes.service.CalculadoraPrecoVendaService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;
import javax.swing.JOptionPane;

public class ProdutoPresenter {

    private final ProdutoView view;
    private final IProdutoRepository produtos;
    private final ICategoriaRepository categorias;
    private final IHistoricoPrecoRepository historicos;
    private final CalculadoraPrecoVendaService calculadoraPrecoVendaService;

    private ProdutoPresenterState estado;
    private Produto produto;

    public ProdutoPresenter(
            IProdutoRepository produtos,
            ICategoriaRepository categorias,
            IHistoricoPrecoRepository historicos,
            CalculadoraPrecoVendaService calculadoraPrecoVendaService,
            Produto produto,
            boolean modoVisualizacao) {

        Objects.requireNonNull(produtos, "Informe um repositório não nulo de produtos.");
        Objects.requireNonNull(categorias, "Informe um repositório não nulo de categorias.");
        Objects.requireNonNull(historicos, "Informe um repositório não nulo de historicos de preço.");
        Objects.requireNonNull(calculadoraPrecoVendaService, "Informe uma calculadora não nula.");

        this.produtos = produtos;
        this.categorias = categorias;
        this.historicos = historicos;
        this.calculadoraPrecoVendaService = calculadoraPrecoVendaService;

        this.produto = produto;
        this.view = new ProdutoView();

        carregarComboCategorias();
        preencherDadosProduto();

        if (modoVisualizacao && produto != null) {
            this.estado = new VisualizacaoProdutoPresenterState(this);
        }
        else if (produto != null) {
            this.estado = new EdicaoProdutoPresenterState(this);
        }
        else {
            this.estado = new InclusaoProdutoPresenterState(this);
        }

    }

    public void carregarComboCategorias() {
        this.view.getCmbCategoriaProduto().removeAllItems();

        List<Categoria> todasCategorias = this.categorias.listarTodos();

        for (int i = 0; i < todasCategorias.size(); i++) {
            this.view.getCmbCategoriaProduto().addItem(todasCategorias.get(i));
        }
    }

    public void preencherDadosProduto() {
        if (this.produto != null) {
            this.view.getTxtNomeProduto().setText(this.produto.getNome());
            this.view.getTxtPrecoCusto().setText(String.format("%.2f", this.produto.getPrecoCusto()).replace(",", "."));
            this.view.getCmbCategoriaProduto().setSelectedItem(this.produto.getCategoria());

            if (this.produto.getPercentualLucro() != 0) {
                this.view.getTxtMargemLucro().setText(String.format("%.2f", this.produto.getPercentualLucro()));
            }
            else {
                this.view.getTxtMargemLucro().setText("-");
            }

            if (this.produto.getPrecoVendaAtual() != 0) {
                this.view.getTxtPrecoVenda().setText(String.format("%.2f", this.produto.getPrecoVendaAtual()));
            }
            else {
                this.view.getTxtPrecoVenda().setText("-");
            }
        }
        else {
            this.view.getTxtNomeProduto().setText("");
            this.view.getTxtPrecoCusto().setText("");
            this.view.getTxtMargemLucro().setText("");
            this.view.getTxtPrecoVenda().setText("");
        }
    }

    private void configurarListeners() {
        this.view.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.salvar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.cancelar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.view.getBtnEditar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.editar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.fechar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.view.getBtnVisualizarHistoricoPrecos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.visualizarHistorico();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public ProdutoView getView() {
        return this.view;
    }

    public IProdutoRepository getProdutoRepository() {
        return this.produtos;
    }

    public ICategoriaRepository getCategoriaRepository() {
        return this.categorias;
    }

    public IHistoricoPrecoRepository getHistoricoRepository() {
        return this.historicos;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public void setEstado(ProdutoPresenterState estado) {
        this.estado = estado;
    }
}
