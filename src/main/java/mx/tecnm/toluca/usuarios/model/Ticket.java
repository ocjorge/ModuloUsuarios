package mx.tecnm.toluca.usuarios.model;

import java.io.Serializable;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String tipo;
    private String modulo;
    private String fecha;
    private String estado;
    private String usuarioSolicitante;
    private String detalles;

    public Ticket() {}

    public Ticket(String id, String tipo, String modulo, String fecha, String estado, 
                  String usuarioSolicitante, String... detalles) {
        this.id = id;
        this.tipo = tipo;
        this.modulo = modulo;
        this.fecha = fecha;
        this.estado = estado;
        this.usuarioSolicitante = usuarioSolicitante;
        // Construir detalles como string
        this.detalles = String.join(", ", detalles);
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getUsuarioSolicitante() { return usuarioSolicitante; }
    public void setUsuarioSolicitante(String usuarioSolicitante) { this.usuarioSolicitante = usuarioSolicitante; }
    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }
}