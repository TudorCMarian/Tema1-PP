import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Stack;
import javax.swing.*;

public class Calculator extends JFrame {
    JButton digits[] = {
            new JButton(" 0 "),
            new JButton(" 1 "),
            new JButton(" 2 "),
            new JButton(" 3 "),
            new JButton(" 4 "),
            new JButton(" 5 "),
            new JButton(" 6 "),
            new JButton(" 7 "),
            new JButton(" 8 "),
            new JButton(" 9 ")
    };

    JButton operators[] = {
            new JButton(" + "),
            new JButton(" - "),
            new JButton(" * "),
            new JButton(" / "),
            new JButton(" = "),
            new JButton(" C ")
    };

    String oper_values[] = {"+", "-", "*", "/", "=", ""};

    String value;
    char operator;

    JTextArea area = new JTextArea(3, 5);

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setSize(230, 250);
        calculator.setTitle(" Java-Calc, Tema PP Lab1 ");
        calculator.setResizable(true);
        calculator.setVisible(true);
        calculator.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static double evaluatePolishNotation(String input) {
        // Convert the input string to Polish notation
        String polishExpression = toPolish(input);

        // Evaluate the Polish notation expression using a stack
        Stack<Double> stack = new Stack<>();
        String[] tokens = polishExpression.split("\\s+");
        for (String token : tokens) {
            if (isOperator(token)) {
                double operand2 = stack.pop();
                double operand1 = stack.pop();
                double result = applyOperator(token, operand1, operand2);
                stack.push(result);
            } else {
                double operand = Double.parseDouble(token);
                stack.push(operand);
            }
        }
        return stack.pop();
    }

    public static String toPolish(String input) {
        // Split the input string into tokens
        String[] tokens = input.split(" ");

        // Create a stack to hold the operators
        Stack<String> stack = new Stack<>();

        // Create a string builder to build the output expression
        StringBuilder output = new StringBuilder();

        // Iterate over the tokens
        for (String token : tokens) {
            if (isOperator(token)) {
                // Pop operators from the stack until we find one with lower precedence
                while (!stack.isEmpty() && precedence(token) <= precedence(stack.peek())) {
                    output.append(stack.pop()).append(" ");
                }
                // Push the current operator onto the stack
                stack.push(token);
            } else {
                // Append operands to the output expression
                output.append(token).append(" ");
            }
        }

        // Pop any remaining operators from the stack and append them to the output
        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        // Trim the output expression and return it
        return output.toString().trim();
    }

    public static double applyOperator(String operator, double operand1, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }


    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    public static int precedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }


    public Calculator() {
        add(new JScrollPane(area), BorderLayout.NORTH);
        JPanel buttonpanel = new JPanel();
        buttonpanel.setLayout(new FlowLayout());

        for (int i=0;i<10;i++)
            buttonpanel.add(digits[i]);

        for (int i=0;i<6;i++)
            buttonpanel.add(operators[i]);

        add(buttonpanel, BorderLayout.CENTER);
        area.setForeground(Color.BLACK);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);

        for (int i=0;i<10;i++) {
            int finalI = i;
            digits[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    area.append(Integer.toString(finalI));
                }
            });
        }

        for (int i=0;i<6;i++){
            int finalI = i;
            operators[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (finalI == 5)
                        area.setText("");
                    else
                    if (finalI == 4) {
                        try {
                            double result = evaluatePolishNotation(area.getText());
                            area.append(" = " + result);
                        } catch (Exception e) {
                            area.setText(" !!!Probleme!!! ");
                        }
                    }
                    else {
                        area.append(" " + oper_values[finalI] + " ");
                        operator = oper_values[finalI].charAt(0);
                    }
                }
            });
        }
    }
}