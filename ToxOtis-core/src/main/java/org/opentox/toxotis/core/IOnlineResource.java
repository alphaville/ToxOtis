package org.opentox.toxotis.core;

/**
 * Interface for an OpenTox component that can be made available online. Not all
 * components stand alone as online resources. For example, normally FeatureValues
 * are always wrapped in datasets and need not be exposed separately.
 * 
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public interface IOnlineResource extends IOTComponent {

}
