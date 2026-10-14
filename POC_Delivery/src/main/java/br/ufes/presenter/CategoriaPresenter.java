package br.ufes.presenter;

import br.ufes.model.Categoria;
import br.ufes.presenterState.CategoriaPresenterState.CategoriaPresenterState;
import br.ufes.presenterState.CategoriaPresenterState.VisualizacaoCategoriaPresenterState;
import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IProdutoRepository;
import br.ufes.service.ExcluirCategoriaService;
import br.ufes.view.CategoriaView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CategoriaPresenter {

    private final CategoriaView view;
    private final ICategoriaRepository categoriaRepository;
    private final IProdutoRepository produtoRepository;

    private CategoriaPresenterState estado;
    private List<Categoria> listaAtual;
    private Categoria categoriaSelecionada;

    public CategoriaPresenter(
            ICategoriaRepository categoriaRepository) {

        this(categoriaRepository, null);
    }

    public CategoriaPresenter(
            ICategoriaRepository categoriaRepository,
            IProdutoRepository produtoRepository) {

        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.view = new CategoriaView();
        this.listaAtual = new ArrayList<>();

        configurarTabela();
        this.estado = new VisualizacaoCategoriaPresenterState(this);
        configurarListeners();
        carregarTabela();

        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    private void configurarTabela() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{
                    "ID",
                    "Categoria",
                    "Percentual de lucro (%)"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.view.getTblCategorias().setModel(modelo);
    }

    private void configurarListeners() {

        this.view.getTblCategorias()
                .getSelectionModel()
                .addListSelectionListener(
                        new ListSelectionListener() {
                    @Override
                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) {
                            return;
                        }

                        if (estado instanceof VisualizacaoCategoriaPresenterState) {

                            int linha = view.getTblCategorias()
                                    .getSelectedRow();

                            if (linha >= 0) {
                                int linhaModelo = view
                                        .getTblCategorias()
                                        .convertRowIndexToModel(linha);

                                DefaultTableModel modelo
                                        = (DefaultTableModel) view
                                                .getTblCategorias()
                                                .getModel();

                                Object valorId = modelo.getValueAt(
                                        linhaModelo, 0
                                );

                                if (valorId instanceof Number) {
                                    long id = ((Number) valorId)
                                            .longValue();

                                    try {
                                        categoriaSelecionada
                                                = categoriaRepository
                                                        .buscarCategoriaPorID(id);

                                        exibirDetalhes(
                                                categoriaSelecionada
                                        );
                                    } catch (IllegalArgumentException ex) {
                                        categoriaSelecionada = null;
                                        limparCampos();
                                    }
                                }
                            }
                            else {
                                categoriaSelecionada = null;
                                limparCampos();
                            }

                            setEstado(
                                    new VisualizacaoCategoriaPresenterState(
                                            CategoriaPresenter.this
                                    )
                            );
                        }
                    }
                }
                );

        this.view.getBtnNovo().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.novo();
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        this.view.getBtnEditar().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.editar();
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        this.view.getBtnExcluir().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.excluir();
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        this.view.getBtnSalvar().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.salvar();
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        this.view.getBtnCancelar().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.cancelar();
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });

        this.view.getBtnFechar().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    estado.fechar();
                } catch (Exception ex) {
                    mostrarErro(ex);
                }
            }
        });
    }

    private void mostrarErro(Exception ex) {
        String mensagem = ex.getMessage();

        if (mensagem == null || mensagem.trim().isEmpty()) {
            mensagem = "Ocorreu um erro inesperado.";
        }

        JOptionPane.showMessageDialog(
                view,
                mensagem,
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void carregarTabela() {
        this.listaAtual = new ArrayList<>(
                this.categoriaRepository.listarTodos()
        );

        DefaultTableModel modelo
                = (DefaultTableModel) this.view
                        .getTblCategorias().getModel();

        modelo.setRowCount(0);

        for (int i = 0; i < this.listaAtual.size(); i++) {
            Categoria categoria = this.listaAtual.get(i);

            modelo.addRow(new Object[]{
                categoria.getIdCategoria(),
                categoria.getNome(),
                String.format(
                java.util.Locale.forLanguageTag("pt-BR"),
                "%.2f",
                categoria.getPercentualLucro()
                )
            });
        }
    }

    public void exibirDetalhes(Categoria categoria) {
        if (categoria == null) {
            limparCampos();
            return;
        }

        this.view.getTxtNomeCategoria().setText(
                categoria.getNome()
        );

        this.view.getTxtPercentualLucro().setText(
                String.format(
                        java.util.Locale.forLanguageTag("pt-BR"),
                        "%.2f",
                        categoria.getPercentualLucro()
                )
        );
    }

    public void limparCampos() {
        this.view.getTxtNomeCategoria().setText("");
        this.view.getTxtPercentualLucro().setText("");
    }

    public long gerarProximoId() {
        long maiorId = 0;

        for (Categoria categoria
                : this.categoriaRepository.listarTodos()) {

            if (categoria.getIdCategoria() > maiorId) {
                maiorId = categoria.getIdCategoria();
            }
        }

        return maiorId + 1;
    }

    public void excluirCategoriaSelecionada() {
        if (this.categoriaSelecionada == null) {
            throw new IllegalStateException(
                    "Selecione uma categoria para excluir."
            );
        }

        Long id = this.categoriaSelecionada.getIdCategoria();

        if (this.produtoRepository != null) {
            ExcluirCategoriaService servico
                    = new ExcluirCategoriaService(
                            this.produtoRepository,
                            this.categoriaRepository
                    );

            servico.excluirCategoria(id);
        }
        else {
            this.categoriaRepository.excluirCategoria(id);
        }

        this.categoriaSelecionada = null;
        limparCampos();
        carregarTabela();

        setEstado(
                new VisualizacaoCategoriaPresenterState(this)
        );
    }

    public CategoriaView getView() {
        return this.view;
    }

    public ICategoriaRepository getCategoriaRepository() {
        return this.categoriaRepository;
    }

    public IProdutoRepository getProdutoRepository() {
        return this.produtoRepository;
    }

    public Categoria getCategoriaSelecionada() {
        return this.categoriaSelecionada;
    }

    public void setCategoriaSelecionada(Categoria categoria) {
        this.categoriaSelecionada = categoria;
    }

    public CategoriaPresenterState getEstado() {
        return this.estado;
    }

    public void setEstado(CategoriaPresenterState estado) {
        this.estado = estado;
    }
}
