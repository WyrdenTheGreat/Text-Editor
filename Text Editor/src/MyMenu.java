import javax.swing.JMenu;

public class MyMenu extends JMenu {
	
	public MyMenu (String theName, MyMenuItem... theMenuItems) {
		setText(theName);
		for (MyMenuItem m : theMenuItems) {
			add(m);
		}
	}
	
	
}
