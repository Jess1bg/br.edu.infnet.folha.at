package br.edu.infnet.folha.at;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MensalistaController {

    // Armazenamento simples em memória (simula um banco de dados)
    private static final Map<Long, Mensalista> mensalistas = new ConcurrentHashMap<>();

    // Construtor registra os endpoints no Javalin
    public MensalistaController(Javalin app) {
        // Endpoint POST para criar um mensalista
        app.post("/mensalista", this::criarMensalista);

        // Endpoint GET para buscar mensalista por matrícula
        app.get("/mensalista/{matricula}", this::buscarPorMatricula);

        // Endpoint GET para listar todos os mensalistas (Rubrica 4 - passo 1)
        app.get("/mensalistas", this::listarMensalistas);
    }

    // Método para criar mensalista a partir do corpo da requisição JSON com validações
    private void criarMensalista(Context ctx) {
        Mensalista mensalista = ctx.bodyAsClass(Mensalista.class);

        // Validações
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

        // Verifica se já existe mensalista com essa matrícula
        if (mensalistas.containsKey(mensalista.getMatricula())) {
            ctx.status(409).result("Já existe um mensalista cadastrado com essa matrícula.");
            return;
        }

        // Se passou nas validações, salva e retorna 201 Created
        mensalistas.put(mensalista.getMatricula(), mensalista);
        ctx.status(201);
        ctx.json(mensalista);
    }

    // Método para buscar mensalista por matrícula via path param
    private void buscarPorMatricula(Context ctx) {
        long matricula = Long.parseLong(ctx.pathParam("matricula"));
        Mensalista mensalista = mensalistas.get(matricula);
        if (mensalista != null) {
            ctx.json(mensalista);
        } else {
            ctx.status(404).result("Mensalista não encontrado.");
        }
    }

    // Método para listar todos os mensalistas cadastrados
    private void listarMensalistas(Context ctx) {
        ctx.json(new ArrayList<>(mensalistas.values()));
    }
}
