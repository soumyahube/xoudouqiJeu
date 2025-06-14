package com.soumya.dao;

public class Piece {
	

    // Hiérarchie des pièces (constantes numériques)
    public static final int ELEPHANT = 8;
    public static final int LION = 7;
    public static final int TIGRE = 6;
    public static final int PANTHERE = 5;
    public static final int CHIEN = 4;
    public static final int LOUP = 3;
    public static final int CHAT = 2;
    public static final int RAT = 1;
    public static final int[] PEUT_SAUTER = {LION, TIGRE};

    // Représentation des pièces avec emojis
    private static final String[] ANIMAUX = {
        "🐘", "🦁", "🐅", "🐆", "🐕", "🐺", "🐈", "🐀"
    };

    // Couleurs joueurs
    public static final String J1 = "\u001B[42m\u001B[30m";  // Vert
    public static final String J2 = "\u001B[45m\u001B[37m";  // Violet
    private static final String FIN = "\u001B[0m";

    public static String[] getAnimaux() {
        return ANIMAUX;
    }
   
    // Méthodes générales de vérification
    public static boolean peutCapturer(int attaquant, int defenseur, boolean dansPiege, Position posDefenseur) {
        if (dansPiege) return true;
        if (attaquant == RAT && defenseur == ELEPHANT) {
            return !Plateau.estDansEau(posDefenseur); // Le rat ne peut capturer l'éléphant QUE s'il n'est pas dans l'eau
        }
        if (attaquant == ELEPHANT && defenseur == RAT) return false; // L'éléphant ne peut jamais capturer le rat
        if (defenseur == RAT && Plateau.estDansEau(posDefenseur)) {
            return attaquant == RAT; // Seul un rat peut capturer un rat dans l'eau
        }
        return attaquant >= defenseur; // Règle générale de force
    }

    public static boolean peutSauterRiviere(int piece) {
        return piece == LION || piece == TIGRE;
    }

    public static boolean peutAllerDansEau(int piece) {
        return piece == RAT;
    }
    
 // Méthode pour obtenir le type d'une pièce à partir de son emoji
    public static int getType(String piece) {
        // Supprimer les codes de couleur pour ne garder que l'emoji
        String emoji = piece.replaceAll("\\p{C}", "").trim();
        
        for (int i = 0; i < ANIMAUX.length; i++) {
            if (ANIMAUX[i].equals(emoji)) {
                switch (i) {
                    case 0: return ELEPHANT;
                    case 1: return LION;
                    case 2: return TIGRE;
                    case 3: return PANTHERE;
                    case 4: return CHIEN;
                    case 5: return LOUP;
                    case 6: return CHAT;
                    case 7: return RAT;
                }
            }
        }
        return -1;
    }

}