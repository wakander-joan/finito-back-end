package com.management.finito.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Traduz método + rota numa descrição amigável do que o usuário tentava fazer. */
public final class AcaoDescriber {

    private static final Map<String, String> NOMES = Map.ofEntries(
            Map.entry("lancamento", "lançamento"),
            Map.entry("lancamentos", "lançamento"),
            Map.entry("meta", "meta"),
            Map.entry("metas", "meta"),
            Map.entry("pessoa", "conta"),
            Map.entry("reserva", "reserva"),
            Map.entry("orcamento", "orçamento"),
            Map.entry("categoria", "categoria"),
            Map.entry("assinatura", "assinatura Premium"),
            Map.entry("feedback", "feedback"),
            Map.entry("feedbacks", "feedback"),
            Map.entry("dashboard", "dashboard"),
            Map.entry("grafico", "gráficos"),
            Map.entry("graficos", "gráficos")
    );

    private AcaoDescriber() {}

    public static String descreve(String metodo, String pathCompleto) {
        if (pathCompleto == null || pathCompleto.isBlank()) return metodo;
        String path = pathCompleto.split("\\?")[0];

        List<String> segs = new ArrayList<>();
        for (String s : path.split("/")) {
            if (s.isBlank()) continue;
            String low = s.toLowerCase();
            if (low.equals("finito") || low.equals("taskflow") || low.equals("api") || low.equals("v1")) continue;
            if (ehId(low)) continue;
            segs.add(low);
        }
        if (segs.isEmpty()) return (metodo == null ? "" : metodo + " ") + path;

        // Casos especiais
        if (segs.contains("webhooks")) return "Receber webhook de pagamento (Asaas)";
        if (segs.contains("checkout")) return "Assinar Premium (checkout)";
        if (segs.contains("login")) return "Fazer login";
        if (segs.contains("cadastro") || segs.contains("registrar")) return "Criar conta (cadastro)";
        if (segs.contains("valoralvo")) return "Ajustar valor-alvo da meta";
        if (segs.contains("pagar")) return "Registrar pagamento";

        String recurso = segs.get(0);
        String sub = segs.size() > 1 ? segs.get(segs.size() - 1) : null;
        String nome = NOMES.getOrDefault(recurso, recurso);

        if ("me".equals(sub)) return "Consultar " + nome;

        String verbo = switch (metodo == null ? "" : metodo.toUpperCase()) {
            case "POST" -> "Criar";
            case "PUT", "PATCH" -> "Atualizar";
            case "DELETE" -> "Apagar";
            case "GET" -> "Consultar";
            default -> (metodo == null ? "Acessar" : metodo);
        };

        String base = verbo + " " + nome;
        if (sub != null && !sub.equals(recurso) && !ehId(sub)) base += " (" + sub + ")";
        return base;
    }

    private static boolean ehId(String s) {
        if (s == null) return false;
        if (s.matches("\\d+")) return true;                 // número
        if (s.length() >= 16 && s.contains("-")) return true; // UUID
        return s.matches("[0-9a-fA-F]{24,}");               // hex longo
    }
}
