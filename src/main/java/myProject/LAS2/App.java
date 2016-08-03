package myProject.LAS2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Iterator;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.InferredOntologyGenerator;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import org.semanticweb.HermiT.Reasoner;

import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils; 
import org.json.*;
import org.mindswap.pellet.exceptions.InconsistentOntologyException;


enum LabelProcess{
		generates,
		belongs,
		signed,
		participates,
		contains
}

/**
 * Hello world!
 *
 */
public class App 
{
	//private static String DB_ID_PATTERN = "(\\w*):(\\d*)"; 
	private static String[] input= {};
	private static String[] output= {};
	// database to access to the classes by labels
	private static HashMap<String, OWLClass> databasecls;
	// database to access to the Objectproperty by labels
	private static HashMap<String, OWLObjectProperty> databaseObj;
	private static String baseOnto = "http://www.semanticweb.org/federica/ontologies/2016/4/OntoLasXML";
	
    public static void main( String[] args ) throws Exception
    {
    	File file = new File("/home/federica/Documenti/MaterialeOnto/Ontologies/ontoLasXML.owl");
	    OWLOntology ontology;
        try {
            
        	// create an ontology manager
            OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            ontology = manager.loadOntologyFromOntologyDocument(file);
            OWLDataFactory df = manager.getOWLDataFactory();
            Reasoner reasoner = new Reasoner(ontology);
            
            //PelletValidator pv = new PelletValidator();
            //pv.validate(ontology, manager);
            //Set<OWLClass> classes = ontology.getClassesInSignature();
            // create a database that storage the ontology classes where for each element the key is the labels and the value is the respective URI
            databasecls = labelMapURI(ontology, df);
            // create a database that storage the ontology objectProperties where for each element the key is the labels and the value is the respective URI
            databaseObj = labelMapUriObj(ontology, df);
            /*for (Map.Entry entry : databasecls.entrySet()) { //stampa il database
                System.out.println(entry.getKey() + ", " + entry.getValue());
            }*/
            // create the Pellet reasoner
            //PelletReasoner reasoner = PelletReasonerFactory.getInstance().createNonBufferingReasoner( ontology );
            // add the reasoner as an ontology change listener
            //manager.addOntologyChangeListener( reasoner );
            System.out.println("The ontology is consistent: "+reasoner.isConsistent());
            System.out.println( "Process Validator..." );
        	ObjectMapper mapper = new ObjectMapper();
        	//JSON from file of graph process to Object (graph.json)
        	JsonParser jp = mapper.readValue(new File("/home/federica/workspace/LAS2/graph_coll.json"), JsonParser.class);
        	List<myProject.LAS2.Node> nodeList = jp.getNodes();
        	List<myProject.LAS2.Link> linkList = jp.getLinks();
        	Iterator<myProject.LAS2.Node> it = nodeList.iterator();
        	Vector<String> entity = new Vector<String>(); //Vettore che contiene le entità che entrano in gioco nell'app
        	while(it.hasNext()){
        		myProject.LAS2.Node n = it.next();
        		String s = n.getEntity();
        		entity.addElement(s);
        	}
        	System.out.println("Basket parsing...");
        	parserBAsket(nodeList, entity);
            //get inputs' and outputs' superClass 
            Set<OWLClass> classInput = new HashSet<OWLClass>();
            for(int i=0;i<linkList.size();i++){
            	if(linkList.get(i).getLabel().equals("generates") || linkList.get(i).getLabel().equals("contains")){ // Costruisco il processo solo se è di tipo generates o contains
	        		System.out.println("Check the validity for the input "+ linkList.get(i).getSource()+ " and the output " +linkList.get(i).getTarget());
            		Process proc = builProcess(linkList.get(i));
	        		OWLClass superCI = checkSuperClass(reasoner, proc.getInput());
	        		OWLClass superCO = checkSuperClass(reasoner, proc.getOutput());
	            	if(superCI != null && superCO != null ){
	            		classInput.add(superCI);
	            		searchRestrictionsIO(reasoner,df, ontology, superCI, superCO);
	            	}
	            	else{
	            		System.out.println("The input or output not valid. Not present into ontology!");
	            	}
            	}
            	else{
            		// controllo se i link tra i nodi sono leciti 
            		OWLClass clazz1 = searchInDBC(linkList.get(i).getSource());
            		OWLClass clazz2 = searchInDBC(linkList.get(i).getTarget());
            		OWLObjectProperty typelink = searchInDBO(linkList.get(i).getLabel());
            		checkRestriction(clazz1, clazz2, typelink, df, reasoner, ontology);
            	
            	}
        	}
            
            
	        }catch (OWLOntologyCreationException ex) {
		           Logger.getLogger(OWLOntology.class.getName()).log(Level.SEVERE, null, ex);
		    }
    	
            /*Set<OWLClass> classInput = new HashSet<OWLClass>();
            for(int i=0;i<input.length;i++){
            	String iN = input[i];
            	OWLClass clazz = database.get(iN);
            	
            	OWLClass superC = checkSuperClass(reasoner, clazz);
            	if(superC != null){
            		classInput.add(superC);
            		if (hasInput(ontology, df, reasoner, superC)) {
                        //System.out.println("Instances of " + iN + " have a country of origin");
                    }
            	}
            }
            Set<OWLClass> classOutput = new HashSet<OWLClass>();
            for(int i=0;i<output.length;i++){
            	String oU = output[i];
            	OWLClass clazz = database.get(oU);
            	OWLClass superC = checkSuperClass(reasoner, clazz);
            	if(superC != null){
            		classOutput.add(superC);
            	}
            }
            //printProperties(manager, ontology, reasoner, clazz);
            //extractAxiom(clazz, ontology);
            //checkSuperClass(reasoner);
            searchRestriction(reasoner, df, ontology, "material collection");
            //searchRestrictions(reasoner, df, ontology);*/
        
    }
    
    private static void parserBAsket(List<myProject.LAS2.Node> nodeList, Vector<String> entity) {
    	 File f = new File("/home/federica/workspace/LAS2/basket_coll1.json");
         if (f.exists()){
         	try{
         		InputStream is = new FileInputStream("/home/federica/workspace/LAS2/basket_coll1.json");
                 String jsonTxt = IOUtils.toString(is);
                 //System.out.println(jsonTxt);
                 JSONObject json = new JSONObject(jsonTxt); 
                 String element = null;
                 for(int i=0;i<entity.size();i++){
	                 element = entity.elementAt(i);
	                 JSONObject obj = json.getJSONObject("properties").getJSONObject(element);
	                 if(!obj.getJSONObject("items").getJSONObject("properties").getJSONObject("recordProcedure").isNull("properties")){
		                 JSONObject properties = obj.getJSONObject("items").getJSONObject("properties").getJSONObject("recordProcedure").getJSONObject("properties");
		                 Set<String> key1 = properties.keySet();
		                 for(String k : key1){
		                	 nodeList.get(i).addProperties(k);
		                 	System.out.println(element+" "+k);
		                 }
	                 }
	                 else if(!obj.getJSONObject("items").getJSONObject("properties").getJSONObject("relationships").getJSONObject("items").getJSONObject("properties").getJSONObject("data").isNull("properties")){
	                 JSONObject data = obj.getJSONObject("items").getJSONObject("properties").getJSONObject("relationships").getJSONObject("items").getJSONObject("properties").getJSONObject("data").getJSONObject("properties");
	                 Set<String> key1 = data.keySet();
		                 for(String k : key1){
		                	 nodeList.get(i).addProperties(k);
		                 	System.out.println(element+" "+k);
		                 }
	                 //System.out.println(r.toString());
	                 }
	                 else{
	                	 System.out.println(element +" non ha proprietà d'interesse");
	                 }
                 }
                 
         	}
         	catch(FileNotFoundException ex)
         	{ ex.getCause();}
         	catch(IOException e){System.out.println(e.getMessage());}
         }
	}
    
    static public Process builProcess(myProject.LAS2.Link link){
    	OWLClass i = searchInDBC(link.getSource());
    	OWLClass o = searchInDBC(link.getTarget());
    	String l = link.getLabel();
    	Process process = new Process(i, o, l);
    	return process;
    }
    

	static public IRI getIRIbyLabel(OWLOntology o, OWLDataFactory dataFactoryf, String inputLabel, Set<OWLClass> classes){
    	IRI Iri;
    	for(OWLClass owlClass: classes){
	    	for (OWLAnnotation annotation : owlClass.getAnnotations(o, dataFactoryf.getRDFSLabel())) {
			    if (annotation.getValue() instanceof OWLLiteral) {
			        OWLLiteral val = (OWLLiteral) annotation.getValue();
			        //System.out.println(val.getLiteral()+ " " + inputLabel);
			        if (val.getLiteral().equals(inputLabel)) {
			        	Iri = owlClass.getIRI();
			        	return Iri;
			        }
			    }
	    	}
    	}
    	return null;
    	
    }
    
    
    
    static public HashMap<String, OWLClass> labelMapURI(OWLOntology owlontology, OWLDataFactory factory)
    {

    	HashMap<String, OWLClass> mapURI = new HashMap<String, OWLClass>();

    	OWLAnnotationProperty label = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
    	// System.out.println(label.getIRI());
    	for (OWLClass cls : owlontology.getClassesInSignature(true))
    	{
    		if(cls.getAnnotations(owlontology, factory.getRDFSLabel()).size() == 0){
				System.out.println(cls +" "+ cls.getIRI().getFragment());
				mapURI.put(cls.getIRI().getFragment(), cls);
			}
    		// Get the annotations on the class that use the label property
    		// System.out.println(cls);
    		for (OWLAnnotation annotation : cls.getAnnotations(owlontology, factory.getRDFSLabel()))
    		{
    			
    			// System.out.println(annotation);
    			if (annotation.getValue() instanceof OWLLiteral)
    			{
    				OWLLiteral val = (OWLLiteral) annotation.getValue();
    				String labelString = val.getLiteral();
    				mapURI.put(labelString, cls);
    			}
    		}
    	}
    	return mapURI;
    }
    
    static public HashMap<String, OWLObjectProperty> labelMapUriObj(OWLOntology owlontology, OWLDataFactory factory)
    {
    	HashMap<String, OWLObjectProperty> mapLabel = new HashMap<String, OWLObjectProperty>();

    	OWLAnnotationProperty label = factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());
    	// System.out.println(label.getIRI());
    	for (OWLObjectProperty property : owlontology.getObjectPropertiesInSignature(true))
    	{
    		// Get the annotations on the class that use the label property
    		// System.out.println(cls);
    		for (OWLAnnotation annotation : property.getAnnotations(owlontology, factory.getRDFSLabel()))
    		{
    			// System.out.println(annotation);
    			if (annotation.getValue() instanceof OWLLiteral)
    			{
    				OWLLiteral val = (OWLLiteral) annotation.getValue();
    				String labelString = val.getLiteral();
    				mapLabel.put(labelString, property);
    			}
    		}
    	}
    	return mapLabel;
    }
    
    static public OWLClass checkSuperClass(Reasoner reasoner, OWLClass clazz){
       	if(clazz!= null){
        	//NodeSet<OWLClass> superC = reasoner.getSuperClasses(clsInput, true);
        	NodeSet<OWLClass> superC2 = reasoner.getSuperClasses(clazz, false); //ritorna tutti gli antenati
        	//System.out.println(superC.getClass() +" "+ superC2.getClass());
        	OWLClass se = searchInDBC("study entity");
        	OWLClass u = searchInDBC("universal entity");
        	OWLClass iao = searchInDBC("information content entity");
        	OWLClass specif = searchInDBC("specifically dependent continuant");
        	OWLClass sup;
        	boolean ver;
        	if(ver = superC2.containsEntity(se)){
        		return se;
        	}
        	else if(ver = superC2.containsEntity(u)){
        		return u;
        	}
        	else if(ver = superC2.containsEntity(iao)){
        		return iao;
        	}
        	else{
        		return specif;
        	}
    	}
       	else{
       		System.out.println(clazz +"Class not in signature of the ontology");
       		return null;
       	}
    	
    }
    
    static public void searchRestrictionsIO(Reasoner reasoner, OWLDataFactory df, OWLOntology onto, OWLClass in, OWLClass ou){
    	OWLClass pp = databasecls.get("planned process");
    	boolean inputOk;
    	boolean outputOk; 
    	OWLObjectProperty hasInput = df.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/OBI_0000293"));
    	OWLObjectProperty hasOutput = df.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/OBI_0000299"));
    	NodeSet<OWLClass> subProcess = reasoner.getSubClasses(pp, true);
    	RestrictionInOutVisitor restrictionVisitor = new RestrictionInOutVisitor(Collections.singleton(onto), df);
    	//Itero su tutti i sottoprocessi diretti di "planned process"
    	for(Node<OWLClass> sub : subProcess){
    		restrictionVisitor.reset();
    		inputOk = false;
    		outputOk = false;
    		//tempAx contiene tutti gli assiomi della classe oggetto dell'iterazione
    		Set<OWLClassAxiom> tempAx = onto.getAxioms(sub.getRepresentativeElement());
    		//System.out.println("-->"+sub.getRepresentativeElement());
    		for(OWLClassAxiom axiom : tempAx){
    			if(axiom.containsEntityInSignature(hasInput) && axiom.containsEntityInSignature(in)){
    				Set<OWLClassExpression> set = axiom.getNestedClassExpressions();
    				for(OWLClassExpression ex : set){
    					if(ex.containsEntityInSignature(hasInput)){
	    					//System.out.println("Expression in searchInput: "+ex);
	    					ex.accept(restrictionVisitor);
	    					if(restrictionVisitor.getFlag() >= 1){
	    						inputOk = true;
	    						restrictionVisitor.setFlag(0);
	    					}
    					}
    				}
    			}
    			else if(axiom.containsEntityInSignature(hasOutput) && axiom.containsEntityInSignature(ou)){
    				Set<OWLClassExpression> set = axiom.getNestedClassExpressions();
    				for(OWLClassExpression ex : set){
    					if(ex.containsEntityInSignature(hasOutput)){
	    					//System.out.println("Expression in searchOutput: "+ex);
    						//ex contiene l'assioma objectSomeVelues oppure ObjectAllValues from (p)-property->(classesandproperty)
	    					ex.accept(restrictionVisitor);
	    					if(restrictionVisitor.getFlag() >= 1){
	    						outputOk = true;
	    						restrictionVisitor.setFlag(0);
	    					}
    					}
    				}
    			}	
    		}
    		if(inputOk && outputOk){
    			System.out.println("Il processo che soddifa il criterio è: " + labelFor(sub.getRepresentativeElement(), onto));
    			
    		}
    	}
    }
    
    private static boolean checkRestriction(OWLClass a, OWLClass b, OWLObjectProperty obj, OWLDataFactory df, Reasoner reasoner, OWLOntology onto){
    	OWLClassExpression expression;
    	try{
    	if(obj!=null){
    		expression = df.getOWLObjectIntersectionOf(a, df.getOWLObjectAllValuesFrom(obj, b));
	    	if (reasoner.isSatisfiable(expression)) {
	     	   System.out.println("yes");
	     	   System.out.println(expression);
	     	   return true;
	     	 }
	    	else return false;
    	}
    	else {
    		Set<OWLClassAxiom> tempAx = onto.getAxioms(a);
    		//System.out.println("-->"+sub.getRepresentativeElement());
    		for(OWLClassAxiom axiom : tempAx){
    			for(OWLClassExpression  ex: axiom.getNestedClassExpressions()){
    				//if(ex.containsEntityInSignature(b)){
	        			for(OWLObjectProperty o:ex.getObjectPropertiesInSignature()){
	        				//OWLClassExpression expression1 = df.getOWLObjectIntersectionOf(a, df.getOWLObjectAllValuesFrom(o, b));
	        				expression = df.getOWLObjectIntersectionOf(a, df.getOWLObjectSomeValuesFrom(o, b));
	        				
	        				if (reasoner.isSatisfiable(expression)) {
	        					System.out.println("yes1");
	    			     	   	System.out.println(expression);
	    			     	   	return true;
	    			     	   	
	        				}
	        				else{ 
	        					System.out.println("no"); //return false;
	        					System.out.println(o +" "+ b);
	        					return false;
	        				}
	        			}
    				//}
        		}
    			
    		}
    		
    	}
    	/*OWLClassExpression expression;
    	for (OWLClass B : classes) {
    	 expression = df.getOWLObjectIntersectionOf(searchInDBC("patient"), df.getOWLObjectSomeValuesFrom(objProp, B));
    	 if (reasoner.isSatisfiable(expression)) {
    	   System.out.println("yes");
    	   System.out.println(expression);
    	 }
    	}*/
		
    	}catch(InconsistentOntologyException ex){
    		System.out.println(ex.getMessage());
    		
    	}
    	return false;
    }
    
    
    
    private static boolean hasInput(OWLOntology onto, OWLDataFactory dataFactory, Reasoner reasoner, OWLClass cls) {
    	OWLObjectProperty hasInput = dataFactory.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/OBI_0000293"));
    	OWLClass matCollection = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/federica/ontologies/2016/4/OntoLasXML#material_collection"));
    	OWLClass matTransfor = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/federica/ontologies/2016/4/OntoLasXML#material_transformation"));
    	OWLClass dataGener = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/federica/ontologies/2016/4/OntoLasXML#data_generation"));
    	OWLClass dataTransfor = dataFactory.getOWLClass(IRI.create("http://www.semanticweb.org/federica/ontologies/2016/4/OntoLasXML#data_transformation"));
    	RestrictionVisitor restrictionVisitor = new RestrictionVisitor(Collections.singleton(onto));
    	//extractAxiom(matCollection, onto);
    	for (OWLSubClassOfAxiom ax : onto.getSubClassAxiomsForSubClass(matCollection)) {
    		
            OWLClassExpression superCls = ax.getSuperClass();
            
            // Ask our superclass to accept a visit from the RestrictionVisitor - if it is an
            // existential restiction then our restriction visitor will answer it - if not our
            // visitor will ignore it
            superCls.accept(restrictionVisitor);
        }
    	
        // Our RestrictionVisitor has now collected all of the properties that have been restricted in existential
        // restrictions - print them out.
        
        return true;
    }
    //metodo sbaglaito da rifare completamente!!!
    static public boolean instanceProcess(Process p, OWLOntology onto, PelletReasoner reasoner, OWLDataFactory df, OWLOntologyManager man){
    	// We would like to state that matthew has a father who is peter.
    	// We need a subject and object - matthew is the subject and peter is the object.
    	// We use the data factory to obtain references to these individuals
    	OWLClass  i = p.getInput();
    	OWLIndividual in = df.getOWLNamedIndividual(IRI.create(baseOnto + "#newinput"));
    	OWLIndividual out = df.getOWLNamedIndividual(IRI.create(baseOnto + "#newoutput"));
    	// Link the subject and object with the hasFather property.
    	//OWLObjectProperty hasInput = df.getOWLObjectProperty(IRI.create(baseOnto + "#hasFather"));
    	// Create the actual assertion (triple), as an object property assertion axiom
    	OWLObjectProperty hasInput = df.getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/OBI_0000312")); //is input
    	OWLObjectPropertyAssertionAxiom assertion = df.getOWLObjectPropertyAssertionAxiom(hasInput, in, out);
    	// Finally, add the axiom to our ontology and save
    	AddAxiom addAxiomChange = new AddAxiom(onto, assertion);
    	man.applyChange(addAxiomChange);
    	// We can also specify that matthew is an instance of Person.
    	OWLClass plannedPro = df.getOWLClass(IRI.create(baseOnto + "#Person"));
    	// Create a Class Assertion to specify that matthew is an instance of Person.
    	OWLClassAssertionAxiom ax = df.getOWLClassAssertionAxiom(plannedPro, in);
    	// Add this axiom to our ontology.
    	man.addAxiom(onto, ax);
    	return reasoner.isSatisfiable(plannedPro);
    }
    
    static public void searchRestriction(PelletReasoner reasoner, OWLDataFactory df, OWLOntology onto, String p){
    	OWLClass pp = databasecls.get(p);
    	RestrictionVisitor restrictionVisitor = new RestrictionVisitor(Collections.singleton(onto));
    	OWLObjectVisitor ov = new OWLObjectVisitor();
        // In this case, restrictions are used as (anonymous) superclasses, so to get the restrictions on
        // pp we need to obtain the subclass axioms for pp.
    	
    	
        for (OWLSubClassOfAxiom ax : onto.getSubClassAxiomsForSubClass(pp)) {
            OWLClassExpression superCls = ax.getSuperClass();
            // Ask our superclass to accept a visit from the RestrictionVisitor - if it is an
            // existential restiction then our restriction visitor will answer it - if not our
            // visitor will ignore it
            superCls.accept(restrictionVisitor);
            superCls.accept(ov);
        }
        // Our RestrictionVisitor has now collected all of the properties that have been restricted in existential
        // restrictions - print them out.
        ov.visit(pp);
        
        Map<OWLObjectPropertyExpression, OWLClass> resS = restrictionVisitor.getSomeValues();
        for (Map.Entry entry : resS.entrySet()) {
            System.out.println(entry.getKey()+ ", " + entry.getValue());
        }
        Map<OWLObjectPropertyExpression, OWLClass> resA = restrictionVisitor.getSomeValues();
        for (Map.Entry entry : resA.entrySet()) {
            System.out.println(entry.getKey() + ", " + entry.getValue());
        }
        System.out.println("Restricted properties for " + pp + ": " + restrictionVisitor.getRestrictedProperties().size());
        for (OWLObjectPropertyExpression prop : restrictionVisitor.getRestrictedProperties()) {
            System.out.println("    " + prop);
            
        }
    	
    	
    	/*Set<OWLClassAxiom> tempAx = onto.getAxioms(pp);
    	OWLObjectVisitor ov = new OWLObjectVisitor();
    	for(OWLClassAxiom ax : tempAx){
    		for(OWLClassExpression ex: ax.getNestedClassExpressions()){
    			//System.out.println("Type: "+ex.getClassExpressionType());
    			if(ex.getClassExpressionType()!= ClassExpressionType.OWL_CLASS){
    				ex.accept(ov);
					Object[] obj = ex.getSignature().toArray();
					for(int i=0;i<obj.length;i++){
						System.out.println("Val: "+obj[i]);
					}
					for(OWLClassExpression nex : ex.getNestedClassExpressions()){
							System.out.println("Type: "+ nex.getClassExpressionType());
							ex.getSignature();
    						Set<OWLObjectProperty> prop = nex.getObjectPropertiesInSignature();
    						System.out.println(prop.toArray().toString());
    						nex.accept(ov);
	    					//String s = labelFor(entity, onto);
	    					//System.out.println(ex.getClassExpressionType()+ " " +s);
    					
    				}
    			}
    			ex.getSignature();	
    			System.out.println("For " + p+ " the restriction are " +ex);
    		}
    	}*/
    	
    	
    }
    
    static public OWLClass searchInDBC(String lb){
    	return databasecls.get(lb);
    }
    
    static public OWLObjectProperty searchInDBO(String lb){
    	return databaseObj.get(lb);
    }
    
    static public String labelFor(OWLEntity clazz, OWLOntology o) {
    	LabelExtractor le = new LabelExtractor();

		Set<OWLAnnotation> annotations = clazz.getAnnotations(o);
		for (OWLAnnotation anno : annotations) {
			String result = anno.accept(le);
			if (result != null) {
				return result;
			}
		}
		return clazz.getIRI().toString();
	}
    
    static public String labelFor(OWLObjectPropertyExpression obj, OWLOntology o) {
    	LabelExtractor le = new LabelExtractor();
    	OWLObjectProperty p = obj.getNamedProperty();
		Set<OWLAnnotation> annotations = p.getAnnotations(o);
		for (OWLAnnotation anno : annotations) {
			String result = anno.accept(le);
			if (result != null) {
				return result;
			}
		}
		return p.getIRI().toString();
	}
    
    /**
     * Prints out the properties that instances of a class expression must have
     *
     * @param man      The manager
     * @param ont      The ontology
     * @param reasoner The reasoner
     * @param cls      The class expression
     */
    private static void printProperties(OWLOntologyManager man, OWLOntology ont, Reasoner reasoner, OWLClass cls) {
        if (!ont.containsClassInSignature(cls.getIRI())) {
            throw new RuntimeException("Class not in signature of the ontology");
        }
        // Note that the following code could be optimised... if we find that instances of the specified class
        // do not have a property, then we don't need to check the sub properties of this property
        System.out.println("----------------------------------------------------------");
        System.out.println("Properties of " + cls);
        System.out.println("----------------------------------------------------------");
        for (OWLObjectPropertyExpression prop : ont.getObjectPropertiesInSignature()) {
            boolean sat = hasProperty(man, reasoner, cls, prop);
            if (sat) {
                System.out.println("Instances of " + cls + " necessarily have the property " + prop);
            }
        }
    }
    private static boolean hasProperty(OWLOntologyManager man, Reasoner reasoner, OWLClass cls, OWLObjectPropertyExpression prop) {
        // To test whether the instances of a class must have a property we create a some values
        // from restriction and then ask for the satisfiability of the class interesected with the complement
        // of this some values from restriction.  If the intersection is satisfiable then the instances of
        // the class don't have to have the property, otherwise, they do.
        OWLDataFactory dataFactory = man.getOWLDataFactory();
        OWLClassExpression restriction = dataFactory.getOWLObjectSomeValuesFrom(prop, dataFactory.getOWLThing());
        // Now we see if the intersection of the class and the complement of this restriction is satisfiable
        OWLClassExpression complement = dataFactory.getOWLObjectComplementOf(restriction);
        OWLClassExpression intersection = dataFactory.getOWLObjectIntersectionOf(cls, complement);
        return !reasoner.isSatisfiable(intersection);
    }

    
    static public void extractAxiom(OWLClass clazz, OWLOntology ont){
    	OWLOntologyWalker walker = new OWLOntologyWalker(Collections.singleton(ont));

        // Now ask our walker to walk over the ontology.  We specify a visitor who gets visited
        // by the various objects as the walker encounters them.

        // We need to create out visitor.  This can be any ordinary visitor, but we will
        // extend the OWLOntologyWalkerVisitor because it provides a convenience method to
        // get the current axiom being visited as we go.
        // Create an instance and override the visit(OWLObjectSomeValuesFrom) method, because
        // we are interested in some values from restrictions.
        OWLOntologyWalkerVisitor<Object> visitor = new OWLOntologyWalkerVisitor<Object>(walker) {

            public Object visit(OWLObjectSomeValuesFrom desc) {
                // Print out the restriction
                System.out.println(desc);
                // Print out the axiom where the restriction is used
                System.out.println("         " + getCurrentAxiom());
                System.out.println();
                // We don't need to return anything here.
                return null;
            }
        };
        
        // Now ask the walker to walk over the ontology structure using our visitor instance.
        walker.walkStructure(visitor);
    	
    }
    static public void printAxioms(Set<OWLAxiom> axioms) { 
    	 
    	  Set<OWLAxiom> axIndividual = new HashSet<OWLAxiom>(); 
    	  Set<OWLAxiom> axDataProperty = new HashSet<OWLAxiom>(); 
    	  Set<OWLAxiom> axObjectProperty = new HashSet<OWLAxiom>(); 
    	  Set<OWLAxiom> axClass = new HashSet<OWLAxiom>(); 
    	  Set<OWLAxiom> axOther = new HashSet<OWLAxiom>(); 
    	 
    	  for (OWLAxiom a : axioms) { 
    	   a.getSignature(); 
    	   if ((a instanceof OWLClassAxiom)) { 
    	    axClass.add(a); 
    	   } else if (a instanceof OWLDataPropertyAxiom) { 
    	    axDataProperty.add(a); 
    	   } else if (a instanceof OWLObjectPropertyAxiom) { 
    	    axDataProperty.add(a); 
    	   } else if (a instanceof OWLIndividualAxiom) { 
    	    axIndividual.add(a); 
    	   } else 
    	    axOther.add(a); 
    	  } 
    	 
    	  System.out.println("ALL AXIOMS (" + axioms.size() + ")"); 
    	  for (OWLAxiom ax : axIndividual) { 
    	   String line; 
    	   line = ax.toString() + " TYPE: Individual"; 
    	   System.out.println(line); 
    	  } 
    	  for (OWLAxiom ax : axDataProperty) { 
    	   String line; 
    	   line = ax.toString() + " TYPE: DataProperty"; 
    	   System.out.println(line); 
    	  } 
    	  for (OWLAxiom ax : axObjectProperty) { 
    	   String line; 
    	   line = ax.toString() + " TYPE: ObjectProperty"; 
    	   System.out.println(line); 
    	  } 
    	  for (OWLAxiom ax : axClass) { 
    	   String line; 
    	   line = ax.toString() + " TYPE: Class"; 
    	   System.out.println(line); 
    	  } 
    	  for (OWLAxiom ax : axOther) { 
    	   String line; 
    	   line = ax.toString() + " TYPE: Other"; 
    	   System.out.println(line); 
    	  } 
    	  System.out.println("-----------------------------------"); 
    	 
    	 } 
     
}

//Now we can query the reasoner, suppose we want to determine the properties that
// instances of Marghertia pizza must have
/*OWLClass clazz = database.get("material collection");
printProperties(manager, ontology, reasoner, clazz);


OWLClass clazz1 = classInput.iterator().next();
printProperties(manager, ontology, reasoner, clazz1);
Iterator<OWLClass> i = classInput.iterator();
while(i.hasNext()){
    // We can also ask if the instances of a class must have a property
    //OWLClass cl = manager.getOWLDataFactory().getOWLClass();
    OWLObjectProperty hasinput = manager.getOWLDataFactory().getOWLObjectProperty(IRI.create("http://purl.obolibrary.org/obo/OBI_0000293"));
    if (hasProperty(manager, reasoner, i.next(), hasinput)) {
        System.out.println("yes " + i.next() + " ");
    }
}*/
