package com.hrs.mediarequesttool.common.tiles;

import org.apache.tiles.startup.TilesInitializer;
import org.apache.tiles.web.startup.AbstractTilesInitializerServlet;

public class TilesServlet extends AbstractTilesInitializerServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4191308333884255862L;

	@Override
	protected TilesInitializer createTilesInitializer() {
		return new FreeMarkerTilesInitializer();
	}
	
	
	
	
}
