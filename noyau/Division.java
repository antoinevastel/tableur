package noyau;

public class Division extends OperationBinaire {
	String signe = "/";
	
	public Division(Case g, Case d){
		super(g,d);
	}
	double eval() throws OperationIncorrecteException{
		if(droite.getValeur() ==0 ){
			throw new OperationIncorrecteException();
		}
		else{
			return gauche.getValeur() / droite.getValeur();
		}
	}
	
	String getSigne(){
		return signe;
	}
}
