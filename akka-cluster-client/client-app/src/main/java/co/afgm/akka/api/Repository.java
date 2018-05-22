package co.afgm.akka.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;

@Component
public class Repository {

	@Autowired
	private Jedis jedis;

	public String queryContactBackend() {
		return jedis.get("backend/seed");
	}
}
