package noyau;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;



public abstract class Formule implements Serializable{
	
	protected Case c;
	abstract double eval() throws OperationIncorrecteException;
	abstract boolean hasCycle2(Case c, Case sommet_init, HashSet<Case> marquage);
	abstract String getContenuFormule();
	abstract String getContenuDeveloppeFormule(String s);
	protected abstract void ajoutAntecedents();
	abstract ArrayList<Case> getArguments();
	abstract void enleverAntecedents();
	abstract boolean isValid();
	boolean hasCycle(Case sommet_init){
		HashSet<Case> marquage = new HashSet<Case>();
		return hasCycle2(sommet_init, sommet_init, marquage);
	}
	
	void setCase(Case c){
		this.c = c;
	}
	
}
