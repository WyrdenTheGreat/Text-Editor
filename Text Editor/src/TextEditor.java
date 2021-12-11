import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

public class TextEditor extends JFrame {
	
	private MyMenuBar menuBar;
	private MyTextBox textBox;
	private MyScrollPane scrollPane;
	
	
	public TextEditor () {
		menuBar = createMenuBar();
		textBox = createTextBox();
		scrollPane = createScrollPane();
		
		setJMenuBar(menuBar);
		add(scrollPane, BorderLayout.CENTER);
		setSize(500,500);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
	}

	private MyScrollPane createScrollPane() {
		return new MyScrollPane(textBox);
	}
	
	private MyTextBox createTextBox() {
		return new MyTextBox();
	}

	private MyMenu createFile() {
		MyMenuItem open = new MyMenuItem("open","Open");
		MyMenuItem save = new MyMenuItem("save","Save");
		MyMenuItem print = new MyMenuItem("print","Print");
		MyMenuItem blank = new MyMenuItem("blank","New");
		MyMenuItem close = new MyMenuItem("close","Exit");
		
		open.addActionListener(new MyActionListener(open.getName()));
		save.addActionListener(new MyActionListener(save.getName()));
		print.addActionListener(new MyActionListener(print.getName()));
		blank.addActionListener(new MyActionListener(blank.getName()));
		close.addActionListener(new MyActionListener(close.getName()));
		
		return new MyMenu("File", blank, save, open, print, close);
	}
	
	private MyMenu createEdit() {
		MyMenuItem cut = new MyMenuItem("cut","Cut");
		MyMenuItem copy = new MyMenuItem("copy","Copy");
		MyMenuItem paste = new MyMenuItem("paste","Paste");
		MyMenuItem undo = new MyMenuItem("undo","Undo");
		MyMenuItem redo = new MyMenuItem("redo","Redo");
		
		cut.addActionListener(new MyActionListener(cut.getName()));
		copy.addActionListener(new MyActionListener(copy.getName()));
		paste.addActionListener(new MyActionListener(paste.getName()));
		undo.addActionListener(new MyActionListener(undo.getName()));
		redo.addActionListener(new MyActionListener(redo.getName()));
		
		return new MyMenu("Edit", undo, redo, cut, copy, paste);
	}
	
	private MyMenu createSettings() {
		MyMenuItem font = new MyMenuItem("font","Font");
		MyMenuItem color = new MyMenuItem("color","Text Color");
		MyMenuItem bgColor = new MyMenuItem("bgColor","Background");
		MyMenuItem ww = new MyMenuItem("ww", "Word Wrap");
		
		font.addActionListener(new MyActionListener(font.getName()));
		color.addActionListener(new MyActionListener(color.getName()));
		bgColor.addActionListener(new MyActionListener(bgColor.getName()));
		ww.addActionListener(new MyActionListener(ww.getName()));
		
		return new MyMenu("Settings", ww, color, font, bgColor);
	}
	
	private MyMenu createHelp() {
		MyMenuItem about = new MyMenuItem("about","About");
		
		about.addActionListener(new MyActionListener(about.getName()));
		
		return new MyMenu("Help", about);
	}
	
	private MyMenuBar createMenuBar() {
		return new MyMenuBar(createFile(), createEdit(), createSettings(), createHelp());
	}
	
	class MyActionListener implements ActionListener{

		private String name;
		
		public MyActionListener(String theName) {
			name = theName;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (name) {
				case "open": runOpen(); break;
				case "save": runSave(); break;
				case "print": runPrint(); break;
				case "blank": runBlank(); break;
				case "close": runClose(); break;
				case "cut": runCut(); break;
				case "copy": runCopy(); break;
				case "paste": runPaste(); break;
				case "undo": runUndo(); break;
				case "redo": runRedo(); break;
				case "font": runFont(); break;
				case "color": runColor(); break;
				case "bgColor": runBgColor(); break;
				case "about": runAbout(); break;
				case "ww" : runWW(); break;
				default : runNotYetImplemented();
			}
			
		}
		private void runWW() {
			textBox.setLineWrap(!textBox.getLineWrap());
			
		}

		private void runNotYetImplemented() {
			JOptionPane error = new JOptionPane("This has not been implemented yet.", JOptionPane.ERROR_MESSAGE);
			JDialog dialog = error.createDialog("Error!");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
			
		}

		private void runAbout() {
			JOptionPane.showMessageDialog(textBox, "Text Editor Version 1.5 \n by WyrdenTheGreat \n 10 December 2021",
					"About", JOptionPane.INFORMATION_MESSAGE);
			
		}
		private void runBgColor() {
			Color newColor = JColorChooser.showDialog(null, "Choose a Background Color", textBox.getBackground());
			if ((newColor != null)) {
				textBox.setBackground(newColor);
				menuBar.setBackground(newColor);
			}
			
		}
		private void runColor() {
			Color newColor = JColorChooser.showDialog(TextEditor.this, "Choose a Text Color", textBox.getForeground());
			textBox.setForeground(newColor);
		}
		private void runFont() {
			JFontChooser j = new JFontChooser();
			j.setSelectedFont(textBox.getFont());
			j.setSelectedFontSize(textBox.getFont().getSize());
			j.setSelectedFontStyle(textBox.getFont().getStyle());
			int result = j.showDialog(null);
			if (result == JFontChooser.OK_OPTION) {
				Font newFont = j.getSelectedFont();
				textBox.setFont(newFont);
			}
			
		}
		private void runRedo() {
			try {
				textBox.redo();
			} 
			catch (CannotRedoException r) {
				JOptionPane error = new JOptionPane("No action can be redone.", JOptionPane.ERROR_MESSAGE);
				JDialog dialog = error.createDialog("Error!");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
			
		}
		private void runUndo() {
			try {
				textBox.undo();
			} 
			catch (CannotUndoException r) {
				JOptionPane error = new JOptionPane("No action can be undone.", JOptionPane.ERROR_MESSAGE);
				JDialog dialog = error.createDialog("Error!");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
			
		}
		private void runPaste() {
			textBox.paste();
			
		}
		private void runCopy() {
			textBox.copy();
			
		}
		private void runCut() {
			textBox.cut();
			
		}
		private void runClose() {
			System.exit(0);
			
		}
		private void runBlank() {
			textBox.setText("");
			
		}
		private void runPrint() {
			try {
				textBox.print();
			}
			catch (PrinterException e) {
				JOptionPane error = new JOptionPane("The file cannot be printed.", JOptionPane.ERROR_MESSAGE);
				JDialog dialog = error.createDialog("Error!");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
			}
		}
			
		private void runOpen() {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					".txt files", "txt");
			JFileChooser j = new JFileChooser("C:/Users/TD/Desktop");
			j.setFileFilter(filter);
			j.setDialogTitle("Open");
			j.setApproveButtonText("Open");
			int choice = j.showOpenDialog(null);
			switch (choice) {
				case JFileChooser.APPROVE_OPTION :
					try {
						File f = new File(j.getSelectedFile().getAbsolutePath());
						Scanner scan = new Scanner(f);

						textBox.setText("");
						String fileContents = "";
						while (scan.hasNextLine()) {
							fileContents = (fileContents + scan.nextLine() + "\n");
						}
						textBox.setText(fileContents);
						scan.close();
					} catch (IOException e) {
						JOptionPane error = new JOptionPane("The file cannot be opened.", JOptionPane.ERROR_MESSAGE);
						JDialog dialog = error.createDialog("Error!");
						dialog.setAlwaysOnTop(true);
						dialog.setVisible(true);
					}
					break;
				case JFileChooser.CANCEL_OPTION :
					break;
				case JFileChooser.ERROR_OPTION :
					JOptionPane error = new JOptionPane("The file cannot be opened.", JOptionPane.ERROR_MESSAGE);
					JDialog dialog = error.createDialog("Error!");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
					break;
				default :
					break;
			}
			return;
		}

		private void runSave() {
			FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt files", "txt");
			JFileChooser j = new JFileChooser("C:/Users/TD/Desktop");
			j.setFileFilter(filter);
			j.setDialogTitle("Save");
			j.setApproveButtonText("Save");
			int choice = j.showSaveDialog(null);
			switch (choice) {
				case JFileChooser.APPROVE_OPTION :
					try {
						String path = "";
						if (!j.getSelectedFile().exists()) {
							path = (j.getSelectedFile().getAbsolutePath() + ".txt");
						} 
						else {
							path = (j.getSelectedFile().getAbsolutePath());
						}
						File f = new File(path);
						FileWriter writer = new FileWriter(f);
						writer.write("");
						writer.write(textBox.getText());
						writer.close();
					} 
					catch (IOException e) {
						JOptionPane error = new JOptionPane("The file cannot be saved.", JOptionPane.ERROR_MESSAGE);
						JDialog dialog = error.createDialog("Error!");
						dialog.setAlwaysOnTop(true);
						dialog.setVisible(true);
					}
					break;
				case JFileChooser.CANCEL_OPTION :
					break;
				case JFileChooser.ERROR_OPTION :
					JOptionPane error = new JOptionPane("The file cannot be saved.", JOptionPane.ERROR_MESSAGE);
					JDialog dialog = error.createDialog("Error!");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
					break;
				default :
					break;
			}
			return;
		}
	}
}
