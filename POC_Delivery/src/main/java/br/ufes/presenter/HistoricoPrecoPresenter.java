package br.ufes.presenter;

import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;

import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.service.PrazoCalculoServiceMock;

import br.ufes.view.HistoricoPrecoView;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class HistoricoPrecoPresenter {

    private final HistoricoPrecoView view;

    public HistoricoPrecoPresenter(IHistoricoPrecoRepository historicos, Produto produto) {
        this.view = new HistoricoPrecoView();

        this.view.getTxtNomeProduto().setText(produto.getNome());
        this.view.getTxtCategoria().setText(produto.getCategoria().getNome());

        List<HistoricoPreco> historicosProdutoAtual
                = historicos.buscarPorIdProduto(produto.getIdProduto());

        DateTimeFormatter formatoData = PrazoCalculoServiceMock.FORMATO_DATA;

        DefaultTableModel modelo
                = (DefaultTableModel) this.view.getTblHistoricoPreco().getModel();

        modelo.setRowCount(0);

        for (HistoricoPreco historicoPreco : historicosProdutoAtual) {
            modelo.addRow(new Object[]{
                historicoPreco.getData().format(formatoData),
                String.format("%.2f", historicoPreco.getPercentualLucroUtilizado()),
                String.format("R$ %.2f", historicoPreco.getPrecoVendaResultante())
            });
        }

        this.view.getBtnFechar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        });
        this.view.setVisible(true);
    }
}
