# Projeto de Sistemas Distribuídos 2015-2016 #

Grupo de SD 67 - Campus Alameda

Eduardo Rafael Sequeira Rodrigues 	78431	eduardo.s.rodrigues@tecnico.pt

Tiago Alexandre Lima Guerreiro	79183	tiagog000@gmail.com

Ricardo Miguel Mendes Matias	70977	ricxl3@hotmail.com


Repositório:
[tecnico-distsys/A_67-project](https://github.com/tecnico-distsys/A_67-project/)

-------------------------------------------------------------------------------

## Instruções de instalação 


### Ambiente

[0] Iniciar sistema operativo

Indicar Windows ou Linux
Linux

[1] Iniciar servidores de apoio

JUDDI:
$: juddi-startup

[2] Criar pasta temporária
cd Desktop
mkdir SD1


[3] Obter código fonte do projeto (versão entregue)


git clone https://github.com/tecnico-distsys/A_67-project.git

 git clone -b SD_R1 https://github.com/tecnico-distsys/A_67-project/



[4] Instalar módulos de bibliotecas auxiliares

cd uddi-naming
mvn clean install


cd A_67-project/broker-ws
mvn clean install

(cd..)  A_67-project/transporter-ws
mvn clean install

-------------------------------------------------------------------------------

### Serviço CA

[1] Construir e executar **servidor*

cd  A_67-project/ca-ws
mvn clean compile exec:java
ou mvn compile exec:java

[2] Construir **cliente** e executar testes

cd A_67-project/ca-ws-cli
mvn clean install exec:java

[3] Executar
cd A_67-project/transporter-ws-cli
mvn exec:java

-------------------------------------------------------------------------------

### Biblioteca WSHandlers

[1] Construir e executar **servidor*

cd  A_67-project/ws-handlers
mvn clean install

-------------------------------------------------------------------------------

-------------------------------------------------------------------------------

### Serviço TRANSPORTER

[1] Construir e executar **servidor*

cd  A_67-project/transporter-ws
mvn clean compile exec:java
ou mvn compile exec:java -Dws.i=%

(Repetir com alterando % por inteiro para 2 p.ex)

[2] Construir **cliente** e executar testes

cd A_67-project/transporter-ws-cli
mvn clean install

[3] Executar
cd A_67-project/transporter-ws-cli
mvn compile exec:java

-------------------------------------------------------------------------------

### Serviço BROKER

[1] Construir e executar **servidor**

cd  A_67-project/broker-ws
mvn clean compile exec:java

[2] Construir **cliente** e executar testes

cd A_67-project/broker-ws-cli
mvn clean compile exec:java

[3] Executar
cd A_67-project/broker-ws-cli
mvn verify

-------------------------------------------------------------------------------
**FIM**
