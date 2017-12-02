# Projeto Cadastro de Campanhas
- _Informaçães técnicas sobre o projeto estão após a descrição_

Eu, como usuário, quero administrar os dados da Campanha e fornecer mecanismos (APIs) para **INCLUIR, CONSULTAR, ATUALIZAR, DELETAR** 
as campanhas. Para tanto, os critérios de aceite dessa história são:

As campanhas deverão ser cadastradas de forma que o serviço retorne essas campanhas seguindo a estrutura abaixo:
 - Nome Da Campanha;
 - ID do Time do Coração;
 - Data de Vigência;
 
**O Sistema não deverá retornar campanhas que estão com a data de vigência vencidas**

**No cadastramento de uma nova campanha, deve-se verificar se já existe uma campanha ativa para aquele período (vigência),** 
 - _Não ficou claro o que é uma campanha dentro da vigência, levei em consideração data de inicio e fim vigência devem estar
  dentro do inicio e fim vigência da campanha a ser cadastrada, campapnha com datas de inicio menores e de fim  vigência maiores serão desconsideradas_
 
**caso exista uma campanha ou N campanhas associadas naquele período, o sistema deverá somar um dia no término da vigência de cada campanha já existente.** 

**Caso a data final da vigência seja igual a outra campanha, deverá ser acrescido um dia a mais de forma que as campanhas não tenham a mesma data de término de vigência.** 

Por fim, efetuar o cadastramento da nova campanha:

Exemplo:

Campanha 1 : inicio dia 01/10/2017 a 03/10/2017;

Campanha 2: inicio dia 01/10/2017 a 02/10/2017;

Cadastrando Campanha 3: inicio 01/10/2017 a 03/10/2017;

-> Sistema:
Campanha 2 : 01/10/2017 a 03/10/2017 (porém a data bate com a campanha 1 e a 3, somando mais 1 dia)

Campanha 2 : 01/10/2017 a 04/10/2017

Campanha 1: 01/10/2017 a 04/10/2017 (bate com a data da campanha 2, somando mais 1 dia)

Campanha 1: 01/10/2017 a 05/10/2017

Incluindo campanha 3 : 01/10/2017 a 03/10/2017

**As campanhas deveram ser controladas por um ID único;**

**No caso de uma nas campanhas já existentes, o sistema deverá ser capas de fornecer recursos para avisar outros sistemas que houve alteração nas campanhas existentes.**

## Informações sobre o projeto

 - **_Não ficou claro o que é uma campanha dentro da vigência, levei em consideração data de inicio e fim vigência devem estar
  dentro do inicio e fim vigência da campanha a ser cadastrada, campapnha com datas de inicio menores e de fim  vigência maiores serão desconsideradas_**

- O caminho **base** para as os endpoins é : **/api/v1**
  - Para esta aplicação temos :  **/api/v1/campanhas** e **/api/v1/webhooks**

- Porta da aplicação :**8080**

- Para iniciar a aplicação execute : --> **gradle bootRun** 

- Para ver a **documentação** e **testar** as APIs inclusive os exemplos usando **curl** veja --> **/api/swagger-ui.html** (ex: http://localhost:8080/api/swagger-ui.html )
    -  Para documentar a API usei o **Swagger** caso não conheça o Swagger veja : --> http://swagger.io/ 
    
- A aplicação contém um banco de dados **MongoDB** embutido que é inicializado junto com aplicação    
    - Porta para acessar o MongoDB: **12345**
- Para log veja o arquivo **campanha.log** criado na raiz da aplicação.

- Para os recursos expostos eu usei o seguinte:
    - **200 OK** - para **GET** requests.
    - **201 Created** - para **POST**.
    - **204 No Content** - para **PUT**, **PATCH**, e **DELETE** requests.
    - Usei alguns principios de **HATEOAS** para a API na qual cada recurso tem o **self link** e para criação de novos 
      recursos é retornado **no header o link para o recurso criado**.

- Java code coverage : **100 % nos pacotes de Rest Controller, Domain e Service**  
                     : **85%** das classes | **64%** dos Metodos e **64%** das linhas de código       

## Tecnologias e frameworks utilizados

- **Java versão 8** - 

- **Gradle** - _Para construção do projeto e gerenciamento de frameworks_

- **Spring boot** - _Padrão para construção de projetos usando Spring_

- **Spring HATEOAS** - _Spring HATEOAS fornece algumas APIs para facilitar a criação de representações REST que seguem 
    o princípio HATEOAS quando se trabalha com Spring e especialmente Spring MVC. O problema central que ele tenta 
    abordar é a criação de links e a montagem de representações._ 
    
- **Spring Cloud Feign** -  _Feign é um projeto que faz parte do grande guarda-chuvas de soluções do Spring Cloud e ele 
   basicamente é utilizado para integração com serviços Rest._  

- **Spring Cloud Hystrix** -  _O Hystrix implementa o padrão Circuit Breaker, que de forma bem rápida é um failover para
 chamadas entre micro serviços, ou seja, caso um micro serviço estiver fora do ar um método de fallback é chamado_      
    
- **MongoDB** - _O MongoDB é um banco de dados de documentos de código aberto que fornece alto desempenho, alta disponibilidade 
  e dimensionamento automático. Um registro no MongoDB é um documento, que é uma estrutura de dados composta de pares de campo e valor.
 Os documentos MongoDB são semelhantes aos objetos JSON._ 

- **Swagger** - _Swagger é uma poderoso framework de código aberto apoiada por um grande ecossistema de ferramentas para projetar,
 Compilar, documentar e consumir as APIs RESTful._

- **AssertJ** - _O AssertJ core é uma biblioteca Java que fornece uma interface fluente para escrever asserções. Seu principal objetivo é
Para melhorar a legibilidade do código de teste e facilitar a manutenção dos testes._

- **Spring Data MONGO** - _Spring Data MONGO: Tecnologia responsável por gerar boa parte do código relacionado a camada de persistência
e mapeamnto Documento(MongoDb) com a classe Java , assim como algumas interfaces básicas para CRUD dos documentos._ 

- **Spring Web MVC** - _Framework web usado como solução para a definição de componentes seguindo o modelo arquitetural REST._ 

- **Jackson** - _API para conversão de dados Java em Json e vice-versa._