import java.awt.Color;

import javax.swing.JMenuBar;

public class MyMenuBar extends JMenuBar {

	public MyMenuBar(MyMenu... menus) {
		setBackground(Color.WHITE);
		for (MyMenu menu : menus) {
			add(menu);
		}
	}
}
