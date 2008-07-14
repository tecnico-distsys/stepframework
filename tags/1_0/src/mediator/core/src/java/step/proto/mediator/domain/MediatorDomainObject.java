package step.proto.mediator.domain;

import javax.persistence.MappedSuperclass;

import step.framework.domain.DomainObject;

/**
 * Superclass of all Mediator domain classes.
 */
@MappedSuperclass
public class MediatorDomainObject extends DomainObject {
    private static final long serialVersionUID = 1L;

}
