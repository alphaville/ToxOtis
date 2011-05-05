package org.opentox.toxotis.core;

import java.util.Set;
import org.opentox.toxotis.client.VRI;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IBibTexReferencable {

    Set<VRI> getBibTeXReferences();

    IBibTexReferencable addBibTeXReferences(VRI... references);

    IBibTexReferencable setBibTeXReferences(VRI... references);

    IBibTexReferencable setBibTeXReferences(Set<VRI> references);
}
