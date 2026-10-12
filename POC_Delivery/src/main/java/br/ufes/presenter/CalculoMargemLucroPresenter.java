
package br.ufes.presenter;


import br.ufes.exception.RegraNegocioException;
import br.ufes.model.HistoricoPreco;
import br.ufes.service.CalculadoraPrecoVendaService;
import br.ufes.view.CalculoMargemLucroView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalculoMargemLucroPresenter {

    private CalculoMargemLucroView view;
    private CalculadoraPrecoVendaService calculoPrecoService;
    private MainPresenter mainPresenter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public CalculoMargemLucroPresenter(MainPresenter mainPresenter, CalculadoraPrecoVendaService calculadoraPrecoVendaService) {
        this.mainPresenter = mainPresenter;
        this.calculoPrecoService = calculoPrecoService;
        this.view = new CalculoMargemLucroView();
        configuraView();
    }

    private void configuraView() {
        view.setVisible(false);

        // Define a data atual formatada como valor padrão no campo
        view.getTxtDataCalculo().setText(LocalDate.now().format(formatter));

        configurarTabela();

        // Botão Calcular
        view.getBtnCalcular().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    calcular();
                } catch (RegraNegocioException ex) {
                    JOptionPane.showMessageDialog(view, ex.getMessage(), "Regra de Negócio", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Falha ao executar cálculo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

        view.setVisible(true);
    }

    private void configurarTabela() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Nome do produto", "Preço unitário", "Categoria", "Percentual de lucro", "Preço de venda calculado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getTblMargemLucro().setModel(model);
    }

    private void calcular() {
        String textoData = view.getTxtDataCalculo().getText();
        LocalDate dataCalculo;

        try {
            dataCalculo = LocalDate.parse(textoData, formatter);
        } catch (Exception e) {
            throw new RuntimeException("Informe uma data válida no formato dd/MM/yyyy.");
        }

        // Executa o cálculo global chamando o serviço (onde a regra dos 10 dias é validada)
        List<HistoricoPreco> resultados = calculoPrecoService.calcular(dataCalculo);

        popularTabela(resultados);
        JOptionPane.showMessageDialog(view, "Cálculo global de preços realizado com sucesso!");
    }

    private void popularTabela(List<HistoricoPreco> historicos) {
        DefaultTableModel model = (DefaultTableModel) view.getTblMargemLucro().getModel();
        model.setRowCount(0);

        for (HistoricoPreco historicoPreco : historicos) {
            String nomeProduto = historicoPreco.getProduto() != null ? historicoPreco.getProduto().getNome() : "-";
            String precoCusto = historicoPreco.getProduto() != null ? String.format("%.2f", historicoPreco.getProduto().getPrecoCusto()) : "-";
            String nomeCategoria = (historicoPreco.getProduto() != null && historicoPreco.getProduto().getCategoria() != null) 
                    ? historicoPreco.getProduto().getCategoria().getNome() : "-";

            model.addRow(new Object[]{
                    nomeProduto,
                    precoCusto,
                    nomeCategoria,
                    String.format("%.2f", historicoPreco.getPercentualLucroUtilizado()) + "%",
                    String.format("%.2f", historicoPreco.getPrecoVendaResultante())
            });
        }
    }

    private void fechar() {
        view.dispose();
    }
}
