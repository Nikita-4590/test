package com.hrs.mediarequesttool.common.tiles;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.factory.AbstractTilesContainerFactory;
import org.apache.tiles.startup.AbstractTilesInitializer;

public class FreeMarkerTilesInitializer extends AbstractTilesInitializer {

	@Override
	protected AbstractTilesContainerFactory createContainerFactory(TilesApplicationContext arg0) {
		return new FreeMarkerTilesContainerFactory();
	}

}
