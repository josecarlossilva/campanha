************************************************************************************************************************
## O que é Deadlock? Detalhe um pouco sobre o caso e como você poderia resolver isso.
************************************************************************************************************************

Antes de falar sobre o deadlock precisamos entender um pouco sobre Threads, Monitores e Locks.

##### Processos e threads:

- **Processo** é geralmente entendido como um programa em execução ,trata-se de uma estrutura mais complexa que contém,
além do programa no seu formato executável, todas as informações necessárias (contexto) à execução e ao controle da
execução do mesmo.

- **Threads**, por outro lado, representam uma nova concepção na forma de um processo paralelizar a execução de partes do
seu código. Os threads, conceitualmente, se assemelham a subprocessos porém, diferentemente destes, não possuem identidade
própria e, portanto, não são independentes.

 No inicio da computação, na época dos grandes computadores e cartões perfurados os computadores somente processavam
uma tarefa por vez. Existia uma fila para usar os computadores e cada processo deveria esperar o término por completo do
processo anterior.

 Com o avanço do hardware e dos sistemas operacionais, criaram-se mecanismos para possibilitar a execução de vários
processos/threads em "paralelo" na qual cada um deles tinha um tempo determinado de processamento gerando-se a ilusão de
paralelismo. Hoje em dia temos processadores multicore que literalmente podem executar vários processos/threads de
forma simultânea.

Com este paralelismo podemos ter alguns problemas quando várias threads acessam o mesmo recurso ao mesmo tempo podendo
gerar algumas inconsistências de dados,  como por ex:

- **Thread - A** Inicia sua execução e acessa o recurso CONTADOR com valor 1, lê o seu valor.

- **Thread - B** Inicia sua execução e também acessa o recurso CONTADOR que ainda esta com o valor 1 e lê o seu valor.

- **Thread - A** Ao terminar o seu processamento incrementa o recurso CONTADOR em 1, o CONTADOR está com valor 2

- **Thread - B** Ao terminar o seu processamento incrementa o recuros CONTADOR em 1, também resultando no valor 2.

O problema é que ao final deste processo o recurso CONTADOR deveria esta com valor 3, dado que seu valor inicial era 1
é após 2 processos/trheads que o incrementaram, ele deveria ter o valor 3.

Explicado de forma simplificada, para se resolver este problema foi criado o mecanismo de monitor/lock na qual quando
uma Thread precise acessar um recurso compartilhado na qual possa gerar inconsistências, ela precisa ter acesso ao "monitor"
e requisitar o lock do recurso para que durante o seu período de uso nenhuma outra Thread consiga ler ou alterar o
recurso, evitando assim a inconsistência mostrada acima. Outras threads que precisem destes recurso ficam bloqueadas e
"aguardando" o término do lock para continuarem o seu processamento.

Porém com este mecanismo de monitor/lock podem ocorrer o famoso Deadlock na qual duas ou mais threads estão no estado
aguardando, na qual uma thread esta aguardando o término da outra de forma simultânea. Thread-A aguardando a thread-B terminar
o seu processamento para concluir a sua tarefa e a thread-B aguardando o término da thread-A r para concluir a sua tarefa.
Elas ficariam neste estado indefinidamente.

O problema do deadlock é que é muito difícil prever-lo em tempo de desenvolvimento e geralmente os deadlocks são "pegos"
em produção durante um período de grande processamento. Também é muito difícil recriar o deadlock em ambientes de testes,
podem ser usados alguns test-stress para tentar localizar a causa do deadlock no código.

**********************

### E como você poderia resolver isso ?

  _Existem vários mecanismos para tentar evitar ou sair de um deadlock._

- **Thread única**
    - Existem algumas situações na qual o deadlock é inevitável, nestes casos a única solução é trabalhar somente com uma
      thread por vez, e sim,  se perderia todo o poder do multiprocessamento, mas para estes casos creio que seja a única solução.
- **Tempo limite de bloqueio**
    - Pode-se definir um tempo limite para o lock de uma thread ou permitir o cancelamento do processo após determinado
período. Infelizmente o synchronized não suporta o cancelamento, uma vez no bloco sincronizado uma thread tem que
esperar até que o bloqueio seja adquirido. O java oferece um outro mecanismo de lock para estes casos que é definido pela
interface Lock. --> **http://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/Lock.html**

- **Não permitir que uma thread obtenha o 2 locks ao mesmo tempo**
    - De forma programática não permitir que uma thread tente obter o lock de outro recurso enquanto ainda está com o lock
de algum recurso ou parte do código, o que poderia gerar o deadlock em alguns casos.

- **Ordem nos locks.**
  - Existem alguns cenários no qual um thread precisa de certa forma obter vários locks ao mesmo tempo, de forma
 programática temos que garantir que todos os locks sejam feitos ou nenhum lock será feito, evitando que durante algum
 dos locks a thread fique infinitamente esperando o lock de outro recurso.

Obs: _Lógico que um bom desenho da aplicação, evitando-se dependências cíclicas entre processos e um bom desenho da
estrutura de dados tende a diminuir o número de deadlocks._



************************************************************************************************************************