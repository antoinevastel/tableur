package noyau;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


public abstract class Fonction extends Formule{
	ArrayList<Case> arguments = new ArrayList<Case> ();
	Map<String, String> noms = new HashMap<String,String>();

	public Fonction(ArrayList<Case> l){
		arguments = l;
	}
	
	String getNom(){
		return noms.get(c.getLangue());
	}

	boolean isValid(){
		return (arguments.size() > 0) ;
	}
	
	public void ajoutAntecedents(){
		for(Case cse : arguments){
			cse.ajoutAntecedent(c);
		}
	}
	
	void enleverAntecedents(){
		for(Case cse : arguments){
			cse.enleverAntecedent(cse);
		}
	}
	
	String getContenuFormule(){
		String s = getNom()+"(";
		for(Case c : arguments){
			s += c.getNom() + ",";
		}
		s+=")";
		
		return s;
	}
	
	String getContenuDeveloppeFormule(String s){
		s+= getNom()+"(";
		//System.out.println("Utilisation getContenuFormDvp fonction");
		for(Case c : arguments){
			//System.out.println("val = "+c);
			if(c.hasArguments()){
				if(c.isFonction()){
					//System.out.println("1Val de s "+s);
					s = c.getContenuDeveloppeRecursif(s);
				}
				else{
					s = c.getContenuDeveloppeRecursif(s);
				}
			}
			else{
				s+= c.getNom()+", ";
			}
		}
		
		s+=")";
		return s;
	}
	
	ArrayList<Case> getArguments(){
		return arguments;
	}
	
	boolean hasCycle2(Case sommet, Case sommet_init, HashSet<Case> marquage){
		//On marque le sommet de départ
		marquage.add(sommet);
		boolean res = false;
		Iterator<Case> it = ((Fonction)(sommet.getFormule())).getArguments().iterator();
		Case c = it.next();
		while(!res && it.hasNext()){
			c = it.next();
				if(marquage.contains(c) != true || c.equals(sommet_init)){
					if(c.equals(sommet_init)){
						res = true;
						return res;
					}
					else{
						if(c.hasArguments() == true){
							res = c.getFormule().hasCycle2(c, sommet_init, marquage);
						}
					}
				}
		}
		return res;
	}
	
	
	void propagationFormule(Grille g, Case sommet){
	}

}