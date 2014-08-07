package com.hrs.mediarequesttool.common.tiles;

import org.apache.tiles.TilesApplicationContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.context.TilesRequestContextFactory;
import org.apache.tiles.evaluator.AttributeEvaluatorFactory;
import org.apache.tiles.factory.BasicTilesContainerFactory;
import org.apache.tiles.freemarker.renderer.FreeMarkerAttributeRenderer;

public class FreeMarkerTilesContainerFactory extends BasicTilesContainerFactory {
	@Override
	protected void registerAttributeRenderers(org.apache.tiles.renderer.impl.BasicRendererFactory rendererFactory, TilesApplicationContext applicationContext, TilesRequestContextFactory contextFactory, TilesContainer container, AttributeEvaluatorFactory attributeEvaluatorFactory) {
		super.registerAttributeRenderers(rendererFactory, applicationContext, contextFactory, container, attributeEvaluatorFactory);

		FreeMarkerAttributeRenderer freemarkerRenderer = new FreeMarkerAttributeRenderer();
		freemarkerRenderer.setApplicationContext(applicationContext);
		freemarkerRenderer.setAttributeEvaluatorFactory(attributeEvaluatorFactory);
		//freemarkerRenderer.setEvaluator(attributeEvaluatorFactory);
		freemarkerRenderer.setRequestContextFactory(contextFactory);

		freemarkerRenderer.setParameter("TemplatePath", "/");
		freemarkerRenderer.setParameter("NoCache", "true");
		freemarkerRenderer.setParameter("ContentType", "text/html");
		freemarkerRenderer.setParameter("template_update_delay", "0");
		freemarkerRenderer.setParameter("default_encoding", "UTF-8");
		freemarkerRenderer.setParameter("number_format", "0.##########");

		freemarkerRenderer.commit();

		rendererFactory.registerRenderer("freemarker", freemarkerRenderer);
	}
}
