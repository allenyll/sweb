FROM java:8
VOLUME /logs
ADD sw-uac.jar /
EXPOSE 10002
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/sw-uac.jar"]
