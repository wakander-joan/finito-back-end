package com.management.finito.assinatura.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.finito.handler.APIException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cliente HTTP do Asaas (https://docs.asaas.com). Autentica pelo header access_token.
 * Sandbox por padrão; em produção defina ASAAS_BASE_URL=https://api.asaas.com/v3 + ASAAS_API_KEY.
 * Erros do Asaas são propagados com a mensagem original (para aparecer no front e nos logs).
 */
@Component
@Log4j2
public class AsaasClient {
    private final RestTemplate rest;
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${asaas.base-url:https://sandbox.asaas.com/api/v3}")
    private String baseUrl;

    @Value("${asaas.api-key:}")
    private String apiKey;

    public AsaasClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(15000);
        factory.setReadTimeout(20000);
        this.rest = new RestTemplate(factory);
    }

    private HttpHeaders headers() {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("access_token", apiKey);
        return h;
    }

    /** Cria (ou reaproveita) um cliente no Asaas. Retorna o id (cus_xxx). */
    public String criarCliente(String nome, String email, String cpfCnpj) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", nome);
        body.put("email", email);
        body.put("cpfCnpj", cpfCnpj);
        Map<?, ?> resp = post("/customers", body);
        return (String) resp.get("id");
    }

    /**
     * Cria a assinatura mensal. billingType UNDEFINED = o cliente escolhe Pix/Boleto/Cartão
     * na página de fatura. Retorna o id da assinatura (sub_xxx).
     */
    public String criarAssinatura(String customerId, BigDecimal valor, String descricao, String successUrl) {
        Map<String, Object> body = new HashMap<>();
        body.put("customer", customerId);
        body.put("billingType", "UNDEFINED");
        body.put("value", valor);
        body.put("nextDueDate", LocalDate.now().toString());
        body.put("cycle", "MONTHLY");
        body.put("description", descricao);

        // Callback (redireciona de volta ao app após o pagamento) só é aceito pelo Asaas
        // se a conta tiver um site cadastrado. Se não tiver, cria a assinatura sem callback.
        if (successUrl != null && !successUrl.isBlank()) {
            Map<String, Object> comCallback = new HashMap<>(body);
            Map<String, Object> callback = new HashMap<>();
            callback.put("successUrl", successUrl);
            callback.put("autoRedirect", true);
            comCallback.put("callback", callback);
            try {
                return (String) post("/subscriptions", comCallback).get("id");
            } catch (APIException e) {
                if (!ehErroDeCallback(e.getMessage())) throw e;
                log.warn("Asaas recusou o callback (site/domínio) — criando assinatura SEM callback (sem auto-redirect).");
            }
        }
        return (String) post("/subscriptions", body).get("id");
    }

    /** Erros do Asaas relacionados ao callback/site/domínio -> criar sem callback. */
    private boolean ehErroDeCallback(String mensagem) {
        if (mensagem == null) return false;
        String m = mensagem.toLowerCase();
        return m.contains("cadastre um site") || m.contains("domínio") || m.contains("dominio")
                || m.contains("informações") || m.contains("informacoes") || m.contains("successurl") || m.contains("callback");
    }

    /** URL da página de fatura da 1ª cobrança (com pequeno retry, pois pode demorar a ser gerada). */
    public String primeiraFaturaUrl(String subscriptionId) {
        for (int tentativa = 1; tentativa <= 3; tentativa++) {
            Map<?, ?> resp = get("/subscriptions/" + subscriptionId + "/payments");
            Object data = resp.get("data");
            if (data instanceof List<?> list && !list.isEmpty() && list.get(0) instanceof Map<?, ?> primeira) {
                Object url = primeira.get("invoiceUrl");
                if (url != null && !url.toString().isBlank()) return url.toString();
            }
            dorme(800);
        }
        return null;
    }

    private Map<?, ?> post(String path, Map<String, Object> body) {
        garantiaChave();
        try {
            ResponseEntity<Map> r = rest.exchange(baseUrl + path, HttpMethod.POST,
                    new HttpEntity<>(body, headers()), Map.class);
            return r.getBody() != null ? r.getBody() : Map.of();
        } catch (HttpStatusCodeException e) {
            String corpo = e.getResponseBodyAsString();
            log.error("Asaas POST {} -> HTTP {} | {}", path, e.getStatusCode(), corpo);
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Asaas: " + extrairMensagem(corpo));
        } catch (RestClientException e) {
            log.error("Asaas POST {} falhou: {}", path, e.getMessage());
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Não foi possível conectar ao provedor de pagamento.");
        }
    }

    private Map<?, ?> get(String path) {
        garantiaChave();
        try {
            ResponseEntity<Map> r = rest.exchange(baseUrl + path, HttpMethod.GET,
                    new HttpEntity<>(headers()), Map.class);
            return r.getBody() != null ? r.getBody() : Map.of();
        } catch (HttpStatusCodeException e) {
            String corpo = e.getResponseBodyAsString();
            log.error("Asaas GET {} -> HTTP {} | {}", path, e.getStatusCode(), corpo);
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Asaas: " + extrairMensagem(corpo));
        } catch (RestClientException e) {
            log.error("Asaas GET {} falhou: {}", path, e.getMessage());
            throw APIException.build(HttpStatus.BAD_GATEWAY, "Não foi possível conectar ao provedor de pagamento.");
        }
    }

    /** Extrai a descrição do erro do JSON do Asaas ({"errors":[{"description":"..."}]}). */
    private String extrairMensagem(String corpo) {
        if (corpo == null || corpo.isBlank()) return "erro desconhecido do provedor.";
        try {
            JsonNode node = mapper.readTree(corpo);
            JsonNode errors = node.get("errors");
            if (errors != null && errors.isArray() && errors.size() > 0) {
                JsonNode desc = errors.get(0).get("description");
                if (desc != null && !desc.asText().isBlank()) return desc.asText();
            }
        } catch (Exception ignore) {
            // corpo não-JSON: cai no retorno bruto abaixo
        }
        return corpo.length() > 220 ? corpo.substring(0, 220) : corpo;
    }

    private void dorme(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void garantiaChave() {
        if (apiKey == null || apiKey.isBlank()) {
            throw APIException.build(HttpStatus.SERVICE_UNAVAILABLE,
                    "Pagamento indisponível: a chave do Asaas não está configurada.");
        }
    }
}
