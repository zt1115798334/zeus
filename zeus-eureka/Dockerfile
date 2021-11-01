FROM openjdk:8u282-oraclelinux8
MAINTAINER zt
VOLUME /tmp
ADD /target/zeus-eureka-0.0.1-SNAPSHOT.jar zeus-eureka-0.0.1-SNAPSHOT.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-jar","-XX:MetaspaceSize=128m","-XX:MaxMetaspaceSize=256m","-Xms256m","-Xmx256m","-Xmn128m","-Xss256k","-XX:SurvivorRatio=8","-XX:+UseConcMarkSweepGC","zeus-eureka-0.0.1-SNAPSHOT.jar"]