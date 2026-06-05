ackage com.autoorion.repository;

import com.autoorion.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE CAST(u.perfil AS string) = :perfil AND CAST(u.status AS string) = 'ativo'")
    List<Usuario> findByPerfilAtivo(@Param("perfil") String perfil);

    /**
     * Busca paginada com filtros opcionais.
     *
     * Nota: comparaÃ§Ãµes de enum no JPQL sÃ£o feitas via CAST para string
     * para evitar problemas de tipo no H2 e outros bancos.
     */
    @Query(value = """
        SELECT * FROM usuarios u
        WHERE (
            CAST(:busca AS text) IS NULL
            OR u.nome  ILIKE '%' || CAST(:busca AS text) || '%'
            OR u.email ILIKE '%' || CAST(:busca AS text) || '%'
            OR u.cargo ILIKE '%' || CAST(:busca AS text) || '%'
        )
        AND ( CAST(:perfil AS text) IS NULL OR u.perfil::text = CAST(:perfil AS text) )
        AND ( CAST(:status AS text) IS NULL OR u.status::text = CAST(:status AS text) )
        ORDER BY u.nome
        """,
        countQuery = """
        SELECT COUNT(*) FROM usuarios u
        WHERE (
            CAST(:busca AS text) IS NULL
            OR u.nome  ILIKE '%' || CAST(:busca AS text) || '%'
            OR u.email ILIKE '%' || CAST(:busca AS text) || '%'
            OR u.cargo ILIKE '%' || CAST(:busca AS text) || '%'
        )
        AND ( CAST(:perfil AS text) IS NULL OR u.perfil::text = CAST(:perfil AS text) )
        AND ( CAST(:status AS text) IS NULL OR u.status::text = CAST(:status AS text) )
        """,
        nativeQuery = true)
    Page<Usuario> findFiltered(
        @Param("busca")  String busca,
        @Param("perfil") String perfil,
        @Param("status") String status,
        Pageable pageable
    );
}
