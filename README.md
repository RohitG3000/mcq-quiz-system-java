# MCQ Quiz System using Java Swing

A desktop-based quiz application developed in Java using Swing for GUI.  
This application presents multiple-choice questions with a timer, provides instant feedback using colors and sounds, and displays final score with grading.

---

## Features

- Multiple-choice questions (MCQ)
- Timer for each question
- Dynamic question loading from file
- Sound effects (correct, wrong, timer)
- Answer highlighting (green/red)
- Score calculation with percentage
- Performance grading
- Restart quiz option

---

## Technologies Used

- Java (Core Java)
- Java Swing (GUI)
- File Handling (BufferedReader)
- Collections (ArrayList)
- Event Handling (ActionListener)
- Java Sound API

---

## Project Structure

```
Quiz Project/
│
├── QuizApp.java
├── questions.txt
├── sounds/
│   ├── correct.wav
│   ├── wrong.wav
│   └── timer.wav
├── app_screenshots/
│   ├── correct.png
│   ├── wrong.png
│   ├── result.png
│   └── quiz-start.png
```

---

## Customization

Questions can be modified by editing the `questions.txt` file.  
Users can add, remove, or change questions without modifying the code.

---

## Screenshots

Available in the `app_screenshots/` folder.

---

## How to Run


javac QuizApp.java
java QuizApp


---

## Author

Rohit Gupta

## Note

This project is created for academic purposes and demonstrates core Java concepts with GUI and audio integration.
