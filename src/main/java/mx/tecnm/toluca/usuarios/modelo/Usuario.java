package mx.tecnm.toluca.usuarios.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "id_usuario", columnDefinition = "UUID DEFAULT uuid_generate_v4()")
    private UUID idUsuario;

    @NotNull
    @Column(name = "id_control", unique = true)
    private Integer idControl; // SERIAL UNIQUE en la BD, se mapea como Integer

    @NotNull
    @Email
    @Size(min = 1, max = 100)
    @Column(name = "correo_electronico", unique = true)
    private String correoElectronico;

    @NotNull
    @Size(min = 6, max = 100) // Se almacenará el hash aquí
    @Column(name = "contrasena")
    private String contrasena;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre_completo")
    private String nombreCompleto;

    @Size(max = 20)
    @Column(name = "telefono")
    private String telefono;

    @Column(name = "direccion", columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "ultima_sesion", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime ultimaSesion;

    @NotNull
    @Column(name = "fecha_creacion", columnDefinition = "TIMESTAMPTZ DEFAULT NOW()")
    private OffsetDateTime fechaCreacion;

    @NotNull
    @JoinColumn(name = "id_tipo_usuario", referencedColumnName = "id_tipo_usuario")
    @ManyToOne(optional = false)
    private TipoUsuario idTipoUsuario;

    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol")
    @ManyToOne
    private RolInterno idRol;

    @NotNull
    @JoinColumn(name = "id_estado_cuenta", referencedColumnName = "id_estado_cuenta")
    @ManyToOne(optional = false)
    private EstadoCuenta idEstadoCuenta;

    @NotNull
    @Size(min = 3, max = 20)
    @Column(name = "username", unique = true, length = 20)
    private String username;

    @JoinColumn(name = "id_modulo", referencedColumnName = "id_modulo")
    @ManyToOne
    private Modulo idModulo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<AuditoriaAcceso> auditoriaAccesoList;

    @OneToMany(mappedBy = "idUsuario")
    private List<AuditoriaAccion> auditoriaAccionList;

    @OneToMany(mappedBy = "idUsuario")
    private List<RestriccionHorario> restriccionesHorarioList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuario")
    private List<TicketRevision> ticketsRevisionList;

    public Usuario() {
        this.fechaCreacion = OffsetDateTime.now();
    }

    // Getters y Setters
    public UUID getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdControl() {
        return idControl;
    }

    public void setIdControl(Integer idControl) {
        this.idControl = idControl;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public OffsetDateTime getUltimaSesion() {
        return ultimaSesion;
    }

    public void setUltimaSesion(OffsetDateTime ultimaSesion) {
        this.ultimaSesion = ultimaSesion;
    }

    public OffsetDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(OffsetDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public TipoUsuario getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(TipoUsuario idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public RolInterno getIdRol() {
        return idRol;
    }

    public void setIdRol(RolInterno idRol) {
        this.idRol = idRol;
    }

    public EstadoCuenta getIdEstadoCuenta() {
        return idEstadoCuenta;
    }

    public void setIdEstadoCuenta(EstadoCuenta idEstadoCuenta) {
        this.idEstadoCuenta = idEstadoCuenta;
    }

    public Modulo getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Modulo idModulo) {
        this.idModulo = idModulo;
    }

    public List<AuditoriaAcceso> getAuditoriaAccesoList() {
        return auditoriaAccesoList;
    }

    public void setAuditoriaAccesoList(List<AuditoriaAcceso> auditoriaAccesoList) {
        this.auditoriaAccesoList = auditoriaAccesoList;
    }

    public List<AuditoriaAccion> getAuditoriaAccionList() {
        return auditoriaAccionList;
    }

    public void setAuditoriaAccionList(List<AuditoriaAccion> auditoriaAccionList) {
        this.auditoriaAccionList = auditoriaAccionList;
    }

    public List<RestriccionHorario> getRestriccionesHorarioList() {
        return restriccionesHorarioList;
    }

    public void setRestriccionesHorarioList(List<RestriccionHorario> restriccionesHorarioList) {
        this.restriccionesHorarioList = restriccionesHorarioList;
    }

    public List<TicketRevision> getTicketsRevisionList() {
        return ticketsRevisionList;
    }

    public void setTicketsRevisionList(List<TicketRevision> ticketsRevisionList) {
        this.ticketsRevisionList = ticketsRevisionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Usuario[id=" + idUsuario + ", email=" + correoElectronico + "]";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
