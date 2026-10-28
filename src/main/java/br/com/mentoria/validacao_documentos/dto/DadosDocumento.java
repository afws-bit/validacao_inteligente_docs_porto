package br.com.mentoria.validacao_documentos.dto;

public record DadosDocumento(
    String nome,
    String cpf,
    String endereco,
    String cidade,
    String dataEmissao
) {}