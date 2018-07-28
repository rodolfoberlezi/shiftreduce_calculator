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
public class Lexico {

    private String pilha_fonte = "";
    private int pilha_valores = 0;
//    private String pilha_operacao = "";
    private final ArrayList<String> pilha_o = new ArrayList();
    private final ArrayList<String> pilha_v = new ArrayList();

    public Lexico(String f) {
        this.pilha_fonte = f;
    }

    public String reconhece_numero() {
        String resultado = "";
        int i = 0;
        int estado = 0;
        while (true) {
            String s = "$";
            if (i < pilha_fonte.length()) {
                s = pilha_fonte.substring(i, i + 1);
            }
            System.out.println("estado:" + String.valueOf(estado));
            if (estado == 0) {
                if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                    estado = 1;
                    pilha_v.add(s);
//                    pilha_valores = pilha_valores + s;
                    resultado = resultado + s;
                    i++;
                } else {
                    return "";
                }
            } else if (estado == 1) {
                if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                    estado = 2;
                    pilha_v.add(s);
//                    pilha_valores = pilha_valores + s;
                    resultado = resultado + s;
                    i++;
                } else if (s.equals(".")) {
                    estado = 2;
                    resultado = resultado + s;
                    i++;
                } else {
                    estado = 4;
                }
            } else if (estado == 2) {
                if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                    estado = 3;
                    pilha_v.add(s);
//                    pilha_valores = pilha_valores + s;
                    resultado = resultado + s;
                    i++;
                } else {
                    return "";
                }
            } else if (estado == 3) {
                if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
                    estado = 4;
                    pilha_v.add(s);
//                    pilha_valores = pilha_valores + s;
                    resultado = resultado + s;
                    i++;
                } else {
                    estado = 4;
                }
            } else if (estado == 4) {
                pilha_fonte = pilha_fonte.substring(1);
                System.out.println("Pilha Fonte: " + pilha_fonte);
//                System.out.println("Pilha Valores: " + pilha_v);
                return resultado;
            }
            if (pilha_fonte.length() == 0) {
                return resultado;
            }
        }
    }

    public String reconhece_operador() {
        String resultado = "";
        String s = pilha_fonte.substring(0, 1);
        if (s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("(") || s.equals(")")) {
            resultado = resultado + s;
            pilha_o.add(s);
//            pilha_operacao = pilha_operacao + s;
            pilha_fonte = pilha_fonte.substring(1);
            return s;
        } else {
            return "";
        }
    }

    public int retorna_valor(int pos) {
//        String aux = pilha_valores.substring(pilha_valores.length() - pos, pilha_valores.length());
//        pilha_valores = pilha_valores.substring(pilha_valores.length() - pos, pilha_valores.length());
        String aux = pilha_v.get(pos);
        int k = Integer.valueOf(aux);
        return k;
    }

    public int calcula(int size) {
        int tam_pilha = size;
        int resultado = 0;
        for (int j = pilha_o.size(); j > 0; j--) {
            String s = pilha_o.get(j-1);
            switch (s) {
                case "*":
                    resultado = retorna_valor(tam_pilha) * retorna_valor(tam_pilha - 1);
                    tam_pilha--;
                    pilha_v.add(tam_pilha, Integer.toString(resultado));
                    break;
                case "/":
                    resultado = retorna_valor(tam_pilha) / retorna_valor(tam_pilha - 1);
                    tam_pilha--;
                    pilha_v.add(tam_pilha, Integer.toString(resultado));
                    break;
                case "+":
                    resultado = retorna_valor(tam_pilha) + retorna_valor(tam_pilha - 1);
                    tam_pilha--;
                    pilha_v.add(tam_pilha, Integer.toString(resultado));
                    break;
                case "-":
                    resultado = retorna_valor(tam_pilha) - retorna_valor(tam_pilha - 1);
                    tam_pilha--;
                    pilha_v.add(tam_pilha, Integer.toString(resultado));
                    break;
                case "(":                    
                    break;
                case ")":                   
                    break;
                default:
                    break;
            }
        }
        return resultado;
    }

    public void resultado_analise() {        
            System.out.println("Resultado: " + calcula(pilha_o.size()));        
    }

    public String analise() {
        String resultado = "";
        while (true) {
            String token = "";
            token = reconhece_numero();
            if (token.length() > 0) {
                resultado = resultado + "i";
            } else {
                token = reconhece_operador();
                if (token.length() > 0) {
                    resultado = resultado + token;
                }
            }
            if (token.length() == 0) {
                System.out.println("ERRO");
                return "0";
            } else {
                if (pilha_fonte.length() == 0) {
                    return resultado;
                }
            }
        }
    }

}
