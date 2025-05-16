package java_mini_project;

import java.util.List;

public interface GameEngine {
    List<Apple> getApples();
    int getTimeLeft();
    int getScore();
    void decrementTime();
    void checkSum();
}