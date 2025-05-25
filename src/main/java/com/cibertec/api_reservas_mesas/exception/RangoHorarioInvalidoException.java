package com.cibertec.api_reservas_mesas.exception;

public class RangoHorarioInvalidoException extends RuntimeException {
    private final String campo;

    public RangoHorarioInvalidoException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
