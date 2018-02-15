package testdatabuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dominio.Libro;
import dominio.Prestamo;

public class PrestamoTestDataBuilder {
	
	private Date fechaSolicitud;
	private Libro libro;
	private Date fechaEntregaMaxima;
	private String nombreUsuario;//deberá ser obligatorio
	
	public PrestamoTestDataBuilder() {
		fechaSolicitud=new Date();
		nombreUsuario="default";
	}
	
	public PrestamoTestDataBuilder conFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud=fechaSolicitud;
		return this;
	}
	public PrestamoTestDataBuilder conFechaSolicitudDesdeTexto(String fechaSolicitudText) {
		try {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		this.fechaSolicitud=df.parse(fechaSolicitudText);
		}catch(ParseException e) {
			System.out.println("No ha sido posible parsear la fecha, se ha generado como:"+new Date());
			this.fechaSolicitud=new Date();
		}
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
