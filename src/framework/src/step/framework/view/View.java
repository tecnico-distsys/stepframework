package step.framework.view;

/**
 * Abstract view of a domain object. Each view should inherit from this class.  
 * 
 * Views cannot be compared using the equals(Object) method because, view subclasses are generated automatically from their XML
 * description and the generator does not produce a useful equals(Object) method.  Comparisons using equals will only check for
 * reference equality.
 *
 * Conceptually views are read-only objects.  Eventhough the automatic generation includes setters they should never be used,
 * except when setting up a newly created view instance (this is because the automatic generation only produces the default
 * constructor).
 */
public abstract class View {}
