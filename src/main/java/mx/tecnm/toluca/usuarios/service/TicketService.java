package mx.tecnm.toluca.usuarios.service;

// Importación para definir el alcance del bean a nivel aplicación
import jakarta.enterprise.context.ApplicationScoped;

// Importaciones para JPA
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

// Importación para manejo de transacciones
import jakarta.transaction.Transactional;

// Importaciones para colecciones y UUID
import java.util.List;
import java.util.UUID;

// Importación de entidades del modelo
import mx.tecnm.toluca.usuarios.model.*;

/**
 * Servicio encargado de la gestión de tickets de revisión.
 * Incluye operaciones de consulta (listar, filtrar, buscar)
 * y actualización de estado.
 */
@ApplicationScoped
public class TicketService {

    /**
     * EntityManager inyectado mediante PersistenceContext.
     * Se utiliza para ejecutar consultas JPQL y operaciones CRUD.
     */
    @PersistenceContext(unitName="UsuariosPU")
    private EntityManager em;

    /**
     * Lista todos los tickets de revisión.
     * Se usan JOIN FETCH para cargar relaciones y evitar problemas
     * de LazyInitialization al usar los datos en la vista.
     *
     * @return lista completa de tickets ordenada por fecha (descendente)
     */
    public List<TicketRevision> listarTodos() {
        return em.createQuery(
            "SELECT t FROM TicketRevision t " +
            "JOIN FETCH t.usuario " +
            "JOIN FETCH t.modulo " +
            "JOIN FETCH t.tipoCambio " +
            "JOIN FETCH t.estado " +
            "ORDER BY t.fechaSolicitud DESC", TicketRevision.class)
            .getResultList();
    }

    /**
     * Lista tickets aplicando filtros opcionales:
     * - módulo
     * - estado
     * - tipo de cambio
     *
     * Se construye dinámicamente la consulta JPQL agregando condiciones
     * solo cuando el parámetro correspondiente no es nulo/vacío.
     *
     * @param moduloId     id del módulo (String) para filtrar, opcional
     * @param estadoId     id del estado (Integer) para filtrar, opcional
     * @param tipoCambioId id del tipo de cambio (Integer) para filtrar, opcional
     * @return lista de tickets filtrados y ordenados por fecha (descendente)
     */
    public List<TicketRevision> listarFiltrados(String moduloId, Integer estadoId, Integer tipoCambioId) {

        // Base de la consulta con fetch de relaciones
        String jpql =
            "SELECT t FROM TicketRevision t " +
            "JOIN FETCH t.usuario " +
            "JOIN FETCH t.modulo " +
            "JOIN FETCH t.tipoCambio " +
            "JOIN FETCH t.estado " +
            "WHERE 1=1 ";

        // Agrega condición por módulo solo si moduloId es válido
        if (moduloId != null && !moduloId.isBlank()) jpql += " AND t.modulo.id = :moduloId ";

        // Agrega condición por estado solo si estadoId no es nulo
        if (estadoId != null) jpql += " AND t.estado.id = :estadoId ";

        // Agrega condición por tipo de cambio solo si tipoCambioId no es nulo
        if (tipoCambioId != null) jpql += " AND t.tipoCambio.id = :tipoCambioId ";

        // Crea el query final con ordenamiento
        var q = em.createQuery(jpql + " ORDER BY t.fechaSolicitud DESC", TicketRevision.class);

        // Asigna parámetros solo cuando fueron agregados a la consulta
        if (moduloId != null && !moduloId.isBlank()) q.setParameter("moduloId", moduloId);
        if (estadoId != null) q.setParameter("estadoId", estadoId);
        if (tipoCambioId != null) q.setParameter("tipoCambioId", tipoCambioId);

        // Ejecuta la consulta y retorna resultados
        return q.getResultList();
    }

    /**
     * Busca un ticket por su identificador.
     *
     * @param id UUID del ticket
     * @return ticket encontrado o null si no existe
     */
    public TicketRevision buscarPorId(UUID id) {
        return em.find(TicketRevision.class, id);
    }

    /**
     * Lista todos los módulos existentes.
     * Ordena por nombre del módulo.
     *
     * @return lista de módulos
     */
    public List<Modulo> listarModulos() {
        return em.createQuery("SELECT m FROM Modulo m ORDER BY m.nombre", Modulo.class)
                 .getResultList();
    }

    /**
     * Lista todos los estados posibles de un ticket.
     * Ordena por nombre del estado.
     *
     * @return lista de estados de ticket
     */
    public List<EstadoTicket> listarEstadosTicket() {
        return em.createQuery("SELECT e FROM EstadoTicket e ORDER BY e.nombre", EstadoTicket.class)
                 .getResultList();
    }

    /**
     * Lista todos los tipos de cambio disponibles.
     * Ordena por nombre del tipo de cambio.
     *
     * @return lista de tipos de cambio
     */
    public List<TipoCambio> listarTiposCambio() {
        return em.createQuery("SELECT tc FROM TipoCambio tc ORDER BY tc.nombre", TipoCambio.class)
                 .getResultList();
    }

    /**
     * Cambia el estado de un ticket específico.
     * Se marca como @Transactional para que el cambio se realice
     * dentro de una transacción.
     *
     * Si el ticket no existe, simplemente termina sin hacer nada.
     *
     * @param idTicket      UUID del ticket a modificar
     * @param nuevoEstadoId id del nuevo estado
     */
    @Transactional
    public void cambiarEstado(UUID idTicket, Integer nuevoEstadoId) {

        // Busca el ticket en la base de datos
        TicketRevision t = em.find(TicketRevision.class, idTicket);

        // Si no existe, no se realiza ninguna acción
        if (t == null) return;

        // Busca el estado destino
        EstadoTicket estado = em.find(EstadoTicket.class, nuevoEstadoId);

        // Actualiza el estado del ticket
        t.setEstado(estado);

        // Realiza merge para sincronizar cambios con la base
        em.merge(t);
    }

    /**
     * Cuenta los tickets pendientes.
     * Se asume que el estado con id = 1 corresponde a "Pendiente".
     *
     * @return número de tickets pendientes
     */
    public long contarPendientes() {
        return em.createQuery(
            "SELECT COUNT(t) FROM TicketRevision t WHERE t.estado.id = 1", Long.class)
            .getSingleResult();
    }
}
