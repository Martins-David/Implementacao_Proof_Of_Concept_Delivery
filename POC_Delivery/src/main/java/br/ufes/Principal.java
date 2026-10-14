package br.ufes;

import br.ufes.model.Categoria;
import br.ufes.model.HistoricoPreco;
import br.ufes.model.Produto;
import br.ufes.repository.ICategoriaRepository;
import br.ufes.repository.IHistoricoPrecoRepository;
import br.ufes.repository.IProdutoRepository;
import java.util.ArrayList;

public class Principal {

    public static void main(String[] args) {
        
        ICategoriaRepository categorias  = new ICategoriaRepository (new ArrayList<Categoria>());
        IProdutoRepository produtos = new IProdutoRepository(new ArrayList<Produto>());

        IHistoricoPrecoRepository historicos = new IHistoricoPrecoRepository(new ArrayList<HistoricoPreco>()) {};

        Seeder.popularDadosIniciais(categorias, produtos, historicos);

        new MainPresenter(catRepo, prodRepo, histRepo);

    }
}
