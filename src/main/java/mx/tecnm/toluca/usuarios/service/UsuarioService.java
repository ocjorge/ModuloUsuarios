package mx.tecnm.toluca.usuarios.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import mx.tecnm.toluca.usuarios.modelo.UsuarioInfo;

@Stateless // Esto convierte la clase en un EJB (Enterprise Java Bean)
public class UsuarioService {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    public List<UsuarioInfo> listarUsuarios() {
        // Hacemos una consulta JPQL (parecido a SQL pero con clases)
        return em.createQuery("SELECT u FROM UsuarioInfo u", UsuarioInfo.class).getResultList();
    }

    public UsuarioInfo login(String user, String password) {
        try {
            String sql = "SELECT id_usuario FROM usuarios WHERE username = ?1 AND contrasena = ?2";

            Object resultado = em.createNativeQuery(sql)
                    .setParameter(1, user) // Pasamos el usuario
                    .setParameter(2, password) // Pasamos la contraseña
                    .getSingleResult();

            if (resultado != null) {
                java.util.UUID id = (java.util.UUID) resultado;
                return em.find(UsuarioInfo.class, id);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
