package com.jteam.vertx.account;

import com.jteam.vertx.account.api.RestUserAccountAPIVerticle;
import com.jteam.vertx.account.imp.JdbcAccountServiceImpl;
import com.jteam.vertx.common.BaseMicroserviceVerticle;

import static com.jteam.vertx.account.AccountService.SERVICE_ADDRESS;
import static com.jteam.vertx.account.AccountService.SERVICE_NAME;
 
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.serviceproxy.ProxyHelper;


/* A verticle publishing the user service.
 *
 * @author Eric Zhao
 */
public class UserAccountVerticle extends BaseMicroserviceVerticle {

	  private AccountService accountService;

	  @Override
	  public void start(Future<Void> future) throws Exception {
	    super.start();	    
	    accountService = new JdbcAccountServiceImpl(vertx, config());
	    
	    accountService.initializePersistence(resultHandler->{
	    	
	    	if(resultHandler.succeeded())
	    	{
	    		System.out.println("数据库初始化成功");
	    		
	    	}else{
	    		System.out.println("数据库初始化失败:"+resultHandler.cause());
	    	}
	    	 
	    });
	    // register the service proxy on event bus
	    ProxyHelper.registerService(AccountService.class, vertx, accountService, SERVICE_ADDRESS);
	    // publish the service and REST endpoint in the discovery infrastructure
	    publishEventBusService(SERVICE_NAME, SERVICE_ADDRESS, AccountService.class)
	      .compose(servicePublished -> deployRestVerticle())
	      .setHandler(future.completer());
	  }

	  private Future<Void> deployRestVerticle() {
	    Future<String> future = Future.future();
	    vertx.deployVerticle(new RestUserAccountAPIVerticle(accountService),
	      new DeploymentOptions().setConfig(config()),
	      future.completer());
	    return future.map(r -> null);
	  }
}
