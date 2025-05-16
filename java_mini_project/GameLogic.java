package java_mini_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic implements GameEngine {
    private List<Apple> apples;//등장하는 사과 리스트
    private int timeLeft;//남은 시간
    private int score;// 점수
    private Random rand = new Random();// 랜덤수 생성

    public GameLogic() {// 게임 초기 상태
        apples = new ArrayList<>();
        timeLeft = 100;
        score = 0;
        spawnApples();
    }

    public List<Apple> getApples() {
        return apples;
    }

    public int getTimeLeft() {//제한시간 설정
        return timeLeft;
    }

    public int getScore() {
        return score;
    }

    public void decrementTime() {// 타이머
    	   if (timeLeft > 0) {
    	        timeLeft--;
    	    }
    }

    public void spawnApples() {//게임 시작 시 사과 배치 
        apples.clear();
        int cols = 15;
        int rows = 10;
        int spacingX = 45;
        int spacingY = 45;
        int offsetX = 50;
        int offsetY = 100;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = offsetX + col * spacingX;
                int y = offsetY + row * spacingY;
                int num = rand.nextInt(9) + 1; // 1~9
                apples.add(new Apple(x, y, num));
            }
        }
    }

    public void checkSum() {//사과들의 숫자의 합
        int sum = 0;
        int count = 0;

        for (Apple a : apples) {
            if (a.selected) {
                sum += a.number;
                count++;
            }
        }

        if (sum == 10) {
            apples.removeIf(a -> a.selected);
            score += count * 10; // 선택된 사과 개수만큼 점수
        } else {
            // 합이 10이 아닐 경우 예외 던지거나, 경고 메시지 표시
            throw new InvalidSumException("합이 10이 아닙니다! 다시 시도해보세요.");
        }
    }
}