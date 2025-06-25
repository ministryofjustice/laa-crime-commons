FROM amazoncorretto:21-alpine
RUN mkdir -p /opt/samples/
WORKDIR /opt/samples/
COPY ./build/libs/samples.jar /opt/samples/app.jar
RUN addgroup -S appgroup && adduser -u 1001 -S appuser -G appgroup
USER 1001
EXPOSE 8020
CMD java -jar app.jar