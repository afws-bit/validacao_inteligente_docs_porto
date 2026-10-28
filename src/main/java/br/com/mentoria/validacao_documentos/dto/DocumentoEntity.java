package br.com.mentoria.validacao_documentos.dto;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_validados")
public class DocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeExtraido;
    private String cpfExtraido;
    private boolean aprovado;
    private String status;
    private LocalDateTime dataProcessamento;

    // Construtor vazio exigido pelo JPA
    public DocumentoEntity() {}

    public DocumentoEntity(String nomeExtraido, String cpfExtraido, boolean aprovado, String status) {
        this.nomeExtraido = nomeExtraido;
        this.cpfExtraido = cpfExtraido;
        this.aprovado = aprovado;
        this.status = status;
        this.dataProcessamento = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    
    public String getNomeExtraido() { return nomeExtraido; }
    public void setNomeExtraido(String nomeExtraido) { this.nomeExtraido = nomeExtraido; }
    
    public String getCpfExtraido() { return cpfExtraido; }
    public void setCpfExtraido(String cpfExtraido) { this.cpfExtraido = cpfExtraido; }
    
    public boolean isAprovado() { return aprovado; }
    public void setAprovado(boolean aprovado) { this.aprovado = aprovado; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getDataProcessamento() { return dataProcessamento; }
    public void setDataProcessamento(LocalDateTime dataProcessamento) { this.dataProcessamento = dataProcessamento; }
}