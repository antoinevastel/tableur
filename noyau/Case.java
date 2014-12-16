package noyau;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;


public class Case implements Serializable, Comparable<Case>{
	private String colonne;
	private int ligne;
	private double valeur;
	private Formule formule;
	private String langue;
	
	//Attribut uniquement utilisable pour l'algorithme de propagation
	private int potentiel;
	
	//On stocke les antecedents dans un hashset pour garantir l'unicite
	private HashSet<Case> antecedents = new HashSet<Case>();
	
	public Case(String c, int l, String langue){
		colonne = c;
		ligne = l;
		formule = null;
		potentiel = 0;
		this.langue = langue;
	}
	
	
	String getLangue(){
		return langue;
	}
	
	void setLangue(String l){
		langue = l;
	}
	
	String getNom(){
		return colonne + String.valueOf(ligne); 
	}
	
	Boolean isBinary(){
		return (getFormule() instanceof OperationBinaire); 
	}
	
	Boolean isFonction(){
		return (getFormule() instanceof Fonction); 
	}
	
	double getValeur(){
		return valeur;
	}
	
	void fixerValeur(double val){
		
		if(getFormule()!=null){
			getFormule().enleverAntecedents();
		}
		
		valeur = val;
		
		propagationValeur();
	}
	
	boolean genereCycle(){
		return getFormule().hasCycle(this);
	}
	
	public HashSet<Case> getAntecedents(){
		return antecedents;
	}
	
	public void propagationValeur(){
		HashSet<Case> cases = triParCoucheInit();
		TreeSet<Case> cases_ordonnees = new TreeSet<Case>();
		
		for(Case c : cases){
			cases_ordonnees.add(c);
		}
		
		for(Case c : cases_ordonnees){
			if(c.getFormule()!=null){
				try {
					c.valeur = c.getFormule().eval();
				} catch (OperationIncorrecteException e) {
					c.valeur = Double.NaN;
					c.setPotentiel(0);
				}
				c.setPotentiel(0);
			}
			
		}
	}
	
	public HashSet<Case> triParCoucheInit(){
		HashSet<Case> caseVisitees = new HashSet<Case>();
		caseVisitees.add(this);
		caseVisitees = triParCouche(caseVisitees);
		return caseVisitees;
	}

	HashSet<Case> triParCouche(HashSet<Case> caseVisitees){
		
			caseVisitees.add(this);
			if(getFormule() != null){
				Case c = Collections.max(getFormule().getArguments());
				if(c.getPotentiel()+1 > getPotentiel()){
					setPotentiel(c.getPotentiel() + 1);
				}
			}
			for(Case cse : getAntecedents()){
				caseVisitees = cse.triParCouche(caseVisitees);
			}
		return caseVisitees;
	}
	
	private void setPotentiel(int val) {
		potentiel = val;
	}
	
	int getPotentiel(){
		return potentiel;
	}
	
	void setFormule(Formule form)throws OperationIncorrecteException{
		
		if(getFormule()!= null){
			getFormule().enleverAntecedents();
		}
		
		formule = form;
		
		if(form != null){
			getFormule().setCase(this);
			
			if(genereCycle() == true){
				formule = null;
			}
			else{
				try {
					fixerValeur(formule.eval());
				} catch (OperationIncorrecteException e) {
					valeur = Double.NaN;
				}
				getFormule().ajoutAntecedents();
			}
		}
	}
	
	void enleverAntecedent(Case c){
		antecedents.remove(c);
	}
	
	String getContenu(){
		if(getFormule() == null){
			return String.valueOf(getValeur());
		}
		else{
			return String.valueOf(getFormule().getContenuFormule());
		}
	}
	
	String getContenuDeveloppe(){
		String s = "";
		/*if(isFonction()){
			return ((Fonction)getFormule()).getNom() +"("+ getContenuDeveloppeRecursif(s)+")";
		}
		else{*/
			return getContenuDeveloppeRecursif(s);
		//}
	}
	
	String getContenuDeveloppeRecursif(String s){
			return getFormule().getContenuDeveloppeFormule(s);
	}
	
	boolean hasArguments(){
		if(getFormule()!=null){
			return true;
		}
		else{
			return false;
		}
	}
	
	String getCoordonnees(){
		return colonne+String.valueOf(ligne);
	}
	
	public Formule getFormule(){
		return formule;
	}
	
	public String toString(){
		return getNom();
	}
	
	void ajoutAntecedent(Case c){
		antecedents.add(c);
	}

	public int compareTo(Case c2) {
		if(getPotentiel() != c2.getPotentiel()){
		      return getPotentiel() - c2.getPotentiel();
		}
		else{
		   return getNom().compareTo(c2.getNom());
		}
	}
	
	
}
