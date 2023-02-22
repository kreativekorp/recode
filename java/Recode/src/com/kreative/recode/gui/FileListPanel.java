package com.kreative.recode.gui;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileListPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private final FileList list;
	private String lastOpenDirectory = null;
	
	public FileListPanel(String caption) {
		this.list = new FileList();
		JScrollPane listPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JButton addButton = new JButton("Add...");
		JButton removeButton = new JButton("Remove");
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);
		setLayout(new BorderLayout(4, 4));
		if (caption != null) add(new JLabel(caption), BorderLayout.PAGE_START);
		add(listPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.PAGE_END);
		
		new DropTarget(list, new FileListDropTarget(list));
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_BACK_SPACE:
				case KeyEvent.VK_DELETE:
					FileListPanel.this.list.removeSelectedFiles();
					break;
				}
			}
		});
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Frame frame = new Frame();
				FileDialog fd = new FileDialog(frame, "Select File", FileDialog.LOAD);
				if (lastOpenDirectory != null) fd.setDirectory(lastOpenDirectory);
				fd.setVisible(true);
				String ds = fd.getDirectory(), fs = fd.getFile();
				fd.dispose();
				frame.dispose();
				if (ds == null || fs == null) return;
				File file = new File((lastOpenDirectory = ds), fs);
				list.addFile(file);
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileListPanel.this.list.removeSelectedFiles();
			}
		});
	}
	
	public List<File> getFiles() {
		return list.getFiles();
	}
	
	public void setFiles(List<File> files) {
		list.setFiles(files);
	}
}
