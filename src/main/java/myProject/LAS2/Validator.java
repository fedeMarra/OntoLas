package myProject.LAS2;

import java.net.URI;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager; 

/**
 * The Validator Modul Interface 
 *  
 * @author mario 
 *  
 */ 
public interface Validator { 
 
 /**
  * Validates a Ontology 
  *  
  * @param ontologyURI 
  *            Physical URI of the ontology to validate 
  * @return ValidationReport Object containing specific Informations about 
  *         the Ontology 
  * @throws Exception 
  *             if problems occurs 
  */ 
 public ValidationReport validate(OWLOntology ontology, OWLOntologyManager manager) throws Exception; 
 
}
