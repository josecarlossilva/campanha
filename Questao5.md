************************************************************************************************************************
**Uma das grandes inclusões no Java 8 foi a API Stream. Com ela podemos fazer diversas operações de loop, filtros, maps,
etc. Porém, existe uma variação bem interessante do Stream que é ParallelStreams. Descreva com suas palavras quando qual
é a diferença entre os dois e quando devemos utilizar cada um deles.**
************************************************************************************************************************

O próprio Stream do java já foi previamente explicado na pergunta, ele oferece diversas operações de loop, filtros, maps,
etc. A ideia do ParallelStreams é permitir de forma "automática" a paralelização do processo! O ponto interessante
aqui é entender o que é paralelização de processos e processamento concorrente!
- Processamento em paralelo é executar ao mesmo tempo tarefas que não entram no estado de block/aguardando (como foi
explicado na questão 4 de Threads e Deadlocks), como por exemplo, tarefas de cálculos intensos.
- Já o processamento concorrente grande parte das tarefas alternam para o estado de block/ aguarando a liberação de
algum recurso ou aguardando o retorno de algum recurso. Otimizando assim o uso do processador.

O ParallelStreams do java independentemente do tipo de tarefa que ele for paralelizar, vai usar sempre a mesma estratégia.
Isto pode levar a 3 situações:
- Ganho de tempo no processamento
- Não haver nenhum ganho de tempo de processamento.
- Levar a um aumento de tempo de processamento.

A paralelização automática geralmente não leva aos resultados esperados por algumas razões:

- O aumento da velocidade é altamente dependente do tipo de tarefa e da estratégia de paralelização.
  E sobre tudo, a melhor estratégia de paralelização depende do tipo de tarefa.

- O aumento da velocidade é altamente dependente do ambiente. Em alguns ambientes, é fácil obter uma diminuição
  da velocidade paralelizando.

- Em alguns cenários dada a diferença de ambientes o paralelismo mostra ganhos de desempenho nos ambientes de desenvolvimento,
  mas piora a performance nos ambientes de produção

Para realmente haver um ganho no processamento paralelizando, o ideal é que criemos uma estratégia para cada tipo de tarefa
o que tiraria toda a "graça" em usar o ParallStream se assim podemos dizer. A paralelização requere:

- Um "pool" de threads para executar as sub-tasks.
- Dividir a tarefa inicial em sub-tasks
- Distribuir as sub-taks para as threads
- Coletar os resultados

Dado tudo isto e após ler um artigo falando sobre como o algorítimo do ParallelStream do java é problemático
       --> http://www.coopsoft.com/ar/CalamityArticle.html e http://www.coopsoft.com/ar/Calamity2Article.html

Eu recomendaria o uso do ParallelStrem em casos:
 - Ambiente com vários processadores(multicore)(Padrão atualmente) no qual:
 - Algumas threads ficaram em blocked por um longo período(como acessando outro servidor)
 - No qual não tenha um grande número de threads executando ao mesmo tempo e principalmente não tenha outros stream
   em paralelo executando ao mesmo tempo, mas uma camada a mais de paralelismo não ajudaria em nada.
 - A lista de objetos na qual se esta processando em paralelo seja imutável como por exemplos variáveis numéricas que
   serão utilizadas em cálculos.


_Para os outros cenários eu recomendaria o uso do Stream sem o paralelismo._


************************************************************************************************************************