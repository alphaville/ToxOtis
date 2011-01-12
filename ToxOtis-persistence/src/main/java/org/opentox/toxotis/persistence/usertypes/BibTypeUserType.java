package org.opentox.toxotis.persistence.usertypes;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class BibTypeUserType extends GenericEnumUserType<org.opentox.toxotis.core.component.BibTeX.BIB_TYPE> {

    public BibTypeUserType() {
        super(org.opentox.toxotis.core.component.BibTeX.BIB_TYPE.class);
    }
}
