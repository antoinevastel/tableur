package inter;

/*
 * TablooProto.java requires no other files.
 * 
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import noyau.CaseInexistanteException;
import noyau.Grille;
import noyau.OperationIncorrecteException;

public class TablooProto extends JPanel{
	JTable table;
    // Fourni: ne rien changer.
    public TablooProto(String nomFic) {
        super(new GridLayout(1, 0));

        // modele de donnees
        // cf. plus loin la inner classe MyTableModel a modifier...
        MyTableModel tableModel = new MyTableModel(nomFic);

        // la JTable et ses parametres
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 500));
        table.setGridColor(Color.BLACK);
        table.setShowGrid(true);

        // on ajoute un scroll
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // parametrage de la 1ere ligne = noms des colonnes
        ((DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // parametrage de la 1ere colonne consacree a la numerotation des lignes
        TableColumn tm = table.getColumnModel().getColumn(0);
        tm.setPreferredWidth(tm.getPreferredWidth() * 2 / 3);
        tm.setCellRenderer(new PremiereColonneSpecificRenderer(Color.LIGHT_GRAY));

    }

    // Inner class pour changer l'aspect de la premiere colonne consacree a la numerotation des lignes
    // Fourni: ne rien changer.
    class PremiereColonneSpecificRenderer extends DefaultTableCellRenderer {

        Color couleur;

        public PremiereColonneSpecificRenderer(Color couleur) {
            super();
            this.couleur = couleur;
            this.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            cell.setBackground(couleur);
            return cell;
        }
    }

    // Inner class pour etablir la connexion entre la JTable graphique et un modele de donnees.
    // Pour nous le modele de donnees sera une grille du noyau de representation et de calcul
    // construite et sauvegardee par serialisation comme precedemmment.
    // Dans ce prototype exemple, le modele de donnees est une simple matrice de String "en dur".
    // Il faudra le remplacer par une connexion a une telle grille.
    class MyTableModel extends AbstractTableModel {

        // TODO
        // remplacer ce tableau en dur du prototype par la grille serialisee:
        // noyau.Grille calc;
        Grille g = new Grille();
        
        MyTableModel(String nomSauvegarde) {
        	try {
				g.load(nomSauvegarde);
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("Erreur chargement grille"+e);
			}
        }

        // Standard: doit retourner le nbre de colonnes de la JTable
        public int getColumnCount() {
            return g.getNbColonnes()+1;
        }

        // Standard: doit retourner le nbre de lignes de la JTable
        public int getRowCount() {
            return g.getNbLignes()+1;
        }

        // Standard: doit renvoyer le nom de la colonne a afficher en tete
        // Fourni: ne rien changer.
        @Override
        public String getColumnName(int col) {
            if (col == 0) {
                return ""; // colonne consacrée aux numeros de ligne
            } else {
                return "" + (char) ((int) ('A') + col - 1);
            }
        }

        // Utilitaire interne fourni (ne rien changer)
        // Retourne le nom d'une case a partir de ses coordonnees dans la JTable.
        String getNomCase(int row, int col) {
            return this.getColumnName(col) + String.valueOf(row + 1); // row commence a 0
        }

        @Override
        // Standard: doit renvoyer le contenu a afficher de la case correspondante
        public Object getValueAt(int row, int col) {
            if (col == 0) {
                // Fourni: ne rien changer.
                // en colonne 0 : numeros de lignes
                return "" + String.valueOf(row + 1);
            } else {
            	String nomCase = this.getColumnName(col) + String.valueOf(row + 1);
            		try{
            			if(g.getCase(nomCase).getFormule() != null){
		            		if(Double.isNaN(g.getValeur(nomCase))){
		            			return ""+g.getContenu(nomCase)+ " = ??? ";
		            		}
		            		else{
		            			return "" + g.getContenu(nomCase) + " = "+String.valueOf(g.getValeur(nomCase));
		            		}
		            	}
		            	else{
		            		if(Double.isNaN(g.getValeur(nomCase))){
		            			return "???";
		            		}
		            		else{
		            			return ""+ String.valueOf(g.getValeur(nomCase));
		            		}
		            	}
            		}
            		catch(CaseInexistanteException e){
            			return "";
            		}
	                // TODO: remplacer par le contenu + la valeur
	                // de la case de nom getNomCase(row, col)
	                // dans la grille (comme dans la figure 1 du sujet).     	
            }
        }

        // Standard.
        // Fourni: ne rien changer.
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        // Standard: determine si une case est editable ou non.
        // Fourni: ne rien changer.
        // Seules les cases de la 1er colonne ne le sont pas
        // (consacrees a la numerotation des lignes)
        @Override
        public boolean isCellEditable(int row, int col) {
            if (col < 1) {
                return false; // col 0 consacree a la numerotation des lignes (non editable)
            } else {
                return true;
            }
        }


        // Standard: l'utilisateur a entré une valeur dans une case,
        // mettre a jour le modèle de donnees connecte.
        // L'utilisateur a modifie une case.
        // Si c'est une valeur numerique (sinon ne rien faire)
        // - modifier la case correspondante dans la grille si cette case existe
        // - ajouter la case correspondante dans la grille
        @Override
        public void setValueAt(Object value, int row, int col) {

            // TODO remplacer par le code correspondant
            if (value instanceof String) {
            	String nomCase = this.getColumnName(col) + String.valueOf(row + 1);
            	try{
            		g.getCase(nomCase);
            	}
            	catch(CaseInexistanteException e){
            		g.addCase(this.getColumnName(col), row + 1);
            	}
            	
            	try{
            		g.fixerValeur(nomCase, (Double.parseDouble((String)value)));
            	}
            	catch(CaseInexistanteException e){}
            	catch(NumberFormatException e){}
            	
            }
            // Ne pas modifier :
            // mise a jour automatique de l'affichage suite a la modification
            fireTableCellUpdated(row, col);
            table.repaint();
        }
    }
    // Fin de la inner class MyTableModel

    // Exécution de l'interface graphique a partir d'un terminal.
    // TODO: parametrer le tout par un fichier de grille serialisee.
}

