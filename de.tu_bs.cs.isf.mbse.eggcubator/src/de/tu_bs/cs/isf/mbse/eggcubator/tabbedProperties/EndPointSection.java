package de.tu_bs.cs.isf.mbse.eggcubator.tabbedProperties;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetWidgetFactory;

import de.tu_bs.cs.isf.mbse.egg.level.Elements.EndPoint;

public class EndPointSection extends GFPropertySection implements ITabbedPropertyConstants, Listener {

	private Text winScreenTitleText;
	private Text winScreenTextText;
	
	private boolean listenerStopped = false;
	
	public EndPointSection() { }

	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
        Composite composite = factory.createFlatFormComposite(parent);
        FormData data;
        
    	// winScreenTitle
        winScreenTitleText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(0, (int) (STANDARD_LABEL_WIDTH * 1.5));
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, VSPACE);
        winScreenTitleText.setLayoutData(data);
        winScreenTitleText.addListener(SWT.FocusOut, this);
        winScreenTitleText.addListener(SWT.KeyDown, this);
 
        CLabel winScreenTitleLabel = factory.createCLabel(composite, "Win screen title:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(winScreenTitleText, -HSPACE);
        data.top = new FormAttachment(winScreenTitleText, 0, SWT.CENTER);
        winScreenTitleLabel.setLayoutData(data);
        
    	// winScreenText
        winScreenTextText = factory.createText(composite, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        data = new FormData();
        data.left = new FormAttachment(winScreenTitleText, 0, SWT.LEFT);
        data.right = new FormAttachment(winScreenTitleText, 0, SWT.RIGHT);
        data.top = new FormAttachment(winScreenTitleText, VSPACE, SWT.BOTTOM);
        data.height = 16 * 6; // 6 times default lines
        winScreenTextText.setLayoutData(data);
        winScreenTextText.addListener(SWT.FocusOut, this);
 
        CLabel winScreenTextLabel = factory.createCLabel(composite, "Win screen text:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(winScreenTextText, -HSPACE);
        data.top = new FormAttachment(winScreenTextText, 0, SWT.TOP);
        winScreenTextLabel.setLayoutData(data);
	}
	
	@Override
	public void refresh() {
		// Level changed
		PictogramElement pe = getSelectedPictogramElement();
		EndPoint endPoint = (EndPoint) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
        
        listenerStopped = true;
        
        // winScreenTitle
        String winScreenTitle = endPoint.getWinScreenTitle();
        if (winScreenTitle == null)
        	winScreenTitle = "";
        winScreenTitleText.setText(winScreenTitle);
        
        // winScreenText
        String winScreenText = endPoint.getWinScreenText();
        if (winScreenText == null)
        	winScreenText = "";
        winScreenTextText.setText(winScreenText);
        
        listenerStopped = false;
	}
	
	@Override
	public void handleEvent(Event event) {
		if (listenerStopped)
			return;
		if (event.type == SWT.FocusOut || (event.type == SWT.KeyDown && event.character == SWT.CR) ||
				event.type == SWT.MouseUp) { // Focus left, enter or mouse up
			// Properties changed
			PictogramElement pe = getSelectedPictogramElement();
			EndPoint endPoint = (EndPoint) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
	        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(getDiagram());
	        
	        if (event.widget == winScreenTitleText) {
		        String winScreenTitle = winScreenTitleText.getText();
		        if (winScreenTitle.isEmpty())
		        	winScreenTitle = null;
		        if (winScreenTitle == endPoint.getWinScreenTitle() || (winScreenTitle != null && winScreenTitle.equals(endPoint.getWinScreenTitle())))
		        	return;
		        final String cWinScreenTitle = winScreenTitle;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "End point win screen title changed: " +
		        		(endPoint.getWinScreenTitle() != null ? endPoint.getWinScreenTitle() : "default") + "->" + (cWinScreenTitle != null ? cWinScreenTitle : "default")) {
					@Override
					protected void doExecute() {
				        endPoint.setWinScreenTitle(cWinScreenTitle);
				        // no update needed
					}
		        });
	        } else if (event.widget == winScreenTextText) {
		        String winScreenText = winScreenTextText.getText();
		        if (winScreenText.isEmpty())
		        	winScreenText = null;
		        if (winScreenText == endPoint.getWinScreenText() || (winScreenText != null && winScreenText.equals(endPoint.getWinScreenText())))
		        	return;
		        final String cWinScreenText = winScreenText;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "End point win screen text changed: " +
		        		(endPoint.getWinScreenText() != null ? endPoint.getWinScreenText() : "default") + "->" + (cWinScreenText != null ? cWinScreenText : "default")) {
					@Override
					protected void doExecute() {
						endPoint.setWinScreenText(cWinScreenText);
				        // no update needed
					}
		        });
	        }
		}
	}
	
}
