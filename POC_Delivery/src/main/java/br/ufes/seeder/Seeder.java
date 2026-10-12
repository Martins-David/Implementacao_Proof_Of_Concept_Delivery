package br.ufes.seeder;


import br.ufes.model.Categoria;
import br.ufes.model.Produto;
import br.ufes.repository.CategoriaRepository;
import br.ufes.repository.ProdutoRepository;
import br.ufes.service.CalculadoraPrecoVendaService;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Seeder {

    
    
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final CalculadoraPrecoVendaService calculadoraPrecoVendaService;

    public Seeder(CategoriaRepository categoriaRepository,
                  ProdutoRepository produtoRepository,
                  CalculadoraPrecoVendaService calculadoraPrecoVendaService) {
        
        Objects.requireNonNull(categoriaRepository, "O repositório de categorias deve ser não nulo.");
        Objects.requireNonNull(produtoRepository, "O repositório de produtos deve ser não nulo.");
        Objects.requireNonNull(calculadoraPrecoVendaService, "A calculadora de preços de venda deve ser não nula.");
        
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
        this.calculadoraPrecoVendaService = calculadoraPrecoVendaService;
    }

    public void executar() {
        criarCategorias();
        criarProdutos();
        criarHistoricoInicial();
    }

    private void criarCategorias() {
        categoriaRepository.salvar(
                List.of(
                        new Categoria(1L, "Educação", 25.00),
                        new Categoria(2L, "Papelaria", 30.00),
                        new Categoria(3L, "Alimentação", 22.00),
                        new Categoria(4L, "Lazer", 35.00),
                        new Categoria(5L, "Entretenimento", 40.00),
                        new Categoria(6L, "Higiene", 28.00),
                        new Categoria(7L, "Limpeza", 25.00)
                        ));
    }

    private void criarProdutos() {
        // Busca categoria auxiliar
        Categoria educacao = buscarCategoriaPorNome("Educação");
        Categoria papelaria = buscarCategoriaPorNome("Papelaria");
        Categoria lazer = buscarCategoriaPorNome("Lazer");
        Categoria entretenimento = buscarCategoriaPorNome("Entretenimento");
        Categoria alimentacao = buscarCategoriaPorNome("Alimentação");
        Categoria higiene = buscarCategoriaPorNome("Higiene");
        Categoria limpeza = buscarCategoriaPorNome("Limpeza");

        produtoRepository.salvar(List.of(
                                new Produto(1L,"Livro didático", 45.00, educacao),
                                new Produto(2L,"Livro paradidático", 30.00, educacao),
                                new Produto(3L,"Mochila escolar", 70.00, educacao),
                                
                                new Produto(4L,"Caderno universitário", 16.00, papelaria),
                                new Produto(5L,"Lápis grafite HB", 1.20, papelaria),
                                new Produto(6L,"Caneta esferográfica azul", 2.20, papelaria),
                                new Produto(7L,"Borracha branca", 1.00, papelaria),
                                new Produto(8L, "Apontador com depósito", 3.50, papelaria),
                                
                                new Produto(9L, "Jogo de tabuleiro", 55.00, lazer),
                                new Produto(10L, "Bola recreativa", 40.00, lazer),
                                new Produto(11L,"Quebra-cabeça 500 peças", 35.00, lazer),
                                
                                new Produto(12L,"Fone de ouvido", 48.00, entretenimento),
                                new Produto(13L,"Caixa de som portátil", 80.00, entretenimento),
                                new Produto(14L,"Revista de passatempos", 12.00, entretenimento),
                                
                                new Produto(15l,"Suco de uva 1 L", 5.50, alimentacao),
                                new Produto(16L,"Barra de cereal", 9.00, alimentacao),
                                new Produto(17l,"Biscoito integral", 3.20, alimentacao),
                                new Produto(18L, "Sabonete", 2.80, higiene),
                                new Produto(19L,"Creme dental", 5.50, higiene),
                                new Produto(20L,"Detergente líquido", 2.60, limpeza),
                                new Produto(21L,"Esponja multiuso", 1.70, limpeza)
        ));
        
    }

    private void criarHistoricoInicial() {
        calculadoraPrecoVendaService.calcular(LocalDate.now().minusDays(10));
        
    }

    private Categoria buscarCategoriaPorNome(String nome) {
        return categoriaRepository.buscarCategoriaPorNome(nome);
    }
}