package com.autoorion.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Armazena mídias (fotos e documentos) como base64 no banco de dados.
 * Usado para fotos do veículo, documentos anexados e foto para redes sociais.
 */
@Entity
@Table(name = "fotos",
    indexes = {
        @Index(name = "idx_foto_entidade", columnList = "entidade_tipo, entidade_id"),
        @Index(name = "idx_foto_tipo",     columnList = "entidade_id, tipo"),
    })
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(callSuper = false)
public class Foto extends Auditable {

    public enum TipoMidia {
        FOTO, DOCUMENTO, FOTO_REDE_SOCIAL
    }

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Conteúdo em base64 (ex: data:image/jpeg;base64,... ou data:application/pdf;base64,...) */
    @Column(name = "dados_base64", nullable = false, columnDefinition = "CLOB")
    private String dadosBase64;

    @Column(name = "mime_type", length = 100)
    @Builder.Default
    private String mimeType = "image/jpeg";

    @Column(name = "nome_arquivo", length = 255)
    private String nomeArquivo;

    @Column(name = "tamanho_bytes")
    private Long tamanhoBytes;

    /** Tipo da entidade dona desta mídia: 'veiculo', 'usuario', etc. */
    @Column(name = "entidade_tipo", length = 50)
    private String entidadeTipo;

    /** ID da entidade dona desta mídia */
    @Column(name = "entidade_id", length = 36)
    private String entidadeId;

    /** Categoria da mídia: FOTO, DOCUMENTO, FOTO_REDE_SOCIAL */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private TipoMidia tipo = TipoMidia.FOTO;

    @Column(length = 500)
    private String descricao;

    /** Ângulo/categoria da foto (ex: 'Frente', 'Motor') ou tipo do documento (ex: 'CRLV') */
    @Column(length = 100)
    private String etapa;

    /** Ordem de exibição dentro do mesmo tipo */
    @Column
    @Builder.Default
    private Integer ordem = 0;
}
