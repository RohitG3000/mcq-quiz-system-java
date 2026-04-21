import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class QuizApp extends JFrame implements ActionListener {

    JLabel questionLabel, timerLabel, qNoLabel;
    JRadioButton opt1, opt2, opt3, opt4;
    JButton nextButton, restartButton;
    ButtonGroup bg;

    ArrayList<String> questions = new ArrayList<>();
    ArrayList<String[]> options = new ArrayList<>();
    ArrayList<Integer> answers = new ArrayList<>();

    int current = 0, score = 0, timeLeft = 10;
    boolean answered = false;

    javax.swing.Timer timer;

    Clip timerClip;   // controls timer sound

    QuizApp() {
        setTitle("Quiz App");
        setSize(700, 450);
        setLayout(null);
        getContentPane().setBackground(new Color(245, 247, 250));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Font qFont = new Font("Segoe UI", Font.BOLD, 18);
        Font optFont = new Font("Segoe UI", Font.PLAIN, 16);

        qNoLabel = new JLabel("Q1/10");
        qNoLabel.setBounds(50, 10, 100, 30);
        qNoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(qNoLabel);

        timerLabel = new JLabel("Time: 10");
        timerLabel.setBounds(580, 10, 100, 30);
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(timerLabel);

        questionLabel = new JLabel();
        questionLabel.setBounds(50, 40, 600, 30);
        questionLabel.setFont(qFont);
        add(questionLabel);

        opt1 = new JRadioButton();
        opt2 = new JRadioButton();
        opt3 = new JRadioButton();
        opt4 = new JRadioButton();

        opt1.setBounds(60, 100, 500, 30);
        opt2.setBounds(60, 140, 500, 30);
        opt3.setBounds(60, 180, 500, 30);
        opt4.setBounds(60, 220, 500, 30);

        opt1.setFont(optFont);
        opt2.setFont(optFont);
        opt3.setFont(optFont);
        opt4.setFont(optFont);

        add(opt1); add(opt2); add(opt3); add(opt4);

        bg = new ButtonGroup();
        bg.add(opt1); bg.add(opt2); bg.add(opt3); bg.add(opt4);

        nextButton = new JButton("Next");
        nextButton.setBounds(300, 300, 120, 35);
        nextButton.addActionListener(this);
        add(nextButton);

        restartButton = new JButton("Restart");
        restartButton.setBounds(300, 340, 120, 35);
        restartButton.addActionListener(e -> restartQuiz());
        restartButton.setVisible(false);
        add(restartButton);

        loadFromFile();

        if (questions.size() == 0) {
            JOptionPane.showMessageDialog(this, "No questions loaded!");
            System.exit(0);
        }

        loadQuestion();
        startTimer();

        setVisible(true);
    }

    void loadFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("questions.txt"));
            String line;

            while ((line = br.readLine()) != null) {
                String parts[] = line.split("\\|");

                questions.add(parts[0]);
                options.add(new String[]{parts[1], parts[2], parts[3], parts[4]});
                answers.add(Integer.parseInt(parts[5]));
            }
            br.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file");
        }
    }

    void loadQuestion() {
        questionLabel.setText(questions.get(current));
        String[] opt = options.get(current);

        opt1.setText(opt[0]);
        opt2.setText(opt[1]);
        opt3.setText(opt[2]);
        opt4.setText(opt[3]);

        qNoLabel.setText("Q" + (current + 1) + "/" + questions.size());

        bg.clearSelection();
        resetColors();
        timeLeft = 10;
        timerLabel.setText("Time: " + timeLeft);
    }

    void startTimer() {
        timer = new javax.swing.Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time: " + timeLeft);

            playSound("timer.wav");

            if (timeLeft <= 3) {
                timerLabel.setForeground(Color.RED);
            } else {
                timerLabel.setForeground(Color.BLACK);
            }

            if (timeLeft <= 0 && !answered) {
                playSound("wrong.wav");
                nextQuestion();
            }
        });
        timer.start();
    }

    void nextQuestion() {

        if (!answered) {

            int selected = -1;

            if (opt1.isSelected()) selected = 0;
            if (opt2.isSelected()) selected = 1;
            if (opt3.isSelected()) selected = 2;
            if (opt4.isSelected()) selected = 3;

            int correct = answers.get(current);

            getOptionButton(correct).setForeground(Color.GREEN);

            if (selected != correct && selected != -1) {
                getOptionButton(selected).setForeground(Color.RED);
                playSound("wrong.wav");
            } else if (selected == correct) {
                score++;
                playSound("correct.wav");
            }

            answered = true;

            new javax.swing.Timer(2000, e -> {
                ((javax.swing.Timer)e.getSource()).stop();
                answered = false;
                current++;

                if (current < questions.size()) {
                    loadQuestion();
                } else {
                    showResult();
                }
            }).start();
        }
    }

    void showResult() {
        timer.stop();
        getContentPane().removeAll();

        int total = questions.size();
        int percent = (score * 100) / total;

        JLabel result = new JLabel("Score: " + score + "/" + total + " (" + percent + "%)");
        result.setBounds(200, 120, 300, 30);
        result.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(result);

        String grade;
        if (percent >= 80) grade = "Excellent";
        else if (percent >= 50) grade = "Good";
        else grade = "Needs Improvement";

        JLabel gradeLabel = new JLabel("Grade: " + grade);
        gradeLabel.setBounds(250, 170, 200, 30);
        gradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(gradeLabel);

        restartButton.setVisible(true);
        add(restartButton);

        repaint();
    }

    void restartQuiz() {
        current = 0;
        score = 0;

        getContentPane().removeAll();

        add(qNoLabel);
        add(timerLabel);
        add(questionLabel);
        add(opt1); add(opt2); add(opt3); add(opt4);
        add(nextButton);

        restartButton.setVisible(false);

        loadQuestion();
        startTimer();

        repaint();
    }

    JRadioButton getOptionButton(int i) {
        switch (i) {
            case 0: return opt1;
            case 1: return opt2;
            case 2: return opt3;
            case 3: return opt4;
        }
        return null;
    }

    void resetColors() {
        opt1.setForeground(Color.BLACK);
        opt2.setForeground(Color.BLACK);
        opt3.setForeground(Color.BLACK);
        opt4.setForeground(Color.BLACK);
    }

    void playSound(String fileName) {
        try {
            if (fileName.equals("timer.wav")) {

                if (timerClip != null && timerClip.isRunning()) {
                    timerClip.stop();
                    timerClip.close();
                }

                File file = new File("sounds/" + fileName);
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                timerClip = AudioSystem.getClip();
                timerClip.open(audio);

                FloatControl volume = (FloatControl) timerClip.getControl(FloatControl.Type.MASTER_GAIN);
                volume.setValue(-10.0f);

                timerClip.start();

            } else {
                File file = new File("sounds/" + fileName);
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audio);
                clip.start();
            }

        } catch (Exception e) {
            System.out.println("Sound error");
        }
    }

    public void actionPerformed(ActionEvent e) {
        nextQuestion();
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}