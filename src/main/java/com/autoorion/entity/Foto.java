package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Armazena fotos como base64 no banco de dados.
 * NÃ£o Ã© o ideal para produÃ§Ã£o em escala (use S3/MinIO para isso),
 * mas Ã© funcional e simples para demonstraÃ§Ã£o.
 */
@Entity
@Table(name = "fotos",
    indexes = {
        @Index(name = "idx_foto_entidade", columnList = "entidade_tipo, entidade_id"),
    })
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Foto extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Imagem em formato base64 (ex: data:image/jpeg;base64,...) */
    @Column(name = "dados_base64", nullable = false, columnDefinition = "CLOB")
    private String dadosBase64;

    @Column(name = "mime_type", length = 50)
    private String mimeType = "image/jpeg";

    @Column(name = "nome_arquivo", length = 200)
    private String nomeArquivo;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    /** Tipo da entidade dona desta foto: 'usuario', 'veiculo', etc. */
    @Column(name = "entidade_tipo", length = 50)
    private String entidadeTipo;

    /** ID da entidade dona desta foto */
    @Column(name = "entidade_id", length = 36)
    private String entidadeId;

    @Column(length = 500)
    private String descricao;

    /** Etapa do processo (ex: 'Frente', 'Motor', 'Acabamento') */
    @Column(length = 100)
    private String etapa;

    @Column(name = "criado_por", length = 36)
    private String criadoPor;
}
