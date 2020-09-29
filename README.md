# Computação Distribuída - 2020/2

## Java Web Socket

### Descrição do Trabalho

Trabalho prático de Computação Distribuída.

---

## Preparando o Ambiente

Para o desenvolvimento deste projeto será utilizado a ferramenta [Docker](https://www.docker.com/get-started), para isolamento da aplicação. E o editor de texto [Visual Studio Code](https://code.visualstudio.com/), para desenvolvimento.

### Instalando Docker

Para sistemas UNIX como Linux, a execução do *script* de instalação é mais do que suficiente.

```shell
curl -fsSL https://get.docker.com | sudo sh
```

Para utilizar o Docker com seu usuário, para não ter que digitar *sudo* e então entrar com o usuário *root* o tempo todo, considere adicionar seu usuário ao grupo "docker".

```shell
sudo usermod -aG docker ${USER}
```

Para a efetivar a alteração é necessário realizar ***LOGOUT*** na máquina.

### Instalando VS Code

Visite o [site official](https://code.visualstudio.com/) e siga as instruções de instalação.

Após instaldo, instale também a extenção [Remote Development](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack). Ela é necessária para acessar o container através do vscode.

```shell
ext install ms-vscode-remote.vscode-remote-extensionpack
```

Após instalada a extenção, contrua o container.