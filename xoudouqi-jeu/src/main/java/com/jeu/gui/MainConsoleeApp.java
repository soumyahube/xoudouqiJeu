package com.jeu.gui;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import com.jeu.dal.DatabaseManager;
import com.soumya.dao.Joueur;
import com.soumya.dao.Piece;
import com.soumya.dao.Plateau;
import com.soumya.dao.Position;

public class MainConsoleeApp {
    
    private static List<Joueur> joueursConnectes = new ArrayList<>();
    private static final int JOUEURS_REQUIS = 2;
    // Couleurs joueurs
    public static final String J1 = "\u001B[42m\u001B[30m";  // Vert
    public static final String J2 = "\u001B[45m\u001B[37m";  // Violet
    public static final String FIN = "\u001B[0m";
    public static void printMenuPrincipal() {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("Joueurs connectés: " + joueursConnectes.size() + "/" + JOUEURS_REQUIS);
        System.out.println("1- S'inscrire");
        System.out.println("2- Se connecter");
        System.out.println("3- Se déconnecter");
        System.out.println("4- Commencer une partie (" + JOUEURS_REQUIS + " joueurs requis)");
        System.out.println("0- Sortir");
    }
    
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
        	Plateau.afficherEnteteJeu();
        	Plateau.initialiser();
        	Plateau.afficherPlateau();
        	Plateau.afficherRegles();
            printMenuPrincipal();
            System.out.print("Choix: ");
            int choix = lireChoix(sc);

            try {
                switch (choix) {
                    case 1:
                        inscrireJoueur(sc);
                        break;
                    case 2:
                        connecterJoueur(sc);
                        break;
                    case 3:
                        deconnecterJoueur(sc);
                        break;
                    case 4:
                        running = false;
                        commencerPartie(sc);
                        break;
                    case 0:
                        running = false;
                        System.out.println("Au revoir !");
                        break;
                    default:
                        System.out.println("Choix invalide !");
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
            }
        }
        sc.close();
    }
    
    private static int lireChoix(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Veuillez entrer un nombre !");
            sc.next();
        }
        return sc.nextInt();
    }
    
    private static void inscrireJoueur(Scanner sc) throws Exception {
        System.out.println("\n--- Inscription ---");
        sc.nextLine();
        System.out.print("Username: ");
        String nom = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        new Joueur(nom, password).sauvegarder();
        System.out.println("Inscription réussie !");
    }
    
    private static void connecterJoueur(Scanner sc) throws Exception {
        if (joueursConnectes.size() >= JOUEURS_REQUIS) {
            System.out.println("Déjà " + JOUEURS_REQUIS + " joueurs connectés !");
            return;
        }
        
        System.out.println("\n--- Connexion ---");
        sc.nextLine();
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String passwd = sc.nextLine();
        
        Joueur joueur = Joueur.charger(user);
        if (joueur == null || !joueur.getPassword().equals(passwd)) {
            System.out.println("Échec connexion !");
            return;
        }
        
        if (joueursConnectes.contains(joueur)) {
            System.out.println("Ce joueur est déjà connecté !");
            return;
        }
        
        joueursConnectes.add(joueur);
        System.out.println("Connecté: " + joueur.getUsername());
    }
    
    private static void deconnecterJoueur(Scanner sc) {
        if (joueursConnectes.isEmpty()) {
            System.out.println("Aucun joueur connecte !");
            return;
        }
        
        System.out.println("\n--- Deconnexion ---");
        sc.nextLine();
        System.out.print("Username: ");
        String user = sc.nextLine();
        
        joueursConnectes.removeIf(j -> j.getUsername().equals(user));
        System.out.println("Déconnecté: " + user);
    }
    
    private static Position demanderPosition(String message, Scanner sc) {
        System.out.print(message);
        String input = sc.nextLine().trim().toUpperCase();

        // Vérifie le format de base : une lettre (A-G) suivie d'un chiffre (1-9)
        if (!input.matches("[A-G][1-9]")) {
            System.out.println("❌ Format invalide. Exemple: A1 (colonne A à G, ligne 1 à 9)");
            return null;
        }

        // Conversion
        int colonne = input.charAt(0) - 'A';
        int ligne = Integer.parseInt(input.substring(1)) - 1;

        // Vérifie les limites du plateau 9x7
        if (colonne < 0 || colonne >= 7 || ligne < 0 || ligne >= 9) {
            System.out.println("❌ Position en dehors du plateau (7 colonnes A-G, 9 lignes 1-9).");
            return null;
        }

        return new Position(ligne, colonne);  // ligne = y, colonne = x
    }

    
    private static void commencerPartie(Scanner sc) {
        if (joueursConnectes.size() < JOUEURS_REQUIS) {
            System.out.println("Il faut " + JOUEURS_REQUIS + " joueurs connectés !");
            return;
        }
        
        Plateau.initialiser();
        System.out.println("\n=== Début de partie ===");
        System.out.println("Joueur 1: " +J1+ joueursConnectes.get(0).getUsername() +FIN);
        System.out.println("Joueur 2: " + J2+joueursConnectes.get(1).getUsername() +FIN);

        int joueurActuel = 0;
        boolean partieEnCours = true;

        while (partieEnCours) {
        	Plateau.afficherPlateau();
            Joueur joueur = joueursConnectes.get(joueurActuel);
            
            // Détermine la couleur en fonction du joueur actuel
            String couleurJoueur = (joueurActuel == 0) ? Plateau.J1 : Plateau.J2;
            String resetCouleur = Plateau.FIN;
            
            System.out.println("\nTour de " + couleurJoueur + joueur.getUsername() + resetCouleur);
            
            // Demander position de départ
            Position depart = null;
            while (depart == null) {
                depart = demanderPosition("Position de départ (ex:(A1): ", sc);
            }
            
            // Demander position d'arrivée
            Position arrivee = null;
            while (arrivee == null) {
                arrivee = demanderPosition("Position d'arrivée (ex: A2): ", sc);
            }
            
            // Vérifier tanière adverse
            if (Plateau.estTaniereAdverse(arrivee, joueurActuel)) {
                System.out.println("🎉 " + joueur.getUsername() + " a gagné !");
                partieEnCours = false;
                break;
            }
            
            // Effectuer le déplacement
            if (Plateau.deplacerPiece(depart, arrivee, joueurActuel)) {
                joueurActuel = (joueurActuel + 1) % 2; // Changement de tour
            } else {
                System.out.println("Déplacement invalide ! Voir les regles de deplacement.");
                Plateau.afficherRegles();
            }
        }
    }
}
