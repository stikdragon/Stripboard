package uk.co.stikman.strip.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.MenuItem;

public class IconMenuItem extends MenuItem {

	public interface MyTemplate extends SafeHtmlTemplates {
		@Template("<img src=\"{0}\" /><span>{1}</span>")
		SafeHtml createItem(SafeUri uri, SafeHtml message);
	}

	private static MyTemplate template = GWT.create(MyTemplate.class);

	public IconMenuItem(String text, ImageResource icon, ScheduledCommand cmd) {
		super(template.createItem(icon.getSafeUri(), SafeHtmlUtils.fromString(text)), cmd);
	}
	
	public IconMenuItem(String text, ScheduledCommand cmd) {
		super(text, cmd);
	}

}
