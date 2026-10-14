package br.ufes.presenter;

import br.ufes.model.HistoricoPreco;
import br.ufes.service.CalculadoraPrecoVendaService;
import br.ufes.service.PrazoCalculoServiceMock;
import br.ufes.view.CalculoMargemLucroView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class CalculoMargemLucroPresenter {

    private final CalculoMargemLucroView view;
    private final CalculadoraPrecoVendaService calculadoraPrecoVendaService;

    private final DateTimeFormatter formatoData = PrazoCalculoServiceMock.FORMATO_DATA;

    public CalculoMargemLucroPresenter(
            CalculadoraPrecoVendaService calculadoraPrecoVendaService) {

        Objects.requireNonNull(calculadoraPrecoVendaService, "Informe uma calculadora não nula.");
        
        
        this.calculadoraPrecoVendaService = calculadoraPrecoVendaService;
        this.view = new CalculoMargemLucroView();

        this.view.getTxtDataCalculo().setText(
                LocalDate.now().format(formatoData)
        );

        configurarListeners();

        this.view.setLocationRelativeTo(null);
        this.view.setVisible(true);
    }

    private void configurarListeners() {

        this.view.getBtnCalcular().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calcular();
            }
        });

        this.view.getBtnFechar().addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
    }

    private void calcular() {
        try {
            String textoData =
                    this.view.getTxtDataCalculo().getText().trim();

            LocalDate data = LocalDate.parse(textoData, formatoData);

            /*
             * Ajuste a chamada abaixo conforme a assinatura real
             * do serviço no seu projeto.
             */
            List<HistoricoPreco> resultados =
                    this.calculadoraPrecoVendaService
                            .calcular(data);

            DefaultTableModel modelo =
                    (DefaultTableModel) this.view
                            .getTblMargemLucro().getModel();

            modelo.setRowCount(0);

            for (int i = 0; i < resultados.size(); i++) {
                HistoricoPreco historico = resultados.get(i);

                modelo.addRow(new Object[]{
                    "Produto ID: " + historico.getProduto().getIdProduto(),
                    "-",
                    "-",
                    String.format(
                            java.util.Locale.forLanguageTag("pt-BR"),
                            "%.2f %% ",
                            historico.getPercentualLucroUtilizado()
                    ),
                    String.format(
                            java.util.Locale.forLanguageTag("pt-BR"),
                            "R$ %.2f",
                            historico.getPrecoVendaResultante()
                    )
                });
            }

            JOptionPane.showMessageDialog(
                    this.view,
                    "Cálculo global executado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this.view,
                    ex.getMessage() == null
                            ? "Não foi possível executar o cálculo."
                            : ex.getMessage(),
                    "Bloqueio de Operação",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }
}