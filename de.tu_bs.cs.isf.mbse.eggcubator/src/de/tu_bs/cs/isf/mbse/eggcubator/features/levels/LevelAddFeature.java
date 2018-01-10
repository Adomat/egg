package de.tu_bs.cs.isf.mbse.eggcubator.features.levels;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeService;
import org.eclipse.graphiti.util.IColorConstant;

import de.tu_bs.cs.isf.mbse.eggcubator.features.ILevelFeature;
import de.tu_bs.isf.cs.mbse.egg.level.Level;

public class LevelAddFeature extends AbstractAddFeature implements ILevelFeature {
	static final int DEFAULT_BLOCK_SIZE = 75; // TODO model: level.blockSize
	static final int OFFSET_X = 20;
	static final int OFFSET_Y = 20;
	static final int INITIAL_BLOCKS_HORIZONTAL = 4; // TODO model: replace level.Width/Height
	static final int INITIAL_BLOCKS_VERTICAL = 4;
	static final int BLOCK_FRAME_WIDTH = 1;
	static final int LEVEL_FRAME_WIDTH = 2;

	public LevelAddFeature(IFeatureProvider fp) {
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context) {
		return context.getNewObject() instanceof Level &&
				context.getTargetContainer() instanceof Diagram &&
				(context.getTargetContainer().getChildren() == null ||
				context.getTargetContainer().getChildren().isEmpty());
	}

	@Override
	public PictogramElement add(IAddContext context) {
		IPeService pe = Graphiti.getPeService();
		IGaService ga = Graphiti.getGaService();
		Level level = (Level) context.getNewObject();
		
		// outer shape with link to level
		ContainerShape levelShape = pe.createContainerShape(context.getTargetContainer(), true);
		link(levelShape, level);
		Integer width = INITIAL_BLOCKS_HORIZONTAL;
		pe.setPropertyValue(levelShape, "width", width.toString());
		Integer height = INITIAL_BLOCKS_VERTICAL;
		pe.setPropertyValue(levelShape, "height", height.toString());
		
		// add visible rectangle
		Rectangle outerLevelRectangle = ga.createRectangle(levelShape);
		outerLevelRectangle.setForeground(manageColor(IColorConstant.BLACK));
		outerLevelRectangle.setBackground(manageColor(IColorConstant.WHITE));
		outerLevelRectangle.setLineWidth(LEVEL_FRAME_WIDTH);
		ga.setLocationAndSize(outerLevelRectangle, OFFSET_X, OFFSET_Y,
				DEFAULT_BLOCK_SIZE * INITIAL_BLOCKS_HORIZONTAL + LEVEL_FRAME_WIDTH * 2,
				DEFAULT_BLOCK_SIZE * INITIAL_BLOCKS_VERTICAL + LEVEL_FRAME_WIDTH * 2);
		
		// add visible empty blocks
		for (Integer x = 0; x < INITIAL_BLOCKS_HORIZONTAL; x++) {
			for (Integer y = 0; y < INITIAL_BLOCKS_VERTICAL; y++) {
				// TODO util
				// the container shape gets properties, so we can search through them
				ContainerShape blockContainerShape = pe.createContainerShape(levelShape, false);
				pe.setPropertyValue(blockContainerShape, "positionX", x.toString());
				pe.setPropertyValue(blockContainerShape, "positionY", y.toString());
				
				// add visible rectangle for each empty block
				// This will be always below real blocks once added
				Rectangle blockRectangle = ga.createRectangle(blockContainerShape);
				blockRectangle.setLineWidth(BLOCK_FRAME_WIDTH);
				blockRectangle.setForeground(manageColor(IColorConstant.LIGHT_BLUE));
				blockRectangle.setBackground(manageColor(IColorConstant.WHITE));
				blockRectangle.setLineStyle(LineStyle.DASH);
				ga.setLocationAndSize(blockRectangle,
						DEFAULT_BLOCK_SIZE * x + LEVEL_FRAME_WIDTH, DEFAULT_BLOCK_SIZE * y + LEVEL_FRAME_WIDTH,
						DEFAULT_BLOCK_SIZE, DEFAULT_BLOCK_SIZE);
			}
		}

		return levelShape;
	}

}
