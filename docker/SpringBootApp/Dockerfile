FROM maven:3.9.8-eclipse-temurin-21

RUN mkdir /alecs
WORKDIR /alecs

RUN git clone --branch main https://github.com/marcosolina/controllocircolarialecs.git
RUN ls -l

RUN \
mvn clean package -f ./controllocircolarialecs/controllocircolariscuola/pom.xml

WORKDIR /alecs/controllocircolarialecs/controllocircolariscuola
RUN mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install-deps"
WORKDIR /alecs

COPY ./start.sh start.sh
RUN sed -i -e 's/\r$//' start.sh
RUN chmod +x start.sh

ENTRYPOINT ["/alecs/start.sh"]