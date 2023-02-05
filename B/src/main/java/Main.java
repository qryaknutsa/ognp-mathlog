import java.util.*;

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


//            System.out.println("______________________\n" + c);
//            System.out.println(i);


            if (expression.charAt(i) == '(') {
                stack.push(c);
            } else if (expression.charAt(i) == ')') {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop();
            } else if (!isOperation(expression.charAt(i), operations) && !c.equals("->")) {
                //Случай, если название переменной больше одного чара
                //System.out.println("BREAK??");
                while (!isOperation(expression.charAt(i), operations) && i < expression.length() - 2) {
                    if (expression.charAt(i + 1) == ('-') || expression.charAt(i + 2) == ('>') || isOperation(expression.charAt(i + 1), operations) || expression.charAt(i + 1) == ')') {
                        //System.out.println("At break:" + c);
                        break;
                    }
                    //System.out.println("C before: " + c);
                    c += String.valueOf(expression.charAt(i + 1));
                    i++;
//                    System.out.println("C after: " + c);
                }
                //наверное делать проверку только на скобку странно, поэтому думаем. Или не глупо
                if (i == expression.length() - 2 && expression.charAt(i + 1) != (')')) {
                    i++;
//                    System.out.println("Last C before: " + c);
                    c += String.valueOf(expression.charAt(i));
//                    System.out.println("Last C after: " + c);
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
//        System.out.println("output:" + output);
        return output;
    }
    static int con(int a, int b) {
        if (a == 1 && b == 1) {
            return 1;
        }
        return 0;
    }

    static int dis(int a, int b) {
        if (a == 1 || b == 1) {
            return 1;
        }
        return 0;
    }

    static int inv(int a) {
        if (a == 1) {
            return 0;
        }
        return 1;
    }

    static int imp(int a, int b) {
        int a1 = inv(a);
        return dis(a1, b);
    }

    static String analyze(ArrayList<String> expression) {
        ArrayList<String> vars = new ArrayList<>();
        int neg_count = 0;
        int pos_count = 0;
        for (String s : expression) {
            if (!s.equals("!") && !s.equals("&") && !s.equals("->") && !s.equals("|") && !vars.contains(s)) {
                vars.add(s);
            }
        }
        int count_vars = vars.size();
        for (int i = 0; i < Math.pow(2, count_vars); i++) {
            Stack<Integer> stack = new Stack<>();
//            System.out.println("______________________\n" + i);
            String ex = Integer.toBinaryString(i);
            for (int j = 0; j < expression.size(); j++) {
                String string = expression.get(j);
                int result = 0;
                if (string.equals("!") || string.equals("&") || string.equals("|") || string.equals("->")) {
                    if (string.equals("!")) {
                        result = inv(stack.pop());
                        stack.push(result);
                    } else if (string.equals("&")) {
                        result = con(stack.pop(), stack.pop());
                        stack.push(result);
                    } else if (string.equals("|")) {
                        result = dis(stack.pop(), stack.pop());
                        stack.push(result);
                    } else if (string.equals("->")) {
                        int first_pop = stack.pop();
                        int second_pop = stack.pop();
                        result = imp(second_pop, first_pop);
                        stack.push(result);
                    }
                } else {
                    int temp = 0;
                    if (vars.contains(string)) {
                        int index = vars.indexOf(string);
//                        System.out.println("this is String: " + string);
//                        System.out.println("this is index: " + index);
                        if (ex.length() > index) temp = Integer.parseInt(String.valueOf(ex.charAt(index)));
                    }
//                    System.out.println("this is temp: " + temp);
                    stack.push(temp);
                }
            }
            if (stack.pop() == 1) pos_count++;
            else neg_count++;
        }
        if (neg_count == 0) return "Valid";
        else if (pos_count == 0) return "Unsatisfiable";
        else return "Satisfiable and invalid, " + pos_count + " true and " + neg_count + " false cases";
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        //!A&!B->!(A|B)
        //A->!B123
        //P1’->!QQ->!R10&S|!T&U&V
        //((PPP->PPP’)->PPP)->PPP
        ArrayList<String> ss = infixToRpn(expression);
        assert ss != null;
        String new_ss = analyze(ss);
        System.out.println(new_ss);
    }
}