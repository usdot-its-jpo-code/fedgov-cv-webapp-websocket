/** LEGACY CODE
 * 
 * This was salvaged in part or in whole from the Legacy System. It will be heavily refactored or removed.
 */
package gov.dot.its.jpo.sdcsdw.websocketsfragment.deposit;

import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.MongoConfig;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.server.WebSocketServer;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.server.utils.ConfigUtils;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

public class DepositProviderServlet extends HttpServlet {
	
	private static final Logger logger = Logger.getLogger(DepositProviderServlet.class
			.getName());
	
	private static final long serialVersionUID = 7267082140595016324L;
	
	private DepositEventListener depositListener;

	@Override
	public void init() throws ServletException {
		super.init();
		try {
			String mongoConfigFile = getInitParameter("depositConfigFile");
			List<MongoConfig> configList = ConfigUtils.loadConfigBeanList(mongoConfigFile, MongoConfig.class);
			logger.info("Using " + configList);
			
			depositListener = new DepositEventListener(configList);
			depositListener.connect();
			WebSocketServer.registerEventListener(depositListener);
			
		} catch (Exception e) {
			logger.error("DepositProviderServlet initialization failed", e);
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		if (depositListener != null) {
			depositListener.close();
		}
	}
}
