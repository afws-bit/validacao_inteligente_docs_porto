package br.com.mentoria.validacao_documentos.service;

import br.com.mentoria.validacao_documentos.dto.DocumentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Long> {
    // O Spring cria o SQL de INSERT, SELECT, etc. automaticamente!
}