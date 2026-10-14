package br.ufes;

import br.ufes.model.Categoria;
import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;

import br.ufes.repository.CategoriaRepository;
import br.ufes.repository.HistoricoPrecoRepository;
import br.ufes.repository.ProdutoRepository;

import br.ufes.service.CalculadoraPrecoVendaService;
import br.ufes.seeder.Seeder;
import br.ufes.presenter.MainPresenter;

import java.util.ArrayList;

public class Principal {

    public static void main(String[] args) {

        CategoriaRepository categorias
                = new CategoriaRepository(new ArrayList<Categoria>());

        ProdutoRepository produtos
                = new ProdutoRepository(new ArrayList<Produto>());

        HistoricoPrecoRepository historicos
                = new HistoricoPrecoRepository(new ArrayList<HistoricoPreco>());

        // 2. Criar o serviço de cálculo
        CalculadoraPrecoVendaService calculadora
                = new CalculadoraPrecoVendaService(
                        produtos,
                        categorias,
                        historicos
                );

        // 3. Popular os dados iniciais
        Seeder seeder = new Seeder(categorias, produtos, calculadora);
        seeder.executar();

        // 4. Abrir a tela principal
        MainPresenter mainPresenter = new MainPresenter(produtos,categorias,historicos,calculadora);
       
    }
}
