package co.afgm.akka.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import akka.actor.ActorRef;
import akka.cluster.client.ClusterClient;

@Service
public class ServiceImpl {

	@Autowired
	ActorRef clientRef;

	public void sayHello() {
		clientRef.tell(new ClusterClient.Send("/user/serviceA", "hello", true), ActorRef.noSender());
	}

}
