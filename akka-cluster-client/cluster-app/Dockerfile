FROM openjdk:8
ADD build/libs/akka-cluster-client-demo-backend.jar .
ENTRYPOINT exec java $JAVA_OPTS -jar /akka-cluster-client-demo-backend.jar 