package persistencia.entitad;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

@Entity(name="Prestamo")
@NamedQuery(name = "Prestamo.findByIsbn", query = "SELECT prestamo FROM Prestamo prestamo WHERE prestamo.libro.isbn = :isbn")
public class PrestamoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="ID_LIBRO",referencedColumnName="id")
	private LibroEntity libro;
	
	@Column(nullable = false)
	private Date fechaSolicitud;
	
	@Column(nullable = true)
	private Date fechaEntregaMaxima;
	
	@Column(nullable = false)
	private String nombreUsuario;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public LibroEntity getLibro() {
		return libro;
	}
	
	public void setLibro(LibroEntity libroEntity) {
		this.libro = libroEntity;
	}
	
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	
	public Date getFechaEntregaMaxima() {
		return fechaEntregaMaxima;
	}
	
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public void setFechaEntregaMaxima(Date fechaEntregaMaxima) {
		this.fechaEntregaMaxima = fechaEntregaMaxima;
	}
	
}
