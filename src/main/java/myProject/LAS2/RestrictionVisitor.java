package myProject.LAS2;

import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RestrictionVisitor extends OWLClassExpressionVisitorAdapter {

    private boolean processInherited = true;

    private Set<OWLClass> processedClasses;

    private Set<OWLObjectPropertyExpression> restrictedProperties;
    
    private Map<OWLObjectPropertyExpression, OWLClass> someValues;
    
	private Map<OWLObjectPropertyExpression, OWLClass> allValues;

    private Set<OWLOntology> onts;

    public RestrictionVisitor(Set<OWLOntology> onts) {
        restrictedProperties = new HashSet<OWLObjectPropertyExpression>();
        someValues = new HashMap<OWLObjectPropertyExpression, OWLClass>();
        allValues = new HashMap<OWLObjectPropertyExpression, OWLClass>();
        processedClasses = new HashSet<OWLClass>();
        this.onts = onts;
    }


    public void setProcessInherited(boolean processInherited) {
        this.processInherited = processInherited;
    }


    public Set<OWLObjectPropertyExpression> getRestrictedProperties() {
        return restrictedProperties;
    }
    
    public Map<OWLObjectPropertyExpression, OWLClass> getSomeValues() {
		return someValues;
	}


	public void setSomeValues(Map<OWLObjectPropertyExpression, OWLClass> someValues) {
		this.someValues = someValues;
	}


	public Map<OWLObjectPropertyExpression, OWLClass> getAllValues() {
		return allValues;
	}


	public void setAllValues(Map<OWLObjectPropertyExpression, OWLClass> allValues) {
		this.allValues = allValues;
	}



    public void visit(OWLClass desc) {
        if (processInherited && !processedClasses.contains(desc)) {
            // If we are processing inherited restrictions then
            // we recursively visit named supers.  Note that we
            // need to keep track of the classes that we have processed
            // so that we don't get caught out by cycles in the taxonomy
            processedClasses.add(desc);
            for (OWLOntology ont : onts) {
                for (OWLAxiom ax: ont.getAxioms(desc)) {    //OWLSubClassOfAxiom ax : ont.getSubClassAxiomsForSubClass(desc)
                	
                    //ax.getSuperClass().accept(this);
                }
            }
        }
    }


    public void reset() {
        processedClasses.clear();
        restrictedProperties.clear();
        someValues.clear();
        allValues.clear();
    }


    public void visit(OWLObjectSomeValuesFrom desc) {
        // This method gets called when a class expression is an
        // existential (someValuesFrom) restriction and it asks us to visit it
        restrictedProperties.add(desc.getProperty());
        for(OWLClass cls : desc.getClassesInSignature()){
        	someValues.put(desc.getProperty(), cls);
        	if(desc.getNestedClassExpressions() != null){
    			for(OWLClassExpression ex : desc.getNestedClassExpressions()){
    				if(ex.getClassExpressionType() != ClassExpressionType.OWL_CLASS){ //&& ex.getClassExpressionType() != ClassExpressionType.OBJECT_SOME_VALUES_FROM){
    					//System.out.println(""+ ex.getClassExpressionType());
    					System.out.println("Some Values: "+ desc.getProperty() + " "+ ex.getClassExpressionType()+ " "+desc.getClassesInSignature());
    					//System.out.println("expression type: "+ desc.getClassExpressionType());
    				}
    			}
    		}
        }
        
    }
    public void visit(OWLObjectAllValuesFrom desc) {
    	// This method gets called when a class expression is an
    	// universal (AllValuesFrom) restriction and it asks us to visit it
    	restrictedProperties.add(desc.getProperty());
    	for(OWLClass cls : desc.getClassesInSignature()){
    		allValues.put(desc.getProperty(), cls);
    		if(desc.getNestedClassExpressions() != null){
    			for(OWLClassExpression ex : desc.getNestedClassExpressions()){
    				if(ex.getClassExpressionType() != ClassExpressionType.OWL_CLASS){ //&& ex.getClassExpressionType() != ClassExpressionType.OBJECT_ALL_VALUES_FROM){
    					//System.out.println(""+ ex.getClassExpressionType());
    					System.out.println("All Values: "+ desc.getProperty() + " "+ ex.getClassExpressionType()+ " "+desc.getClassesInSignature());
    					//System.out.println("expression type: "+ desc.getClassExpressionType());
    				}
    			}
    		}

    	}
    }
    
    @Override
    public void visit(OWLDataMaxCardinality desc) {
        super.visit(desc);
        //logger.info("Max Cardinality =["+desc.getCardinality()+"]");
        System.out.println("Max Cardinality =["+desc.getCardinality()+"]");
    }

    @Override
    public void visit(OWLDataMinCardinality desc) {
        super.visit(desc);
        //logger.info("Min Cardinality =["+desc.getCardinality()+"]");
        System.out.println("Min Cardinality =["+desc.getCardinality()+"]");
    }

    @Override
    public void visit(OWLObjectMaxCardinality desc) {
        super.visit(desc);
        //logger.info("Object Max Cardinality =["+desc.getCardinality()+"]");
        System.out.println("Object Max Cardinality =["+desc.getCardinality()+"]");
    }

    @Override
    public void visit(OWLObjectMinCardinality desc) {
        super.visit(desc);
        //logger.info("Object Min Cardinality =["+desc.getCardinality()+"]");
        System.out.println("Object Min Cardinality =["+desc.getCardinality()+"]");
    }

    @Override
    public void visit(OWLDataExactCardinality desc) {
        super.visit(desc);
        //logger.info("Exact Data Cardinality =["+desc.getCardinality()+"]");
        System.out.println("Exact Data Cardinality =["+desc.getCardinality()+"]");
    }

    @Override
    public void visit(OWLObjectExactCardinality desc) {
        super.visit(desc);
        //logger.info("Exact Object Cardinality =["+desc.getCardinality()+"]");
        System.out.println("Exact Object Cardinality =["+desc.getCardinality()+"]");
    }
}
