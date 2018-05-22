package co.afgm.akka.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.client.ClusterClientReceptionist;
import co.afgm.akka.backend.config.ApplicationConfig;
import redis.clients.jedis.Jedis;

public class Application {

	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

		Repository repository = context.getBean(Repository.class);

		ActorSystem actorSystem = context.getBean(ActorSystem.class);

		ActorRef serviceA = actorSystem.actorOf(Props.create(Actor.class), "serviceA");

		ClusterClientReceptionist.get(actorSystem).registerService(serviceA);

		Cluster.get(actorSystem).registerOnMemberUp(() -> {

			System.out.println("----------> Member is UP");

			if (Cluster.get(actorSystem).state().leader().isDefined()) {
				System.out.println(Cluster.get(actorSystem).state().leader().get().toString());
				// akka.tcp://backend@172.17.0.7:2551
				repository.save(Cluster.get(actorSystem).state().leader().get().toString());
			}

		});

	}

	@Component
	static class Repository {

		@Autowired
		Jedis jedis;

		public void save(String data) {
			jedis.set("backend/seed", data);
		}

	}

	static class Actor extends AbstractActor {

		@Override
		public Receive createReceive() {
			return receiveBuilder().match(String.class, message -> {
				System.out.println(message);
			}).build();
		}

	}

}
