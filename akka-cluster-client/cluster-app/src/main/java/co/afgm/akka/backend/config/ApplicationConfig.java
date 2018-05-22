package co.afgm.akka.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import akka.actor.ActorSystem;
import akka.management.AkkaManagement;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import redis.clients.jedis.Jedis;

@Configuration
@ComponentScan(basePackages = "co.afgm.akka.backend")
public class ApplicationConfig {

	@Bean
	public ActorSystem actorSystem() {

		ActorSystem actorSystem = ActorSystem.create("backend");
		AkkaManagement.get(actorSystem).start();
		ClusterBootstrap.get(actorSystem).start();

		return actorSystem;
	}

	@Bean
	public Jedis redis(Environment env) {

		String redisHost = env.getProperty("redis.host");
		String redisPort = env.getProperty("redis.port");

		return new Jedis(redisHost, Integer.parseInt(redisPort));
	}

}
