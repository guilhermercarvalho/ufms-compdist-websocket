FROM ubuntu:20.04

# Change the 'USERNAME' to your username system (command: whoami)
ARG USERNAME=guilherme

ARG USER_UID=1000
ARG USER_GID=$USER_UID

ENV TZ=America/Campo_Grande

# Set up timezone
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Install requirement tools
RUN apt-get update && apt-get install -y default-jdk git vim bash-completion
    
# Create the user
RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    #
    # [Optional] Add sudo support. Omit if you don't need to install software after connecting.
    && apt-get install -y sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Mount vscode extensions
RUN mkdir -p /home/$USERNAME/.vscode-server/extensions \
        /home/$USERNAME/.vscode-server-insiders/extensions \
    && chown -R $USERNAME \
        /home/$USERNAME/.vscode-server \
        /home/$USERNAME/.vscode-server-insiders

# [Optional] Set the default user. Omit if you want to keep the default as root.
USER $USERNAME
