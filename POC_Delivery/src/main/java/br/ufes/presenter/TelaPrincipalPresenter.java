/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufes.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import br.ufes.view.TelaPrincipalView;

/**
 *
 * @author paulo.campos
 */
public class TelaPrincipalPresenter {

    private final TelaPrincipalView view;
    private final MainPresenter mainPresenter;

    public TelaPrincipalPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
        this.view = new TelaPrincipalView();
        configurarView();
    }

    private void configurarView() {

        view.getMenuItmIncluirProdutos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPresenter.abrirIncluirProdutos();
            }
        });

        view.getMenuItmBuscarProdutos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPresenter.abrirBuscarProdutos();
            }
        });

        view.getMenuItmCategorias().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPresenter.abrirCategorias();
            }
        });

        view.getMenuItmCalcMargLucro().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPresenter.abrirCalculoMargemLucro();
            }
        });

        view.setVisible(true);
    }

}
