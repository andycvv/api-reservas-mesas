package com.cibertec.api_reservas_mesas.exception;

public class RegistroEnUsoException extends RuntimeException {
    private String entidad;
    private Integer id;

    public RegistroEnUsoException(String entidad, Integer id, String message) {
        super(message);
        this.entidad = entidad;
        this.id = id;
    }

    public String getEntidad() {
        return entidad;
    }

    public Integer getId() {
        return id;
    }
}
