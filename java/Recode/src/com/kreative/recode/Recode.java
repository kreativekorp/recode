package com.kreative.recode;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.kreative.recode.gui.RecodeFrame;

public class Recode {
	public static void main(String[] args) {
		RecodeArgumentParser a = new RecodeArgumentParser();
		for (String arg : args) a.parseArgument(arg);
		a.finishParsing();
		switch (a.getMode()) {
		case STDIO: recodeStdio(a); break;
		case FILES: recodeFiles(a); break;
		case CLIPBOARD: recodeClipboard(a); break;
		case AUTO: case GUI: mainGUI(a); break;
		default: break;
		}
	}
	
	private static void recodeStdio(RecodeArgumentParser a) {
		RecoderTransformer tx = new RecoderTransformer();
		tx.list().addAll(a.getTransformations());
		if (a.getMode() == RecodeArgumentParser.Mode.ERROR) return;
		try {
			tx.recodeTransformStream(System.in, System.out, a.getInputEncoding(), a.getOutputEncoding());
		} catch (IOException e) {
			System.err.println("Error: Failed to recode standard input/output.");
			if (a.stackTrace()) e.printStackTrace();
		}
	}
	
	private static void recodeFiles(RecodeArgumentParser a) {
		RecoderTransformer tx = new RecoderTransformer();
		tx.list().addAll(a.getTransformations());
		if (a.getMode() == RecodeArgumentParser.Mode.ERROR) return;
		Iterator<File> ii = a.getInputFiles().iterator();
		Iterator<File> oi = a.getOutputFiles().iterator();
		while (ii.hasNext() && oi.hasNext()) {
			File inputFile = ii.next();
			File outputFile = oi.next();
			try {
				File tempFile = File.createTempFile("krecode-", ".tmp");
				
				InputStream in = new FileInputStream(inputFile);
				OutputStream out = new FileOutputStream(tempFile);
				tx.recodeTransformStream(in, out, a.getInputEncoding(), a.getOutputEncoding());
				out.flush();
				out.close();
				in.close();
				
				in = new FileInputStream(tempFile);
				out = new FileOutputStream(outputFile);
				byte[] buffer = new byte[0x10000];
				int l;
				while ((l = in.read(buffer)) > 0) {
					out.write(buffer, 0, l);
				}
				out.flush();
				out.close();
				in.close();
				
				tempFile.delete();
			} catch (IOException e) {
				System.err.println("Error: Failed to recode " + inputFile.getName() + ".");
				if (a.stackTrace()) e.printStackTrace();
			}
		}
	}
	
	private static void recodeClipboard(RecodeArgumentParser a) {
		RecoderTransformer tx = new RecoderTransformer();
		tx.list().addAll(a.getTransformations());
		if (a.getMode() == RecodeArgumentParser.Mode.ERROR) return;
		try {
			Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
			if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
				String s = cb.getData(DataFlavor.stringFlavor).toString();
				s = tx.transformString(s);
				cb.setContents(new StringSelection(s), new ClipboardOwner() {
					@Override public void lostOwnership(Clipboard c, Transferable t) {}
				});
			} else {
				System.err.println("Warning: Clipboard is empty or contains no text.");
			}
		} catch (IOException e) {
			System.err.println("Warning: Clipboard is empty or contains no text.");
			if (a.stackTrace()) e.printStackTrace();
		} catch (UnsupportedFlavorException e) {
			System.err.println("Warning: Clipboard is empty or contains no text.");
			if (a.stackTrace()) e.printStackTrace();
		}
	}
	
	private static void mainGUI(final RecodeArgumentParser a) {
		try { System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Kreative Recode"); } catch (Exception e) {}
		try { System.setProperty("apple.laf.useScreenMenuBar", "true"); } catch (Exception e) {}
		try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
		
		try {
			Method getModule = Class.class.getMethod("getModule");
			Object javaDesktop = getModule.invoke(Toolkit.getDefaultToolkit().getClass());
			Object allUnnamed = getModule.invoke(Recode.class);
			Class<?> module = Class.forName("java.lang.Module");
			Method addOpens = module.getMethod("addOpens", String.class, module);
			addOpens.invoke(javaDesktop, "sun.awt.X11", allUnnamed);
		} catch (Exception e) {}
		
		try {
			Toolkit tk = Toolkit.getDefaultToolkit();
			Field aacn = tk.getClass().getDeclaredField("awtAppClassName");
			aacn.setAccessible(true);
			aacn.set(tk, "Kreative Recode");
		} catch (Exception e) {}
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new RecodeFrame(a);
			}
		});
	}
}
