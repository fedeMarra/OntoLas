package myProject.LAS2;


import java.net.URI; 
 
import org.apache.log4j.Logger; 
import org.semanticweb.owlapi.apibinding.OWLManager; 
import org.semanticweb.owlapi.io.UnparsableOntologyException; 
import org.semanticweb.owlapi.model.IRI; 
import org.semanticweb.owlapi.model.OWLOntology; 
import org.semanticweb.owlapi.model.OWLOntologyManager; 
import org.semanticweb.owlapi.reasoner.InconsistentOntologyException; 
 
import com.clarkparsia.pellet.expressivity.ExpressivityChecker; 
import com.clarkparsia.pellet.owlapiv3.PelletReasoner; 
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory; 
 
/**
 * The Pellet Validation Implementation 
 *  
 * @author 
 *  
 */ 
public class PelletValidator implements Validator { 
 // Pellet 2.0 RC6 
 
 Logger log = Logger.getLogger(PelletValidator.class); 
 
 /*
  * (non-Javadoc) 
  *  
  * @see de.fuberlin.agcsw.svont.validation.Validator#validate(java.net.URI) 
  */ 
 public ValidationReport validate(OWLOntology ontology, OWLOntologyManager manager) throws Exception { 
 
  //log.debug("Loading URI:" + ontURI); 
  ValidationReport vr = new ValidationReport(); 
  try { 
 
   //Ceate OWL API OntologyManager and Ontology Object 
   //OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); 
   //OWLOntology ontology = manager.loadOntologyFromOntologyDocument(IRI.create(ontURI)); 
 
   //Create reasoner and bind Ontology  
   PelletReasonerFactory reasonerFactory = PelletReasonerFactory.getInstance();    
   PelletReasoner reasoner = reasonerFactory.createReasoner(ontology); 
   reasoner.prepareReasoner(); 
 
   //1.  if ontologie got successfully loaded its syntax is ok 
   vr.setSyntax(true); 
 
   //2.  test for inconsistencies 
   if (!reasoner.isConsistent()) { 
    log.debug("check consistency of Ontologie: " + reasoner.isConsistent()); 
    try { 
     vr.setInconsistentClasses(reasoner.getUnsatisfiableClasses().getEntities()); 
    } catch (InconsistentOntologyException e) { 
     log.debug("Got InconsistentOntologyException while fetching inconsistent classes"); 
     //this sucks -> why we get this exception if we try to get the inconsistent classes!!?? 
    } 
     vr.setValidationMessage("Ontology is not consistent"); 
    return vr; 
   } 
   // else ontology is consistent 
   vr.setConsistent(true); 
 
   //3. check expressivity 
   ExpressivityChecker checker = new ExpressivityChecker(reasoner.getKB()); 
   log.debug("Expressivity: " + checker.getExpressivity()); 
   boolean isEL = checker.getExpressivity().isEL(); 
   if (!isEL) { 
    log.debug("expressivity is el?:" + isEL); 
    vr.setValidationMessage("Expressivity is not EL"); 
    return vr; 
   } 
 
   //all done all ok, set valid to true 
   vr.setExpressivity(true); 
   vr.setValid(true); 
  } catch (Exception pe) { 
   vr.setValidationMessage("There are syntactic errors in this Ontologie\r\n"+ pe.getMessage()); 
  } 
  return vr; 
 
 } 
 
}
