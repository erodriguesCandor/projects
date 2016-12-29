/** @version $Id: AppendDataToFile.java,v 1.12 2014/12/01 18:52:04 ist179027 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

import poof.system.Manager;
import poof.exception.*;

import poof.textui.*;

/**
 * §2.2.8.
 */
public class AppendDataToFile extends Command<Manager>{
	/**
	 * @param manager
	 */
	public AppendDataToFile(Manager manager) {
		super(MenuEntry.APPEND, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
	   String fname = IO.readString(Message.fileRequest());
      String content = IO.readString(Message.textRequest());
      try {
		   _receiver.writeToFile(content,fname);
      }
      catch (CoreEntryUnknownException e) {
         throw new EntryUnknownException(e.getName());
      }
      catch (CoreIsNotFileException e) {
         throw new IsNotFileException(e.getName());
      }
      catch (CoreAccessDeniedException e) {
         throw new AccessDeniedException(e.getName());
      }
	}

}
