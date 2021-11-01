FROM openjdk:8u282-oraclelinux8
MAINTAINER zt
VOLUME /tmp
ADD /target/zeus-transfer-local-0.0.1-SNAPSHOT.jar zeus-transfer-local-0.0.1-SNAPSHOT.jar
RUN export LC_ALL=zh_CN.UTF-8
RUN echo "export LC_ALL=zh_CN.UTF-8"  >>  /etc/profile
RUN echo "Asia/shanghai" > /etc/timezone
ENTRYPOINT ["java","-jar","-XX:MetaspaceSize=512m","-XX:MaxMetaspaceSize=1024m","-Xms128m","-Xmx1024m","-Xmn256m","-Xss256k","-XX:SurvivorRatio=8","-XX:+UseConcMarkSweepGC","zeus-transfer-local-0.0.1-SNAPSHOT.jar"]