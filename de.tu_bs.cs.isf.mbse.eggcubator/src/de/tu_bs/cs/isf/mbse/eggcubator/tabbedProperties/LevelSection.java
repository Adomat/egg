package de.tu_bs.cs.isf.mbse.eggcubator.tabbedProperties;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.TextChangeListener;
import org.eclipse.swt.custom.TextChangedEvent;
import org.eclipse.swt.custom.TextChangingEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import de.tu_bs.isf.cs.mbse.egg.level.Level;

public class LevelSection extends GFPropertySection implements ITabbedPropertyConstants, VerifyListener, Listener {
	
	private Text nameText;
	private Text widthText;
	private Text heightText;
	
	private boolean listenerStopped = false;

	public LevelSection() {
	}
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
        Composite composite = factory.createFlatFormComposite(parent);
        FormData data;
 
        // name
        nameText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
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
	}

	@Override
	public void refresh() {
		PictogramElement pe = getSelectedPictogramElement();
        Level level = (Level) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
        
        listenerStopped = true;
        
        // name
        // TODO model
        //String name = level.getName();
        //nameText.setText(name);
        

        // width
        // TODO model
        //Integer width = level.getWidth();
        //widthText.setText(width.toString());

        // height
        // TODO model
        //Integer height = level.getHeight();
        //heightText.setText(height.toString());
        
        listenerStopped = false;
	}

	@Override
	public void verifyText(VerifyEvent e) {
		if (!listenerStopped)
			e.text = e.text.replaceAll("[^\\d]", ""); // only allow numbers/digits
	}

	@Override
	public void handleEvent(Event event) {
		if (listenerStopped)
			return;
		if (event.type == SWT.FocusOut || (event.type == SWT.KeyDown && event.character == SWT.LF)) {

			PictogramElement pe = getSelectedPictogramElement();
	        Level level = (Level) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
	        
	        // name
	        // TODO model
	        //String name = nameText.getText();
	        //level.setName(name);
	        

	        // width
	        // TODO model
	        //Integer width = Integer.parseInt(widthText.getText());
	        //level.setWidth(width);

	        // height
	        // TODO model
	        //Integer height = Integer.parseInt(heightText.getText());
	        //level.setHeight(height);
		}
	}
	
}
