FROM vixns/java8:latest

ADD ./target/comments-example-1.0.0.jar /usr/local/simple/comments-example-1.0.0.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/usr/local/simple/comments-example-1.0.0.jar"]