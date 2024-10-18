#!/bin/bash

composeFile="/opt/alecs/docker/docker-compose.yml"

docker-compose -f $composeFile down

containerName="alecontrollocircolari"
imageName="controllo-circolari"

#if [ "$(docker container ls -a -f "name=$containerName" -q)" ]; then
#    docker container rm $containerName
#fi

#if [ "$(docker image ls -a -f "reference=$imageName" -q)" ]; then
#    docker image rm $imageName
#fi

docker system prune -a --volumes -f

docker-compose -f $composeFile up -d