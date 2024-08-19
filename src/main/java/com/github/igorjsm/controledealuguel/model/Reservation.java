package com.github.igorjsm.controledealuguel.model;

import java.time.LocalDate;

public class Reservation {
    // Atributos de classe semelhantes às colunas da tabela "reservations" do banco
    // de dados
    private int id;
    private String renterName;
    private LocalDate deliveryDate;
    private LocalDate pickupDate;
    private String address;
    private boolean isPaid;
    private boolean isPickedUp;
    private String items;

    // Construtor vazio
    public Reservation() {
    }

    // Construtor cheio
    public Reservation(String renterName, LocalDate deliveryDate, LocalDate pickupDate, String address, boolean isPaid,
            boolean isPickedUp, String items) {
        this.renterName = renterName;
        this.deliveryDate = deliveryDate;
        this.pickupDate = pickupDate;
        this.address = address;
        this.isPaid = isPaid;
        this.isPickedUp = isPickedUp;
        this.items = items;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean isPickedUp) {
        this.isPickedUp = isPickedUp;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
