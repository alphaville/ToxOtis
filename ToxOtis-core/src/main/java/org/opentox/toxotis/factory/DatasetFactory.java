/*
 *
 * ToxOtis
 *
 * ToxOtis is the Greek word for Sagittarius, that actually means ‘archer’. ToxOtis
 * is a Java interface to the predictive toxicology services of OpenTox. ToxOtis is
 * being developed to help both those who need a painless way to consume OpenTox
 * services and for ambitious service providers that don’t want to spend half of
 * their time in RDF parsing and creation.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 *
 */
package org.opentox.toxotis.factory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.Enumeration;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.FeatureValue;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * DatasetFactory provides methods for creating Datasets from
 * various sources. Main functionality features parsing an <code>.arff</code> file
 * or a weka Instances object to create a Dataset.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DatasetFactory {

    /**
     * Create a dataset using a <code>weka.core.Instances</code> object (based on
     * Weka, version 3.6.2). Since datasets structurally differ from Instances
     * object for they store the information in a more expanded way including meta
     * data and nodes that do not appear in Instances object (or ARFF files), the
     * provided object has to possess a certain structure: The first attribute of
     * it has to be always named <code>compound_uri</code> and be of type <code>string</code>.
     * This attribute stores the URIs of the compounds of the dataset. Secondd, the rest
     * attributes have to be of type <code>string</code> or <code>numeric</code> or
     * <code>nominal</code> and their name should be an acceptable feature uri (for
     * example <code>http://someserver.com:1234/opentox/feature/54234</code>).
     *
     * @param instances
     *      Instances object to be converted into a Dataset.
     * @return
     *      The dataset that is created from the provided Instances object.
     * @throws ToxOtisException
     *      In case the conversion is not possible due to structural inconsistencies
     *      of the provided Instances object.
     */
    public static Dataset createFromArff(Instances instances) throws ToxOtisException {
        if (instances.attribute("compound_uri") == null && instances.attribute("URI") == null) {
            throw new ToxOtisException("Cannot create an OpenTox dataset out of this dataset because "
                    + "'compound_uri' was not found in it's attribute list");
        }
        Dataset ds = new Dataset();
        Enumeration instancesEnum = instances.enumerateInstances();
        while (instancesEnum.hasMoreElements()) {
            Instance instance = (Instance) instancesEnum.nextElement();
            ds.getDataEntries().add(createDataEntry(instance));
        }
        try {
            ds.setUri(new VRI(instances.relationName()));
        } catch (URISyntaxException ex) {
            throw new ToxOtisException("The relation name '" + instances.relationName() + "' is not"
                    + "a valid dataset URI!", ex);
        }
        return ds;
    }

    /**
     * Create a dataset using an ARFF file at a given location. Since datasets structurally
     * differ from Instances object for they store the information in a more expanded way including meta
     * data and nodes that do not appear in Instances object (or ARFF files), the
     * provided ARFF file has to possess a certain structure: The first attribute of
     * it has to be always named <code>compound_uri</code> and be of type <code>string</code>.
     * This attribute stores the URIs of the compounds of the dataset. Secondd, the rest
     * attributes have to be of type <code>string</code> or <code>numeric</code> or
     * <code>nominal</code> and their name should be an acceptable feature uri (for
     * example <code>http://someserver.com:1234/opentox/feature/54234</code>).
     *
     * @param file
     *      Pointer to an ARFF file
     * @return
     *      The dataset that is created from the provided ARFF file.
     * @throws ToxOtisException
     *      In case the conversion is not possible due to structural inconsistencies
     *      of the provided Instances object or the file is not found.
     */
    public static Dataset createFromArff(File file) throws ToxOtisException {
        try {
            return createFromArff(new FileReader(file));
        } catch (FileNotFoundException ex) {
            throw new ToxOtisException(ex);
        }
    }

    public static Dataset createFromArff(InputStream stream) throws ToxOtisException {
        return createFromArff(new InputStreamReader(stream));
    }

    public static Dataset createFromArff(Reader reader) throws ToxOtisException {
        try {
            return createFromArff(new Instances(reader));
        } catch (IOException ex) {
            throw new ToxOtisException(ex);
        }
    }

    /**
     * Create a {@link DataEntry data entry} from a single instance.
     * @param instance
     * @return
     *      A Data Entry that corresponds to the provided instance.
     * @throws ToxOtisException
     */
    public static DataEntry createDataEntry(Instance instance) throws ToxOtisException {
        Enumeration attributes = instance.enumerateAttributes();
        DataEntry de = new DataEntry();
        try {
            while (attributes.hasMoreElements()) {
                Attribute attribute = (Attribute) attributes.nextElement();
                if (attribute.name().equals(Dataset.compound_uri) || attribute.name().equals("URI")) {
                    de.setConformer(new Compound(new VRI(instance.stringValue(attribute))));
                } else {
                    FeatureValue fv = new FeatureValue();
                    Feature feature = new Feature(new VRI(attribute.name()));

                    LiteralValue value = null;
                    if (attribute.isNumeric()) {
                        value = new LiteralValue<Double>(instance.value(attribute), XSDDatatype.XSDdouble);
                        feature.getOntologicalClasses().add(OTClasses.NumericFeature());
                    } else if (attribute.isString() || attribute.isDate()) {
                        value = new LiteralValue<String>(instance.stringValue(attribute), XSDDatatype.XSDstring);
                        feature.getOntologicalClasses().add(OTClasses.StringFeature());
                    } else if (attribute.isNominal()) {
                        value = new LiteralValue<String>(instance.stringValue(attribute), XSDDatatype.XSDstring);
                        Enumeration nominalValues = attribute.enumerateValues();
                        feature.getOntologicalClasses().add(OTClasses.NominalFeature());
                        while (nominalValues.hasMoreElements()) {
                            String nomValue = (String) nominalValues.nextElement();
                            feature.getAdmissibleValues().add(new LiteralValue<String>(nomValue, XSDDatatype.XSDstring));
                        }
                    }
                    fv.setFeature(feature);
                    fv.setValue(value);
                    de.addFeatureValue(fv);
                }
            }
        } catch (URISyntaxException ex) {
            throw new ToxOtisException(ex);
        }
        return de;
    }
}
