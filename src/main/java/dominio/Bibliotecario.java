package dominio;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;
	private static final Log log = LogFactory.getLog(Bibliotecario.class);

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn) {
		System.out.println("ingreso prestar");
		Libro lb = repositorioLibro.obtenerPorIsbn(isbn);
		System.out.println(lb!=null?lb.getTitulo():"---libro no encontrado");
		
		Prestamo pr = repositorioPrestamo.obtener(isbn);
		System.out.println("iniciado prestar libro");
		if (lb != null) {
			if (pr != null) {
				System.out.println("libro, prestado");
				throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
			} else {
				System.out.println("libro no prestado");
				Prestamo prestamo = new Prestamo(new Date(), lb, new Date(), "username");
				repositorioPrestamo.agregar(prestamo);
			}
		}

	}

	public boolean esPrestado(String isbn) {
		Libro pr = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return pr != null;
	}

}
