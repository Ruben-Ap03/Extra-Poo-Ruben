package biblioteca_14;

import Controlador.ctrBiblioteca;
import Modelo.consultaLibro;
import Modelo.libro;
import Vista.gestionLibros;

/**
 *
 * @author CC_Laboratorio
 */
public class Biblioteca_14 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        gestionLibros vGl = new gestionLibros();
        libro lib = new libro();
        consultaLibro consultas = new consultaLibro();
        ctrBiblioteca biblioteca = new ctrBiblioteca(vGl, lib, consultas);
    }
    
}
