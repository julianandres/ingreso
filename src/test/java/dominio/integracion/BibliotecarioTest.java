package dominio.integracion;

import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.Prestamo;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;
import testdatabuilder.PrestamoTestDataBuilder;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	private static final String NOMBRE_USUARIO = "Pepito Perez";
	private static final String ISBN_MAYOR_TREINTA = "T878B85Z";
	private static final String ISBN_MENOR_TREINTA = "T8AOB85Z";
	private static final String FECHA_VEINTICUATRO_MAYO_DOSMILDIESISIETE = "24/05/2017";
	private static final String FECHA_VEINTISEIS_MAYO_DOSMILDIESISIETE = "26/05/2017";

	private SistemaDePersistencia sistemaPersistencia;

	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	private DateFormat dateFormat;

	@Before
	public void setUp() {

		sistemaPersistencia = new SistemaDePersistencia();

		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();

		sistemaPersistencia.iniciar();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	}

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	/**
	 * El objetivo de esta prueba sera comprobar el funcionamiento del sistema al
	 * momento de realizar un prestamo(punto 1)
	 * 
	 */
	@Test
	public void prestarLibroTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro).conFechaSolicitud(new Date())
				.conNombreUsuario(NOMBRE_USUARIO).build();
		// act
		blibliotecario.prestar(libro.getIsbn(), prestamo);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Assert.assertNotNull(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));
	}

	/**
	 * El objetivo de esta prueba es llevar a cabo un préstamo de un libro que
	 * incluya un nombre de usuario al final se comprueba si el nombre de usuario
	 * preestablecido fue el guardado en la base de datos Se resuelve el punto 4
	 **/

	@Test
	public void prestarLibroConUsuarioTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro).conFechaSolicitud(new Date())
				.conNombreUsuario(NOMBRE_USUARIO).build();// Se inserta un nombre de usuario
		// act
		blibliotecario.prestar(libro.getIsbn(), prestamo);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Libro lbPrestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
		Prestamo prestamoObtenido = repositorioPrestamo.obtener(libro.getIsbn());
		Assert.assertTrue(prestamoObtenido != null && prestamoObtenido.getNombreUsuario().equals(NOMBRE_USUARIO));
		// se verifica el  nombre de usuario, punto 4
		Assert.assertNotNull(lbPrestado);
	}

	/**
	 * En esta prueba se lleva a cabo el ingreso de una fecha, en este caso el
	 * 24/05/2017 con un ISBN que sus dígitos suman más de 30 para lo cual de
	 * acuerdo a las premisas anteriormente planteadas se esperará que la fecha de
	 * retorno sea 09/06/2017 Se resuelve el punto 5
	 */
	@Test
	public void prestarLibroCalculoFechaEntregaVeintucuatroMayoDosmildiesisieteISBNMayorTreintaTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).conIsbn(ISBN_MAYOR_TREINTA)
				.build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro)
				.conFechaSolicitudDesdeTexto(FECHA_VEINTICUATRO_MAYO_DOSMILDIESISIETE).build();
		// act
		blibliotecario.prestar(libro.getIsbn(), prestamo);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Libro lbPrestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
		Prestamo prestamoObtenido = repositorioPrestamo.obtener(libro.getIsbn());
		Assert.assertEquals(dateFormat.format(prestamoObtenido.getFechaEntregaMaxima()), "09/06/2017");// se verifica
																										// fechamaximadeentrega,
																										// punto 5
		Assert.assertNotNull(lbPrestado);
	}

	/**
	 * En esta prueba se lleva a cabo el ingreso de una fecha, en este caso el
	 * 26/05/2017 con un ISBN que sus dígitos suman más de 30 para lo cual de
	 * acuerdo a las premisas anteriormente planteadas se esperará que la fecha de
	 * retorno sea 12/06/2017 Se resuelve el punto 5
	 */
	@Test
	public void prestarLibroCalculoFechaEntregaVeintiseisMayoDosmildiesisieteISBNMayorTreintaTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).conIsbn(ISBN_MAYOR_TREINTA)
				.build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro)
				.conFechaSolicitudDesdeTexto(FECHA_VEINTISEIS_MAYO_DOSMILDIESISIETE).build();
		// act
		blibliotecario.prestar(libro.getIsbn(), prestamo);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Libro lbPrestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
		Prestamo prestamoObtenido = repositorioPrestamo.obtener(libro.getIsbn());
		Assert.assertEquals(dateFormat.format(prestamoObtenido.getFechaEntregaMaxima()), "12/06/2017");// se verifica
																										// maxima de
																										// entrega,
																										// punto 5
		Assert.assertNotNull(lbPrestado);
	}

	/**
	 * En esta prueba se lleva a cabo el ingreso de una fecha cualquiera y un ISBN
	 * de suma de dígitos menor a 30, la idea principal será recibir una fecha de
	 * ingreso nula Se resuelve el punto 5
	 */
	@Test
	public void prestarLibroCalculoFechaEntregaVeintiseisMayoDosmildiesisieteISBNMMENORTreintaTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).conIsbn(ISBN_MENOR_TREINTA)
				.build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro)
				.conFechaSolicitudDesdeTexto(FECHA_VEINTISEIS_MAYO_DOSMILDIESISIETE).build();
		// act
		blibliotecario.prestar(libro.getIsbn(), prestamo);

		// assert
		Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
		Libro lbPrestado = repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
		Prestamo prestamoObtenido = repositorioPrestamo.obtener(libro.getIsbn());
		Assert.assertNull(prestamoObtenido.getFechaEntregaMaxima());// se verifica maxima de entrega, deberá ser null
																	// punto 5
		Assert.assertNotNull(lbPrestado);
	}

	@Test
	public void prestarLibroNoDisponibleTest() {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();

		repositorioLibros.agregar(libro);

		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

		// act
		Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro).conFechaSolicitud(new Date())
				.conNombreUsuario(NOMBRE_USUARIO).build();
		blibliotecario.prestar(libro.getIsbn(), prestamo);
		try {

			blibliotecario.prestar(libro.getIsbn(), prestamo);
			fail();

		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		}
	}
}
