package step.framework.view;

import java.io.Serializable;

/**
 * Abstract view of a domain object. Each view should inherit from this class.  
 * 
 * Views cannot be compared using the equals(Object) method because, view subclasses are generated automatically from their XML
 * description and the generator does not produce a useful equals(Object) method.  Comparisons using equals will only check for
 * reference equality.
 *
 * Conceptually views are read-only objects.
 */
public abstract class View implements Serializable {
	private static final long serialVersionUID = 1L;}
