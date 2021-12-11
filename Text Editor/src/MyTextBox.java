import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.undo.UndoManager;

public class MyTextBox extends JTextArea {
	
	private UndoManager manager;
	
	public MyTextBox() {
		
		manager = new UndoManager();
		
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		setFont(new Font("RomanBaseLine", Font.ROMAN_BASELINE, 18));
		setEditable(true);
		setEnabled(true);
		setFocusable(true);
		setVisible(true);
		getDocument().addUndoableEditListener(manager);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setLineWrap(true);
		setWrapStyleWord(true);
		requestFocusInWindow();
		
		
	}
	
	public void undo() {
		manager.undo();
	}
	
	public void redo() {
		manager.redo();
	}
	
}
