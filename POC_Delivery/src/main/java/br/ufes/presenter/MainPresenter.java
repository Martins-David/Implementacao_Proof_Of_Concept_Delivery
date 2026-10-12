package br.ufes.presenter;

import model.Produto;
import repositorio.CategoriaRepository;
import repositorio.HistoricoPrecoRepository;
import repositorio.ICategoriaRepository;
import repositorio.IHistoricoPrecoRepository;
import repositorio.IProdutoRepository;
import repositorio.ProdutoRepository;
import seeder.Seeder;
import service.CalculoPrecoService;
import service.CategoriaService;
import service.ProdutoService;

/**
 *
 * @author paulo.campos
 */
public class MainPresenter {

    private final ICategoriaRepository categoriaRepository;
    private final IProdutoRepository produtoRepository;
    private final IHistoricoPrecoRepository historicoPrecoRepository;
    
    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;
    private final CalculoPrecoService calculoPrecoService;

    public MainPresenter() {
        this.categoriaRepository = new CategoriaRepository();
        this.produtoRepository = new ProdutoRepository();
        this.historicoPrecoRepository = new HistoricoPrecoRepository();
        
        this.categoriaService = new CategoriaService(categoriaRepository, produtoRepository);
        this.produtoService = new ProdutoService(produtoRepository, categoriaRepository);
        this.calculoPrecoService = new CalculoPrecoService(produtoRepository, categoriaRepository, historicoPrecoRepository);
    }

    public void iniciar() {
        // Executa o Seeder antes de exibir a primeira tela (Requisito 8)
        new Seeder(categoriaService, produtoService, calculoPrecoService).executar();
        
        // Abre a Tela Principal
        new TelaPrincipalPresenter(this);
    }

    public void abrirIncluirProdutos() {
        new ProdutoPresenter(this, produtoService, categoriaRepository, historicoPrecoRepository);
    }

    public void abrirEditarProduto(Produto produto) {
        new ProdutoPresenter(this, produtoService, categoriaRepository, historicoPrecoRepository, produto);
    }

    public void abrirBuscarProdutos() {
        new BuscaProdutoPresenter(this, produtoRepository, historicoPrecoRepository);
    }

    public void abrirVisualizarProduto(Produto produto) {
        new ProdutoVisualizacaoPresenter(this, produto, categoriaRepository, historicoPrecoRepository);
    }

    public void abrirHistoricoPreco(Produto produto) {
        new HistoricoPrecoPresenter(this, produto, historicoPrecoRepository);
    }

    public void abrirCategorias() {
        new CategoriaPresenter(this, categoriaService);
    }

    public void abrirCalculoMargemLucro() {
        new CalculoMargemLucroPresenter(this, calculoPrecoService);
    }
}