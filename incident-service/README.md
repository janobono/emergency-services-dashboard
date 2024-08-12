# incident-service

## build with docker

- [Docker](https://docs.docker.com/get-docker/)

```shell
./build.sh
```

or

```shell
docker build -t sk.janobono.emergency/incident-service:latest .
```

## build with local tools

- [jdk21](https://adoptium.net/)
- [maven 3.9](https://maven.apache.org/)

```shell
mvn clean install
```
