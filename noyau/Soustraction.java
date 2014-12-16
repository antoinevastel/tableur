package noyau;

public class Soustraction extends OperationBinaire {
	String signe = "-";
	
	public Soustraction(Case g, Case d){
		super(g,d);
	}
	double eval(){
		return gauche.getValeur() - droite.getValeur();
	}
	
	String getSigne(){
		return signe;
	}
}

