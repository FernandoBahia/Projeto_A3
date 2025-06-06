# Projeto A3 - Controle de Estoque Java e MySQL
Repositório voltado para o projeto A3 da faculdade -
🧾 Resumo Geral
O Projeto_A3 é um sistema desktop em Java (com Swing) para gerenciamento de estoque. Permite inserir, editar, excluir e listar produtos e categorias, além de gerar um relatório que exibe a quantidade de produtos por categoria. O banco de dados usado é MySQL, acessado via JDBC.

🔍 Estrutura do Projeto
1. Pacotes e Arquivos Principais
br.com.controleestoque.model

Produto: atributos como id, nome, preço, unidade, estoque min/max e referência à categoria.

Categoria: id, nome, tamanho e embalagem.

RelatorioCategoria: guarda o resultado do relatório (nome da categoria e quantidade).

br.com.controleestoque.dao

Conexao: configura conexão JDBC com MySQL (classe driver, URL, usuário/senha).

ProdutoDAO: métodos inserir, atualizar, excluir, listar, salvar, reajustarPrecos, além de métodos para identificar produtos abaixo do mínimo ou acima do máximo.

CategoriaDAO: métodos inserir, atualizar, excluir, listarTodos, além de relatório de quantidade por categoria.

RelatorioDAO: consulta SQL que retorna List<RelatorioCategoria>.

br.com.controleestoque.view

Principal: janela principal com abas para Produtos, Categorias e botão “Relatórios”. As tabelas exibem dados com formatação monetária e botões para abrir formulários.

FormProduto e FormCategoria: formulários para cadastro/atualização de registros.

FormRelatorio: cria janela que carrega dados usando RelatorioDAO e exibe em tabela.

🧪 Fluxo de Funcionamento
Ao executar Principal.main, abre janela principal. Carrega inicialmente listas de produtos e categorias no banco e exibe nas tabelas.

Cadastro/Edição: botão “Produtos” ou “Categorias” abre formulário, que usa DAO para persistir dados e retorna à Principal, que atualiza a tabela.

Exclusão de produto: a funcionalidade foi adicionada no ProdutoDAO.excluir(int id). Para ativá-la, você pode:

Adicionar botão “Excluir” ao FormProduto, chamar produtoDAO.excluir(produto.getId()) e atualizar a tabela.

Relatórios: clicando em "Relatórios", instancia FormRelatorio, que usa RelatorioDAO.obterQuantidadeProdutosPorCategoria() para preencher a tabela com nome da categoria e quantidade de produtos.

✅ Bibliotecas, Tipos e Integração com Banco
Java Swing: usado para GUI.

JDBC (com driver MySQL): é necessário incluir o .jar do conector (ex.: mysql-connector-java-8.0.33.jar) no classpath tanto na compilação quanto na execução (-cp bin;lib/* ou similar).

SQL: cada DAO contém SQL padrão:

SELECT, INSERT, UPDATE, DELETE.

RelatorioDAO: GROUP BY retorna quantidade por categoria.

🔧 Compilação e Execução
Compilação:

ps1
Copiar
Editar
javac -d bin -cp "lib/*" src\br\com\controleestoque\model\*.java src\br\com\controleestoque\dao\*.java src\br\com\controleestoque\view\*.java
Inclusão do conector JDBC: colar o .jar em lib/ e usá-lo como classpath.

Execução:

ps1
Copiar
Editar
java -cp "bin;lib/mysql-connector-java-8.0.33.jar" br.com.controleestoque.view.Principal
Saídas da compilação:

.class gerados nas mesmas pastas internas dentro de bin/.

Erros comuns:

Falta do driver JDBC: ClassNotFoundException: com.mysql.cj.jdbc.Driver – indica que o .jar não está no classpath.

Falhas ao compilar RelatorioDAO: se a classe RelatorioCategoria não estiver incluída no javac, aparecem erros cannot find symbol.

📝 Recomendações Finais
Assegure que todos os .java (model, dao, view) estejam incluídos na compilação e que RelatorioCategoria.java esteja em src/br/com/controleestoque/model/.

Coloque o .jar JDBC dentro de lib/ e use -cp "bin;lib/*" tanto para javac quanto para java.

Após javac, verifique se bin/br/com/controleestoque/.../*.class estão lá para todas as classes.

Para adicionar exclusão no FormProduto, inclua um botão "Excluir" que chama produtoDAO.excluir(id) e fecha/atualiza tabela.

#Para compliar o projeto:
java -cp "bin;lib/mysql-connector-j-8.0.33.jar" br.com.controleestoque.view.Principal

#
git add .
git commit -m "commit do projeto A3"
git push origin main
