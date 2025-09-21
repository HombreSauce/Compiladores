package app.funcionalidades;

public class MiniTokenizador {
    public static String miniTokenizar(char ch) {
        if (Character.isUpperCase(ch)) {
            return "L";
        } else if (Character.isLowerCase(ch)) {
            return "l";
        } else if (Character.isDigit(ch)) {
            return "num";
        } else if (ch == '+') {
            return "+";
        } else if (ch == '-') {
            return "-";
        } else if (ch == '=') {
            return "=";
        } else if (ch == '*') {
            return "*";
        } else if (ch == '/') {
            return "/";
        } else if (ch == '.') {
            return ".";
        } else if (ch == ',') {
            return ",";
        } else if (ch == ';') {
            return ";";
        } else if (ch == ':') {
            return ":";
        } else if (ch == '>') {
            return ">";
        } else if (ch == '<') {
            return "<";
        } else if (ch == '!') {
            return "!";
        } else if (ch == '_') {
            return "_";
        } else if (ch == '@') {
            return "@";
        } else if (ch == '&') {
            return "&";
        } else if (ch == '(') {
            return "(";
        } else if (ch == ')') {
            return ")";
        } else if (ch == '{') {
            return "{";
        } else if (ch == '}') {
            return "}";
        } else if (ch == '%') {
            return "%";
        } else if (ch == ' ') {
            return "espacio";
        } else if (ch == '\n') {
            return "salto";
        }
        return null; // si no lo reconocés
    }
}
