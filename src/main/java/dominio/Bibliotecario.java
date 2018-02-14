package dominio;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String LOS_LIBROS_POLINDROMOS_SOLO_SE_PUEDEN_UTILIZAR_EN_LA_BIBLIOTECA = "Los libros políndromos solo se pueden utilizar en la biblioteca";

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;
	
	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn,Prestamo prestamoEntrante) {
		if (!verificarPolindromos(isbn)) {
			Libro lb = repositorioLibro.obtenerPorIsbn(isbn);
			Prestamo pr = repositorioPrestamo.obtener(isbn);
			if (lb != null) {
				if (pr != null) {
					throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
				} else {
					Prestamo prestamo = new Prestamo(prestamoEntrante.getFechaSolicitud(), lb, new Date(), prestamoEntrante.getNombreUsuario());
					repositorioPrestamo.agregar(prestamo);
				}
			}
		}else {
			throw new PrestamoException(LOS_LIBROS_POLINDROMOS_SOLO_SE_PUEDEN_UTILIZAR_EN_LA_BIBLIOTECA);
		}

	}
	
	public boolean esPrestado(String isbn) {
		Libro pr = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return pr != null;
	}

	public boolean verificarPolindromos(String isbn) {
		int longitud = isbn.length();
		String nuevaCadena = "";
		int i = longitud - 1;
		do {
			char caracter = isbn.charAt(i);
			nuevaCadena += caracter;
			i--;
		} while (nuevaCadena.length() != isbn.length());
		System.out.println(nuevaCadena);
		return nuevaCadena.equals(isbn);
	}

}
