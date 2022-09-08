package com.l2jserver.cli.config;

import static org.aeonbits.owner.Config.DisableableFeature.PARAMETER_FORMATTING;

import org.aeonbits.owner.Mutable;

/**
 * ServerConfiguration.
 * @author Zoey76
 * @version 1.0.0
 */
public interface ServerConfiguration extends Mutable {
	
	@Key("DatabaseDriver")
	String getDatabaseDriver();
	
	@Key("DatabaseURL")
	String getDatabaseURL();
	
	@Key("DatabaseName")
	String getDatabaseName();
	
	@Key("DatabaseUser")
	String getDatabaseUser();
	
	@Key("DatabasePassword")
	@DisableFeature(PARAMETER_FORMATTING)
	String getDatabasePassword();
	
	@Key("DatabaseConnectionPool")
	String getDatabaseConnectionPool();
	
	@Key("DatabaseMaximumPoolSize")
	int getDatabaseMaximumPoolSize();
	
	@Key("DatabaseMaximumIdleTime")
	int getDatabaseMaximumIdleTime();
}
