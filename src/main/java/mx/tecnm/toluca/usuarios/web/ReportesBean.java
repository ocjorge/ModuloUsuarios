package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.tecnm.toluca.usuarios.service.ReportesService;

@Named
@ViewScoped
public class ReportesBean implements Serializable {

    @Inject private ReportesService reportesService;
    @Inject private SessionManager sessionManager;

    private long totalAccesos;
    private long accesosExitosos;
    private long accesosFallidos;
    private long ticketsPendientes;

    // JSON strings para Chart.js
    private String accesosLabelsJson;
    private String accesosDataJson;

    private String ticketsLabelsJson;
    private String ticketsDataJson;

    @PostConstruct
    public void init() {
        totalAccesos = reportesService.totalAccesos();
        accesosExitosos = reportesService.accesosExitosos();
        accesosFallidos = reportesService.accesosFallidos();
        ticketsPendientes = reportesService.ticketsPendientes();

        buildAccesosChart();
        buildTicketsChart();
    }

    public void verificarSesion() throws IOException {
        sessionManager.redirigirSiNoAutenticado();
    }

    private void buildAccesosChart() {
        List<Object[]> rows = reportesService.accesosUltimos7Dias();
        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();

        for (Object[] r : rows) {
            labels.add(String.valueOf(r[0]));
            data.add((Number) r[1]);
        }

        accesosLabelsJson = toJsonArray(labels, true);
        accesosDataJson   = toJsonArray(data, false);
    }

    private void buildTicketsChart() {
        List<Object[]> rows = reportesService.ticketsPorEstado();
        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();

        for (Object[] r : rows) {
            labels.add(String.valueOf(r[0]));
            data.add((Number) r[1]);
        }

        ticketsLabelsJson = toJsonArray(labels, true);
        ticketsDataJson   = toJsonArray(data, false);
    }

    private String toJsonArray(List<?> list, boolean quoteStrings) {
        StringBuilder sb = new StringBuilder("[");
        for (int i=0; i<list.size(); i++) {
            Object v = list.get(i);
            if (i>0) sb.append(",");
            if (quoteStrings) sb.append("\"").append(String.valueOf(v)).append("\"");
            else sb.append(String.valueOf(v));
        }
        sb.append("]");
        return sb.toString();
    }

    // getters
    public long getTotalAccesos() { return totalAccesos; }
    public long getAccesosExitosos() { return accesosExitosos; }
    public long getAccesosFallidos() { return accesosFallidos; }
    public long getTicketsPendientes() { return ticketsPendientes; }

    public String getAccesosLabelsJson() { return accesosLabelsJson; }
    public String getAccesosDataJson() { return accesosDataJson; }
    public String getTicketsLabelsJson() { return ticketsLabelsJson; }
    public String getTicketsDataJson() { return ticketsDataJson; }

    public SessionManager getSessionManager() { return sessionManager; }
}
