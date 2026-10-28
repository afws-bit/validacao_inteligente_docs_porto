package br.com.mentoria.validacao_documentos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Service
public class OcrService {

    @Value("${app.ocr.command:tesseract}")
    private String tesseractCommand;

    @Value("${app.ocr.language:por}")
    private String language;

    public String extrairTexto(String imagemBase64) {
        if (imagemBase64 == null || imagemBase64.isEmpty()) {
            throw new IllegalArgumentException("Imagem Base64 não pode ser vazia.");
        }

        try {
            // 1. Decodifica o Base64 e salva em um arquivo temporário
            String base64Limpo = imagemBase64.replace("data:image/png;base64,", "").replace("data:image/jpeg;base64,", "");
            byte[] imagemBytes = Base64.getDecoder().decode(base64Limpo);
            
            Path tempFile = Files.createTempFile("documento_", ".png");
            Files.write(tempFile, imagemBytes);

            // 2. Prepara o comando do Tesseract
            // Ex de comando: tesseract caminho/da/imagem stdout -l por
            ProcessBuilder processBuilder = new ProcessBuilder(
                    tesseractCommand,
                    tempFile.toAbsolutePath().toString(),
                    "stdout", // Isso diz ao Tesseract para imprimir o texto no terminal em vez de salvar em arquivo
                    "-l", language
            );

            Process process = processBuilder.start();
            
            // 3. Lê a saída do terminal (o texto extraído)
            StringBuilder textoExtraido = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    textoExtraido.append(linha).append("\n");
                }
            }

            process.waitFor();
            Files.deleteIfExists(tempFile); // Limpa o arquivo temporário

            return textoExtraido.toString().trim();

        } catch (Exception e) {
            // Fallback para o MVP (como mencionado no seu README)
            System.err.println("Erro no Tesseract: " + e.getMessage());
            throw new RuntimeException("Falha ao extrair texto da imagem.", e);
        }
    }
}