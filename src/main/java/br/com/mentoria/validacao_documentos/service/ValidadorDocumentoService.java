package br.com.mentoria.validacao_documentos.service;

import br.com.mentoria.validacao_documentos.dto.DadosDocumento;
import br.com.mentoria.validacao_documentos.dto.ResultadoValidacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.mentoria.validacao_documentos.dto.DocumentoEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidadorDocumentoService {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private IaService iaService;

    public ResultadoValidacao processarValidacao(String imagemBase64, String conteudoDocumento) {
        String textoParaAnalisar;

        if (imagemBase64 != null && !imagemBase64.isEmpty()) {
            textoParaAnalisar = ocrService.extrairTexto(imagemBase64);
        } else {
            textoParaAnalisar = conteudoDocumento;
        }

        if (textoParaAnalisar == null || textoParaAnalisar.isBlank()) {
            throw new RuntimeException("Nenhum texto ou imagem válida foi fornecida.");
        }

        DadosDocumento dados = iaService.extrairDadosEstruturados(textoParaAnalisar);
        return aplicarRegras(dados);
    }

    private ResultadoValidacao aplicarRegras(DadosDocumento dados) {
        List<String> regrasAtendidas = new ArrayList<>();
        List<String> pendencias = new ArrayList<>();

        verificarCampo(dados.nome(), "Nome identificado", "Nome não encontrado", regrasAtendidas, pendencias);
        verificarCampo(dados.cpf(), "CPF encontrado", "CPF não encontrado", regrasAtendidas, pendencias);
        verificarCampo(dados.endereco(), "Endereço encontrado", "Endereço não encontrado", regrasAtendidas, pendencias);
        verificarCampo(dados.cidade(), "Cidade encontrada", "Cidade não encontrada", regrasAtendidas, pendencias);

        if (dados.dataEmissao() == null || dados.dataEmissao().isBlank()) {
            pendencias.add("Data de emissão não encontrada");
        } else {
            LocalDate dataEmissao = LocalDate.parse(dados.dataEmissao());
            LocalDate limite = LocalDate.now().minusDays(90);
            
            if (dataEmissao.isBefore(limite)) {
                pendencias.add("Emissão superior a 90 dias");
            } else {
                regrasAtendidas.add("Emissão dentro de 90 dias");
            }
        }

        boolean aprovado = pendencias.isEmpty();
        String statusFinal = aprovado ? "APROVADO" : "REPROVADO"; 

        DocumentoEntity entity = new DocumentoEntity(
            dados.nome(),
            dados.cpf(),
            aprovado,
            statusFinal // Agora o Java sabe de onde vem esse valor
        );
        
        documentoRepository.save(entity);
        System.out.println("Documento salvo no banco com ID: " + entity.getId());
        
        return new ResultadoValidacao(
            aprovado,
            aprovado ? "APROVADO" : "REPROVADO",
            regrasAtendidas,
            pendencias
        );
    }

    private void verificarCampo(String valor, String msgSucesso, String msgErro, List<String> regras, List<String> pendencias) {
        if (valor != null && !valor.isBlank()) {
            regras.add(msgSucesso);
        } else {
            pendencias.add(msgErro);
        }
    }
}