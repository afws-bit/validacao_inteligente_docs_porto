# 🏛️ Validação Inteligente de Documentos

**Aluno:** Augusto Freitas Wanderley de Siqueira  
**Curso:** ADS - Regular - Cesar School  
**Turma:** Avanade 2

## 📌 Sobre o Projeto
Este projeto é um MVP (Minimum Viable Product) desenvolvido para automatizar a análise, extração de informações e validação de documentos. O sistema recebe uma imagem ou PDF, utiliza OCR para extrair o texto, Inteligência Artificial para estruturar os dados e regras de negócio em Java para aprovar ou rejeitar o documento, salvando o histórico em banco de dados.

O objetivo principal é reduzir o trabalho manual, aumentar a velocidade da conferência e padronizar o processo.

## ⚙️ Arquitetura e Fluxo
O fluxo da solução é composto pelas seguintes etapas:
1. O usuário envia uma imagem (Base64) ou o texto do documento.
2. **OpenCV** (planejado) realiza o pré-processamento da imagem.
3. **Tesseract OCR** extrai o texto bruto da imagem.
4. **Inteligência Artificial (OpenAI)** interpreta o texto e estrutura os dados em JSON.
5. **Java/Spring Boot** aplica as regras de validação (ex: data de emissão, campos obrigatórios).
6. O resultado (Aprovado/Rejeitado) e os dados extraídos são salvos no **Banco de Dados H2**.

## 🛠️ Tecnologias Utilizadas
*   **Java 17** + **Spring Boot 3**
*   **Spring Data JPA** (Persistência de dados)
*   **Banco de Dados H2** (Em memória para testes)
*   **Tesseract OCR** (Extração de texto)
*   **OpenAI API** (Interpretação de dados via IA - com fallback local para testes)

## 🚀 Como Executar o Projeto

### Pré-requisitos
*   Java 17 instalado
*   Maven instalado
*   Tesseract instalado no sistema (`sudo apt install tesseract-ocr tesseract-ocr-por`)

### Passo a Passo
1. Clone o repositório.
2. (Opcional) Configure sua chave da OpenAI no arquivo `src/main/resources/application.properties`:
   ```properties
   app.ai.openai.api-key=sk-sua-chave-aqui