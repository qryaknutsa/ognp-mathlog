import java.util.*;
import java.lang.Character;

public class Parser {

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


    static ArrayList<String> infixToRpn(String expression) {
        fillMap(operations);
        Stack<String> stack = new Stack<>();
        ArrayList<String> output = new ArrayList<>();
        for (int i = 0; i < expression.length(); ++i) {
            String c = String.valueOf(expression.charAt(i));
            if (expression.charAt(i) == '\n') {
                continue;
            }
            if (c.equals("-") && expression.charAt(i + 1) == '>') {
                c += String.valueOf(expression.charAt(i + 1));
                i++;
            }


            System.out.println("______________________\n" + c);
            System.out.println(i);


            if (expression.charAt(i) == '(') {
                stack.push(c);
            } else if (expression.charAt(i) == ')') {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop();
            } else if (!isOperation(expression.charAt(i), operations) && !c.equals("->")) {
                //Случай, если название переменной больше одного чара
                System.out.println("BREAK??");
                while (!isOperation(expression.charAt(i), operations) && i < expression.length() - 2) {
                    if (expression.charAt(i + 1) == ('-') || expression.charAt(i + 2) == ('>') || isOperation(expression.charAt(i + 1), operations) || expression.charAt(i + 1) == ')') {
                        System.out.println("At break:" + c);
                        break;
                    }
                    System.out.println("C before: " + c);
                    c += String.valueOf(expression.charAt(i + 1));
                    i++;
                    System.out.println("C after: " + c);
                }
                //наверное делать проверку только на скобку странно, поэтому думаем. Или не глупо
                if (i == expression.length() - 2 && expression.charAt(i + 1) != (')')) {
                    i++;
                    System.out.println("Last C before: " + c);
                    c += String.valueOf(expression.charAt(i));
                    System.out.println("Last C after: " + c);
                }

                output.add(c);
            } else {
                if (c.equals("->")) {
                    while (!stack.isEmpty() && getPrecedence(c) < getPrecedence(stack.peek())) {
                        output.add(stack.pop());
                    }
                } else {
                    while (!stack.isEmpty() && getPrecedence(c) <= getPrecedence(stack.peek())) {
                        output.add(stack.pop());
                    }
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                System.out.println("Oops, it's null");
                return null;
            }
            output.add(stack.pop());
        }
        System.out.println("output:" + output);
        return output;
    }

}
