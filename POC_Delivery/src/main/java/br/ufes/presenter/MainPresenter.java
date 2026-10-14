package br.ufes.presenter;

import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.repository.IProdutoRepository;
import br.ufes.service.CalculadoraPrecoVendaService;
import br.ufes.view.TelaPrincipalView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MainPresenter {
    
    private final TelaPrincipalView view;
    private final ICategoriaRepository categorias;
    private final IProdutoRepository produtos;
    private final IHistoricoPrecoRepository historicos;
    private final CalculadoraPrecoVendaService calculadoraPrecoVendaService;
    
    public MainPresenter(
            IProdutoRepository produtos,
            ICategoriaRepository categorias,
            IHistoricoPrecoRepository historicos,
            CalculadoraPrecoVendaService calculadoraPrecoVendaService
            ){
        Objects.requireNonNull(produtos, "Informe um repositório não nulo de produtos.");
        Objects.requireNonNull(categorias, "Informe um repositório não nulo de categorias.");
        Objects.requireNonNull(historicos, "Informe um repositório não nulo de historicos de preço.");
        Objects.requireNonNull(calculadoraPrecoVendaService, "Informe uma calculadora não nula.");

        
        
        this.produtos = produtos;
        this.categorias = categorias;
        this.historicos = historicos;
        this.calculadoraPrecoVendaService = calculadoraPrecoVendaService;
        
        
        this.view = new TelaPrincipalView();
        
        configurarView();
        
    }
    
    
    public void configurarView(){
        this.view.getMenuItmIncluirProdutos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ProdutoPresenter(produtos, categorias, historicos,calculadoraPrecoVendaService, null, false);
            }
        });

        this.view.getMenuItmBuscarProdutos().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BuscarProdutoPresenter(produtos, categorias, historicos);
            }
        });

        this.view.getMenuItmCategorias().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CategoriaPresenter(categorias);
            }
        });

        this.view.getMenuItmCalcMargLucro().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CalculoMargemPresenter(calculadoraPrecoVendaService);
            }
        });
    }
    
    
    
}
