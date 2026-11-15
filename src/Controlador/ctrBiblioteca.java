package Controlador;

import Modelo.consultaLibro;
import Modelo.item;
import Modelo.libro;
import Vista.gestionLibros;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ctrBiblioteca {

    private libro lib;
    private consultaLibro consultas;
    private gestionLibros vGl;

    public ctrBiblioteca(gestionLibros vG, libro li, consultaLibro cons) {
        this.vGl = vG;
        this.lib = li;
        this.consultas = cons;

        this.eventos();
        this.iniciar();
    }

    private void eventos() {
        this.vGl.getBtnGuardar().addActionListener(e -> guardar());
        this.vGl.getBtnBuscar().addActionListener(e -> Buscar());
        this.vGl.getBtnEliminar().addActionListener(e -> Eliminar());
        this.vGl.getBtnModificar().addActionListener(e -> Modificar());
        this.vGl.getBtnLimpiar().addActionListener(e -> limpiar());
        this.vGl.getBtnFoto().addActionListener(e -> elegirFoto());
        this.vGl.getTblListaProducto().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                eleccion();
            }
        });
    }

    private void iniciar() {
        this.vGl.setTitle("Gestión de Libros");
        this.vGl.setLocationRelativeTo(null);
        this.vGl.setVisible(true);

        this.consultas.llenarCombo(this.vGl.getCbAutor(), "autor", "idAutor");
        this.consultas.llenarCombo(this.vGl.getCbEditorial(), "editorial", "idEditorial");
        this.consultas.llenarCombo(this.vGl.getCbGenero(), "genero", "idGenero");

        listar();
    }

    // Pasa los datos de la vista al modelo. Devuelve false si falta algo.
    private boolean leerFormulario() {
        String isbn = this.vGl.getTxtISBN().getText().trim();
        String titulo = this.vGl.getTxtNombre().getText().trim();

        if (isbn.isEmpty() || titulo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "ISBN y Título son obligatorios");
            return false;
        }
        if (this.vGl.getCbAutor().getSelectedItem() == null
                || this.vGl.getCbEditorial().getSelectedItem() == null
                || this.vGl.getCbGenero().getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Selecciona autor, editorial y género");
            return false;
        }

        this.lib.setIsbn(isbn);
        this.lib.setTitulo(titulo);
        this.lib.setIdAutor(((item) this.vGl.getCbAutor().getSelectedItem()).getId());
        this.lib.setIdEditorial(((item) this.vGl.getCbEditorial().getSelectedItem()).getId());
        this.lib.setIdGenero(((item) this.vGl.getCbGenero().getSelectedItem()).getId());
        // 0 = Disponible -> 1, 1 = No hay -> 2
        this.lib.setEstatus(this.vGl.getCbEstatus().getSelectedIndex() == 0 ? 1 : 2);
        // la foto ya está en lib.getFoto() si se eligió
        return true;
    }

    private void guardar() {
        if (!leerFormulario()) {
        return;
    }
    if (this.consultas.existeIsbn(this.lib.getIsbn(), 0)) {
        JOptionPane.showMessageDialog(null, "Ya existe un libro con ese ISBN");
        return;
    }
        if (this.consultas.insertar(this.lib)) {
            JOptionPane.showMessageDialog(null, "Libro guardado correctamente");
            nuevoLibro();
            listar();
        } else {
            JOptionPane.showMessageDialog(null, "Error al guardar el libro");
        }
    }

    private void Buscar() {
        String isbn = this.vGl.getTxtISBN().getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingresa un ISBN");
            return;
        }

        this.lib.setIsbn(isbn);

        if (this.consultas.Buscar(this.lib)) {
            this.vGl.getCbEstatus().setSelectedIndex(this.lib.getEstatus() == 1 ? 0 : 1);
            this.vGl.getTxtNombre().setText(this.lib.getTitulo());
            seleccionarItem(this.vGl.getCbAutor(), this.lib.getIdAutor());
            seleccionarItem(this.vGl.getCbEditorial(), this.lib.getIdEditorial());
            seleccionarItem(this.vGl.getCbGenero(), this.lib.getIdGenero());
            mostrarFoto(this.lib.getFoto());
        } else {
            JOptionPane.showMessageDialog(null, "Libro no encontrado");
            nuevoLibro();
        }
    }

    private void Modificar() {
        if (this.lib.getIdLibro() == 0) {
            JOptionPane.showMessageDialog(null, "Primero busca o selecciona un libro");
            return;
        }
        if (!leerFormulario()) {
            return;
        }
        if (this.consultas.existeIsbn(this.lib.getIsbn(), this.lib.getIdLibro())) {
            JOptionPane.showMessageDialog(null, "Otro libro ya usa ese ISBN");
            return;
        }
        if (this.consultas.Modificar(this.lib)) {
            JOptionPane.showMessageDialog(null, "Modificación exitosa");
            nuevoLibro();
            listar();
        
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el libro");
        }
    }

    private void Eliminar() {
        if (this.lib.getIdLibro() == 0) {
            JOptionPane.showMessageDialog(null, "Error, no hay nada que eliminar");
            return;
        }
        if (this.consultas.eliminar(this.lib)) {
            JOptionPane.showMessageDialog(null, "Libro eliminado correctamente");
            nuevoLibro();
            listar();
        } else {
            JOptionPane.showMessageDialog(null, "No se encontró el libro");
        }
    }

    private void limpiar() {
        nuevoLibro();
    }

    private void listar() {
        DefaultTableModel modelo = (DefaultTableModel) this.vGl.getTblListaProducto().getModel();
        this.consultas.listar(modelo);
    }

    // Al hacer clic en una fila, busca el libro por su ISBN (columna 5)
    private void eleccion() {
        int fila = this.vGl.getTblListaProducto().getSelectedRow();
        if (fila >= 0) {
            this.vGl.setTxtISBN(this.vGl.getTblListaProducto().getValueAt(fila, 5).toString());
            Buscar();
        }
    }

    // ---------- Foto ----------

    private void elegirFoto() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this.vGl) == JFileChooser.APPROVE_OPTION) {
            try {
                byte[] bytes = Files.readAllBytes(fc.getSelectedFile().toPath());
                this.lib.setFoto(Base64.getEncoder().encodeToString(bytes));
                mostrarFoto(this.lib.getFoto());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "No se pudo leer la imagen");
            }
        }
    }

    private void mostrarFoto(String base64) {
        if (base64 == null || base64.isEmpty()) {
            this.vGl.getLblFoto().setIcon(null);
            return;
        }
        byte[] bytes = Base64.getDecoder().decode(base64);
        ImageIcon icono = new ImageIcon(bytes);
        Image img = icono.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
        this.vGl.getLblFoto().setIcon(new ImageIcon(img));
    }

    // ---------- Auxiliares ----------

    private void seleccionarItem(JComboBox<item> combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            if (combo.getItemAt(i).getId() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    // Limpia la vista y reinicia el objeto libro (para que no queden datos viejos)
    private void nuevoLibro() {
        this.lib.setIdLibro(0);
        this.lib.setFoto(null);
        this.vGl.limpiarCampos();
    }
}