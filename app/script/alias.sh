#!/bin/sh

# Add alias to startup fast the process of compile and run
PATH_APP_SRC=/workspaces/websocket-compdist/app/src
PATH_APP_BIN=/workspaces/websocket-compdist/app/bin

echo -n "\n" | tee -a ~/.bashrc ~/.zshrc >> /dev/null

echo "alias serexec=\"reset; javac ${PATH_APP_SRC}/*.java -d \
${PATH_APP_BIN}; cd ${PATH_APP_BIN}; java Server -t; cd -\"" | tee -a ~/.bashrc ~/.zshrc >> /dev/null

echo "alias cliexec=\"reset; cd ${PATH_APP_BIN}; java Cliente; cd -\"" | tee -a ~/.bashrc ~/.zshrc >> /dev/null