FROM openjdk:8u282-oraclelinux8
MAINTAINER zt
VOLUME /tmp
ADD /target/zeus-transfer-0.0.1-SNAPSHOT.jar zeus-transfer-0.0.1-SNAPSHOT.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-jar","zeus-transfer-0.0.1-SNAPSHOT.jar"]