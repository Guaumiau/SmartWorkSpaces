package pe.isil.smartworkspaces.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pe.isil.smartworkspaces.model.Usuario;

import java.util.Collection;
import java.util.Collections;

public class AppUserDetails implements UserDetails {

    private final Usuario usuario;

    public AppUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    // Este metodo devuelve el nombre real del usuario para mostrarlo en el HTML
    public String getNombre() {
        return usuario.getNombres();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // CAMBIO: Convertimos el Rol (ej. ADMIN) a "ROLE_ADMIN" para Spring Security
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    // Métodos de estado de cuenta (siempre true para simplificar)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}