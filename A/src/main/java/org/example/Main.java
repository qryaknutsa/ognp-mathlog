package org.example;

import java.util.*;
import java.lang.Character;

public class Main {

    static Map<String, Character> operations = new HashMap<>();

    static void fillMap(Map<String, Character> map) {
        map.put("inv", '!');
        map.put("con", '&');
        map.put("dis", '|');
    }

    private static boolean isOperation(Character i, Map<String, Character> map) {
        return map.containsValue(i);
    }

    static int getPrecedence(String op) {
        if (op.equals("->"))
            return 1;
        else if (op.equals("|"))
            return 2;
        else if (op.equals("&"))
            return 3;
        else if (op.equals("!"))
            return 4;
        else
            return -1;
    }


    static String infixToRpn(String expression) {
        fillMap(operations);
        Stack<String> stack = new Stack<>();
        String output = "";
        for (int i = 0; i < expression.length(); ++i) {
            String c = String.valueOf(expression.charAt(i));
            if (c.equals("-") && expression.charAt(i + 1) == '>') {
                c += String.valueOf(expression.charAt(i + 1));
                i++;
            }
            if (expression.charAt(i) == '(') {
                stack.push(c);
            } else if (expression.charAt(i) == ')') {
                while (!stack.isEmpty() && !stack.peek().equals("("))
                    output += stack.pop();
                stack.pop();
            } else if (!isOperation(expression.charAt(i), operations) && !c.equals("->")) {
                if (i == expression.length() - 1) {
                    output += c;
                    output += " ";
                    break;
                }
                while (!isOperation(expression.charAt(i), operations) && expression.charAt(i + 1) != ('-') && !isOperation(expression.charAt(i + 1), operations) && expression.charAt(i + 1) != ')') {
                    c += String.valueOf(expression.charAt(i + 1));
                    i++;
                }
                output += c;
                output += " ";
            } else {
                if (c.equals("->")) {
                    while (!stack.isEmpty() && getPrecedence(c) < getPrecedence(stack.peek())) {
                        output += stack.pop();
                    }
                } else {
                    while (!stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())) {
                        output += stack.pop();
                    }
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            if (stack.peek().equals("("))
                return "This expression is invalid";
            output += stack.pop();
        }
        return output;

    }

    static String superFunc(String expression) {
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < expression.length(); ++i) {
            String to_stack = "";
            String element = String.valueOf(expression.charAt(i));
            if (element.equals("-")) {
                element += String.valueOf(expression.charAt(i + 1));
                i++;
            }

            if (isOperation(expression.charAt(i), operations) || element.equals("->")) {
                if (element.equals("!")) {
                    to_stack += "(" + element + "," + stack.pop() + ")";
                    stack.push(to_stack);
                } else {
                    String first_pop = stack.pop();
                    String second_pop = stack.pop();
                    to_stack += "(" + element + "," + second_pop + "," + first_pop + ")";
                    stack.push(to_stack);
                }
            } else {
                while (expression.charAt(i) != ' ') {
                    i++;
                    element += expression.charAt(i);
                }
                stack.push(element.trim());
            }
        }
        return stack.pop();
    }


    static String cleaning(String expression) {
        ArrayList<String> to_replace = new ArrayList<>();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(' && expression.charAt(i + 1) == '!') {
                String susp = String.valueOf(expression.charAt(i));
                while (expression.charAt(i) != ')') {
                    i++;
                    susp += String.valueOf(expression.charAt(i));
                }
                int count_commas = 0;
                for (int j = 0; j < susp.length(); j++) {
                    if (susp.charAt(j) == ',') {
                        count_commas++;
                    }
                }
                if (count_commas == 1) to_replace.add(susp);
            }
        }

        for (String s : to_replace) {
            String element = "";
            for (int k = 0; k < s.length(); k++) {
                if (s.charAt(k) == ',') {
                    k++;
                    while (s.charAt(k) != ')') {
                        element += String.valueOf(s.charAt(k));
                        k++;
                    }
                }
            }
            expression = expression.replaceFirst(s, "!" + element);
        }
        return expression;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
//        String expression = "P1’->!QQ->!R10&S|!T&U&V";
//        System.out.println(expression);
        //System.out.println(infixToRpn(expression));
        System.out.println(cleaning(superFunc(infixToRpn(expression))));

    }
}
