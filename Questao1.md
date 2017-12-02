************************************************************************************************************************
Juntamente com o Código, deve-se documentar a estratégia utilizada para a criação da aplicação, a arquitetura utilizada
e os padrões. A documentação pode ser feita via GIT/Bitbucket;
************************************************************************************************************************

- Para o projeto optei por utilizar **Spring Boot** o qual se tornou o padrão de facto para projetos java, pelas
integrações oferecidas e pelo fato de vir com **Tomcat** embarcado.

- Para persistência utilizei como banco de dados o **MongoDB** pela **flexibilidade** , **desempenho** e similidade(BSON -
JSON) com o formato Json que será trafegado nas APIs Rest.

- Utilizei o **Spring Data Mongo** por abstrair e facilitar toda a parte de persistência,  repositórios e servir como 
ponto de partida para os comandos de CRUD de persistencia de dados. Já que ele oferece algumas interfaces básicas como 
as de busca e inserção.

- Para criar os serviços REST optei pelo Spring Rest pela facilidade e melhor integração com **Spring boot** ao invés 
de utilizar o JAX-RS com JERSEY.

- Utilizei o **Spring Cloud Feign** para consumir os serviços REST e o **Spring Cloud Hystrix** como **Circuit Braker**!

Para criar a aplicação foi implementado os serviços Rest para o **CRUD** de **Campanhas**

 Para tal Foram criadas 2 Entidades **Campanha** e **CampanhaResource**:
 - _A entidade **Campanha** faz a modelagem Java - MongoDB_
 - _A entidade **CampanhaResource** é a entidade que vai ser serializada para ser recebida e entregue pelas interfaces da
   API Rest em formato Json. Tende a ser mais leve que a entidade Campanha em um ambiente real e leva as anotações para 
   documentação da API em **Swagger**_

 O serviço de criação de Campanhas é acionado pelo método **POST** em **/api/v1/campanhas** , algumas informações sobre:
 - _O conteúdo trafegado entre o cliente e o serviço suporta o formato **application/json**._
 - _Recebe um um Json com os seguintes campos (**nome, timeCoracaoId, inicioVigencia e fimVigencia)**_
 - _OS campos de data devem ter o seguinte formato : "**YYYY-MM-DD"**_
 - _Após a criação da campanha é retornado no **HEADER** a **URI** da campanha criada_

O serviço de consultas das campanhas é acionado pelo método **GET** em **/api/v1/campanhas** , **/api/v1/campanhas/{id}** 
  e **/api/v1/campanhas/time-coracao/{timeCoracao}**, algumas informações sobre:
 - _O conteúdo trafegado entre o cliente e o serviço suporta o formato **application/json**._
 - _Retorna um um Json com os seguintes campos (**nome, timeCoracaoId, inicioVigencia e fimVigencia**)_

O serviço de deletar uma campanha é acionado pelo método **DELETE** em **/api/v1/campanhas/{id}** , algumas informações sobre:
  - _A API retorna o status **204 No Content**_;

O serviço de atualizar a campanha é acionado pelo método **PUT**(Não foi implementado o **PATCH** para esta demo) 
    **api/v1/campanhas/{id}** , algumas informações sobre:
  - O _conteúdo trafegado entre o cliente e o serviço suporta o formato **application/json**._
  - _Recebe um um Json com os seguintes campos (**nome, timeCoracaoId, inicioVigencia e fimVigencia**) para atualização 
    dos campos_
  - _A API retorna o status **204 No Content**_;

Foi criada uma estrutura de **Webhook**, na qual existe um endpoint para cadastro dos **URLs** que receberão o a notificação
via POST das atualizações das campanhas.

 O serviço de criação de **Webhook** é acionado pelo método **POST** em /api/v1/webhooks , algumas informações sobre:
 - _O conteúdo trafegado entre o cliente e o serviço suporta o formato application/json._
 - _Recebe um um Json com os seguintes campos (**url, chaveAcesso**)_
 - _Não pode ser cadastrado um webhook com o mesmo url de um existente._
 - _Após a criação do webhook é retornado no **HEADER** a URI do webhook criada_

 **Documento** : campanha

##  Pacotes e componentes:

**br.com.campanha** - _Pacote principal de da aplicação._
 - **ApplicationStarter** - _Responsável por configurar o Spring boot e Inicializar a aplicação_

**br.com.campanha.api**  - _Pacote com os componentes da API Rest_
 - **domain** - _Pacote com as entidades deque serão serializadas pela API_
    - **CampanhaResource** - _Representa o resource que será enviado pela API no formato JSON_
    - **ErrorInfo** - _Representa a mensagem de erro que será retornada pela api com URL e a exception_
 - **rest**
    - **CampanhaControler** - _Classe controller que irá expor os serviços Rest de campanha_

**br.com.campanha.configuration** -  _Pactote com as classes de configurações da aplicação_
 - **Swaggerconfig** - _Classe para configurar o Swagger na aplicação e mapear os endPoints_

**br.com.campanha.domain** - _Pacote com as classes de dominio para integrar com o banco de dados_
 - **Campanha** - _Clase que mapeia o objeto java para o documento do MongoDB_

**br.com.campanha.exception** - _Pacote com a execções criadas para aplicação_
 - **RecursoNaoEncontradoException** - _Exceção que será lançada quando a busca por ID não encontrar nenhum resultado_

**br.com.campanha.repository** - _Pacote para as classes de Repository (Acesso ao banco de dados)_
 - **CampanhaRepository** - _Interface que implementa o Spring Data Mongo para acesso ao MongoDB - Operações de CRUD_

**br.com.campanha.service** - _Pacote para as classes de serviço da aplicação_
 - **CampanhaService** - _Interface para encapsular as operaçoes de CRUD e regras de campanha_
  -**impl**
   - **CampanhaServiceImpl** - _Implementação da interface CampanhaService, responsável por abstrair toda a parte de
        acesso a regras e aos dados de campanha_

**br.com.campanha.webhook** - _Pacote para configuração do webhook para avisar APIS sobre atualizaççoes nas campanhas_.

## Testes  e Regras:

**test/br.com.campanha** - ApplicationStarterTest - Valida se a inicialização da applicação carrega os dados

**test/br.com.campanha.repository** - Validação dos repositórios
 * Testa o requisito 1 -  O Sistema não deverá retornar campanhas que estão com a data de vigência vencidas
 - **CampanhaRepositoryTest**._deveTrazerCampanhasComDataDeFimVigenciaoSuperiorADataDoParametro()_
                             ._naoDeveTrazerCampanhasComDataDeFimVigenciaoInferiorADataDoParametro()_

 * Faz parte o requisito 2 - trazer as campanhas ativas no período
 - **CampanhaRepositoryTest**._deveTrazerCampanhasAtivasPorPeriodo()_
                         ._naoDeveTrazerCampanhasQuandoPeriodoEstiverForaDosParametros()_

**test/br.com.campanha.service** - Pacote para teste dos serviços
 * Testa o requisito 2 -
 * caso exista uma campanha ou N campanhas associadas naquele período, o sistema deverá somar um dia no término da vigência de cada campanha já existente.*
 * Caso a data final da vigência seja igual a outra campanha, deverá ser acrescido um dia a mais de forma que as campanhas não tenham a mesma data de término de vigência.
  - **CampanhaServiceTest**._campanhaDeveSerCadastradasComDadosCorretos()_
                       _.buscarCampanhasAtivasPorPeriodo()_

**test/br.com.campanha.api.rest** - Pacote para teste dos Controles
 - **CampanhaControllerTest** - _Testa todos os acessos Rest da campanha_

**test/br.com.campanha.fixture** - _Pacote para geradores de dados para teste_



************************************************************************************************************************