package co.afgm.akka.api;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;

import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.cluster.client.ClusterClient;
import akka.cluster.client.ClusterClientSettings;
import akka.cluster.client.ContactPointAdded;
import akka.cluster.client.ContactPointRemoved;
import akka.cluster.client.ContactPoints;
import akka.cluster.client.SubscribeContactPoints;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ClientListener extends AbstractActor {

	protected final LoggingAdapter log = Logging.getLogger(context().system(), this);
	private ActorRef targetClient;
	private final Set<ActorPath> contactPoints = new HashSet<>();
	private Repository repository;

	public ClientListener(ActorRef targetClient, ApplicationContext applicationContext) {
		this.targetClient = targetClient;
		repository = applicationContext.getBean(Repository.class);
	}

	@Override
	public void preStart() {
		targetClient.tell(SubscribeContactPoints.getInstance(), self());
		context().watch(targetClient);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(ContactPoints.class, msg -> {
			contactPoints.addAll(msg.getContactPoints());
			// Now do something with an up-to-date "contactPoints"
			log.info("$$$$$$$$$$$$$ContactPoints {}", contactPoints);
		}).match(ContactPointAdded.class, msg -> {
			contactPoints.add(msg.contactPoint());
			// Now do something with an up-to-date "contactPoints"
			log.info("$$$$$$$$$$$$$ContactPointAdded {}", msg.contactPoint());
			log.info("$$$$$$$$$$$$$ContactPoints {}", contactPoints);
		}).match(ContactPointRemoved.class, msg -> {
			contactPoints.remove(msg.contactPoint());
			// Now do something with an up-to-date "contactPoints"
			log.info("$$$$$$$$$$$$$ContactPointRemoved {}", msg.contactPoint());
			log.info("$$$$$$$$$$$$$ContactPoints {}", contactPoints);
		}).match(Terminated.class, msg -> {

			log.info("$$$$$$$$$$$$$Terminated {}", msg);

			Set<ActorPath> initialContacts = new HashSet<>();
			String backendContact = repository.queryContactBackend();

			initialContacts.add(Utility.actorPath(backendContact));

			targetClient = context().system()
					.actorOf(ClusterClient.props(
							ClusterClientSettings.create(context().system()).withInitialContacts(initialContacts)),
							"client");
			targetClient.tell(SubscribeContactPoints.getInstance(), self());
			context().watch(targetClient);

		}).build();
	}

}
