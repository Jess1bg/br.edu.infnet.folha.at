package br.edu.infnet.folha.at;

public class Mensalista {

    private long matricula;
    private String nome;
    private String cargo;
    private double salario;

    // Construtor vazio obrigatório para desserialização JSON
    public Mensalista() {
    }

    // Construtor com parâmetros
    public Mensalista(long matricula, String nome, String cargo, double salario) {
        this.matricula = matricula;
        this.nome = nome;
        this.cargo = cargo;
        this.salario = salario;
    }

    public long getMatricula() {
        return matricula;
    }

    public void setMatricula(long matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    @Override
    public String toString() {
        return "Mensalista{" +
                "matricula=" + matricula +
                ", nome='" + nome + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                '}';
    }
}
