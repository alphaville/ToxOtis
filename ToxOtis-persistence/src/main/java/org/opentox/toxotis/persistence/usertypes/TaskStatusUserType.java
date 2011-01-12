package org.opentox.toxotis.persistence.usertypes;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class TaskStatusUserType extends GenericEnumUserType<org.opentox.toxotis.core.component.Task.Status> {

    public TaskStatusUserType() {
        super(org.opentox.toxotis.core.component.Task.Status.class);
    }
}
