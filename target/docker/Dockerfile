FROM dockerfile/java:oracle-java8

VOLUME /tmp
ADD authservice.jar /opt/authservice/
EXPOSE 8080
WORKDIR /opt/authservice/
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xms512m", "-Xmx1g", "-jar", "authservice.jar"]
