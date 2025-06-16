package br.edu.infnet.folha.at;

import io.javalin.Javalin;
import java.time.LocalDateTime;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));

        app.get("/status", ctx -> {
            ctx.json(Map.of("status", "ok", "timestamp", LocalDateTime.now().toString()));
        });

        app.post("/echo", ctx -> {
            Map<String,String> body = ctx.bodyAsClass(Map.class);
            ctx.json(body);
        });

        app.get("/saudacao/{nome}", ctx -> {
            String nome = ctx.pathParam("nome");
            ctx.json(Map.of("mensagem", "Olá, " + nome + "!"));
        });

        new MensalistaController(app);
    }
}
