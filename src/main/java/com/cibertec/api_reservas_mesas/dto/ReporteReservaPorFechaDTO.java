package com.cibertec.api_reservas_mesas.dto;

import java.util.Map;

public class ReporteReservaPorFechaDTO {
    private Map<String, Integer> reservasPorDia;
    private int totalReservas;
    private double promedioDiario;
    private int cancelaciones;
    private int numeroDeDias;
    
	public Map<String, Integer> getReservasPorDia() {
		return reservasPorDia;
	}
	public void setReservasPorDia(Map<String, Integer> reservasPorDia) {
		this.reservasPorDia = reservasPorDia;
	}
	public int getTotalReservas() {
		return totalReservas;
	}
	public void setTotalReservas(int totalReservas) {
		this.totalReservas = totalReservas;
	}
	public double getPromedioDiario() {
		return promedioDiario;
	}
	public void setPromedioDiario(double promedioDiario) {
		this.promedioDiario = promedioDiario;
	}
	public int getCancelaciones() {
		return cancelaciones;
	}
	public void setCancelaciones(int cancelaciones) {
		this.cancelaciones = cancelaciones;
	}
	public int getNumeroDeDias() {
		return numeroDeDias;
	}
	public void setNumeroDeDias(int numeroDeDias) {
		this.numeroDeDias = numeroDeDias;
	}
}
