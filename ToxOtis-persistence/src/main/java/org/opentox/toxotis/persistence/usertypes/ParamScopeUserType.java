package org.opentox.toxotis.persistence.usertypes;

import org.opentox.toxotis.core.component.Parameter.ParameterScope;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class ParamScopeUserType extends GenericEnumUserType<org.opentox.toxotis.core.component.Parameter.ParameterScope> {

    public ParamScopeUserType() {
        super(org.opentox.toxotis.core.component.Parameter.ParameterScope.class);
    }
}
