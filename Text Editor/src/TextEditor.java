import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class TextEditor {

	private JFrame frame;
	private JMenuBar bar;
	private JTextArea text;
	private UndoManager manager;

	public static void main(String[] args) {
		TextEditor editor = new TextEditor();
		editor.runEditor();
	}

	private void runEditor() {

		// Initialize everything
		frame = new JFrame();
		bar = new JMenuBar();
		text = new JTextArea();
		manager = new UndoManager();

		// Create all of the JMenuItems that will be used
		JMenuItem open = new JMenuItem("Open");
		JMenuItem save = new JMenuItem("Save");
		JMenuItem print = new JMenuItem("Print");
		JMenuItem blank = new JMenuItem("New");
		JMenuItem close = new JMenuItem("Exit");
		JMenuItem cut = new JMenuItem("Cut");
		JMenuItem copy = new JMenuItem("Copy");
		JMenuItem paste = new JMenuItem("Paste");
		JMenuItem undo = new JMenuItem("Undo");
		JMenuItem redo = new JMenuItem("Redo");
		JMenuItem font = new JMenuItem("Font");
		JMenuItem color = new JMenuItem("Color");
		color.setForeground(Color.MAGENTA);
		JMenuItem ww = new JMenuItem("Word Wrap");
		JMenuItem bgColor = new JMenuItem("Background");
		JMenuItem about = new JMenuItem("About");

		// Create the JMenus and add the JMenuItems to it
		JMenu file = new JMenu("File");
		file.add(blank);
		file.add(save);
		file.add(open);
		file.add(print);
		file.add(close);

		JMenu edit = new JMenu("Edit");
		edit.add(undo);
		edit.add(redo);
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);

		JMenu txt = new JMenu("Text");
		txt.add(font);
		txt.add(color);

		JMenu settings = new JMenu("Settings");
		settings.add(ww);
		settings.add(bgColor);

		JMenu help = new JMenu("Help");
		help.add(about);

		// Create the JMenuBar and add the JMenus to it
		bar.setBackground(Color.LIGHT_GRAY);
		bar.setEnabled(true);
		bar.setVisible(true);
		bar.add(file);
		bar.add(edit);
		bar.add(txt);
		bar.add(settings);
		bar.add(help);

		// Create the JTextArea for typing and set the initial font
		text = new JTextArea();
		text.setEditable(true);
		text.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		text.setFont(new Font("RomanBaseLine", Font.ROMAN_BASELINE, 18));
		text.setLineWrap(true);
		text.setTabSize(4);
		text.setWrapStyleWord(true);
		text.requestFocusInWindow();
		text.setEnabled(true);
		text.setFocusable(true);
		text.setVisible(true);
		text.setBackground(Color.LIGHT_GRAY);
		text.setForeground(Color.BLACK);
		text.getDocument().addUndoableEditListener(manager);

		// Create the layout for the JFrame
		LayoutManager layout = new BorderLayout(0, 0);

		// Create scroll bars that will appear if the text would go off the page
		JScrollPane scroll = new JScrollPane(frame.getContentPane());
		scroll.setVerticalScrollBarPolicy(
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scroll.setAutoscrolls(false);
		scroll.setEnabled(true);
		scroll.setVisible(true);

		// Create the JFrame and add the JMenuBar and the JTextArea box
		frame.setLayout(layout);
		frame.setJMenuBar(bar);
		frame.add(text, BorderLayout.CENTER);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setTitle("Version 1.0");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setContentPane(scroll);
		frame.setVisible(true);

		// Set up what happens when the buttons are clicked
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Made on 6 December 2021",
						"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		color.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(frame,
						"Choose a Text Color", text.getForeground());
				if ((newColor != null)) {
					text.setForeground(newColor);
				}
			}
		});

		bgColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color newColor = JColorChooser.showDialog(frame,
						"Choose a Background Color", text.getBackground());
				if ((newColor != null)) {
					text.setBackground(newColor);
					bar.setBackground(newColor);
				}
			}
		});

		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						".txt files", "txt");
				JFileChooser j = new JFileChooser("C:/Users/TD/Desktop");
				j.setFileFilter(filter);
				j.setDialogTitle("Open");
				j.setApproveButtonText("Open");
				openFile(j);
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				saveFile();
			}
		});

		print.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					text.print();
				} catch (PrinterException p) {
					JOptionPane error = new JOptionPane(
							"The file cannot be printed.",
							JOptionPane.ERROR_MESSAGE);
					JDialog dialog = error.createDialog("Error!");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
			}
		});

		blank.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("");
			}
		});

		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		cut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.cut();
			}
		});

		copy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.copy();
			}
		});

		paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.paste();
			}
		});

		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manager.undo();
				} catch (CannotUndoException u) {
				}
			}
		});

		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					manager.redo();
				} catch (CannotRedoException r) {
				}
			}
		});

		font.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFontChooser j = new JFontChooser();
				j.setSelectedFont(text.getFont());
				j.setSelectedFontSize(text.getFont().getSize());
				j.setSelectedFontStyle(text.getFont().getStyle());
				int result = j.showDialog(frame);
				if (result == JFontChooser.OK_OPTION) {
					Font newFont = j.getSelectedFont();
					text.setFont(newFont);
				}
			}
		});

		ww.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setLineWrap(!text.getLineWrap());
			}
		});
	}

	private void openFile(JFileChooser j) {
		int choice = j.showOpenDialog(frame);
		switch (choice) {
			case JFileChooser.APPROVE_OPTION :
				try {
					File f = new File(j.getSelectedFile().getAbsolutePath());
					Scanner scan = new Scanner(f);

					text.setText("");
					String fileContents = "";
					while (scan.hasNextLine()) {
						fileContents = (fileContents + scan.nextLine() + "\n");
					}
					text.setText(fileContents);
					scan.close();
				} catch (IOException e) {
					JOptionPane error = new JOptionPane(
							"The file cannot be opened.",
							JOptionPane.ERROR_MESSAGE);
					JDialog dialog = error.createDialog("Error!");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
				break;
			case JFileChooser.CANCEL_OPTION :
				break;
			case JFileChooser.ERROR_OPTION :
				JOptionPane error = new JOptionPane(
						"The file cannot be opened.",
						JOptionPane.ERROR_MESSAGE);
				JDialog dialog = error.createDialog("Error!");
				dialog.setAlwaysOnTop(true);
				dialog.setVisible(true);
				break;
			default :
				break;
		}
		return;
	}

	private void saveFile() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				".txt files", "txt");
		JFileChooser j = new JFileChooser("C:/Users/TD/Desktop");
		j.setFileFilter(filter);
		j.setDialogTitle("Save");
		j.setApproveButtonText("Save");
		int choice = j.showSaveDialog(frame);
		switch (choice) {
			case JFileChooser.APPROVE_OPTION :
				try {
					String path = "";
					if (!j.getSelectedFile().exists()) {
						path = (j.getSelectedFile().getAbsolutePath() + ".txt");
					} else {
						path = (j.getSelectedFile().getAbsolutePath());
					}
					File f = new File(path);
					FileWriter writer = new FileWriter(f);
					writer.write("");
					writer.write(text.getText());
					writer.close();
				} catch (IOException e) {
					JOptionPane error = new JOptionPane(
							"The file cannot be saved.",
							JOptionPane.ERROR_MESSAGE);
					JDialog dialog = error.createDialog("Error!");
					dialog.setAlwaysOnTop(true);
					dialog.setVisible(true);
				}
				break;
			case JFileChooser.CANCEL_OPTION :
				break;
			case JFileChooser.ERROR_OPTION :
				JOptionPane error = new JOptionPane("The file cannot be saved.",
						JOptionPane.ERROR_MESSAGE);
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
