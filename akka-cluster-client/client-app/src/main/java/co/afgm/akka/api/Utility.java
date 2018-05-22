package co.afgm.akka.api;

import akka.actor.ActorPath;
import akka.actor.ActorPaths;

public class Utility {

	public static ActorPath actorPath(String path) {

		StringBuilder uri = new StringBuilder();
		uri.append(path);
		uri.append("/system/receptionist");

		return ActorPaths.fromString(uri.toString());
	}

}
