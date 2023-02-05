import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Main {

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
            System.out.println("______________________\n" + i);
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
                        System.out.println("this is String: " + string);
                        System.out.println("this is index: " + index);
                        if (ex.length() > index) temp = Integer.parseInt(String.valueOf(ex.charAt(index)));
                    }
                    System.out.println("this is temp: " + temp);
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
        ArrayList<String> ss = Parser.infixToRpn(expression);
        assert ss != null;
        String new_ss = analyze(ss);
        System.out.println(new_ss);
//        assert ss != null;
//        System.out.println(Parser.cleaning(Parser.superFunc(ss)));
    }
}