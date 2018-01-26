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
	private CCombo heroOnEntryCombo;
	
	private boolean listenerStopped = false;
	
	private boolean heroNotFound = false;
	private HashMap<String, HeroDescription> heros = new HashMap<>();
	
	public WarpPointSection() { }
	
	protected void getHeros(HeroDescription addHeroNotFound) {
		// hero descriptions
		heros.clear();
		heroNotFound = addHeroNotFound != null;
		if (heroNotFound)
			heros.put("(Hero not found)", addHeroNotFound);
		heros.put("No entry", null); // none element
		for (Description desc : EggScriptionLoader.getDescriptions())
			if (desc instanceof HeroDescription)
				heros.put(((HeroDescription) desc).getName(), (HeroDescription) desc);
	}
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
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
        
		// heroOnEntry
        heroOnEntryCombo = factory.createCCombo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		data = new FormData();
		data.left = new FormAttachment(warpToText, 0, SWT.LEFT);
		data.right = new FormAttachment(warpToText, 0, SWT.RIGHT);
		data.top = new FormAttachment(warpToText, VSPACE, SWT.BOTTOM);
		heroOnEntryCombo.setLayoutData(data);
		heroOnEntryCombo.setItems(heros.keySet().toArray(new String[heros.keySet().size()]));
		heroOnEntryCombo.addListener(SWT.FocusOut, this);
		
		CLabel changeHeroToLabel = factory.createCLabel(composite, "Use hero on entry:");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(heroOnEntryCombo, -HSPACE);
		data.top = new FormAttachment(heroOnEntryCombo, 0, SWT.CENTER);
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

        // heroOnEntry
        HeroDescription heroDesc = warpPoint.getHeroOnEntry();
        heroOnEntryCombo.deselectAll();
        int heroSelect = 0;
        if (heroDesc != null && heroDesc.getName() == null)
        	getHeros(heroDesc);
        else
        	getHeros(null);
        heroOnEntryCombo.setItems(heros.keySet().toArray(new String[heros.keySet().size()]));
        
        if (heroDesc != null && heroDesc.getName() != null) { // could be null or set but not found
        	for (int i = 0; i < heroOnEntryCombo.getItemCount(); i++) {
        		if (heroDesc.getName().equals(heroOnEntryCombo.getItems()[i])) {
        			heroSelect = i;
        			break;
        		}
        	}
        }
        heroOnEntryCombo.select(heroSelect);
        
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
		        if (warpTo == null && warpPoint.getHeroOnEntry() == null && warpPoint.getWarpTo() != null) { // cannot unset both!
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
	        } else if (event.widget == heroOnEntryCombo) {
		        String hero = heroOnEntryCombo.getItem(heroOnEntryCombo.getSelectionIndex());
		        HeroDescription heroDesc = heros.get(hero);
		        if (heroDesc == null && warpPoint.getWarpTo() == null && warpPoint.getHeroOnEntry() != null) { // cannot unset both!
		        	heroOnEntryCombo.deselectAll();
		        	int heroSelect = 0;
		        	if (warpPoint.getHeroOnEntry().getName() != null) {
			        	for (int i = 0; i < heroOnEntryCombo.getItemCount(); i++) {
			        		if (warpPoint.getHeroOnEntry().getName().equals(heroOnEntryCombo.getItems()[i])) {
			        			heroSelect = i;
			        			break;
			        		}
			        	}
		        	}
		        	heroOnEntryCombo.select(heroSelect);
		        	return;
		        } else if (heroDesc == warpPoint.getHeroOnEntry() || (heroDesc == null && warpPoint.getHeroOnEntry().getName() == null)) // compare objects
		        	return;
		        if (heroNotFound)
		        	getHeros(null); // remove after first change
		        final HeroDescription cHeroDesc = heroDesc;
		        domain.getCommandStack().execute(new RecordingCommand(domain, "Hero of warp point changed: " +
				        (warpPoint.getHeroOnEntry() != null && warpPoint.getHeroOnEntry().getName() != null ? warpPoint.getHeroOnEntry().getName() :
				        	(warpPoint.getHeroOnEntry() != null ? "(Hero not found)" : "")) +
				        "->" + (cHeroDesc != null ? cHeroDesc.getName() : "")) {
					@Override
					protected void doExecute() {
						warpPoint.setHeroOnEntry(cHeroDesc);
				        // no update needed
					}
		        });
	        }
		}
	}

}
