# ToDo Simple API

Este repositório contém a API do projeto ToDo Simple, desenvolvido com **Spring Boot**. A API permite gerenciar tarefas e usuários, fornecendo funcionalidades como criação, leitura, atualização e exclusão de tarefas, além de operações relacionadas aos usuários.

## Funcionalidades

- **CRUD de tarefas**: Criar, listar, atualizar e excluir tarefas.
- **Usuários**: Associar tarefas a usuários.
- **Autenticação**: API projetada para autenticação via métodos HTTP com base em JSON.

## Requisitos

Antes de executar o projeto, verifique se você tem os seguintes pré-requisitos:

- **JDK 17** ou superior instalado.
- **Maven 3.9.9** ou versão compatível.
- **Postman** ou outro cliente HTTP para testar as rotas da API (opcional, mas recomendado).

## Como rodar o projeto

### 1. Clone o repositório

Clone este repositório para a sua máquina local:

```bash
git clone https://github.com/luizcarmoo/todosimple-api.git
```

### 2. Navegue até o diretório do projeto

```bash
cd todosimple-api
```

### 3. Compile e execute o projeto

No diretório do projeto, execute o seguinte comando para compilar e rodar a aplicação com Maven:

```bash
mvn spring-boot:run
```
O Spring Boot iniciará o servidor na porta padrão 8080.

### 4. Testando a API

Agora que a aplicação está rodando, você pode testar as funcionalidades utilizando o Postman ou qualquer outro cliente HTTP.
Exemplos de rotas:

    GET /task: Lista todas as tarefas.
    POST /task: Cria uma nova tarefa. Enviar um JSON com os dados da tarefa.
    PUT /task/{id}: Atualiza uma tarefa específica.
    DELETE /task/{id}: Exclui uma tarefa específica.
    GET /task/user/{id}: Lista as tarefas associadas a um usuário específico.

### 5. Erros comuns

    Erro 404 (Not Found): Certifique-se de que as rotas estão corretas e o servidor está em execução na porta 8080.
    Erro 400 (Bad Request): Verifique se o corpo da requisição (para POST e PUT) está formatado corretamente.

Caso queira contribuir

    Faça o fork deste repositório.
    Crie uma nova branch (git checkout -b feature/nova-feature).
    Faça as alterações e commit (git commit -am 'Adiciona nova feature').
    Envie para o seu repositório (git push origin feature/nova-feature).
    Crie um Pull Request.