package com.soumya.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jeu.dal.DatabaseManager;

public class Joueur {
	private String username;
	private String password;
	private int score;
	public Joueur(String username,String mdp) {
		this.username=username;
		this.password=mdp;
	}
	private  final static String DB_URL = "jdbc:sqlite:xoudougi.db";
	public void sauvegarder() {
        String sql = "INSERT INTO joueurs(username, password) VALUES(?,?)";
        
        try (Connection conn = DriverManager.getConnection(Joueur.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, this.username);
            pstmt.setString(2, this.password);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	// Méthode pour récupérer un joueur
    public static Joueur charger(String username) {
        String sql = "SELECT * FROM joueurs WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DatabaseManager.DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Joueur(rs.getString("username"), rs.getString("password"));
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
