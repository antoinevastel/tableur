package application;
import inter.TablooProto;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import noyau.Addition;
import noyau.Case;
import noyau.CaseInexistanteException;
import noyau.Division;
import noyau.Grille;
import noyau.LangueInconnueException;
import noyau.Moyenne;
import noyau.OperationIncorrecteException;
import noyau.Somme;
import noyau.Soustraction;


public class Application {
	static Scanner in = new Scanner(System.in);
	static Grille g = new Grille();
	static void construire_grille(Grille g){
		
		g.addCase("A",1);
		g.addCase("A",2);
		g.addCase("A",3);
		g.addCase("A",4);
		g.addCase("A",6);
		
		g.addCase("B",2);
		g.addCase("B",4);
		g.addCase("B",6);
		
		g.addCase("C",2);
		g.addCase("C",4);
		g.addCase("C",6);
		
		g.addCase("D",6);
		
		g.addCase("E",4);
		
		try{
			g.fixerValeur("A1",100);
			g.fixerValeur("A2",50);
			g.fixerValeur("A3",0.5);
			g.fixerValeur("A4",0.0);
			
			g.fixerValeur("B2",12);
			
			g.fixerValeur("C2",30);
			
			g.addFormule("A6", new Soustraction(g.getCase("C6"),g.getCase("C2")));
			g.addFormule("B4", new Addition(g.getCase("A1"), g.getCase("A2")));
		}
		catch(CaseInexistanteException e){}
		catch(OperationIncorrecteException e){}
		
			try {
				g.addFormule("C4", new Division(g.getCase("A1"), g.getCase("A3")));
			} catch (OperationIncorrecteException e) {} 
			catch (CaseInexistanteException e) {}
			
			try {
				g.addFormule("E4", new Division(g.getCase("A2"), g.getCase("A4")));
			} catch (OperationIncorrecteException e) {}
			catch (CaseInexistanteException e) {}
			
			ArrayList<Case> lb6 = new ArrayList<Case>();
			try{
				lb6.add(g.getCase("A2"));
				lb6.add(g.getCase("B2"));
				lb6.add(g.getCase("A3"));
			}
			catch(CaseInexistanteException e){
				System.out.println("Case(s) Inexistante(s)");
			}
			
			try {
				g.addFormule("B6", new Somme(lb6));
			} catch (OperationIncorrecteException e) {}
			catch (CaseInexistanteException e) {}
			
			ArrayList<Case> lc6 = new ArrayList<Case>();
			try{
				lc6.add(g.getCase("B6"));
				lc6.add(g.getCase("B4"));
				lc6.add(g.getCase("A4"));
			}catch(CaseInexistanteException e){
				System.out.println("Case(s) Inexistante(s)");
			}
			
			try {
				g.addFormule("C6", new Moyenne(lc6));
			} catch (OperationIncorrecteException e) {}
			catch (CaseInexistanteException e) {}
			
			ArrayList<Case> ld6 = new ArrayList<Case>();
			try{
				ld6.add(g.getCase("B4"));
				ld6.add(g.getCase("E4"));
			}catch(CaseInexistanteException e){
				System.out.println("Case(s) Inexistante(s)");
			}
			
			try {
				g.addFormule("D6", new Moyenne(ld6));
			} catch (OperationIncorrecteException e) {}
			catch (CaseInexistanteException e) {}
		
	}
	
	static void chargerGrille(String nomFic, Grille g){
		try{
			g.load(nomFic);
		}catch(IOException e){
			System.out.println("Echec chargement serialisation : "+e);
		}
		catch(ClassNotFoundException e){
			System.out.println("Classe non trouvee"+e);
		}
	}
	
	static void sauvegarderGrille(String nomFic, Grille g){
		try{
			g.save(nomFic);
		}catch(IOException e){
			System.out.println("Echec serialisation : "+e);
		}
	}
	
	static void Menu(Scanner in, Grille g){
		int rep;
		String s;
		String nomFichier;
		double val;
		
		System.out.println("Voulez vous charger un fichier ? (o/n)");
		s = in.nextLine();
		
		if(s.substring(0, 1).equals("o")){
			System.out.println("Entrez le nom du fichier a charge (termine par .bin) ");
			nomFichier = in.nextLine();
			chargerGrille(nomFichier, g);
			
		}
		else{
			System.out.println("Entrez le nom du fichier dans lequel sera sauvegarde la grille");
			nomFichier = in.nextLine();
			construire_grille(g);
		}
		
		do{
			System.out.println("\n1 : Afficher l'etat de la grille");
			System.out.println("2 : Modifier Case");
			System.out.println("3 : Choisir la langue");
			System.out.println("4 : Passer en mode fenetre");
			System.out.println("5 : Quitter");
			
			rep = in.nextInt();
			s = in.nextLine();
			
			switch(rep){
			case 1 : 
				g.displayGrille();
				break;
			case 2 : 
				System.out.println("Entrez la case a modifier (ex : A6, B4 etc ...) \n");
				s = in.nextLine();
				
				System.out.println("Entrez la valeur de la case");
				try{
					val = in.nextDouble();
					try {
						g.fixerValeur(s, val);
					} catch (CaseInexistanteException e) {
						System.out.println("Case Inexistante");
					}
				}
				catch(InputMismatchException e){
					in.nextLine();
					System.out.println("Veuillez entrer un nombre");
				}
				
				break;
			case 3 :
				System.out.println("Entrez la langue (fr ou en)");
				s = in.nextLine();
				try{
					g.definirLangue(s);
				}
				catch(LangueInconnueException e){
					System.out.println("Langue non disponible");
				}
				break;
				
			case 4 :
				//On sauvegarde la grille afin de repercute les modifs faites en mode console
				sauvegarderGrille(nomFichier, g);
				TablooProto tableur = new TablooProto(nomFichier);

		        // Creation de l'application et lancement
		        // Fourni: ne rien changer.
		        JFrame frame = new JFrame("TABLOO");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        tableur.setOpaque(true);
		        frame.setContentPane(tableur);
		        frame.pack();
		        frame.setVisible(true);
		        break;
		        
			case 5 : 
				sauvegarderGrille(nomFichier, g);
				System.out.println("Fermeture du programme");
				break;
			
			default : 
				System.out.println("Veuillez entrer un chiffre valide");
				break;
				
			}
			
		}while(rep != 5);
		
	}
	
	public static void main(String[] args) {
		
		Menu(in, g);
	
	
	}

}
