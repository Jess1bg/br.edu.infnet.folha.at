package br.edu.infnet.folha.at;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MensalistaController {

    private static final Map<Long, Mensalista> mensalistas = new ConcurrentHashMap<>();

    public MensalistaController(Javalin app) {
        app.post("/mensalista", this::criarMensalista);
        app.get("/mensalista/{matricula}", this::buscarPorMatricula);
        app.get("/mensalistas", this::listarMensalistas);
        app.get("/hello", ctx -> ctx.result("Hello, Javalin!"));
    }

    private void criarMensalista(Context ctx) {
        Mensalista mensalista = ctx.bodyAsClass(Mensalista.class);
        mensalistas.put(mensalista.getMatricula(), mensalista);
        ctx.status(201);
        ctx.json(mensalista);
    }

    private void buscarPorMatricula(Context ctx) {
        long matricula = Long.parseLong(ctx.pathParam("matricula"));
        Mensalista mensalista = mensalistas.get(matricula);
        if (mensalista != null) {
            ctx.json(mensalista);
        } else {
            ctx.status(404).result("Mensalista não encontrado.");
        }
    }

    private void listarMensalistas(Context ctx) {
        ctx.json(new ArrayList<>(mensalistas.values()));
    }
}
