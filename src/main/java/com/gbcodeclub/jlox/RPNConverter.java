package com.gbcodeclub.jlox;

import com.gbcodeclub.jlox.Expr.Binary;
import com.gbcodeclub.jlox.Expr.Grouping;
import com.gbcodeclub.jlox.Expr.Literal;
import com.gbcodeclub.jlox.Expr.Unary;

class RPNConverter implements Expr.Visitor<String>{

    @Override
    public String visitBinaryExpr(Binary expr) {
        String leftRepr = expr.left.accept(this);
        String rightRepr = expr.right.accept(this);
        return sequence(leftRepr, rightRepr, expr.operator.lexeme);
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return expr.expression.accept(this);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        String operandRepr = expr.right.accept(this);
        String operatorRepr;
        if (expr.operator.type == TokenType.MINUS) {
            operatorRepr = "~";
        } else {
            operatorRepr = expr.operator.lexeme;
        }
        return sequence(operandRepr, operatorRepr);
    }

    private String sequence(String... strings) {
        StringBuilder result = new StringBuilder();
        for (String s: strings) {
            result.append(s);
            result.append(" ");
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
    
    public static void main(String[] args) {
        Expr expr = new Binary(
            new Grouping(new Binary(
                new Literal(1), 
                new Token(TokenType.PLUS, "+", null, 0), 
                new Literal(2))), 
            new Token(TokenType.STAR, "*", null, 0), 
            new Grouping(new Binary(
                new Literal(4), 
                new Token(TokenType.MINUS, "-", null, 0), 
                new Literal(3))));
        System.out.println(expr.accept(new RPNConverter())); 
    }
}
