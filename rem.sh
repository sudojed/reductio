#!/bin/bash

# Dados do autor que substituirão o GitHub Copilot
NOME="Abner Lourenço"
EMAIL="abnerjaredejede@gmail.com"

git filter-branch --env-filter '
if [ "$GIT_AUTHOR_NAME" = "GitHub Copilot" ] || [ "$GIT_COMMITTER_NAME" = "GitHub Copilot" ]
then
    export GIT_AUTHOR_NAME="'"$NOME"'"
    export GIT_AUTHOR_EMAIL="'"$EMAIL"'"
    export GIT_COMMITTER_NAME="'"$NOME"'"
    export GIT_COMMITTER_EMAIL="'"$EMAIL"'"
fi
' --tag-name-filter cat -- --all

# Atualiza o repositório remoto
git push --force --tags origin 'refs/heads/*'
