package noyau;


public class Addition extends OperationBinaire {
	String signe = "+";
	
	public Addition(Case g, Case d){
		super(g,d);
	}
	
	double eval(){
		return gauche.getValeur() + droite.getValeur();
	}
	
	String getSigne(){
		return signe;
	}
}
