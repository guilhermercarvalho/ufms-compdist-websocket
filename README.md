# Computação Distribuída - 2020/2

## Websocket - Servidor HTTP

### Descrição do Trabalho

Este repositório se trata de um trabalho prático de Computação Distribuída. Onde deverá ser implementado um servidor HTTP de acordo com a RFC 2616, uma interface de comunicação de processos CGI-BIN, e o servidor deverá permitir conexões persistentes para lidar com múltiplas requisições.

Dentre as linguagens disponíveis para a implementação (C, C++ ou Java), a linguagem escolhida para este projeto foi o Java.

---

## Preparando o Ambiente

Para o desenvolvimento deste projeto em ambiente **UNIX** será utilizado a ferramenta [Docker](https://www.docker.com/get-started), para isolamento da aplicação, e o editor de texto [Visual Studio Code](https://code.visualstudio.com/), para o desenvolvimento do *Web Socket*.

Para o desenvolvimento em ambientes **Windows** será utilizado o virtualizador [Oracle Virtual Box](https://www.virtualbox.org/), com uma imagem da distro [GNU/Linux Ubuntu Minimal 20.04 LTS](http://archive.ubuntu.com/ubuntu/dists/focal/main/installer-amd64/current/legacy-images/netboot/) executada em modo *Headless*, cujo o acesso será realizado via protocolo SSH pelo [Visual Studio Code](https://code.visualstudio.com/).

---

## Instalação e Configuração das Ferramentas

Para configurar o ambiente de desenvolvimento e suas ferramentas corretamente siga os seguintes artigos que descrevem detalhadamente o processo:

- [Visual Studio Code + Docker Containers](https://medium.com/@guilhermercarvalho512/docker-vscode-instala%C3%A7%C3%A3o-e-configura%C3%A7%C3%A3o-e87c0d9d4065)
- [Visual Studio Code + SSH + Oracle Virtual Box](https://medium.com/@guilhermercarvalho512/ssh-vscode-instala%C3%A7%C3%A3o-e-configura%C3%A7%C3%A3o-a2530986a192)