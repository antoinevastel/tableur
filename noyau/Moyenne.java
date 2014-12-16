package noyau;
import java.util.ArrayList;


public class Moyenne extends Fonction {
	
	public Moyenne(ArrayList<Case> l){
		super(l);
		noms.put("fr","MOYENNE");
		noms.put("en", "AVERAGE");
	}
	
	double eval(){
		double somme = 0;
		double cpt = 0;
		for(Case c : arguments){
			somme += c.getValeur();
			cpt++;
		}
		
		return somme / cpt ;
	}
}
