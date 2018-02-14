package testdatabuilder;

import java.util.Date;

import dominio.Libro;
import dominio.Prestamo;

public class PrestamoTestDataBuilder {
	
	private Date fechaSolicitud;
	private Libro libro;
	private Date fechaEntregaMaxima;
	private String nombreUsuario;
	
	public PrestamoTestDataBuilder() {
		fechaSolicitud=new Date();
	}
	
	public PrestamoTestDataBuilder conFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud=fechaSolicitud;
		return this;
	}
	public PrestamoTestDataBuilder conLibro(Libro libro) {
		this.libro=libro;
		return this;
	}
	public PrestamoTestDataBuilder conFechaEntregaMaxima(Date fechaEntregaMaxima) {
		this.fechaEntregaMaxima=fechaEntregaMaxima;
		return this;
	}
	public PrestamoTestDataBuilder conNombreUsuario(String nombreUsuario) {
		this.nombreUsuario=nombreUsuario;
		return this;
	}
	public Prestamo build() {
		return new Prestamo(this.fechaSolicitud,this.libro,this.fechaEntregaMaxima,this.nombreUsuario);
	}

}
