#!/bin/sh

# Add alias to startup fast the process of compile and run
echo "alias serexec=\"reset; javac /workspaces/java-test/src/*.java -d /workspaces/java-test/bin; cd /workspaces/java-test/bin; java Servidor; cd -\"" | tee -a ~/.bashrc ~/.zshrc >> /dev/null
echo "alias cliexec=\"reset; cd /workspaces/java-test/bin; java Cliente; cd -\"" | tee -a ~/.bashrc ~/.zshrc >> /dev/null