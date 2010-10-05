package org.opentox.toxotis.factory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.core.Compound;
import org.opentox.toxotis.core.Conformer;
import org.opentox.toxotis.core.DataEntry;
import org.opentox.toxotis.core.Dataset;
import org.opentox.toxotis.core.Feature;
import org.opentox.toxotis.core.FeatureValue;
import org.opentox.toxotis.util.spiders.TypedValue;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

/**
 * DatasetFactory provides methods for creating Datasets from
 * various sources. Main functionality features parsing an .arff file
 * or a weka Instances object to create a Dataset.
 *
 * @author Charalampos Chomenides
 * @author Pantelis Sopasakis
 */
public class DatasetFactory {

    public static Dataset createFromArff(Instances instances) throws ToxOtisException {
        Dataset ds = new Dataset();
        Enumeration instancesEnum = instances.enumerateInstances();
        while (instancesEnum.hasMoreElements()) {
            Instance instance = (Instance) instancesEnum.nextElement();
            ds.getDataEntries().add(createDataEntry(instance));
        }
        try {
            ds.setUri(new VRI(instances.relationName()));
        } catch (URISyntaxException ex) {
            throw new ToxOtisException("The relation name '"+instances.relationName()+"' is not" +
                    "a valid dataset URI!",ex);
        }
        return ds;
    }

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

    public static DataEntry createDataEntry(Instance instance) throws ToxOtisException {
        Enumeration attributes = instance.enumerateAttributes();
        DataEntry de = new DataEntry();
        try {
            while (attributes.hasMoreElements()) {
                Attribute attribute = (Attribute) attributes.nextElement();
                if (attribute.name().equals(Dataset.compound_uri)) {
                    de.setConformer(new Compound(new VRI(instance.stringValue(attribute))));
                } else {
                    FeatureValue fv = new FeatureValue();
                    Feature feature = new Feature(new VRI(attribute.name()));

                    TypedValue value = null;
                    if (attribute.isNumeric()) {
                        value = new TypedValue<Double>(instance.value(attribute), XSDDatatype.XSDdouble);
                    } else if (attribute.isString() || attribute.isDate()) {
                        value = new TypedValue<String>(instance.stringValue(attribute), XSDDatatype.XSDstring);
                    } else if (attribute.isNominal()) {
                        value = new TypedValue<String>(instance.stringValue(attribute), XSDDatatype.XSDstring);
                        Enumeration nominalValues = attribute.enumerateValues();
                        while (nominalValues.hasMoreElements()) {
                            String nomValue = (String) nominalValues.nextElement();
                            feature.getAdmissibleValue().add(new TypedValue<String>(nomValue, XSDDatatype.XSDstring));
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
