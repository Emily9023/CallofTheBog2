package com.emilyn.callofthebog.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.emilyn.callofthebog.CallofTheBog;

public class HtmlLauncher extends GwtApplication {

	@Override
	public GwtApplicationConfiguration getConfig() {
		return new GwtApplicationConfiguration(true);
	}

	@Override
	public ApplicationListener createApplicationListener() { return new CallofTheBog(); }
}
