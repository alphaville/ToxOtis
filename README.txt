ToxOtis
Version: 0.0, planning

1. Components

1.1. Necessary Components
The following components should be part of the OpenTox client:
i.    Compound/Conformer (Maybe Compound is abstract and conformer impl? or conformer extends compound?)
ii.   Feature
iii.  Dataset (?) Feature Value?
iv.   Algorithm
v.    Parameter
vi.   Model
vii.  Task
viii. Token
ix.   Policy
x.    Person/Group (for policies)
xi.   Error Report

Ontologies:
i.   OpenTox ontology
ii.  Endpoint ontology
iii. Feature Ontology
iv.  Algorithm ontology
v.   External Ontologies (e.g. BibTex)

Ontology-related properties:
i.   DC Meta data (Dublin Core)
ii.  OpenTox meta data (hasSource etc)

Clients:
i.   Get Client (Accept)
ii.  Post (Configurable Headers, Parameter to be posted, Binary Data)
iii. Put
iv.  Delete
v.   Options

--> URL query parameters
--> Combined with auth/auth of OpenTox

1.2. Attributes for each component
Every component is uniquely identified by its URI. Consider using java.net.URI for that purpose

1.2.1. Compound:
 - Chemical Identifiers: CasRN, Smiles


2. Supported Functionalities
2.1. Related to compounds:
  - Download a specific compound (given its URI)
  - Search for compounds given their smiles etc
  - Create new compoiund and get its URI
  - Update/Delete
  - Get a representation of a compound and store it in a file
  - Image? Get the depiction or the image from the URI
  - Create a dataset with all available/some features for a compound
  - Create/update feature for compound
  - Delete a feature from the compound
  - Get all conformers (list of Conformer objects)
  - Remove/Update/Create conformer
Note: Make compound an ABSTRACT class, and Conformer its implementation


2.2. Related to features:
  - Download feature info from a given URI
  - Create new feature providing information about it/ Update feature/ Delete from remote URI
  - Search for features given some search criterion (same as... etc)
  - 

2.3. Dataset

2.4. Algorithm
  - An enumeration of some available algorithms is needed (URIs)
  - Create an Algorithm object providing the URI of an algorithm
  - Apply an algorithm (might return a task



