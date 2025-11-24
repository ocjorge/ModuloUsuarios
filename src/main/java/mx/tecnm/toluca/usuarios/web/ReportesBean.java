package mx.tecnm.toluca.usuarios.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import mx.tecnm.toluca.usuarios.model.AuditoriaAccion;
import mx.tecnm.toluca.usuarios.model.AuditoriaAcceso;
import mx.tecnm.toluca.usuarios.service.ReportesService;

@Named
@ViewScoped
public class ReportesBean implements Serializable {

    @Inject
    private ReportesService reportesService;

    private List<AuditoriaAcceso> accesos;
    private List<AuditoriaAccion> acciones;

    @PostConstruct
    public void init() {
        try {
            accesos  = reportesService.listarAccesos();
            acciones = reportesService.listarAcciones();
        } catch (Exception ex) {
            accesos  = Collections.emptyList();
            acciones = Collections.emptyList();
            System.err.println("ERROR en ReportesBean.init(): " + ex.getMessage());
        }
    }

    // ----- Getters usados por el XHTML -----

    public List<AuditoriaAcceso> getAccesos() {
        return accesos;
    }

    public List<AuditoriaAccion> getAcciones() {
        return acciones;
    }

    // PROPIEDADES COMPUTADAS

    public int getTotalAccesos() {
        return accesos != null ? accesos.size() : 0;
    }

    public int getTotalAcciones() {
        return acciones != null ? acciones.size() : 0;
    }

    public long getAccesosExitosos() {
        return accesos.stream().filter(a -> a.getTipoEvento().getId() == 1).count();
    }

    public long getAccesosFallidos() {
        return accesos.stream().filter(a -> a.getTipoEvento().getId() == 3).count();
    }
}
