package com.jteam.vertx.config;

import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.json.JsonObject;

public class ConfigTest {

	public static void main(String[] args)
	{
		ConfigStoreOptions httpStore = new ConfigStoreOptions()
				  .setType("http")
				  .setConfig(new JsonObject()
				    .put("host", "localhost").put("port", 8080).put("path", "/conf"));
		
		
	}
	
}
