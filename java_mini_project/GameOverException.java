package java_mini_project;

public class GameOverException extends RuntimeException {//게임 종료를 위한 예외 처리
    public GameOverException(String message) {
        super(message);
    }
}