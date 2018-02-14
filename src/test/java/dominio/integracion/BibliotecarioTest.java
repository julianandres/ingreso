package dominio.integracion;

import static org.junit.Assert.fail;

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
    
    private SistemaDePersistencia sistemaPersistencia;
    
    private RepositorioLibro repositorioLibros;
    private RepositorioPrestamo repositorioPrestamo;

    @Before
    public void setUp() {
        
        sistemaPersistencia = new SistemaDePersistencia();
        
        repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
        repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();
        
        sistemaPersistencia.iniciar();
    }
    

    @After
    public void tearDown() {
        sistemaPersistencia.terminar();
    }

    @Test
    public void prestarLibroTest() {

        // arrange
        Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
        repositorioLibros.agregar(libro);
        Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
        Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro).
        		conFechaSolicitud(new Date()).conNombreUsuario(NOMBRE_USUARIO).build();
        // act
        blibliotecario.prestar(libro.getIsbn(),prestamo);

        // assert
        Assert.assertTrue(blibliotecario.esPrestado(libro.getIsbn()));
        Libro lbPrestado =repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn());
        Prestamo prestamoObtenido = repositorioPrestamo.obtener(libro.getIsbn());
        Assert.assertTrue(prestamoObtenido!=null&&prestamoObtenido.getNombreUsuario().equals(NOMBRE_USUARIO));
        Assert.assertNotNull(lbPrestado);
    }

    @Test
    public void prestarLibroNoDisponibleTest() {

        // arrange
        Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
        
        repositorioLibros.agregar(libro);
        
        Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);

        // act
        Prestamo prestamo = new PrestamoTestDataBuilder().conLibro(libro).
        		conFechaSolicitud(new Date()).conNombreUsuario(NOMBRE_USUARIO).build();
        blibliotecario.prestar(libro.getIsbn(),prestamo);
        try {
            
            blibliotecario.prestar(libro.getIsbn(),prestamo);
            fail();
            
        } catch (PrestamoException e) {
            // assert
            Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
        }
    }
}

