package mx.tecnm.toluca.usuarios.web;

// Importación para ejecutar lógica después de la construcción del bean
import jakarta.annotation.PostConstruct;

// Alcance del bean a nivel de vista (se mantiene mientras la vista esté activa)
import jakarta.faces.view.ViewScoped;

// Inyección de dependencias
import jakarta.inject.Inject;
import jakarta.inject.Named;

// Importaciones para manejo de E/S y serialización
import java.io.IOException;
import java.io.Serializable;

// Importaciones para colecciones
import java.util.ArrayList;
import java.util.List;

// Importación del servicio de reportes
import mx.tecnm.toluca.usuarios.service.ReportesService;

/**
 * Bean de respaldo para la vista de reportes.
 * Se encarga de obtener métricas del sistema y
 * preparar los datos en formato JSON para Chart.js.
 */
@Named
@ViewScoped
public class ReportesBean implements Serializable {

    /**
     * Servicio de reportes.
     * Proporciona los datos estadísticos desde la base de datos.
     */
    @Inject 
    private ReportesService reportesService;

    /**
     * Administrador de sesión.
     * Se utiliza para verificar si el usuario está autenticado.
     */
    @Inject 
    private SessionManager sessionManager;

    /**
     * Métricas generales del sistema.
     */
    private long totalAccesos;
    private long accesosExitosos;
    private long accesosFallidos;
    private long ticketsPendientes;

    /**
     * Cadenas JSON utilizadas por Chart.js
     * para la gráfica de accesos.
     */
    private String accesosLabelsJson;
    private String accesosDataJson;

    /**
     * Cadenas JSON utilizadas por Chart.js
     * para la gráfica de tickets por estado.
     */
    private String ticketsLabelsJson;
    private String ticketsDataJson;

    /**
     * Método ejecutado automáticamente después de crear el bean.
     * Inicializa las métricas y construye los datos de las gráficas.
     */
    @PostConstruct
    public void init() {

        // Obtiene métricas generales
        totalAccesos = reportesService.totalAccesos();
        accesosExitosos = reportesService.accesosExitosos();
        accesosFallidos = reportesService.accesosFallidos();
        ticketsPendientes = reportesService.ticketsPendientes();

        // Construye los datos para las gráficas
        buildAccesosChart();
        buildTicketsChart();
    }

    /**
     * Verifica si existe una sesión activa.
     * Si no está autenticado, redirige al login.
     *
     * @throws IOException en caso de error de redirección
     */
    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    /**
     * Construye los datos de la gráfica de accesos
     * correspondientes a los últimos 7 días.
     */
    private void buildAccesosChart() {

        // Obtiene los registros de accesos por día
        List<Object[]> rows = reportesService.accesosUltimos7Dias();

        // Listas auxiliares para etiquetas y valores
        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();

        // Recorre los resultados y separa etiquetas y datos
        for (Object[] r : rows) {
            labels.add(String.valueOf(r[0])); // fecha
            data.add((Number) r[1]);          // total de accesos
        }

        // Convierte las listas a formato JSON
        accesosLabelsJson = toJsonArray(labels, true);
        accesosDataJson   = toJsonArray(data, false);
    }

    /**
     * Construye los datos de la gráfica de tickets agrupados por estado.
     */
    private void buildTicketsChart() {

        // Obtiene los registros de tickets por estado
        List<Object[]> rows = reportesService.ticketsPorEstado();

        // Listas auxiliares para etiquetas y valores
        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();

        // Recorre los resultados y separa etiquetas y datos
        for (Object[] r : rows) {
            labels.add(String.valueOf(r[0])); // nombre del estado
            data.add((Number) r[1]);          // total de tickets
        }

        // Convierte las listas a formato JSON
        ticketsLabelsJson = toJsonArray(labels, true);
        ticketsDataJson   = toJsonArray(data, false);
    }

    /**
     * Convierte una lista genérica en un arreglo JSON.
     *
     * @param list lista de valores
     * @param quoteStrings indica si los valores deben ir entre comillas
     * @return representación JSON en String
     */
    private String toJsonArray(List<?> list, boolean quoteStrings) {

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < list.size(); i++) {
            Object v = list.get(i);

            // Agrega separador entre elementos
            if (i > 0) sb.append(",");

            // Agrega comillas si es una lista de strings
            if (quoteStrings) {
                sb.append("\"").append(String.valueOf(v)).append("\"");
            } else {
                sb.append(String.valueOf(v));
            }
        }

        sb.append("]");
        return sb.toString();
    }

    // ---------------- GETTERS ----------------

    /**
     * @return total de accesos
     */
    public long getTotalAccesos() { 
        return totalAccesos; 
    }

    /**
     * @return total de accesos exitosos
     */
    public long getAccesosExitosos() { 
        return accesosExitosos; 
    }

    /**
     * @return total de accesos fallidos
     */
    public long getAccesosFallidos() { 
        return accesosFallidos; 
    }

    /**
     * @return total de tickets pendientes
     */
    public long getTicketsPendientes() { 
        return ticketsPendientes; 
    }

    /**
     * @return JSON de etiquetas de accesos
     */
    public String getAccesosLabelsJson() { 
        return accesosLabelsJson; 
    }

    /**
     * @return JSON de datos de accesos
     */
    public String getAccesosDataJson() { 
        return accesosDataJson; 
    }

    /**
     * @return JSON de etiquetas de tickets
     */
    public String getTicketsLabelsJson() { 
        return ticketsLabelsJson; 
    }

    /**
     * @return JSON de datos de tickets
     */
    public String getTicketsDataJson() { 
        return ticketsDataJson; 
    }

    /**
     * @return SessionManager actual
     */
    public SessionManager getSessionManager() { 
        return sessionManager; 
    }
}
