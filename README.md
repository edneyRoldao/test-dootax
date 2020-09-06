# test-dootax

### Descrição do projeto:
Este projeto é dividido em duas partes.
<br>Primeiro: Processo batch utilizando o approach de job em tasklet para processamento OI
<br>Segunda: API para consultar as chaves dos documentos e validar as mesmas.

### Bibliotecas utilizadas no projeto:
- Maven
- Spring framework
- Spring data (utiliza para JPA)
- Spring batch (utiliado para implementar o processo de leitura de arquivos)
- Swagger (utilizado para documentar a API)
- Lombok (gerador de getters and setters, builder de objetos, log com SL4J)
- H2 para trabalhar com banco de dados em memória

### Principais propriedades do projeto
- extensoes-arquivo: pra melhorar a performance, devemos informar a lista de extensões de arquivos que são permitidos.
  default: csv e txt
- tempo-execucao-job-milisegundos: periodicidade da execução do Job. default: 180 segundos
- total-arquivos-por-processo: O Job está configurado pra trabalhar de forma assíncrona, utilizar essa
  propriedade para definir a quantidade de arquivos processados por processo assíncrono.
  por default 500 arquivos são processados por thread. 
- total-linhas-por-processo: O mesmo se aplica para as linhas de cada arquivo. O default é 1000 linhas por thread
- habilitar-processo: utilizado para ativar e desativaro processo batch
- caminho-diretorio: o caminho do diretório para leitura dos arquivos deve ser definido pela variável de ambiente:
                     APP_CAMINHO_DIRETORIO
                     
### Setup do projeto
- definir o caminho do arquivo na variável de ambiente: APP_CAMINHO_DIRETORIO
- comando que devem ser executados na raíz do projeto (linux)
<br>`export APP_CAMINHO_DIRETORIO=/caminho/da/pasta`
<br>`mvn clean package`
<br>`java -jar target/Teste-1.0.0-SNAPSHOT.jar`

### Considerações finais sobre o projeto
- Ficou faltando implementar os testes unitários, de integração como (Jpa e Api). 
  Os mesmos não foram implementados por falta de tempo.
- Seria importante implementar testes para garantir requisitos não funcionais
  de performance do processo batch
- Em um senário real, eu levantaria a hipótese de segregar o processo batch um microservico
- Alguns processos assíncronos poderiam ser executados utilizando tópicos e filas como kafka
- Outras soluções que poderiam ser utilizados: indexador de busca como elasticSearch, criação de índices no banco de dados
  ou até mesmo um Redis pra uma solução com cache.
- A evolução do desenvolvimento do projeto pode ser observada a partir das branches criadas.
- Iniciei o desenvolvimento do projeto no Sábado de manhã, e finalizei o mesmo no domingo de tarde.
- De acordo com os meus testes, 2500 arquivos com 9000 linhas foram processados em 250 segundos. Total de 22_500_000 de registros
