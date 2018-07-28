/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.ArrayList;

/**
 *
 * @author rodolfo.berlezi
 */
public class ShiftReduceMine {

    public String[][] gramatica = {{"A", "A-E", "E"},
    {"E", "E+K", "K"},
    {"K", "K/T", "T"},
    {"T", "T*F", "F"},
    {"F", "(A)", "i"}};

    public String pilha_esquerda = "";
    public String pilha_direita = "i+i";
    private final ArrayList<Float> pilha_o = new ArrayList();
    private final ArrayList<Float> pilha_v = new ArrayList();

    public void transfere() {
        if (pilha_direita.length() > 0) {
            pilha_esquerda = pilha_esquerda + pilha_direita.substring(0, 1);
            pilha_direita = pilha_direita.substring(1);

           //transfere da pilha de float da direita para a pilha e float d esquerda
        }

    }

    public int eh_inicio_regra() {
        if (pilha_direita.length() == 0) {
            return -1;
        }
        int resultado = -1;
        int cont = 0;
        while (resultado < 0 && cont < pilha_esquerda.length()) {
            String s = pilha_esquerda.substring(cont) + pilha_direita.substring(0, 1);
            for (String[] regra : gramatica) {
                for (int i = 1; i < regra.length; i++) {
                    if (regra[i].length() >= s.length()) {
                        if (s.equals(regra[i].substring(0, s.length()))) {
                            resultado = cont;
                        }
                    }
                }
            }
            cont++;
        }
        return resultado;
    }

    public String reduz(String expressao) {
        String resposta = "";
        for (String[] regra : gramatica) {
            for (int i = 1; i < regra.length; i++) {
                if (expressao.equals(regra[i])) {
                    resposta = regra[0];
                }
            }
        }
        return resposta;
    }

    public boolean tenta_reduzir() {
        boolean resposta = false;
        int cont = 0;
        while (resposta == false && cont < pilha_esquerda.length()) {
            String reducao = reduz(pilha_esquerda.substring(cont));
            if (!reducao.equals("")) {
                pilha_esquerda = pilha_esquerda.substring(0, cont) + reducao;
                resposta = true;
                //if pilha_esquerda.substring(cont) > 1               
                //pilha_esquerda.substring(cont)[1] = +operacao  
            }
            cont++;
        }
        return resposta;
    }

    public boolean shiftreduce() {
        int iter = 0;
        while (true) {
            iter++;
            int tam_pilha_direita = pilha_direita.length();
            System.out.println("Interação " + String.valueOf(iter) + " $" + pilha_esquerda + "  " + pilha_direita + "$");
            boolean reduzir = false;
            if (eh_inicio_regra() >= 0) {
                transfere();
            } else {
                reduzir = tenta_reduzir();
                if (reduzir == false) {
                    transfere();
                }
                if (tam_pilha_direita == 0 && reduzir == false) {
                    return pilha_esquerda.equals(gramatica[0][0]);
                }
            }
        }
    }

    public static void main(String[] args) {

        Lexico lexico = new Lexico("3/(2+1)");
        String expressao = lexico.analise();
        lexico.resultado_analise();
        if (expressao.length() > 0) {
            ShiftReduceMine sr = new ShiftReduceMine();

            sr.pilha_esquerda = "";
            sr.pilha_direita = expressao;

            if (sr.shiftreduce()) {
                System.out.println("Aceita palavra");
            } else {
                System.out.println("Recusa palavra");
            }

        }

    }

}
