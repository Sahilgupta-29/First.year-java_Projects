import javax.swing.*;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

public class ScientificCalculator {
    private JTextArea display;
    private JPanel panel;
    private boolean isSceintificMode = false;
    private boolean isRadianMode = true;
    private JFrame frame;
    public static void main(String[] args) {
        new ScientificCalculator().createCalculator();
    }

    public void createCalculator() {
        frame = new JFrame("|| Scientific Calculator ||");
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400, 500);

        display = new JTextArea(2, 20);
        display.setEditable(false);
        display.setFont(new Font("Roboto Mono", Font.BOLD, 26));
        display.setBackground(Color.LIGHT_GRAY);
        display.setForeground(Color.BLACK);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        frame.add(new JScrollPane(display), BorderLayout.NORTH);

        JButton toogleMode = new JButton("Standard");
        toogleMode.setFont(new Font("Roboto Mono", Font.PLAIN, 14));
        toogleMode.addActionListener(e -> {
            isSceintificMode = !isSceintificMode;
            toogleMode.setText(isSceintificMode ? "Scientific" : "Standard");
            frame.setSize(isSceintificMode ? 600 : 400, 500);
            updateButtons();
        });

        JButton angles = new JButton("Rad");
        angles.setFont(new Font("Roboto Mono", Font.PLAIN, 14));
        angles.addActionListener(e -> {
            isRadianMode = !isRadianMode;
            angles.setText(isRadianMode ? "Rad" : "Deg");
        });

        JPanel tooglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tooglePanel.add(toogleMode);
        tooglePanel.add(angles);
        frame.add(tooglePanel, BorderLayout.SOUTH);

        panel = new JPanel(new GridLayout(5, 4, 8, 8));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(panel, BorderLayout.CENTER);
        updateButtons();
        frame.setVisible(true);
    }

    private void updateButtons() {
        panel.removeAll();

        String[] basicButtons = {
                "AC", "DEL", "%", "÷",
                "7", "8", "9", "×",
                "4", "5", "6", "–",
                "1", "2", "3", "+",
                "()", "0", ".", "="
        };
        for (String label : basicButtons) {
            addButtons(label);
        }

        if (isSceintificMode) {
            String[] scientificButtons = {
                    "sin", "cos", "tan", "√",
                    "log", "ln", "π", "e",
                    "x²", "x³", "x^y", "10^x",
                    "sin⁻¹", "cos⁻¹", "tan⁻¹", "ⁿ√x",
                    "n!", "(", ")", "|x|"
            };
            for (String label : scientificButtons) {
                addButtons(label);
            }
            panel.setLayout(new GridLayout(0, 4, 8, 8));
        } else {
            panel.setLayout(new GridLayout(5, 4, 8, 8));
        }
        frame.revalidate();
        frame.repaint();

    }

        private void addButtons(String lable){

            JButton btns = new JButton(lable);
            btns.setFont(new Font("Roboto Mono", Font.BOLD, 20));
            btns.setFocusPainted(false);

            if("AC DEL ÷ × – + = sin cos tan √ log ln π e x² x³ x^y 10^x sin⁻¹ cos⁻¹ tan⁻¹ ⁿ√x n! |x|".contains(lable)){
            btns.setBackground(new Color(255, 153, 51));
            btns.setForeground(Color.WHITE);
            }
            else{
                btns.setBackground(Color.WHITE);
                btns.setForeground(Color.BLACK);
            }

            btns.addActionListener(e -> Handlebuttons(lable));
            panel.add(btns);
        }

    private void Handlebuttons (String label){
        String current = display.getText();

        switch (label){
            case "AC" :
                display.setText("");
                break;

            case "DEL" :
                if(!current.isEmpty()){
                    display.setText(current.substring(0, current.length()-1));
                }
                break;

            case "=" :
                try{
                    String result = evaluate(current);
                    display.setText(result);
                }catch(Exception exp){
                    display.setText("Error");
            }
                break;

            case "()":
                if (current.chars().filter(ch -> ch == '(').count() > current.chars().filter(ch -> ch == ')').count()) {
                    display.setText(current + ")");
                } else {
                    display.setText(current + "(");
                }
                break;
            case "π":
                display.setText(current + Math.PI);
                break;
            case "e":
                display.setText(current + Math.E);
                break;
            case "x²":
                display.setText(current + "^2");
                break;
            case "x³":
                display.setText(current + "^3");
                break;
            case "n!":
                try {
                    double num = Double.parseDouble(current);
                    if (num < 0 || num != (int)num) {
                        display.setText("Error");
                    } else {
                        display.setText(String.valueOf(factorial((int)num)));
                    }
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
                break;
            case "|x|":
                try {
                    double num = Double.parseDouble(current);
                    display.setText(String.valueOf(Math.abs(num)));
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
                break;
            case "√":
                display.setText(current + "sqrt(");
                break;
            case "ⁿ√x":
                display.setText(current + "^(1/");
                break;
            default:
                if (label.matches("sin|cos|tan|sin⁻¹|cos⁻¹|tan⁻¹|log|ln|10\\^x")) {
                    display.setText(current + label + "(");
                } else {
                    display.setText(current + label);
                }
        }
    }

    private String evaluate(String expressions){
        expressions = expressions.replace("×", "*");
        expressions = expressions.replace("÷", "/");
        expressions = expressions.replace("–", "-");
        expressions = expressions.replace("%", "/100.0");
        expressions = expressions.replace("π", String.valueOf(Math.PI));
        expressions = expressions.replace("e", String.valueOf(Math.E));
        expressions = expressions.replace("√", "sqrt");
        expressions = expressions.replace("sin⁻¹", "asin");
        expressions = expressions.replace("cos⁻¹", "acos");
        expressions = expressions.replace("tan⁻¹", "atan");
        expressions = expressions.replace("log", "log10");
        expressions = expressions.replace("ln", "log");
        expressions = expressions.replace("10^x", "pow(10,");
        expressions = expressions.replace("x²", "^2");
        expressions = expressions.replace("x³", "^3");
        expressions = expressions.replace("x^y", "^");
        expressions = expressions.replace("ⁿ√x", "^(1/");

        if(!isRadianMode){
            expressions = expressions.replace("sin(","sin(toRadians(");
            expressions = expressions.replace("cos(","cos(toRadians(");
            expressions = expressions.replace("tan(","tan(toRadians(");
        }

        try {

            try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("Js");
            if(engine != null){
                engine.eval("function toRadians(angle) { return angle * Math.PI / 180; }");
                engine.eval("function asin(x) { return Math.asin(x); }");
                engine.eval("function acos(x) { return Math.acos(x); }");
                engine.eval("function atan(x) { return Math.atan(x); }");
                engine.eval("function log10(x) { return Math.log10(x); }");
                engine.eval("function sqrt(x) { return Math.sqrt(x); }");

                Object result = engine.eval(expressions);
                return formatResult(result.toString());
            }
            }catch (Exception e){
                System.out.println("Javascript engine not available...");
            }
        return evaluateBasicExpressions(expressions);
        }catch(Exception exp){
    exp.printStackTrace();
    return "Error";
        }
    }

    private String formatResult(String result) {
        try {
            double value = Double.parseDouble(result);
            if (value == (int) value) {
                return String.valueOf((int) value);  // 2.0 → 2
            } else {
                return String.valueOf(value);        // 2.5 → 2.5
            }
        } catch (NumberFormatException e) {
            return "Error";
        }
    }

    private String evaluateBasicExpressions(String expressions){
       try{
           if (expressions.matches("[0-9+\\-*/.^() ]+")) {
               return String.valueOf(eval(expressions));
           }
           return "Error: Complex expr";
       } catch (Exception e) {
           return "Error";
       }
       }
    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = isRadianMode ? Math.sin(x) : Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = isRadianMode ? Math.cos(x) : Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = isRadianMode ? Math.tan(x) : Math.tan(Math.toRadians(x));
                    else if (func.equals("log")) x = Math.log(x);
                    else if (func.equals("log10")) x = Math.log10(x);
                    else if (func.equals("asin")) x = isRadianMode ? Math.asin(x) : Math.toDegrees(Math.asin(x));
                    else if (func.equals("acos")) x = isRadianMode ? Math.acos(x) : Math.toDegrees(Math.acos(x));
                    else if (func.equals("atan")) x = isRadianMode ? Math.atan(x) : Math.toDegrees(Math.atan(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    private int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }
    }

