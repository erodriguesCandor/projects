/** @version $Id: Shell.java,v 1.8 2014/11/13 22:09:38 ist179027 Exp $ */
package poof.textui;

import static ist.po.ui.Dialog.IO;

import java.io.IOException;
import poof.system.Manager;

/**
 * Class that starts the application's textual interface.
 */
public class Shell {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Manager mgr = new Manager();

		String datafile = System.getProperty("import"); //$NON-NLS-1$
		if (datafile != null) {
		   try {
			   mgr = new Manager(datafile);
			}
			catch (ClassNotFoundException e) {
			   // never actually happens
			   e.printStackTrace();
			}
		}
		poof.textui.main.MenuBuilder.menuFor(mgr);
		IO.closeDown();
	}

}
