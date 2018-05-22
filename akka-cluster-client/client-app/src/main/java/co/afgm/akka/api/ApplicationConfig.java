package co.afgm.akka.api;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import redis.clients.jedis.Jedis;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackages = "co.afgm.akka.api")
public class ApplicationConfig {

	private static final String ACTOR_SYSTEM_CLIENT_NAME = "frontend";

	@Bean
	public ActorSystem actorSystem() {
		ActorSystem actorSystem = ActorSystem.create(ACTOR_SYSTEM_CLIENT_NAME);
		return actorSystem;
	}

	@Bean
	public Jedis redis(Environment env) {

		String redisHost = env.getProperty("redis.host");
		String redisPort = env.getProperty("redis.port");

		return new Jedis(redisHost, Integer.parseInt(redisPort));
	}

	@Qualifier("client")
	@Bean("client")
	public ActorRef client(ActorSystem actorSystem, Repository repository) {

		Set<ActorPath> initialContacts = new HashSet<>();
		String backendContact = repository.queryContactBackend();

		initialContacts.add(Utility.actorPath(backendContact));

		return actorSystem.actorOf(
				ClusterClient.props(ClusterClientSettings.create(actorSystem).withInitialContacts(initialContacts)),
				"client");
	}
}
