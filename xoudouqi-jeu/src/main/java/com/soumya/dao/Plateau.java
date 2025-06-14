package com.soumya.dao;

public class Plateau {
    // Animaux avec emojis
    public static final String[] ANIMAUX = {
        "🐘", "🦁", "🐅", "🐆", "🐕", "🐺", "🐈", "🐀"
    };
    
    // Cases spéciales
    public static final String LAC = "\u001B[44m\u001B[37m🌊\u001B[0m";
    public static final String PIEGE = "\u001B[41m\u001B[37m⚠️\u001B[0m";
    public static final String SANCTUAIRE = "\u001B[43m\u001B[30m🏯\u001B[0m";
    public static final String VIDE = "\u001B[47m\u001B[30m  \u001B[0m";
    
    // Couleurs joueurs
    public static final String J1 = "\u001B[42m\u001B[30m";  // Vert
    public static final String J2 = "\u001B[45m\u001B[37m";  // Violet
    public static final String FIN = "\u001B[0m";

    private static String[][] plateau;

	// Initialisation du plateau
    public static void initialiser() {
        plateau = new String[9][7];
        
        // Initialiser toutes les cases comme vides
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 7; j++) {
                plateau[i][j] = VIDE;
            }
        }
        
        // Placer les lacs
        for (int i = 3; i <= 5; i++) {
            plateau[i][1] = LAC;
            plateau[i][2] = LAC;
            plateau[i][4] = LAC;
            plateau[i][5] = LAC;
        }
        
        // Sanctuaires
        plateau[0][3] = SANCTUAIRE; // J2
        plateau[8][3] = SANCTUAIRE; // J1
        
        // Pièges
        plateau[0][2] = PIEGE;
        plateau[0][4] = PIEGE;
        plateau[1][3] = PIEGE;
        plateau[8][2] = PIEGE;
        plateau[8][4] = PIEGE;
        plateau[7][3] = PIEGE;
        
        // Joueur 1 (Vert)
        plateau[8][0] = J1 + "🦁" + FIN;
        plateau[8][6] = J1 + "🐅" + FIN;
        plateau[7][1] = J1 + "🐕" + FIN;
        plateau[7][5] = J1 + "🐈" + FIN;
        plateau[6][0] = J1 + "🐀" + FIN;
        plateau[6][2] = J1 + "🐆" + FIN;
        plateau[6][4] = J1 + "🐺" + FIN;
        plateau[6][6] = J1 + "🐘" + FIN;
        
        // Joueur 2 (Violet)
        plateau[0][0] = J2 + "🐅" + FIN;
        plateau[0][6] = J2 + "🦁" + FIN;
        plateau[1][1] = J2 + "🐈" + FIN;
        plateau[1][5] = J2 + "🐕" + FIN;
        plateau[2][0] = J2 + "🐘" + FIN;
        plateau[2][2] = J2 + "🐺" + FIN;
        plateau[2][4] = J2 + "🐆" + FIN;
        plateau[2][6] = J2 + "🐀" + FIN;
    }

    // Méthodes de vérification
    public static boolean horsLimites(Position pos) {
        return pos.getLigne() < 0 || pos.getLigne() >= 9 || 
               pos.getColonne() < 0 || pos.getColonne() >= 7;
    }

    public static boolean estSanctuaireAllie(Position pos, int joueur) {
        return (joueur == 0 && pos.getLigne() == 8 && pos.getColonne() == 3) ||
               (joueur == 1 && pos.getLigne() == 0 && pos.getColonne() == 3);
    }

    public static boolean estTaniereAdverse(Position pos, int joueur) {
        return (joueur == 0 && pos.getLigne() == 0 && pos.getColonne() == 3) ||
               (joueur == 1 && pos.getLigne() == 8 && pos.getColonne() == 3);
    }

    public static boolean estLac(Position pos) {
        int l = pos.getLigne();
        int c = pos.getColonne();
        return (l >= 3 && l <= 5) && ((c >= 1 && c <= 2) || (c >= 4 && c <= 5));
    }
    
    static boolean estDansEau(Position pos) {
        return estLac(pos);
    }
    public static boolean estPiege(Position pos) {
        int l = pos.getLigne();
        int c = pos.getColonne();
        return (l == 0 && (c == 2 || c == 4)) || 
               (l == 1 && c == 3) ||
               (l == 8 && (c == 2 || c == 4)) ||
               (l == 7 && c == 3);
    }

    public static boolean estDeplacementOrthogonal(Position depart, Position arrivee) {
        int dl = Math.abs(arrivee.getLigne() - depart.getLigne());
        int dc = Math.abs(arrivee.getColonne() - depart.getColonne());
        return (dl == 1 && dc == 0) || (dl == 0 && dc == 1);
    }
    private static boolean aRatDansLeLac(Position depart, Position arrivee) {
        int l1 = depart.getLigne(), l2 = arrivee.getLigne();
        int c1 = depart.getColonne(), c2 = arrivee.getColonne();

        if (l1 == l2) {
            int min = Math.min(c1, c2);
            int max = Math.max(c1, c2);
            for (int c = min + 1; c < max; c++) {
                if (estLac(new Position(l1, c))) {
                    String contenu = plateau[l1][c];
                    if (Piece.getType(contenu) == Piece.RAT) return true;
                }
            }
        } else if (c1 == c2) {
            int min = Math.min(l1, l2);
            int max = Math.max(l1, l2);
            for (int l = min + 1; l < max; l++) {
                if (estLac(new Position(l, c1))) {
                    String contenu = plateau[l][c1];
                    if (Piece.getType(contenu) == Piece.RAT) return true;
                }
            }
        }

        return false;
    }
    private static boolean sauteRiviereValide(Position depart, Position arrivee) {
        int diffLignes = Math.abs(arrivee.getLigne() - depart.getLigne());
        int diffColonnes = Math.abs(arrivee.getColonne() - depart.getColonne());
        
        // Saut horizontal de 3 cases
        if (depart.getLigne() == arrivee.getLigne() && diffColonnes == 3) {
            return true;
        }
        // Saut vertical de 3 cases
        if (depart.getColonne() == arrivee.getColonne() && diffLignes == 3) {
            return true;
        }
        return false;
    }
    
    private static boolean toutesCasesEntreSontLac(Position depart, Position arrivee) {
        if (depart.getLigne() == arrivee.getLigne()) { // Déplacement horizontal
            int ligne = depart.getLigne();
            int minCol = Math.min(depart.getColonne(), arrivee.getColonne());
            int maxCol = Math.max(depart.getColonne(), arrivee.getColonne());
            
            // Pour un saut de 3 cases, il doit y avoir exactement 2 cases de lac entre
            if (maxCol - minCol != 3) return false;
            
            for (int col = minCol + 1; col < maxCol; col++) {
                if (!estLac(new Position(ligne, col))) {
                    return false;
                }
            }
        } 
        else if (depart.getColonne() == arrivee.getColonne()) { // Déplacement vertical
            int colonne = depart.getColonne();
            int minLig = Math.min(depart.getLigne(), arrivee.getLigne());
            int maxLig = Math.max(depart.getLigne(), arrivee.getLigne());
            
            // Pour un saut de 3 cases, il doit y avoir exactement 2 cases de lac entre
            if (maxLig - minLig != 3) return false;
            
            for (int lig = minLig + 1; lig < maxLig; lig++) {
                if (!estLac(new Position(lig, colonne))) {
                    return false;
                }
            }
        } 
        else {
            return false; // Pas en ligne droite
        }
        return true;
    }


    // Méthode principale pour déplacer une pièce
    public static boolean deplacerPiece(Position depart, Position arrivee, int joueur) {
        // 1. Vérifications de base
        if (horsLimites(depart) || horsLimites(arrivee)) return false;
        if (estSanctuaireAllie(arrivee, joueur)) return false;

        String piece = plateau[depart.ligne][depart.colonne];
        String cible = plateau[arrivee.ligne][arrivee.colonne];

        // 2. Vérification de la propriété de la pièce
        if (!piece.startsWith(joueur == 0 ? J1 : J2)) return false;

        int typePiece = Piece.getType(piece);
        int typeCible = Piece.getType(cible);

        // ===== CAS 1 : DÉPLACEMENT VERS CASE VIDE =====
        if (cible.equals(VIDE) || cible.equals(PIEGE) || cible.equals(SANCTUAIRE)) {
            // --- Cas spécial : Saut du Lion/Tigre ---
            if (Piece.peutSauterRiviere(typePiece)) {
                // Sauts verticaux (3→7)
                if (depart.ligne == 3 && arrivee.ligne == 7 && depart.colonne == arrivee.colonne) {
                    if (aRatDansLeLac(depart, arrivee)) return false; // Bloqué si Rat présent
                    plateau[depart.ligne][depart.colonne] = VIDE;
                    plateau[arrivee.ligne][arrivee.colonne] = piece;
                    return true;
                }
                // Sauts horizontaux (1→4)
                else if (depart.colonne == 1 && arrivee.colonne == 4 && depart.ligne == arrivee.ligne) {
                    if (aRatDansLeLac(depart, arrivee)) return false; // Bloqué si Rat présent
                    plateau[depart.ligne][depart.colonne] = VIDE;
                    plateau[arrivee.ligne][arrivee.colonne] = piece;
                    return true;
                }
                // Sauts horizontaux (4→7)
                else if (depart.colonne == 4 && arrivee.colonne == 7 && depart.ligne == arrivee.ligne) {
                    if (aRatDansLeLac(depart, arrivee)) return false; // Bloqué si Rat présent
                    plateau[depart.ligne][depart.colonne] = VIDE;
                    plateau[arrivee.ligne][arrivee.colonne] = piece;
                    return true;
                }
                return false;
            }

            // --- Déplacement normal (1 case) ---
            if (estDeplacementOrthogonal(depart, arrivee)) {
                // Seul le Rat peut entrer dans l'eau
                if (estLac(arrivee) && typePiece != Piece.RAT) return false;

                plateau[depart.ligne][depart.colonne] = estLac(depart) ? LAC : VIDE;
                plateau[arrivee.ligne][arrivee.colonne] = piece;
                return true;
            }
            return false;
        }
        // ===== CAS 2 : CAPTURE =====
        else {
            // Vérifications préalables
            if (cible.startsWith(joueur == 0 ? J1 : J2)) return false; // Alliée
            if (!estDeplacementOrthogonal(depart, arrivee)) return false; // Diagonale

            // --- Cas spécial : Rat vs Éléphant ---
            boolean captureValide;
            if (typePiece == Piece.RAT && typeCible == Piece.ELEPHANT) {
                captureValide = !estLac(depart); // Rat ne peut pas capturer depuis l'eau
            } 
            // --- Cas normal : Hiérarchie ou piège ---
            else {
                boolean cibleDansPiege = estPiege(arrivee) && !cible.startsWith(joueur == 0 ? J1 : J2);
                captureValide = (typePiece <= typeCible) || cibleDansPiege;
            }

            if (!captureValide) return false;

            // Exécution de la capture
            plateau[depart.ligne][depart.colonne] = estLac(depart) ? LAC : VIDE;
            plateau[arrivee.ligne][arrivee.colonne] = piece;
            return true;
        }
    }

        public static void afficherRegles() {
            System.out.println("\n=== RÈGLES DE DÉPLACEMENT ===");
            System.out.println("+-------------+----------------------------------+--------------------------------+");
            System.out.println("|  Animal     | Déplacement                      | Règles Spéciales               |");
            System.out.println("+-------------+----------------------------------+--------------------------------+");
            System.out.println("| 🐘 Éléphant | 1 case (haut/bas/gauche/droite)  | Ne peut pas capturer le Rat    |");
            System.out.println("| 🦁 Lion     | 1 case ou saut lac               | Saut sans rat intermédiaire    |");
            System.out.println("| 🐅 Tigre    | 1 case ou saut  lac              | Saut sans rat intermédiaire    |");
            System.out.println("| 🐆 Panthère | 1 case                           | Aucune                         |");
            System.out.println("| 🐕 Chien    | 1 case                           | Aucune                         |");
            System.out.println("| 🐺 Loup     | 1 case                           | Aucune                         | ");
            System.out.println("| 🐈 Chat     | 1 case                           | Aucune                         |");
            System.out.println("| 🐀 Rat      | 1 case (peut nager)              | Capture l'éléphant hors eau    |");
            System.out.println("+-------------+----------------------------------+--------------------------------+");
            System.out.println("** Cases spéciales :**");
            System.out.println("- 🌊 Lac : Seul le Rat peut entrer");
            System.out.println("- ⚠️ Piège : Toute pièce ennemie est capturée");
            System.out.println("- 🏯 Sanctuaire : Victoire si atteint (case D1/D9)");
        }
      
       
        public static void afficherEnteteJeu() {
            System.out.println("+-------------------------------------------------+");
            System.out.println("|   🀄 JUNGLE CHESS - 鬥兽棋 (Dòushòuqí) 🀡         |");
            System.out.println("+-------------------------------------------------+");
            System.out.println("|  OBJECTIF :                                     |");
            System.out.println("|  • Atteindre le sanctuaire ennemi (🏯)          |");
            System.out.println("|  • OU capturer toutes les pièces adverses       |");
            System.out.println("|  STRATÉGIE :                                    |");
            System.out.println("|  • Hiérarchie animale (🐘 > 🦁 > 🐀...)          |");
            System.out.println("|  • Terrain spécial (🌊 Lacs, ⚠ Pièges)          |");
            System.out.println("+-------------------------------------------------+");
            System.out.println(" Commande : [Lettre][Chiffre] → [Lettre][Chiffre]");
            System.out.println(" Exemple : A7 → B7  |  /aide pour les règles");
            System.out.println();
        }
    // Affichage du plateau
    public static void afficherPlateau() {
        System.out.println("    A   B   C   D   E   F   G");
        System.out.println("  ┌───┬───┬───┬───┬───┬───┬──┐");
        
        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + " │");
            for (int j = 0; j < 7; j++) {
                System.out.print(plateau[i][j] + " │");
            }
            System.out.println();
            
            if (i < 8) {
                System.out.println("  ├───┼───┼───┼───┼───┼───┼──┤");
            }
        }
        System.out.println("  └───┴───┴───┴───┴───┴───┴──┘");
        
        System.out.println("\n🐘>🦁>🐅>🐆>🐕>🐺>🐈>🐀>🐘 | 🐀🌊=nage | 🦁🐅=saut");
    }
    
}