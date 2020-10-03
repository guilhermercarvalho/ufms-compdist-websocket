# Computação Distribuída - 2020/2

## Java Web Socket

### Descrição do Trabalho

Trabalho prático de Computação Distribuída.

---

## Preparando o Ambiente

---

Para o desenvolvimento deste projeto em ambiente **UNIX** será utilizado a ferramenta [Docker](https://www.docker.com/get-started), para isolamento da aplicação, e o editor de texto [Visual Studio Code](https://code.visualstudio.com/), para o desenvolvimento do *Web Socket*.

Para o desenvolvimento em ambientes **Windows** será utilizado o virtualizador [Oracle Virtual Box](https://www.virtualbox.org/), com uma imagem da distro [GNU/Linux Ubuntu Minimal 20.04 LTS](http://archive.ubuntu.com/ubuntu/dists/focal/main/installer-amd64/current/legacy-images/netboot/) executada em modo *Headless*, cujo o acesso será realizado via *ssh* pelo [Visual Studio Code](https://code.visualstudio.com/).

---

## Instalação e Configuração das Ferramentas

---

### Docker

Para sistemas UNIX como Linux, a execução do *script* de instalação é mais do que suficiente.

```shell
curl -fsSL https://get.docker.com | sudo sh
```

Para utilizar o **Docker** com seu usuário, e não ter que digitar *sudo* o tempo todo e então entrar com o usuário *root*, considere adicionar seu usuário ao grupo "docker".

```shell
sudo usermod -aG docker ${USER}
```

Para a efetivar a alteração é necessário realizar ***logout*** na máquina.

---

### Visual Studio Code

Para instalar o **Visual Studio Code** (**vscode** para os mais íntimos) basta visitar o site oficial na aba de [*Download*](https://code.visualstudio.com/Download) e baixar os binários específicos para sua versão do sistema. Siga as instruções de instalação.

Após instalado, instale também a extensão [Remote Development](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack). Ela é necessária para acessar o *container* através do vscode.

Com o vscode aberto:

- aperte as teclas ```Ctrl+Shift+P```
- apague o caractere ```>```
- cole o seguinte comando:

    ```shell
    ext install ms-vscode-remote.vscode-remote-extensionpack
    ```

Instale a extensão, clique no novo ícone de cor verde abaixo do ícone de configuração e execute: ```Remote-Containers: Reopen in Container```

Para mais informações sobre a extensão **Remote Containers** acesse sua documentação [aqui](https://code.visualstudio.com/docs/remote/containers).

### Usuário para Container

Para funcionamento correto do *container* é necessário definir o usuário nos arquivos ```devcontainer.json``` e ```Dockerfile```.

Para identificar seu usuário execute o seguinte comando em um terminal:

```shell
whoami
```

Altere os arquivos mencionados substituindo o meu usuário (```guilherme```) para o seu usuário.

---

### Oracle Virtual Box

Para instalar o virtualizador **Virtual Box** acesse a página oficial na aba de [*Download*](https://www.virtualbox.org/wiki/Downloads) e baixar os binários específicos para sua versão do sistema. Siga as instruções de instalação.

Baixe também a imagem mínima do Ubuntu ([mini.iso](http://archive.ubuntu.com/ubuntu/dists/focal/main/installer-amd64/current/legacy-images/netboot/)) e crie uma máquina virtual com as seguintes especificações:

- Tipo: **Linux**
- Versão: **Ubuntu (64-bit)**
- RAM: **1024 MB**
- Disco Virtual (VDI), Dinamicamente alocado: **10 GB**

Após criada a *VM* é necessário realizar mais algumas **Configurações**:

- Sistema
  - Processador
    - Processador(es) -> **2 CPU**

- Rede
  - Adaptador 1
    - Conectado a -> **Placa em modo Bridge**

Realizadas as configurações inicie a *VM* e instale a imagem *mini.iso*.

### Ubuntu Minimal 20.04 LTS

Realize a instalação padrão do Ubuntu. Crie um usuário e senha os quais não devem ser esquecidos (de preferência com mesmo nome do usuário do *host*). Exemplo: ```joao```.

Selecione os pacotes:

- openSSH
- Basic Ubuntu Server

Esses pacotes fornecerão todos programas mínimos necessários para utilização do sistema.

Faça *login* na *VM* e execute:

```shell
ip a
```

Salve seu IP para realizar a conexão *ssh*. Exemplo: ```192.168.1.10```.

**Recomendo fortemente a realização de um ```Clone``` da *VM* após o fim da instalação. Em caso de eventuais imprevistos acelera o processo de reinstalação do sistema.

### Java

Para instalar **Java 11** no sistema execute o seguinte comando na *VM*:

```shell
sudo apt install -y default-jdk
```

### SSH Key

Para acessar de modo mais eficiente o sistema via *ssh* crie uma chave específica para acessar a *VM*.

### No seu sistema ***host*** (abra o terminal integrado presente no vscode -> ```Ctrl+J```)

- Crie uma chave e salve-a no diretório .ssh de seu usuário. Deixe a senha em branco (*Press Enter*)

```shell
ssh-keygen -t rsa -b 2048 -f .ssh/id_vm_java_web_socket
```

- Copie a chave pública para a *VM*

```shell
ssh-copy-id -i .ssh/id_vm_java_web_socket.pub joao@192.168.1.10
```

Acesse a *VM* e desligue o sistema:

```shell
sudo shutdown now
```

### VScode + SSH

A partir deste ponto seu sistema está praticamente pronto, basta iniciar sua *VM* em modo *Headless* e acessá-la via *ssh* pela extensão ```Remote-SSH``` do vscode.

- Virtual Box

    Iniciar (T) -> clique na setinha (↓)

    - **Início Headless**

- VScode

    Instale a extensão, clique no novo ícone de cor verde abaixo do ícone de configuração e execute: ```Remote-SSH: Connect to Host```

    - \+ Add New SSH Host...
      - **ssh joao@192.168.1.10**

    Informe a senha e realize a primeira conexão.

    Após realizada a conexão, altere o arquivo ```~/.ssh/config``` inserindo o caminho para a localização da chave privada. Necessário para realização da autenticação via chave. Exemplo:

    ```markdown
    Host 192.168.1.10
    HostName 192.168.1.10
    User joao
    IdentityFile .ssh/id_vm_java_web_socket
    ```

Para mais informações sobre a extensão **Remote SSH** acesse sua documentação [aqui](https://code.visualstudio.com/docs/remote/ssh).

---
