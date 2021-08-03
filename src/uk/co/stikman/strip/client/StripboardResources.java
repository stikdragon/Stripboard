package uk.co.stikman.strip.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

interface StripboardResources extends ClientBundle {
	public static StripboardResources INSTANCE = GWT.create(StripboardResources.class);

	@Source(value = {"library.txt"})
	TextResource library();
}