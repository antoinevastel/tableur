package noyau;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


public class Grille implements Serializable{
	private Map<String, Case> grille = new HashMap<String,Case>();
	public static final int MAX_LIGNES = 20;
	public static final int MAX_COLONNES = 26;
	private String langue = "fr";
	
	public void definirLangue(String l) throws LangueInconnueException{
		if(l.equals("fr") || l.equals("en")){
			langue = l;
			for(Case c : grille.values()){
				c.setLangue(l);
			}
		}
		else{
			throw new LangueInconnueException();
		}
	}
	
	public double getValeur(String nomCase) throws CaseInexistanteException{
		return getCase(nomCase).getValeur();
	}
	
	public String getContenu(String nomCase) throws CaseInexistanteException{
			return getCase(nomCase).getContenu();
	}
	
	public String getContenuDeveloppe(String nomCase) throws CaseInexistanteException{
		return getCase(nomCase).getContenuDeveloppe();
	}
	
	public void addCase(String c, int l){
		grille.put(c + String.valueOf(l), new Case(c,l, langue));
	}
	
	public void addFormule(String c, Formule f) throws OperationIncorrecteException, CaseInexistanteException{
		if(!f.isValid()){
			getCase(c).fixerValeur(Double.NaN);
			throw new OperationIncorrecteException();
		}
		
		getCase(c).setFormule(f);
	}
	
	public void fixerValeur(String c, double val) throws CaseInexistanteException{
		try{
			if(getCase(c).getFormule()== null){
				getCase(c).fixerValeur(val);
			}
			else{
				try {
					getCase(c).setFormule(null);
				} catch (OperationIncorrecteException e) {}
				getCase(c).fixerValeur(val);
			}
		}catch(NullPointerException e){
			throw new CaseInexistanteException();
		}
	}
	
	public Case getCase(String c) throws CaseInexistanteException{
		Case cse = grille.get(c);
		if(cse !=null){
			return cse;
		}
		else{
			throw new CaseInexistanteException();
		}
	}

	public void save(String fichSauv) throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fichSauv));
		out.writeObject(this.grille);
		out.close();
	}
	
	public void load(String fichSauv) throws IOException, ClassNotFoundException{
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(fichSauv));
		this.grille = (Map<String, Case>) in.readObject();
		in.close();
	}
	
	public int getNbColonnes(){
		return MAX_COLONNES;
	}
	
	public int getNbLignes(){
		return MAX_LIGNES;
	}

	public void displayGrille(){
		for(Case c : grille.values()){
			if(c.getFormule() != null){
	            if(Double.isNaN(c.getValeur())){
	            	System.out.println(c+" : "+c.getContenu()+ " = ??? ");
	            }
	            else{
	            	System.out.println(c+ " : "+c.getContenu() + " = "+String.valueOf(c.getValeur()));
	            }
	         }
	         else{
	            System.out.println(c+ " : "+String.valueOf(c.getValeur()));
	         }
		}
	}
	
	
}
