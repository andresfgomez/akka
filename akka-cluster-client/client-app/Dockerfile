FROM openjdk:8
ADD build/libs/akka-cluster-client-demo-frontend.jar .
ENTRYPOINT exec java $JAVA_OPTS -jar /akka-cluster-client-demo-frontend.jar