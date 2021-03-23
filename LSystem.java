import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.Line;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import LSystem.Rule.Angle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;

/**
 * LSystem, wip
 */
public class LSystem {
    public static void main(String[] args) {
        LSystem ls = new LSystem();
        ls.draw(ls.generate("X", 6));
    }

    private JFrame frame;
    private Surface surface;

    private Map<Character, Rule> rules;

    public LSystem() {
        createUI();
        createRules();
    }
    private void createUI() {
        frame = new JFrame("LSystem Visualization");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 750);
        frame.setVisible(true);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setResizable(false);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JLabel alphabetLabel = new JLabel("Alphabet: X, F, -, +, []");
        JLabel axiomLabel = new JLabel("Axiom: X");
        JLabel rulesLabel = new JLabel("Rules:  X -> F+[[X]-X]-F[-FX]+X | F -> FF | - -> Rotate left | + -> Rotate right | [] Save state, restore state");
        topPanel.add(alphabetLabel);
        topPanel.add(axiomLabel);
        topPanel.add(rulesLabel);
        frame.add(topPanel, BorderLayout.NORTH);
        surface = new Surface();
        frame.add(surface, BorderLayout.CENTER);
    }

    private void createRules() {
        rules = new HashMap<>();
        // Rule: X, Y, Angle, Sentence
        rules.put('X', new Rule(0.0, Rule.Angle.NONE, "F+[[X]-X]-F[-FX]+X"));
        rules.put('F', new Rule(1.0, Rule.Angle.NONE, "FF"));
        rules.put('-', new Rule(0.75, Rule.Angle.RIGHT, ""));
        rules.put('+', new Rule(0.75, Rule.Angle.LEFT, ""));
    }

    public String generate(String sentence, int depth) {
        StringBuilder next = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            for (int j = 0; j < sentence.length(); j++) {
                if (rules.containsKey(sentence.charAt(j))) {
                    next.append(rules.get(sentence.charAt(j)).str);
                }
            }
            String s = next.toString();
            next.setLength(0);
            sentence = s;
        }
        return sentence;
    }

    // private String generateRec(String sentence, StringBuilder sentenceSb, int i, int depth, int maxDepth) {
    //     if (depth >= maxDepth) return sentence;
    //     if (i >= sentence.length()) {
    //         String next = sentenceSb.toString();
    //         sentenceSb.setLength(0);
    //         return generateRec(next, sentenceSb, 0, depth + 1, maxDepth);
    //     }
    //     if (rules.containsKey(sentence.charAt(i))) {
    //         sentenceSb.append(rules.get(sentence.charAt(i)).str);
    //     }
    //     return generateRec(sentence, sentenceSb, i + 1, depth, maxDepth);
    // }

    public void draw(String sentence) {
        double x = 500; // Middle
        double y = 675; // Bottom
        Rule.Angle angle = Rule.Angle.NONE;
        double angleLength = 0;
        Deque<State> states = new ArrayDeque<>();
        for (int i = 0; i < sentence.length(); i++) {
            char c = sentence.charAt(i);
            double newX = x;
            double newY = y;
            if (c == '[') {
                states.addLast(new State(newX, newY, angle));
            } else if (c == ']') {
                State state = states.removeLast();
                newX = state.x;
                newY = state.y;
                angle = state.angle;
            } else {
                Rule rule = rules.get(c);
                if (rule.angle != Rule.Angle.NONE) {
                    angle = rule.angle;
                    angleLength = rule.length;
                } else if (isNotZero(rule.length)) {
                    if (angle == Rule.Angle.LEFT) {
                        newX -= angleLength;
                    } else if (angle == Rule.Angle.RIGHT) {
                        newX += angleLength;
                    }
                    newY -= rule.length;
                    surface.addLine(x, y, newX, newY);
                }
            }
            x = newX;
            y = newY;
        }
    }

    // @SuppressWarnings("java:S107") // Recursive, needs parameters
    // private void drawRec(int x, int y, double angle, String sentence, int i, Deque<State> saved) {
    //     if (i >= sentence.length()) return;
    //     int newX = x;
    //     int newY = y;
    //     if (sentence.charAt(i) == '[') {
    //         saved.addLast(new State(x, y, angle));
    //     } else if (sentence.charAt(i) == ']') {
    //         State state = saved.removeLast();
    //         newX = state.x;
    //         newY = state.y;
    //         angle = state.angle;
    //     } else {
    //         Rule rule = rules.get(sentence.charAt(i));
    //         if (rule != null) {
    //             if (isNotZero(rule.angle)) angle += rule.angle;
    //             if (isNotZero(rule.xAdd) || isNotZero(rule.yAdd)) {
    //                 if (angle < 0) {
    //                     newX -= rule.xAdd + (isNotZero(angle) ? (Math.cos(Math.toRadians(angle)) * 10) : 0);
    //                     newY -= rule.yAdd + (isNotZero(angle) ? (Math.sin(Math.toRadians(angle)) * 10) : 0);
    //                 } else {
    //                     newX += rule.xAdd + (isNotZero(angle) ? (Math.cos(Math.toRadians(angle)) * 10) : 0);
    //                     newY -= rule.yAdd + (isNotZero(angle) ? (Math.sin(Math.toRadians(angle)) * 10) : 0);
    //                 }
    //                 surface.addLine(x, y, newX, newY);
    //             }
    //         }
    //     }
    //     drawRec(newX, newY, angle, sentence, i + 1, saved);
    // }

    private boolean isNotZero(double n) {
        return n < -0.001 || n > 0.001;
    }

    class State {
        public double x;
        public double y;
        public Rule.Angle angle;

        public State(double x, double y, Rule.Angle angle) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }
    }

    class Surface extends JPanel {
        private static final long serialVersionUID = 1L;
        private transient List<Line> lines = new ArrayList<>();
        
        public void addLine(double sX, double sY, double eX, double eY) {
            lines.add(new Line(sX, sY, eX, eY));
        }

        @Override
        public void paintComponent(Graphics g) {
            for (Line line : lines) {
                line.paint(g);
            }
        }
    }

    class Line {
        private double sX, sY, eX, eY;

        public Line(double sX, double sY, double eX, double eY) {
            this.sX = sX;
            this.sY = sY;
            this.eX = eX;
            this.eY = eY;
        }
        
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D)g;
            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(new Line2D.Double(sX, sY, eX, eY));
        }
    }

    static class Rule {
        public double length;
        public Angle angle;
        public String str;

        public Rule(double length, Angle angle, String str) {
            this.length = length;
            this.angle = angle;
            this.str = str;
        }

        enum Angle {
            NONE,
            LEFT,
            RIGHT
        }
    }
}