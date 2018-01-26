package de.tu_bs.cs.isf.mbse.eggcubator.tabbedProperties;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import de.tu_bs.cs.isf.mbse.egg.level.Level;

public class LevelSection extends GFPropertySection implements ITabbedPropertyConstants, VerifyListener, Listener {
	
	private Text nameText;
	private Text widthText;
	private Text heightText;
	private Text elementSizeText;
	private Button entryPointButton;
	private Text gravityText;
	private Text backgroundImageText;
	private Text backgroundColorText;
	private Text deathScreenTitleText;
	private Text deathScreenTextText;
	
	private boolean listenerStopped = false;

	public LevelSection() { }
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
        Composite composite = factory.createFlatFormComposite(parent);
        FormData data;
        
        // name
        nameText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(0, (int) (STANDARD_LABEL_WIDTH * 1.5));
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, VSPACE);
        nameText.setLayoutData(data);
        nameText.addListener(SWT.FocusOut, this);
        nameText.addListener(SWT.KeyDown, this);
 
        CLabel nameLabel = factory.createCLabel(composite, "Name:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(nameText, -HSPACE);
        data.top = new FormAttachment(nameText, 0, SWT.CENTER);
        nameLabel.setLayoutData(data);
        
        // width
        widthText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(nameText, 0, SWT.LEFT);
        data.right = new FormAttachment(nameText, 0, SWT.RIGHT);
        data.top = new FormAttachment(nameText, VSPACE, SWT.BOTTOM);
        widthText.setLayoutData(data);
        widthText.addVerifyListener(this);
        widthText.addListener(SWT.FocusOut, this);
        widthText.addListener(SWT.KeyDown, this);
 
        CLabel widthLabel = factory.createCLabel(composite, "Width:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(widthText, -HSPACE);
        data.top = new FormAttachment(widthText, 0, SWT.CENTER);
        widthLabel.setLayoutData(data);
        
        // height
        heightText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(widthText, 0, SWT.LEFT);
        data.right = new FormAttachment(widthText, 0, SWT.RIGHT);
        data.top = new FormAttachment(widthText, VSPACE, SWT.BOTTOM);
        heightText.setLayoutData(data);
        heightText.addVerifyListener(this);
        heightText.addListener(SWT.FocusOut, this);
        heightText.addListener(SWT.KeyDown, this);
 
        CLabel heightLabel = factory.createCLabel(composite, "Height:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(heightText, -HSPACE);
        data.top = new FormAttachment(heightText, 0, SWT.CENTER);
        heightLabel.setLayoutData(data);
        
        // elementSize
        elementSizeText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(heightText, 0, SWT.LEFT);
        data.right = new FormAttachment(heightText, 0, SWT.RIGHT);
        data.top = new FormAttachment(heightText, VSPACE, SWT.BOTTOM);
        elementSizeText.setLayoutData(data);
        elementSizeText.addVerifyListener(this);
        elementSizeText.addListener(SWT.FocusOut, this);
        elementSizeText.addListener(SWT.KeyDown, this);
 
        CLabel elementSizeLabel = factory.createCLabel(composite, "Element Size:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(elementSizeText, -HSPACE);
        data.top = new FormAttachment(elementSizeText, 0, SWT.CENTER);
        elementSizeLabel.setLayoutData(data);
        
        // entryPoint
        entryPointButton = factory.createButton(composite, "", SWT.CHECK);
        data = new FormData();
        data.left = new FormAttachment(elementSizeText, 0, SWT.LEFT);
        data.right = new FormAttachment(elementSizeText, 0, SWT.RIGHT);
        data.top = new FormAttachment(elementSizeText, VSPACE, SWT.BOTTOM);
        entryPointButton.setLayoutData(data);
        entryPointButton.addListener(SWT.MouseUp, this);
 
        CLabel entryPointLabel = factory.createCLabel(composite, "Is Entry Point:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(entryPointButton, -HSPACE);
        data.top = new FormAttachment(entryPointButton, 0, SWT.CENTER);
        entryPointLabel.setLayoutData(data);
        
    	// gravity
        gravityText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(entryPointButton, 0, SWT.LEFT);
        data.right = new FormAttachment(entryPointButton, 0, SWT.RIGHT);
        data.top = new FormAttachment(entryPointButton, VSPACE, SWT.BOTTOM);
        gravityText.setLayoutData(data);
        gravityText.addVerifyListener(this);
        gravityText.addListener(SWT.FocusOut, this);
        gravityText.addListener(SWT.KeyDown, this);
 
        CLabel gravityLabel = factory.createCLabel(composite, "Gravity:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(gravityText, -HSPACE);
        data.top = new FormAttachment(gravityText, 0, SWT.CENTER);
        gravityLabel.setLayoutData(data);
        
    	// backgroundImage
        backgroundImageText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(gravityText, 0, SWT.LEFT);
        data.right = new FormAttachment(gravityText, 0, SWT.RIGHT);
        data.top = new FormAttachment(gravityText, VSPACE, SWT.BOTTOM);
        backgroundImageText.setLayoutData(data);
        backgroundImageText.addListener(SWT.FocusOut, this);
        backgroundImageText.addListener(SWT.KeyDown, this);
 
        CLabel backgroundImageLabel = factory.createCLabel(composite, "Background Image:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(backgroundImageText, -HSPACE);
        data.top = new FormAttachment(backgroundImageText, 0, SWT.CENTER);
        backgroundImageLabel.setLayoutData(data);
        
    	//  backgroundColor
        backgroundColorText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(backgroundImageText, 0, SWT.LEFT);
        data.right = new FormAttachment(backgroundImageText, 0, SWT.RIGHT);
        data.top = new FormAttachment(backgroundImageText, VSPACE, SWT.BOTTOM);
        backgroundColorText.setLayoutData(data);
        backgroundColorText.addVerifyListener(this);
        backgroundColorText.addListener(SWT.FocusOut, this);
        backgroundColorText.addListener(SWT.KeyDown, this);
 
        CLabel backgroundColorLabel = factory.createCLabel(composite, "Background Color:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(backgroundColorText, -HSPACE);
        data.top = new FormAttachment(backgroundColorText, 0, SWT.CENTER);
        backgroundColorLabel.setLayoutData(data);
        
    	// deathScreenTitle
        deathScreenTitleText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(backgroundColorText, 0, SWT.LEFT);
        data.right = new FormAttachment(backgroundColorText, 0, SWT.RIGHT);
        data.top = new FormAttachment(backgroundColorText, VSPACE, SWT.BOTTOM);
        deathScreenTitleText.setLayoutData(data);
        deathScreenTitleText.addListener(SWT.FocusOut, this);
        deathScreenTitleText.addListener(SWT.KeyDown, this);
 
        CLabel deathScreenTitleLabel = factory.createCLabel(composite, "Death screen title:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(deathScreenTitleText, -HSPACE);
        data.top = new FormAttachment(deathScreenTitleText, 0, SWT.CENTER);
        deathScreenTitleLabel.setLayoutData(data);
        
    	// deathScreenText
        deathScreenTextText = factory.createText(composite, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        data = new FormData();
        data.left = new FormAttachment(deathScreenTitleText, 0, SWT.LEFT);
        data.right = new FormAttachment(deathScreenTitleText, 0, SWT.RIGHT);
        data.top = new FormAttachment(deathScreenTitleText, VSPACE, SWT.BOTTOM);
        data.height = 16 * 6; // 6 times default lines
        deathScreenTextText.setLayoutData(data);
        deathScreenTextText.addListener(SWT.FocusOut, this);
 
        CLabel deathScreenTextLabel = factory.createCLabel(composite, "Death screen text:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(deathScreenTextText, -HSPACE);
        data.top = new FormAttachment(deathScreenTextText, 0, SWT.TOP);
        deathScreenTextLabel.setLayoutData(data);
	}

	@Override
	public void refresh() {
		// Level changed
		PictogramElement pe = getSelectedPictogramElement();
        Level level = (Level) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
        
        listenerStopped = true;
        
        // name
        String name = level.getName();
        if (name == null || name.isEmpty())
        	name = "Level";
        nameText.setText(name);
        
        // width
        Integer width = level.getWidth();
        if (width < 3)
        	width = 3;
        widthText.setText(width.toString());

        // height
        Integer height = level.getHeight();
        if (height < 3)
        	height = 3;
        heightText.setText(height.toString());
        
        // elementSize
        Integer elementSize = level.getElementSize();
        if (elementSize < 15)
        	elementSize = 15;
        elementSizeText.setText(elementSize.toString());

        // entryPoint
        boolean entryPoint = level.isEntryPoint();
        entryPointButton.setSelection(entryPoint);
        
        // gravity
        Float gravity = level.getGravity();
        if (gravity <= 0)
        	gravity = 0.1f;
        gravityText.setText(gravity.toString());
        
        // backgroundImage
        String backgroundImage = level.getBackgroundImage();
        if (backgroundImage == null)
        	backgroundImage = "";
        backgroundImageText.setText(backgroundImage);
        
        // backgroundColor
        String backgroundColor = level.getBackgroundColor();
        if (backgroundColor == null)
        	backgroundColor = "";
        backgroundColorText.setText(backgroundColor);
        
        // deathScreenTitle
        String deathScreenTitle = level.getDeathScreenTitle();
        if (deathScreenTitle == null)
        	deathScreenTitle = "";
        deathScreenTitleText.setText(deathScreenTitle);
        
        // deathScreenText
        String deathScreenText = level.getDeathScreenText();
        if (deathScreenText == null)
        	deathScreenText = "";
        deathScreenTextText.setText(deathScreenText);
        
        listenerStopped = false;
	}

	@Override
	public void verifyText(VerifyEvent e) {
		if (!listenerStopped) {
			if ((e.widget == widthText || e.widget == heightText || e.widget == elementSizeText))
				e.text = e.text.replaceAll("[^\\d]", ""); // only allow numbers/digits
			else if (e.widget == gravityText)
				e.text = e.text.replaceAll("[^\\d\\.]", ""); // only allow numbers/digits with dot
			else if (e.widget == backgroundColorText) {
				 // only allow hex values (max 6)
				e.text = e.text.replaceAll("[^\\dA-Fa-f]", "");
				if (backgroundColorText.getText().length() + e.text.length() > 6)
					e.text = backgroundColorText.getText().length() < 6 ? e.text.substring(0, 5 - backgroundColorText.getText().length()) : "";
			}
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (listenerStopped)
			return;
		if (event.type == SWT.FocusOut || (event.type == SWT.KeyDown && event.character == SWT.CR) ||
				event.type == SWT.MouseUp) { // Focus left, enter or mouse up
			// Properties changed
			PictogramElement pe = getSelectedPictogramElement();
	        Level level = (Level) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
	        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(getDiagram());
	        
	        if (event.widget == nameText) {
		        String name = nameText.getText();
		        if (name.isEmpty()) {
		            listenerStopped = true;
		        	nameText.setText(level.getName());
		            listenerStopped = false;
		            return;
		        }
		        if (name.equals(level.getName()))
		        	return;
		        final String cName = name;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level name changed: " + level.getName() + "->" + cName) {
					@Override
					protected void doExecute() {
				        level.setName(cName);
				        if (!getDiagram().getName().equals(cName)) // TODO should be possibly somewhere else, but where?
				        	getDiagram().setName(cName);
				        // trigger Update
				        IUpdateContext context = new UpdateContext(getSelectedPictogramElement());
				        IUpdateFeature updateFeature = getDiagramTypeProvider().getFeatureProvider().getUpdateFeature(context);
				        if (updateFeature != null)
				        	updateFeature.update(context);
					}
		        });
	        } else if (event.widget == widthText) {
		        Integer width = Integer.parseInt(widthText.getText());
		        if (width < 3) {
		            listenerStopped = true;
		        	width = 3;
		        	widthText.setText(String.valueOf(width));
		            listenerStopped = false;
		        }
		        if (width == level.getWidth())
		        	return;
		        final Integer cWidth = width;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level width changed: " + level.getWidth() + "->" + cWidth.toString()) {
					@Override
					protected void doExecute() {
				        level.setWidth(cWidth);
				        // trigger Update
				        IUpdateContext context = new UpdateContext(getSelectedPictogramElement());
				        IUpdateFeature updateFeature = getDiagramTypeProvider().getFeatureProvider().getUpdateFeature(context);
				        if (updateFeature != null)
				        	updateFeature.update(context);
					}
		        });
	        } else if (event.widget == heightText) {
		        Integer height = Integer.parseInt(heightText.getText());
		        if (height < 3) {
		            listenerStopped = true;
		        	height = 3;
		        	heightText.setText(String.valueOf(height));
		            listenerStopped = false;
		        }
		        if (height == level.getHeight())
		        	return;
		        final Integer cHeight = height;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level height changed: " + level.getHeight() + "->" + cHeight.toString()) {
					@Override
					protected void doExecute() {
				        level.setHeight(cHeight);
				        // trigger Update
				        IUpdateContext context = new UpdateContext(getSelectedPictogramElement());
				        IUpdateFeature updateFeature = getDiagramTypeProvider().getFeatureProvider().getUpdateFeature(context);
				        if (updateFeature != null)
				        	updateFeature.update(context);
					}
		        });
	        } else if (event.widget == elementSizeText) {
		        Integer elementSize = Integer.parseInt(elementSizeText.getText());
		        if (elementSize < 15) {
		            listenerStopped = true;
		        	elementSize = 15;
		        	elementSizeText.setText(String.valueOf(elementSize));
		            listenerStopped = false;
		        }
		        if (elementSize == level.getElementSize())
		        	return;
		        final Integer cElementSize = elementSize;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level element size changed: " + String.valueOf(level.getElementSize()) + "->" + cElementSize.toString()) {
					@Override
					protected void doExecute() {
				        level.setElementSize(cElementSize);
				        // trigger Update
				        IUpdateContext context = new UpdateContext(getSelectedPictogramElement());
				        IUpdateFeature updateFeature = getDiagramTypeProvider().getFeatureProvider().getUpdateFeature(context);
				        if (updateFeature != null)
				        	updateFeature.update(context);
					}
		        });
	        } else if (event.widget == entryPointButton) {
	        	final boolean entryPointVal = entryPointButton.getSelection();
		        if (entryPointVal == level.isEntryPoint())
		        	return;
		        domain.getCommandStack().execute(new RecordingCommand(domain, entryPointVal ? "Set entry point flag for level" : "Removed entry point flag from level") {
					@Override
					protected void doExecute() {
				        level.setEntryPoint(entryPointVal);
				        // no update needed
					}
		        });
	        } else if (event.widget == gravityText) {
		        Float gravity = Float.parseFloat(gravityText.getText());
		        if (gravity <= 0) {
		            listenerStopped = true;
		            gravity = 0.1f;
		            gravityText.setText(String.valueOf(gravity));
		            listenerStopped = false;
		        }
		        if (gravity == level.getGravity())
		        	return;
		        final Float cGravity = gravity;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level gravity changed: " + String.valueOf(level.getGravity()) + "->" + cGravity.toString()) {
					@Override
					protected void doExecute() {
				        level.setGravity(cGravity);
				        // no update needed
					}
		        });
	        }  else if (event.widget == backgroundImageText) {
		        String backgroundImage = backgroundImageText.getText();
		        if (backgroundImage.isEmpty())
		        	backgroundImage = null;
		        if (backgroundImage == level.getBackgroundImage() || (backgroundImage != null && backgroundImage.equals(level.getBackgroundImage())))
		        	return;
		        final String cBackgroundImage = backgroundImage;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level background image changed: " +
		        		(level.getBackgroundImage() != null ? level.getBackgroundImage() : "") + "->" + (cBackgroundImage != null ? cBackgroundImage : "")) {
					@Override
					protected void doExecute() {
				        level.setBackgroundImage(cBackgroundImage);
				        // no update needed
					}
		        });
	        } else if (event.widget == backgroundColorText) {
		        String backgroundColor = backgroundColorText.getText();
		        if (backgroundColor.isEmpty())
		        	backgroundColor = null;
		        if (backgroundColor == level.getBackgroundColor() || (backgroundColor != null && backgroundColor.equals(level.getBackgroundColor())))
		        	return;
		        final String cBackgroundColor = backgroundColor;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level background color changed: " +
		        		(level.getBackgroundColor() != null ? level.getBackgroundColor() : "") + "->" + (cBackgroundColor != null ? cBackgroundColor : "")) {
					@Override
					protected void doExecute() {
				        level.setBackgroundColor(cBackgroundColor);
				        // no update needed
					}
		        });
	        } else if (event.widget == deathScreenTitleText) {
		        String deathScreenTitle = deathScreenTitleText.getText();
		        if (deathScreenTitle.isEmpty())
		        	deathScreenTitle = null;
		        if (deathScreenTitle == level.getDeathScreenTitle() || (deathScreenTitle != null && deathScreenTitle.equals(level.getDeathScreenTitle())))
		        	return;
		        final String cDeathScreenTitle = deathScreenTitle;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level death screen title changed: " +
		        		(level.getDeathScreenTitle() != null ? level.getDeathScreenTitle() : "default") + "->" + (cDeathScreenTitle != null ? cDeathScreenTitle : "default")) {
					@Override
					protected void doExecute() {
				        level.setDeathScreenTitle(cDeathScreenTitle);
				        // no update needed
					}
		        });
	        } else if (event.widget == deathScreenTextText) {
		        String deathScreenText = deathScreenTextText.getText();
		        if (deathScreenText.isEmpty())
		        	deathScreenText = null;
		        if (deathScreenText == level.getDeathScreenText() || (deathScreenText != null && deathScreenText.equals(level.getDeathScreenText())))
		        	return;
		        final String cDeathScreenText = deathScreenText;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Level death screen text changed: " +
		        		(level.getDeathScreenText() != null ? level.getDeathScreenText() : "default") + "->" + (cDeathScreenText != null ? cDeathScreenText : "default")) {
					@Override
					protected void doExecute() {
				        level.setDeathScreenText(cDeathScreenText);
				        // no update needed
					}
		        });
	        }
		}
	}
	
}
