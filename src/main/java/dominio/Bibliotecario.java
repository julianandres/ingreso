package dominio;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String LOS_LIBROS_POLINDROMOS_SOLO_SE_PUEDEN_UTILIZAR_EN_LA_BIBLIOTECA = "Los libros pol�ndromos solo se pueden utilizar en la biblioteca";

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, Prestamo prestamoEntrante) {
		if (!verificarPolindromos(isbn)) {

			Libro lb = repositorioLibro.obtenerPorIsbn(isbn);
			Prestamo pr = repositorioPrestamo.obtener(isbn);
			if (lb != null) {
				if (pr != null) {
					throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
				} else {
					Date fechaEntrega = validarSumaSiSumaEsMenorATreinta(isbn) ? null
							: retornarFechaEntregaDesdeFechaPrestamo(prestamoEntrante.getFechaSolicitud());
					Prestamo prestamo = new Prestamo(prestamoEntrante.getFechaSolicitud(), lb, fechaEntrega,
							prestamoEntrante.getNombreUsuario());
					repositorioPrestamo.agregar(prestamo);
				}
			}
		} else {
			throw new PrestamoException(LOS_LIBROS_POLINDROMOS_SOLO_SE_PUEDEN_UTILIZAR_EN_LA_BIBLIOTECA);
		}

	}

	public boolean esPrestado(String isbn) {
		Libro pr = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);
		return pr != null;
	}

	/**
	 * Este m�todo se encarga de verificar si el ISBN de entrada para el pr�stamo es
	 * un pol�ndromo
	 * 
	 * @param isbn
	 * @return boolean isPolindromo?
	 * @author Ing. Juli�n Andr�s Bola�os Ortega
	 */
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

	/**
	 * Este m�todo devuelve una validaci�n de la suma de d�gitos de acuerdo a un
	 * ISBN de entrada
	 * 
	 * @param entradaIsbn
	 * @return validationSumaISBN
	 * @author Ing. Juli�n Andr�s Bola�os Ortega
	 */
	public boolean validarSumaSiSumaEsMenorATreinta(String entradaIsbn) {
		// obtener suma d�gitos
		int suma = 0;
		for (int i = 0; i < entradaIsbn.length(); i++) {
			StringBuilder sb = new StringBuilder();
			char digito = entradaIsbn.charAt(i);
			sb.append(digito);
			try {
				int enteroDigito = Integer.parseInt(sb.toString());
				suma += enteroDigito;
			} catch (NumberFormatException e) {
			}
		}
		return suma < 30;
	}

	/**
	 * Este m�todo se encarga de retornar una fecha de entrega de acuerdo a las
	 * premisas planteadas se ingresa la fecha de prestamo y se regresa la fecha de
	 * entrega correspondiente
	 * 
	 * @param fechaPrestamo
	 * @return fechaMaximaEntrega
	 * @author Ing. Juli�n Andr�s Bola�os Ortega
	 */
	public Date retornarFechaEntregaDesdeFechaPrestamo(Date fechaPrestamo) {

		Date fechaActual = fechaPrestamo;
		int diasIncrementados = 0;
		Calendar cal = new GregorianCalendar();
		cal.setTime(fechaActual);
		while (diasIncrementados != 15) {
			if (cal.get(GregorianCalendar.DAY_OF_WEEK) != 1) {
				diasIncrementados++;
			}
			if (diasIncrementados < 15) {
				cal.add(GregorianCalendar.DAY_OF_MONTH, 1);
			}
		}
		Date resultado = cal.getTime();
		return resultado;

	}

}
