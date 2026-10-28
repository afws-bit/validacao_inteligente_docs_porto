package br.com.mentoria.validacao_documentos.dto;

import java.util.List;

public record ResultadoValidacao(
    boolean aprovado,
    String status,
    List<String> regrasAtendidas,
    List<String> pendencias
) {}