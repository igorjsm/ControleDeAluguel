package com.github.igorjsm.controledealuguel.model;

public class User {
    // Atributos de classe semelhante as colunas da tabela "User" do banco de dados
    private int id;
    private String username;
    private String password;
    private boolean isAdmin;

    // Construtor vazio
    public User() {
    }

    // Construtor cheio
    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
