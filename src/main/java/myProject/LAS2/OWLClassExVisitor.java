package myProject.LAS2;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorAdapter;

public class OWLClassExVisitor extends OWLClassExpressionVisitorAdapter{
	
	private String type = null;

	public void visit(OWLObjectIntersectionOf ce){
		this.type = "Intersection";
    	//System.out.println("Intersection");
    	//System.out.println("Type: " +ce);
    }

    public void visit(OWLObjectUnionOf ce){
    	this.type = "union"; 
    	//System.out.println("union");
    }

    public void visit(OWLObjectComplementOf ce){
    	this.type = "complement";
    	//System.out.println("complement");
    	//System.out.println("Type: " +ce);
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
