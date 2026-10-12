package seeder;


import br.ufes.model.Categoria;
import java.time.LocalDate;

public class Seeder {

    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;
    private final CalculoPrecoService calculoPrecoService;

    public Seeder(CategoriaService categoriaService,
                  ProdutoService produtoService,
                  CalculoPrecoService calculoPrecoService) {
        this.categoriaService = categoriaService;
        this.produtoService = produtoService;
        this.calculoPrecoService = calculoPrecoService;
    }

    public void executar() {
        criarCategorias();
        criarProdutos();
        criarHistoricoInicial();
    }

    private void criarCategorias() {
        categoriaService.cadastrar("Educação", 25.00);
        categoriaService.cadastrar("Papelaria", 30.00);
        categoriaService.cadastrar("Alimentação", 22.00);
        categoriaService.cadastrar("Lazer", 35.00);
        categoriaService.cadastrar("Entretenimento", 40.00);
        categoriaService.cadastrar("Higiene", 28.00);
        categoriaService.cadastrar("Limpeza", 25.00);
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

        // Educação
        produtoService.cadastrar("Livro didático", 45.00, educacao);
        produtoService.cadastrar("Livro paradidático", 30.00, educacao);
        produtoService.cadastrar("Mochila escolar", 70.00, educacao);

        // Papelaria
        produtoService.cadastrar("Caderno universitário", 16.00, papelaria);
        produtoService.cadastrar("Lápis grafite HB", 1.20, papelaria);
        produtoService.cadastrar("Caneta esferográfica azul", 2.20, papelaria);
        produtoService.cadastrar("Borracha branca", 1.00, papelaria);
        produtoService.cadastrar("Apontador com depósito", 3.50, papelaria);

        // Lazer
        produtoService.cadastrar("Jogo de tabuleiro", 55.00, lazer);
        produtoService.cadastrar("Bola recreativa", 40.00, lazer);
        produtoService.cadastrar("Quebra-cabeça 500 peças", 35.00, lazer);

        // Entretenimento
        produtoService.cadastrar("Fone de ouvido", 48.00, entretenimento);
        produtoService.cadastrar("Caixa de som portátil", 80.00, entretenimento);
        produtoService.cadastrar("Revista de passatempos", 12.00, entretenimento);

        // Alimentação
        produtoService.cadastrar("Suco de uva 1 L", 5.50, alimentacao);
        produtoService.cadastrar("Barra de cereal", 9.00, alimentacao);
        produtoService.cadastrar("Biscoito integral", 3.20, alimentacao);

        // Higiene
        produtoService.cadastrar("Sabonete", 2.80, higiene);
        produtoService.cadastrar("Creme dental", 5.50, higiene);

        // Limpeza
        produtoService.cadastrar("Detergente líquido", 2.60, limpeza);
        produtoService.cadastrar("Esponja multiuso", 1.70, limpeza);
    }

    private void criarHistoricoInicial() {
        // Gera o histórico retroativo de 10 dias para permitir novo cálculo na data atual (Requisito 12.3)
        calculoPrecoService.calcular(LocalDate.now().minusDays(10));
    }

    private Categoria buscarCategoriaPorNome(String nome) {
        return categoriaService.listar().stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null);
    }
}