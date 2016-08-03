package myProject.LAS2;

import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;
import org.semanticweb.owlapi.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RestrictionInOutVisitor extends OWLClassExpressionVisitorAdapter {

    private boolean processInherited = true;

    private Set<OWLClass> processedClasses;

    private int flag;
    
    private Set<OWLOntology> onts;
    

    public RestrictionInOutVisitor(Set<OWLOntology> onts, OWLDataFactory df) {
        processedClasses = new HashSet<OWLClass>();
        this.onts = onts;
        flag = 0;
    }


    public void setProcessInherited(boolean processInherited) {
        this.processInherited = processInherited;
    }
    
   

    public void visit(OWLClass desc) {
        if (processInherited && !processedClasses.contains(desc)) {
            // If we are processing inherited restrictions then
            // we recursively visit named supers.  Note that we
            // need to keep track of the classes that we have processed
            // so that we don't get caught out by cycles in the taxonomy
            processedClasses.add(desc);
        }
    }


    public void reset() {
        processedClasses.clear();
        flag = 0;
    }


    public void visit(OWLObjectSomeValuesFrom desc) {
    	if(desc.getNestedClassExpressions()!= null){
    		OWLClassExVisitor visit = new OWLClassExVisitor();
    		for(OWLClassExpression ex : desc.getNestedClassExpressions()){
    	    	ex.accept(visit);    	    
    		}
    		if(visit.getType() == "complement"){
    			Set<OWLClass> classs = desc.getClassesInSignature();
	    		//System.out.println("class complement: "+classs);
    		}
    		else {//caso union or intesaction
    			flag++;
    		}
    		
	    		
    	}
    }
    
    public void visit(OWLObjectAllValuesFrom desc) {
    	if(desc.getNestedClassExpressions()!= null){
    		OWLClassExVisitor visit = new OWLClassExVisitor();
    		for(OWLClassExpression ex : desc.getNestedClassExpressions()){
    	    	ex.accept(visit);    	    
    		}
    		if(visit.getType() == "complement"){
    			Set<OWLClass> classs = desc.getClassesInSignature();
    			
	    		//System.out.println("all Values: "+ desc.getProperty() + " "+classs);
    		}
    		else{//caso union or intersaction
    			flag++;
    		}
    		
	    		
    	}
    	// This method gets called when a class expression is an
    	// universal (AllValuesFrom) restriction and it asks us to visit it
    	/*for(OWLClass cls : desc.getClassesInSignature()){
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
    	}*/
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


	public int getFlag() {
		return flag;
	}


	public void setFlag(int flag) {
		this.flag = flag;
	}
}
