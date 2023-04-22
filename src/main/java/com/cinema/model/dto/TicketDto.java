package com.cinema.model.dto;

public class TicketDto {

    private int rowNumber;
    private int placeNumber;

    public TicketDto(int rowNumber, int placeNumber) {
        this.rowNumber = rowNumber;
        this.placeNumber = placeNumber;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public void setPlaceNumber(int placeNumber) {
        this.placeNumber = placeNumber;
    }
}
