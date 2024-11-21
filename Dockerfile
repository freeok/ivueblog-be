FROM openjdk:11-slim

ADD ./ivueblog-0.0.1-SNAPSHOT.jar ivueblog.jar

ENTRYPOINT [ \
"java", "-Xms64m", "-Xmx64m", "-Dfile.encoding=UTF-8", \
"-Duser.timezone=GMT+08", \
"-Dspring.profiles.active=prod", \
"-Djava.security.egd=file:/dev/./urandom", \
"-jar", "ivueblog.jar"]

EXPOSE 8181