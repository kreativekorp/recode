package com.kreative.recode.gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import com.kreative.recode.transformation.TextTransformationFactory;
import com.kreative.recode.transformation.TextTransformationLibrary;

public class TextTransformationList extends JList {
	private static final long serialVersionUID = 1L;
	
	private final TextTransformationLibrary txLib;
	private final List<String> txNames;
	
	public TextTransformationList(TextTransformationLibrary txLib) {
		this.txLib = txLib;
		this.txNames = new ArrayList<String>();
		this.txNames.addAll(txLib.listByName());
		this.txNames.sort(new Comparator<String>() {
			@Override
			public int compare(String a, String b) {
				int ac = (a.equalsIgnoreCase("Remap") || a.equalsIgnoreCase("Reinterpret")) ? 0 : 1;
				int bc = (b.equalsIgnoreCase("Remap") || b.equalsIgnoreCase("Reinterpret")) ? 0 : 1;
				if (ac != bc) return ac - bc;
				return a.compareToIgnoreCase(b);
			}
		});
		setListData(txNames.toArray());
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addMouseListener(new MouseAdapter() {
			@Override public void mouseEntered(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseExited(MouseEvent e) { updateToolTip(e); }
		});
		addMouseMotionListener(new MouseMotionListener() {
			@Override public void mouseMoved(MouseEvent e) { updateToolTip(e); }
			@Override public void mouseDragged(MouseEvent e) { updateToolTip(e); }
		});
	}
	
	public TextTransformationFactory getTransformation() {
		Object o = this.getSelectedValue();
		if (o == null) return null;
		else return txLib.getByName(o.toString());
	}
	
	private void updateToolTip(MouseEvent e) {
		int i = locationToIndex(e.getPoint());
		if (i < 0) {
			setToolTipText(null);
		} else {
			String txName = txNames.get(i);
			TextTransformationFactory tx = txLib.getByName(txName);
			setToolTipText(tx.getDescription());
		}
	}
}
