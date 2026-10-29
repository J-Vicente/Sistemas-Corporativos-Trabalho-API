# Sistema de Chamados IFRN

API REST em Java 21 e Spring Boot 4 para registro e acompanhamento de chamados de suporte. A aplicação usa Spring Data JPA, PostgreSQL, Flyway, DTOs validados e MapStruct para converter entidades em respostas sem expor diretamente o modelo de persistência.

## Executar

Pré-requisito: Docker com Compose.

```bash
docker compose up --build
```

A API fica disponível em `http://localhost:8080`. O Compose inicializa o PostgreSQL, aguarda a disponibilidade do banco e persiste os dados no volume `postgres_data`. Os valores de desenvolvimento podem ser alterados pela variável `DB_PASSWORD`.

As migrations `V1` e `V2` criam as tabelas e carregam dois setores, quatro categorias e quatro usuários de demonstração. IDs iniciais: solicitante `1`, técnicos `2` (COTIC) e `3` (MANUT), segundo solicitante `4`.

## Endpoints previstos

Cadastros: CRUD de `/setores`, `/categorias` e `/usuarios` (usuários sem exclusão), com listagens paginadas por `page`, `size`, `sort` e filtros `nome`, `setorId` e `tipo` aplicáveis. `/setores/{id}/categorias` lista as categorias do setor. Chamados: `POST/GET /chamados`, `GET /chamados/{id}`, `GET /usuarios/{id}/chamados`, `PATCH /chamados/{id}/assumir`, `/resolver`, `/cancelar` e `POST /chamados/{id}/interacoes`. Use filtros como `status`, `prioridade`, `setorId`, `categoriaId`, `solicitanteId`, `tecnicoId`, `dataInicio` e `dataFim` em `/chamados`.

As coleções retornam `itens`, `pagina`, `tamanho`, `totalElementos` e `totalPaginas`. Os itens de listagem são resumos; endpoints por ID e mutações retornam os DTOs de consulta completos. Os corpos de criação e atualização usam DTOs independentes e são validados antes da execução das regras de negócio. Erros seguem o formato `timestamp`, `status`, `mensagem` e `caminho`.

A camada de serviço concentra validações, regras de negócio, resolução de relacionamentos e montagem de respostas. Os serviços de setores, categorias, usuários e chamados ficam separados por recurso. Controllers somente encaminham os parâmetros e DTOs de entrada ao serviço. Entidades JPA declaram os campos persistidos; conversões de entrada e saída são feitas pelos mappers MapStruct.

Exemplo de abertura:

```http
POST http://localhost:8080/chamados
Content-Type: application/json

{"titulo":"Wi-Fi indisponível","descricao":"Sem conexão no laboratório 1","local":"Lab 1","solicitanteId":1,"categoriaId":2,"prioridade":"ALTA"}
```

Exemplos adicionais estão em [requests.http](requests.http).

## Migrations e validação do esquema

O Flyway registra cada migration executada em `flyway_schema_history`, incluindo versão e checksum. Se o conteúdo de uma migration já aplicada for alterado, o checksum deixa de coincidir e o Flyway sinaliza erro de validação; por isso, correções devem ser adicionadas em uma nova migration versionada, sem editar as anteriores. `ddl-auto=update` não é usado porque permite alterações implícitas e não versionadas no esquema. Aqui o Hibernate usa `validate`: confere o mapeamento contra as tabelas que o Flyway criou, sem alterá-las.
