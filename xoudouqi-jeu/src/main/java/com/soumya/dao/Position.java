package com.soumya.dao;

public class Position {
	public  int ligne;
	public int colonne;
	public Position(int l,int c) {
		this.ligne=l;
		this.colonne=c;
	}
	public int getLigne() {
		return ligne;
	}
	public void setLigne(int ligne) {
		this.ligne = ligne;
	}
	public int getColonne() {
		return colonne;
	}
	public void setColonne(int colonne) {
		this.colonne = colonne;
	}
	public boolean equals(Position other) {
        return ligne == other.ligne && colonne == other.colonne;
    }
}
