package com.LBA.LBAResource;

import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * @author payalpatel
 * 
 */
public class LBAResourceServerMain {

	public static void main(String[] args) throws Exception {

		// Create a new Component.
		Component component = new Component();

		// Add a new HTTP server listening on port 8182.
		component.getServers().add(Protocol.HTTP, 8182);

		component.getDefaultHost().attach("/LocationBasedAdRest",
				new LBAResourceApplication());

		// Start the component.
		component.start();

	}
}
