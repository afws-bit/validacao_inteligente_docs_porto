package br.com.mentoria.validacao_documentos.controller;

import br.com.mentoria.validacao_documentos.dto.ResultadoValidacao;
import br.com.mentoria.validacao_documentos.service.ValidadorDocumentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @Autowired
    private ValidadorDocumentoService validadorService;

    @PostMapping("/validar")
    public ResponseEntity<ResultadoValidacao> validarDocumento(@RequestBody Map<String, String> payload) {
        // Pega os dados enviados no corpo da requisição (JSON)
        String imagemBase64 = payload.get("imagemBase64");
        String conteudoDocumento = payload.get("conteudoDocumento");

        // Chama o serviço que criamos para processar e validar
        ResultadoValidacao resultado = validadorService.processarValidacao(imagemBase64, conteudoDocumento);
        
        // Retorna o resultado com o status HTTP 200 (OK)
        return ResponseEntity.ok(resultado);
    }
}