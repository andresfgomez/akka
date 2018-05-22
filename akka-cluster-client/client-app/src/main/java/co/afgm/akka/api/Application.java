package co.afgm.akka.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Application {

	@Autowired
	ActorSystem actorSystem;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ApplicationConfig.class, args);

		ActorSystem actorSystem = context.getBean(ActorSystem.class);

		ActorRef clientRef = (ActorRef) context.getBean("client");

		actorSystem.actorOf(Props.create(ClientListener.class, clientRef, context));

	}

}
