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
    private final ArrayList<Float> pilha_ve = new ArrayList();
    private final ArrayList<Float> pilha_vd = new ArrayList();

    public void transfere() {
        if (pilha_direita.length() > 0) {
            pilha_esquerda = pilha_esquerda + pilha_direita.substring(0, 1);
            pilha_direita = pilha_direita.substring(1);
            if ((pilha_esquerda.endsWith("i"))) {
                pilha_ve.add(pilha_vd.remove(0));
            }
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
                    if (expressao.length() > 2) {
                        calcula_expresaso(expressao);
                    }
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

    public void calcula_expresaso(String expressao) {
        if(expressao.charAt(0)=='(')
            return;
        Float valor_1 = pilha_ve.remove(pilha_ve.size() - 1);
        //System.out.println(valor_1);
        Float valor_2 = pilha_ve.remove(pilha_ve.size() - 1);
        //System.out.println(valor_2);
        switch (expressao.charAt(1)) {
            case '+':
                pilha_ve.add(valor_1 + valor_2);
                break;
            case '-':
                pilha_ve.add(valor_1 - valor_2);
                break;
            case '/':
                pilha_ve.add(valor_1 / valor_2);
                break;
            case '*':
                pilha_ve.add(valor_1 * valor_2);
                break;
            case '(':
                break;
            case ')':
                break;
            default:
                break;
        }
    }

    public void valor_expressao(String expressao) {
        String[] vetor = expressao.split("");
        for (String string : vetor) {
            if (Character.isDigit(string.charAt(0))) {
                pilha_vd.add(Float.valueOf(string));
            }
        }

    }

    public static void main(String[] args) {

        String expressao = "(5+4)/2";
        Lexico lexico = new Lexico(expressao);
        String simbolos = lexico.analise();
        //lexico.resultado_analise();
        if (expressao.length() > 0) {
            ShiftReduceMine sr = new ShiftReduceMine();

            sr.pilha_esquerda = "";
            sr.pilha_direita = simbolos;
            sr.valor_expressao(expressao);
//            System.out.println(expressao);

            if (sr.shiftreduce()) {
                System.out.println("Aceita palavra");
            } else {
                System.out.println("Recusa palavra");
            }

            System.out.println(sr.pilha_ve);
//            System.out.println(sr.pilha_vd);

        }

    }

}
