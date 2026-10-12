/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufes.presenter;

/**
 *
 * @author paulo.campos
 */

import exception.RegraNegocioException;
import model.Categoria;
import service.CategoriaService;
import view.CategoriaView;

import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author paulo.campos
 */
public class CategoriaPresenter {

    private CategoriaView view;
    private CategoriaService categoriaService;
    private MainPresenter mainPresenter;
    private Categoria categoriaSelecionada;
    private boolean modoEdicao = false;

    public CategoriaPresenter(MainPresenter mainPresenter, CategoriaService categoriaService) {
        this.mainPresenter = mainPresenter;
        this.categoriaService = categoriaService;
        this.view = new CategoriaView();
        configuraView();
    }

    private void configuraView() {
        view.setVisible(false);

        configurarTabela();
        carregarCategorias();
        modoVisualizacao();

        // Botão Novo
        view.getBtnNovo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    novo();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });

        // Botão Editar
        view.getBtnEditar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    editar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });

        // Botão Excluir
        view.getBtnExcluir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    excluir();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Botão Salvar
        view.getBtnSalvar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    salvar();
                } catch (RegraNegocioException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Erro", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha ao salvar: " + ex.getMessage());
                }
            }
        });

        // Botão Cancelar
        view.getBtnCancelar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    cancelar();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha: " + ex.getMessage());
                }
            }
        });

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

        // Listener de Seleção da Tabela
        view.getTblCategorias().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selecionarCategoriaDaTabela();
                }
            }
        });

        view.setVisible(true);
    }

    private void configurarTabela() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Categoria", "Percentual de lucro (%)"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getTblCategorias().setModel(model);
    }

    private void carregarCategorias() {
        DefaultTableModel model = (DefaultTableModel) view.getTblCategorias().getModel();
        model.setRowCount(0);

        List<Categoria> lista = categoriaService.listar();
        for (Categoria c : lista) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    String.format("%.2f", c.getPercentualLucro())
            });
        }
    }

    private void selecionarCategoriaDaTabela() {
        int linha = view.getTblCategorias().getSelectedRow();
        if (linha != -1) {
            long id = (long) view.getTblCategorias().getValueAt(linha, 0);
            List<Categoria> lista = categoriaService.listar();
            for (Categoria c : lista) {
                if (c.getId() == id) {
                    this.categoriaSelecionada = c;
                    exibirCategoriaNosCampos(c);
                    break;
                }
            }
        }
    }

    private void exibirCategoriaNosCampos(Categoria c) {
        view.getTxtNomeCategoria().setText(c.getNome());
        view.getTxtPercentualLucro().setText(String.format("%.2f", c.getPercentualLucro()));
    }

    private void novo() {
        this.categoriaSelecionada = null;
        this.modoEdicao = false;
        limparCampos();
        modoEdicaoOuInclusao();
    }

    private void editar() {
        if (categoriaSelecionada == null) {
            throw new RuntimeException("Selecione uma categoria na tabela para editar.");
        }
        this.modoEdicao = true;
        modoEdicaoOuInclusao();
    }

    private void salvar() {
        String nome = view.getTxtNomeCategoria().getText();
        double percentual = 0.0;

        try {
            percentual = Double.parseDouble(view.getTxtPercentualLucro().getText().replace(",", "."));
        } catch (Exception e) {
            throw new RuntimeException("Informe um percentual válido no formato 99.99");
        }

        if (modoEdicao && categoriaSelecionada != null) {
            categoriaService.atualizar(categoriaSelecionada.getId(), nome, percentual);
            JOptionPane.showMessageDialog(view, "Categoria atualizada com sucesso!");
        } else {
            categoriaService.cadastrar(nome, percentual);
            JOptionPane.showMessageDialog(view, "Categoria salva com sucesso!");
        }

        carregarCategorias();
        modoVisualizacao();
    }

    private void excluir() {
        if (categoriaSelecionada == null) {
            throw new RuntimeException("Selecione uma categoria na tabela para excluir.");
        }

        int opcao = JOptionPane.showConfirmDialog(view, 
                "Deseja realmente excluir a categoria \"" + categoriaSelecionada.getNome() + "\"?\nEsta operação não poderá ser desfeita.", 
                "Confirmação de Exclusão", 
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            categoriaService.excluir(categoriaSelecionada.getId());
            JOptionPane.showMessageDialog(view, "Categoria excluída com sucesso!");
            carregarCategorias();
            limparCampos();
            modoVisualizacao();
        }
    }

    private void cancelar() {
        limparCampos();
        modoVisualizacao();
    }

    private void fechar() {
        view.dispose();
    }

    private void limparCampos() {
        view.getTxtNomeCategoria().setText("");
        view.getTxtPercentualLucro().setText("");
        view.getTblCategorias().clearSelection();
        this.categoriaSelecionada = null;
    }

    private void modoVisualizacao() {
        view.getTxtNomeCategoria().setEnabled(false);
        view.getTxtPercentualLucro().setEnabled(false);

        view.getBtnNovo().setEnabled(true);
        view.getBtnEditar().setEnabled(true);
        view.getBtnExcluir().setEnabled(true);
        view.getBtnFechar().setEnabled(true);

        view.getBtnSalvar().setEnabled(false);
        view.getBtnCancelar().setEnabled(false);
    }

    private void modoEdicaoOuInclusao() {
        view.getTxtNomeCategoria().setEnabled(true);
        view.getTxtPercentualLucro().setEnabled(true);

        view.getBtnNovo().setEnabled(false);
        view.getBtnEditar().setEnabled(false);
        view.getBtnExcluir().setEnabled(false);
        view.getBtnFechar().setEnabled(false);

        view.getBtnSalvar().setEnabled(true);
        view.getBtnCancelar().setEnabled(true);
    }
}
