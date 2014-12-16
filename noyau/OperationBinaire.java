package noyau;
import java.util.ArrayList;
import java.util.HashSet;



public abstract class OperationBinaire extends Formule{
	Case gauche;
	Case droite;
	String signe;
	
	public OperationBinaire(Case g, Case d){
		gauche = g;
		droite = d;
	}
	
	String getSigne(){
		return signe;
	}
	
	ArrayList<Case> getArguments(){
		ArrayList<Case> l = new ArrayList<Case>();
		l.add(gauche);
		l.add(droite);
		
		return l;
	}
	
	boolean isValid(){
		if(gauche != null && droite !=null){
			return true;
		}
		else{
			System.out.println("Erreur op binaire "+gauche+" , "+droite);
			return false;
		}
	}
	
	protected void ajoutAntecedents(){
		gauche.ajoutAntecedent(c);
		droite.ajoutAntecedent(c);
	}
	
	void enleverAntecedents(){
		gauche.enleverAntecedent(c);
		droite.enleverAntecedent(c);
	}
	String getContenuFormule(){
		return gauche.getNom() +getSigne()+ droite.getNom();
	}
	
	String getContenuDeveloppeFormule(String s){
		if(gauche.hasArguments()){
			s = gauche.getContenuDeveloppeRecursif(s);
		}
		else{
			System.out.println(gauche);
			s += "("+gauche.getNom()+ getSigne();
		}
		
		if(droite.hasArguments()){
			s = droite.getContenuDeveloppeRecursif(s)+")";
		}
		else{
			s += droite.getNom()+"), ";
		}
		return s;
	}
	
	boolean hasCycle2(Case sommet, Case sommet_init, HashSet<Case> marquage){
		//On marque le sommet de départ
		marquage.add(sommet);
		boolean res = false;
		
		if(marquage.contains(gauche) != true || gauche.equals(sommet_init)){
			if(gauche.equals(sommet_init)){
				res = true;
				return res;
			}
			else if(gauche.hasArguments()==true){
				res = gauche.getFormule().hasCycle2(gauche, sommet_init, marquage);
			}
		}
		
		if(marquage.contains(droite) != true || droite.equals(sommet_init)){
			if(droite.equals(sommet_init)){
				res = true;
				return res;
			}
			else if(droite.hasArguments()==true){
				res = droite.getFormule().hasCycle2(droite, sommet_init, marquage);
			}
		}
		
		return res;
	}
	
	
}
