/* Project - POOF */
package poof.exception;

/**
 * Thrown when any problem occurs in the file system
 */
public abstract class CoreException extends Exception {
   /** Name of the agent who caused the problem */
   private String _name;
   
   /**
	 * @param name
	 *             the agent's name
	 */
   public CoreException(String name) {
      _name=name;
   }
   
   /**
	 * @return the agent's name
	 */
   public String getName() {
      return _name;
   }
}                                
