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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.Enumeration;
import org.opentox.toxotis.client.ClientFactory;
import org.opentox.toxotis.client.HttpStatusCodes;
import org.opentox.toxotis.client.IPostClient;
import org.opentox.toxotis.client.VRI;
import org.opentox.toxotis.client.collection.Media;
import org.opentox.toxotis.client.collection.Services;
import org.opentox.toxotis.core.component.Compound;
import org.opentox.toxotis.core.component.DataEntry;
import org.opentox.toxotis.core.component.Dataset;
import org.opentox.toxotis.core.component.Feature;
import org.opentox.toxotis.core.component.FeatureValue;
import org.opentox.toxotis.core.component.Task;
import org.opentox.toxotis.exceptions.impl.RemoteServiceException;
import org.opentox.toxotis.exceptions.impl.ServiceInvocationException;
import org.opentox.toxotis.exceptions.impl.ToxOtisException;
import org.opentox.toxotis.ontology.LiteralValue;
import org.opentox.toxotis.ontology.collection.OTClasses;
import org.opentox.toxotis.util.aa.AuthenticationToken;
import org.opentox.toxotis.util.spiders.TaskSpider;
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
public final class DatasetFactory {

    private org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DatasetFactory.class);
    private static DatasetFactory factory = null;

    /**
     * Returns the DatasetFactory object associated with the current Java application.
     * All factories in ToxOtis are singletons and have a single access point.
     *
     * @return
     *      The DatasetFactory object associated with the current Java application.
     */
    public static DatasetFactory getInstance() {
        if (factory == null) {
            factory = new DatasetFactory();
        }
        return factory;
    }

    /** dummy constructor */
    private DatasetFactory() {
        super();
    }

    /**
     * Create a dataset using a <code>weka.core.Instances</code> object (based on
     * Weka, version 3.6.2). Since datasets structurally differ from Instances
     * object for they store the information in a more expanded way including meta
     * data and nodes that do not appear in Instances object (or ARFF files), the
     * provided object has to possess a certain structure: The first attribute of
     * it has to be always named <code>compound_uri</code> and be of type <code>string</code>.
     * This attribute stores the URIs of the compounds of the dataset. Second, the rest
     * attributes have to be of type <code>string</code> or <code>numeric</code> or
     * <code>nominal</code> and their name should be an acceptable feature URI (for
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
    public Dataset createFromArff(Instances instances) throws ToxOtisException {
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
     * This attribute stores the URIs of the compounds of the dataset. Second, the rest
     * attributes have to be of type <code>string</code> or <code>numeric</code> or
     * <code>nominal</code> and their name should be an acceptable feature URI (for
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
    public Dataset createFromArff(File file) throws ToxOtisException {
        try {
            return createFromArff(new FileReader(file));
        } catch (FileNotFoundException ex) {
            throw new ToxOtisException(ex);
        }
    }

    public Dataset createFromArff(InputStream stream) throws ToxOtisException {
        InputStreamReader isr = new InputStreamReader(stream);
        Dataset ds = createFromArff(isr);
        try {
            isr.close();
        } catch (IOException ex) {
            throw new ToxOtisException("Could not close input stream reader", ex);
        }
        return ds;
    }

    /**
     * 
     * @param reader
     * @return
     * @throws ToxOtisException 
     */
    public Dataset createFromArff(Reader reader) throws ToxOtisException {
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
    public DataEntry createDataEntry(Instance instance) throws ToxOtisException {
        Enumeration attributes = instance.enumerateAttributes();
        DataEntry de = new DataEntry();
        try {
            while (attributes.hasMoreElements()) {
                Attribute attribute = (Attribute) attributes.nextElement();
                if (attribute.name().equals(Dataset.compoundUri) || attribute.name().equals("URI")) {
                    de.setConformer(new Compound(new VRI(instance.stringValue(attribute))));
                } else {
                    FeatureValue fv = new FeatureValue();
                    Feature feature = new Feature(new VRI(attribute.name()));

                    LiteralValue value = null;
                    if (attribute.isNumeric()) {
                        value = new LiteralValue<Double>(instance.value(attribute), XSDDatatype.XSDdouble);
                        feature.getOntologicalClasses().add(OTClasses.numericFeature());
                    } else if (attribute.isString() || attribute.isDate()) {
                        value = new LiteralValue<String>(instance.stringValue(attribute), XSDDatatype.XSDstring);
                        feature.getOntologicalClasses().add(OTClasses.stringFeature());
                    } else if (attribute.isNominal()) {
                        value = new LiteralValue<String>(instance.stringValue(attribute), XSDDatatype.XSDstring);
                        Enumeration nominalValues = attribute.enumerateValues();
                        feature.getOntologicalClasses().add(OTClasses.nominalFeature());
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

    /**
     * Publishes a dataset reading from a file. The mediatype of the file
     * should be provided.
     * @param sourceFile
     *      The source file which can be any sort of 'chemical' file format,
     *      such as {@link Media#CHEMICAL_MDLSDF SFD}, {@link Media#CHEMICAL_MDLMOL MOL} 
     *      etc. Check out in {@link Media} for more.
     * @param fileType
     *      MIME type of the file provided as a string.
     * @param token
     *      An authentication token is usually necessary to upload data.
     * @param service
     *      The remote dataset service to undertake the uploading and storage.
     * @return
     *      A Task with which to monitor the progress of the upload process.
     * @throws ServiceInvocationException 
     *      In case the remote service does not respond as expected.
     * @throws FileNotFoundException
     *      In case the specified file cannot be located on the file-system.
     * 
     * @see #publishFromFile(java.io.File, org.opentox.toxotis.client.collection.Media, org.opentox.toxotis.util.aa.AuthenticationToken) 
     */
    public Task publishFromFile(File sourceFile, String fileType, AuthenticationToken token, VRI service)
            throws ServiceInvocationException, FileNotFoundException {
        if (!sourceFile.exists()) {
            throw new FileNotFoundException(String.format("The file '%s' was not found", sourceFile.getAbsolutePath()));
        }
        InputStream is = null;
        try {
            is = new FileInputStream(sourceFile);
        } catch (final FileNotFoundException ex) {
            String msg = String.format("Cannot open a stream to the file '%s'", sourceFile.getAbsolutePath());
            logger.error(msg, ex);
            throw new IllegalArgumentException(msg, ex);
        }
        // Use the method publishFromStream
        Task task = publishFromStream(is, fileType, token, service);
        // and then close the input stream
        if (is != null) {
            try {
                is.close();
            } catch (IOException ex) {
                String msg = String.format("There is a stream established towards '%s' which cannot close!!!",
                        sourceFile.getAbsolutePath());
                logger.error(msg, ex);
                throw new IllegalArgumentException(msg, ex);
            }
        }
        return task;
    }

    public Task publishFromFile(File sourceFile, Media fileType, AuthenticationToken token, VRI service)
            throws ServiceInvocationException, FileNotFoundException {
        return publishFromFile(sourceFile, fileType.getMime(), token, service);
    }

    /**
     * 
     * @param sourceFile
     * @param fileType
     * @param token
     * @return
     * @throws ServiceInvocationException 
     */
    public Task publishFromFile(File sourceFile, Media fileType, AuthenticationToken token)
            throws ServiceInvocationException, FileNotFoundException {
        VRI standardDatasetVri = Services.ideaconsult().augment("dataset");
        return publishFromFile(sourceFile, fileType, token, standardDatasetVri);
    }

    public Task publishFromStream(InputStream source, String fileType, AuthenticationToken token, VRI service)
            throws ServiceInvocationException {
        try {
            IPostClient postClient = ClientFactory.createPostClient(new VRI(service));
            postClient.authorize(token);
            postClient.setPostable(source);
            postClient.setContentType(fileType);
            postClient.setMediaType(Media.TEXT_URI_LIST);
            postClient.post();
            VRI newVRI = new VRI(postClient.getResponseText());
            int responseStatus = -1;
            responseStatus = postClient.getResponseCode();

            if (responseStatus == HttpStatusCodes.Accepted.getStatus()) {
                TaskSpider tskSp = new TaskSpider(newVRI);
                return tskSp.parse();
            } else if (responseStatus == HttpStatusCodes.Success.getStatus()) {
                Task t = new Task();
                t.setResultUri(newVRI);
                t.setStatus(Task.Status.COMPLETED);
                return t;
            } else {
                String message = "HTTP Status : " + responseStatus;
                logger.debug(message);
                throw new ServiceInvocationException(message);
            }
        } catch (final URISyntaxException ex) {
            String message = "Service URI is invalid";
            logger.debug(message, ex);
            throw new RemoteServiceException(message, ex);
        }
    }

    public Task publishFromStream(InputStream source, Media fileType, AuthenticationToken token)
            throws ServiceInvocationException {
        return publishFromStream(source, fileType.getMime(), token, Services.ideaconsult().augment("dataset"));
    }
    
    
}
