package java_mini_project;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Apple Game");
        MainMenu mainMenu = new MainMenu();  // 메인 메뉴 화면을 초기화
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainMenu);  // 메인 메뉴 화면을 프레임에 추가
        frame.setSize(800, 600);  // 창 크기 설정
        frame.setVisible(true);
    }
}