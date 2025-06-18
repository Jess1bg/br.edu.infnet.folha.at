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
    }

    private void criarMensalista(Context ctx) {
        Mensalista mensalista = ctx.bodyAsClass(Mensalista.class);

        if (mensalista.getMatricula() <= 0) {
            ctx.status(400).result("Matrícula inválida. Deve ser maior que zero.");
            return;
        }

        if (mensalista.getNome() == null || mensalista.getNome().isBlank()) {
            ctx.status(400).result("Nome é obrigatório.");
            return;
        }

        if (mensalista.getSalario() <= 0) {
            ctx.status(400).result("Salário deve ser maior que zero.");
            return;
        }

        if (mensalistas.containsKey(mensalista.getMatricula())) {
            ctx.status(409).result("Já existe um mensalista cadastrado com essa matrícula.");
            return;
        }

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
