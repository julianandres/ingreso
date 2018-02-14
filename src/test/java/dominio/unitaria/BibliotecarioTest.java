package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	@Test
	public void esPrestadoTest() {

		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();

		Libro libro = libroTestDataBuilder.build();

		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		boolean esPrestado = bibliotecario.esPrestado(libro.getIsbn());

		// assert
		assertTrue(esPrestado);
	}

	@Test
	public void libroNoPrestadoTest() {

		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();

		Libro libro = libroTestDataBuilder.build();

		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		boolean esPrestado = bibliotecario.esPrestado(libro.getIsbn());

		// assert
		assertFalse(esPrestado);
	}

	@Test
	public void prestarLibroPolindromo() {

		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder().conIsbn("a3d3a");

		Libro libro = libroTestDataBuilder.build();

		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);

		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);

		// act
		try {

			bibliotecario.prestar(libro.getIsbn(),null);
			
		} catch (PrestamoException e) {
			// assert
			System.out.println(e.getMessage());
			Assert.assertEquals(Bibliotecario.LOS_LIBROS_POLINDROMOS_SOLO_SE_PUEDEN_UTILIZAR_EN_LA_BIBLIOTECA, e.getMessage());
		}

	}
}
