package noyau;
import java.util.ArrayList;


public class Somme extends Fonction{
	public Somme(ArrayList<Case> l){
		super(l);
		noms.put("fr","SOMME");
		noms.put("en", "SUM");
	}
	
	double eval(){
		double somme = 0;
		
		for(Case c : arguments){
			somme += c.getValeur();
		}
		
		return somme;
	}
}
