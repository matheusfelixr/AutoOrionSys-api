ackage com.autoorion.service;

import com.autoorion.entity.Usuario;
import com.autoorion.exception.BusinessException;
import com.autoorion.exception.ResourceNotFoundException;
import com.autoorion.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final NotificacaoService notificacaoService;

    public Page<Usuario> findAll(String busca, String perfil, String status,
                                  int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return repository.findFiltered(
            busca.isBlank() ? null : busca,
            perfil.isBlank() ? null : perfil,
            status.isBlank() ? null : status,
            pageable
        );
    }

    public Usuario findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuÃ¡rio", id));
    }

    public Usuario findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("UsuÃ¡rio por email", email));
    }

    public Usuario create(Map<String, Object> body) {
        String email = (String) body.get("email");
        if (email != null && repository.existsByEmail(email)) {
            throw new BusinessException("JÃ¡ existe um usuÃ¡rio com este e-mail.", org.springframework.http.HttpStatus.CONFLICT, "CONFLICT");
        }
        var usuario = Usuario.builder()
                .nome((String) body.get("nome"))
                .email(email)
                .senha(passwordEncoder.encode(body.getOrDefault("senha", "autoorion123").toString()))
                .cargo((String) body.get("cargo"))
                .perfil(Usuario.PerfilUsuario.valueOf((String) body.getOrDefault("perfil", "visualizador")))
                .status(Usuario.StatusUsuario.valueOf((String) body.getOrDefault("status", "ativo")))
                .telefone((String) body.get("telefone"))
                .dataCadastro(LocalDateTime.now())
                .build();
        var saved = repository.save(usuario);
        log.info("[UsuarioService] Criado: email={}", email);
        // Notifica o prÃ³prio usuÃ¡rio criado
        notificacaoService.enviar(
            saved.getId(),
            "Bem-vindo ao autoorion!",
            "Sua conta foi criada com sucesso. Bem-vindo, " + saved.getNome() + "!",
            "success"
        );
        return saved;
    }

    public Usuario update(String id, Map<String, Object> body) {
        var usuario = findById(id);
        String novoEmail = (String) body.get("email");
        if (novoEmail != null && !novoEmail.equals(usuario.getEmail())
                && repository.existsByEmail(novoEmail)) {
            throw new BusinessException("E-mail jÃ¡ cadastrado por outro usuÃ¡rio.", org.springframework.http.HttpStatus.CONFLICT, "CONFLICT");
        }
        if (body.containsKey("nome"))     usuario.setNome((String) body.get("nome"));
        if (body.containsKey("email"))    usuario.setEmail(novoEmail);
        if (body.containsKey("cargo"))    usuario.setCargo((String) body.get("cargo"));
        if (body.containsKey("telefone"))  usuario.setTelefone((String) body.get("telefone"));
        if (body.containsKey("avatarUrl")) usuario.setAvatarUrl((String) body.get("avatarUrl"));
        if (body.containsKey("perfil"))   usuario.setPerfil(Usuario.PerfilUsuario.valueOf((String) body.get("perfil")));
        if (body.containsKey("status"))   usuario.setStatus(Usuario.StatusUsuario.valueOf((String) body.get("status")));
        log.info("[UsuarioService] Atualizado: id={}", id);
        var updated = repository.save(usuario);
        notificacaoService.enviar(
            updated.getId(),
            "Perfil atualizado",
            "Seus dados foram atualizados pelo administrador.",
            "info"
        );
        return updated;
    }

    public void delete(String id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("UsuÃ¡rio", id);
        repository.deleteById(id);
        log.info("[UsuarioService] ExcluÃ­do: id={}", id);
    }

    public void updateUltimoAcesso(String id) {
        repository.findById(id).ifPresent(u -> {
            u.setUltimoAcesso(LocalDateTime.now());
            repository.save(u);
        });
    }
}
