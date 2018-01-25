package de.tu_bs.cs.isf.mbse.eggcubator.tabbedProperties;

import java.util.HashMap;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.context.impl.UpdateContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.platform.GFPropertySection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
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

import de.tu_bs.cs.isf.mbse.egg.descriptions.Description;
import de.tu_bs.cs.isf.mbse.egg.descriptions.gameelements.HeroDescription;
import de.tu_bs.cs.isf.mbse.egg.level.Elements.WarpPoint;
import de.tu_bs.cs.isf.mbse.eggcubator.EggScriptionLoader;

public class WarpPointSection extends GFPropertySection implements ITabbedPropertyConstants, Listener {

	private Text warpToText;
	private Button entryButton;
	private CCombo changeHeroToCombo;
	
	private boolean listenerStopped = false;
	
	private HashMap<String, HeroDescription> heros = new HashMap<>();
	
	public WarpPointSection() { }
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		
		// hero descriptions
		heros.clear();
		heros.put("", null); // none element
		for (Description desc : EggScriptionLoader.getDescriptions())
			if (desc instanceof HeroDescription)
				heros.put(((HeroDescription) desc).getName(), (HeroDescription) desc);
		
		TabbedPropertySheetWidgetFactory factory = getWidgetFactory();
        Composite composite = factory.createFlatFormComposite(parent);
        FormData data;

        // warpTo
        warpToText = factory.createText(composite, "");
        data = new FormData();
        data.left = new FormAttachment(0, (int) (STANDARD_LABEL_WIDTH * 1.5));
        data.right = new FormAttachment(100, 0);
        data.top = new FormAttachment(0, VSPACE);
        warpToText.setLayoutData(data);
        warpToText.addListener(SWT.FocusOut, this);
        warpToText.addListener(SWT.KeyDown, this);
 
        CLabel warpToLabel = factory.createCLabel(composite, "Warp to page/level:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(warpToText, -HSPACE);
        data.top = new FormAttachment(warpToText, 0, SWT.CENTER);
        warpToLabel.setLayoutData(data);
                
        // entry
        entryButton = factory.createButton(composite, "", SWT.CHECK);
        data = new FormData();
        data.left = new FormAttachment(warpToText, 0, SWT.LEFT);
        data.right = new FormAttachment(warpToText, 0, SWT.RIGHT);
        data.top = new FormAttachment(warpToText, VSPACE, SWT.BOTTOM);
        entryButton.setLayoutData(data);
        entryButton.addListener(SWT.MouseUp, this);
 
        CLabel entryLabel = factory.createCLabel(composite, "Is Entry:");
        data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(entryButton, -HSPACE);
        data.top = new FormAttachment(entryButton, 0, SWT.CENTER);
        entryLabel.setLayoutData(data);
        
		// changeHeroTo
        changeHeroToCombo = factory.createCCombo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new FormData();
		data.left = new FormAttachment(entryButton, 0, SWT.LEFT);
		data.right = new FormAttachment(entryButton, 0, SWT.RIGHT);
		data.top = new FormAttachment(entryButton, VSPACE, SWT.BOTTOM);
		changeHeroToCombo.setLayoutData(data);
		changeHeroToCombo.setItems(heros.keySet().toArray(new String[heros.keySet().size()]));
		changeHeroToCombo.addListener(SWT.FocusOut, this);
		
		CLabel changeHeroToLabel = factory.createCLabel(composite, "Switch to hero:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(changeHeroToCombo, -HSPACE);
		data.top = new FormAttachment(changeHeroToCombo, 0, SWT.CENTER);
		changeHeroToLabel.setLayoutData(data);
	}
	
	@Override
	public void refresh() {
		// WarpPoint changed
		PictogramElement pe = getSelectedPictogramElement();
        WarpPoint warpPoint = (WarpPoint) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
        
        listenerStopped = true;
        
        // warpTo
        String warpTo = warpPoint.getWarpTo();
        if (warpTo == null)
        	warpTo = "";
        warpToText.setText(warpTo);
        
        // entry
        boolean entry = warpPoint.isEntry();
        entryButton.setSelection(entry);

        // changeHeroTo
        HeroDescription heroDesc = warpPoint.getChangeHeroTo();
        changeHeroToCombo.deselectAll();
        int heroSelect = 0;
        if (heroDesc != null && heroDesc.getName() != null) { // could be null or set but not found
        	for (int i = 0; i < changeHeroToCombo.getItemCount(); i++) {
        		if (heroDesc.getName().equals(changeHeroToCombo.getItems()[i])) {
        			heroSelect = i;
        			break;
        		}
        	}
        }
        changeHeroToCombo.select(heroSelect);
        changeHeroToCombo.setEnabled(entry); // enable only if needed
        
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
			WarpPoint warpPoint = (WarpPoint) pe.getLink().getBusinessObjects().get(0); // Filter assured this is set
	        TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(getDiagram());
	        
	        if (event.widget == warpToText) {
		        String warpTo = warpToText.getText();
		        if (warpTo.isEmpty())
		        	warpTo = null;
		        if (warpTo == null && !warpPoint.isEntry()) { // cannot unset both!
		        	warpToText.setText(warpPoint.getWarpTo());
		        	return;
		        } else if (warpTo == warpPoint.getWarpTo() || (warpTo != null && warpTo.equals(warpPoint.getWarpTo())))
		        	return;
		        final String cWarpTo = warpTo;
		        domain.getCommandStack().execute(new RecordingCommand(domain,
		        		"Warp destination changed: " + (warpPoint.getWarpTo() != null ? warpPoint.getWarpTo() : "") + "->" + (cWarpTo != null ? cWarpTo : "")) {
					@Override
					protected void doExecute() {
						warpPoint.setWarpTo(cWarpTo);
				        // trigger Update
				        IUpdateContext context = new UpdateContext(getSelectedPictogramElement());
				        IUpdateFeature updateFeature = getDiagramTypeProvider().getFeatureProvider().getUpdateFeature(context);
				        if (updateFeature != null)
				        	updateFeature.update(context);
					}
		        });
	        } else if (event.widget == entryButton) {
	        	final boolean entryVal = entryButton.getSelection();
		        if (!entryVal && warpPoint.getWarpTo() == null) { // cannot unset both!
		        	entryButton.setSelection(warpPoint.isEntry());
		        	return;
		        } else if (entryVal == warpPoint.isEntry())
		        	return;
		        domain.getCommandStack().execute(new RecordingCommand(domain, entryVal ? "Set entry flag for warp point" : "Removed entry flag from warp point") {
					@Override
					protected void doExecute() {
						warpPoint.setEntry(entryVal);
				        // trigger Update
				        IUpdateContext context = new UpdateContext(getSelectedPictogramElement());
				        IUpdateFeature updateFeature = getDiagramTypeProvider().getFeatureProvider().getUpdateFeature(context);
				        if (updateFeature != null)
				        	updateFeature.update(context);
					}
		        });
		        changeHeroToCombo.setEnabled(entryVal); // enable only if needed
	        } else if (event.widget == changeHeroToCombo) {
		        String hero = changeHeroToCombo.getItem(changeHeroToCombo.getSelectionIndex());
		        HeroDescription heroDesc = heros.get(hero);
		        // compare objects (don't change anything if old heroDesc not found and non was selected)
		        if (heroDesc == warpPoint.getChangeHeroTo() || (heroDesc == null && warpPoint.getChangeHeroTo().getName() == null))
		        	return;
		        final HeroDescription cHeroDesc = heroDesc;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Hero of warp point changed: " +
				        (warpPoint.getChangeHeroTo() != null && warpPoint.getChangeHeroTo().getName() != null ? warpPoint.getChangeHeroTo().getName() : "") +
				        "->" + (cHeroDesc != null ? cHeroDesc.getName() : "")) {
					@Override
					protected void doExecute() {
						warpPoint.setChangeHeroTo(cHeroDesc);
				        // no update needed
					}
		        });
	        }
		}
	}

}
