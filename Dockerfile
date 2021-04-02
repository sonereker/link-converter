FROM gradle:6.8.3-jdk15 AS build
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
RUN gradle build --stacktrace --info

FROM adoptopenjdk:15-jre-hotspot
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/app/build/libs/*.jar /app/lc.jar

ENTRYPOINT ["java", "--enable-preview", "-jar","/app/lc.jar"]
