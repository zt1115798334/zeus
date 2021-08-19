FROM openjdk:8u282-oraclelinux8
MAINTAINER zt
VOLUME /tmp
ADD /target/zeus-admin-0.0.1-SNAPSHOT.jar zeus-admin-0.0.1-SNAPSHOT.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-jar","zeus-admin-0.0.1-SNAPSHOT.jar"]