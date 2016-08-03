package myProject.LAS2;

import org.semanticweb.owlapi.model.OWLClass;

public class Process {
	private OWLClass input;
	private OWLClass output;
	private String labalProcess;
	
	public Process(OWLClass i, OWLClass o, String l){
		this.setInput(i);
		this.setOutput(o);
		this.setLabalProcess(l);
	}

	public OWLClass getInput() {
		return input;
	}

	public void setInput(OWLClass input) {
		this.input = input;
	}

	public OWLClass getOutput() {
		return output;
	}

	public void setOutput(OWLClass output) {
		this.output = output;
	}

	public String getLabalProcess() {
		return labalProcess;
	}

	public void setLabalProcess(String labalProcess) {
		this.labalProcess = labalProcess;
	}
	

}
