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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.toxotis.ToxOtisException;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.IGetClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.FeatureValue;
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
        if (instances.attribute("compound_uri") == null) {
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
                if (attribute.name().equals(Dataset.compound_uri)) {
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
