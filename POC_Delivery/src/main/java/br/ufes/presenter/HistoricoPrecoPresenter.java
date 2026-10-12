package br.ufes.presenter;

import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.view.HistoricoPrecoView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoricoPrecoPresenter {

    private HistoricoPrecoView view;
    private Produto produto;
    private IHistoricoPrecoRepository historicoPrecoRepository;
    private MainPresenter mainPresenter;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public HistoricoPrecoPresenter(MainPresenter mainPresenter, Produto produto, IHistoricoPrecoRepository historicoPrecoRepository) {
        this.mainPresenter = mainPresenter;
        this.produto = produto;
        this.historicoPrecoRepository = historicoPrecoRepository;
        this.view = new HistoricoPrecoView();
        configuraView();
    }

    private void configuraView() {
        view.setVisible(false);

        // Identificadores superiores do produto
        view.getTxtNomeProduto().setText(produto.getNome());
        view.getTxtNomeProduto().setEditable(false);
        
        String nomeCat = produto.getCategoria() != null ? produto.getCategoria().getNome() : "-";
        view.getTxtCategoria().setText(nomeCat);
        view.getTxtCategoria().setEditable(false);

        configurarTabela();
        carregarHistorico();

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
                new String[]{"Data", "Percentual de lucro (%)", "Preço de venda"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        view.getTblHistoricoPreco().setModel(model);
    }

    private void carregarHistorico() {
        DefaultTableModel model = (DefaultTableModel) view.getTblHistoricoPreco().getModel();
        model.setRowCount(0);

        // O repositório já retorna ordenado do mais recente para o mais antigo (Requisito 9)
        List<HistoricoPreco> historicos = historicoPrecoRepository.buscarPorIdProduto(produto.getIdProduto());

        for (HistoricoPreco historicoPreco : historicos) {
            String dataFormatada = historicoPreco.getData() != null ? historicoPreco.getData().format(formatter) : "-";
            
            model.addRow(new Object[]{
                    dataFormatada,
                    String.format("%.2f", historicoPreco.getPercentualLucroUtilizado()),
                    String.format("%.2f", historicoPreco.getPrecoVendaResultante())
            });
        }
    }

    private void fechar() {
        view.dispose();
    }
}