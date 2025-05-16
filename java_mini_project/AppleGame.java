package java_mini_project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppleGame extends JPanel implements MouseListener, MouseMotionListener {
	private boolean isGameOver = false;
    private GameLogic game;
    private JButton resetButton;
    private JButton returnButton;
    private boolean dragging = false;
    private int dragStartX, dragStartY, dragEndX, dragEndY;
    
    // 배너에 표시될 경고 메시지
    private String bannerMessage = "";
    private boolean showBanner = false;

    public AppleGame() {
    	setLayout(null);  // 수동 배치

    	// 초기화 버튼
    	resetButton = new JButton("다시 시작");
    	resetButton.setBounds(120, 10, 100, 30);
    	resetButton.addActionListener(e -> {
    	    game = new GameLogic();  // 게임 로직 다시 생성
    	    repaint();
    	});
    	add(resetButton);

    	// 메인 메뉴로 가는 버튼
    	returnButton = new JButton("메인 메뉴");
    	returnButton.setBounds(10,10,100,30);
    	returnButton.addActionListener(e -> {
    	    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
    	    frame.getContentPane().removeAll();  // 현재 화면 제거
    	    frame.add(new MainMenu());          // 메인 메뉴로 이동
    	    frame.revalidate();
    	    frame.repaint();
    	});
    	add(returnButton);
        game = new GameLogic();
        setPreferredSize(new Dimension(800, 600));
        addMouseListener(this);
        addMouseMotionListener(this);
        startGameTimer();
        
    }

    private void startGameTimer() {
        Timer gameTimer = new Timer(1000, e -> {
            try {
                game.decrementTime();
                if (game.getTimeLeft() <= 0) {
                    throw new GameOverException("게임 종료! 점수: " + game.getScore());
                }
                repaint();
            } catch (GameOverException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                ((Timer) e.getSource()).stop();
            }
        });
        gameTimer.start();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragStartX = e.getX();
        dragStartY = e.getY();
        dragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragEndX = e.getX();
        dragEndY = e.getY();
        dragging = false;

        try {
            // 합이 10이면 제거
            game.checkSum();
        } catch (InvalidSumException ex) {
            // 배너에 메시지를 설정하고 하단에 띄운다
            showBannerWithMessage(ex.getMessage());
        }

        // 드래그 끝났으니 선택 해제
        for (Apple apple : game.getApples()) {
            apple.setSelected(false);
        }

        repaint();
    }

    private void showBannerWithMessage(String message) {
        bannerMessage = message;
        showBanner = true;

        // 3초 후에 메시지를 숨긴다
        Timer hideBannerTimer = new Timer(3000, e -> {
            showBanner = false;
            repaint(); // 배너 숨기기
        });
        hideBannerTimer.setRepeats(false); // 한 번만 실행
        hideBannerTimer.start();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragEndX = e.getX();
        dragEndY = e.getY();

        updateSelectionBox(dragStartX, dragStartY, dragEndX, dragEndY);
        repaint();
    }

    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // 선택된 사과 업데이트 메서드
    private void updateSelectionBox(int x1, int y1, int x2, int y2) {
        for (Apple apple : game.getApples()) {
            if (apple.x >= Math.min(x1, x2) && apple.x <= Math.max(x1, x2) &&
                apple.y >= Math.min(y1, y2) && apple.y <= Math.max(y1, y2)) {
                apple.setSelected(true);
            } else {
                apple.setSelected(false);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 드래그 중일 때 드래그 박스를 그린다
        if (dragging) {
            g.setColor(new Color(0, 0, 255, 50)); // 반투명한 파란색
            g.fillRect(Math.min(dragStartX, dragEndX), Math.min(dragStartY, dragEndY),
                    Math.abs(dragEndX - dragStartX), Math.abs(dragEndY - dragStartY));
        }

        // 폰트 설정 (Arial Unicode MS로 변경)
        Font font = new Font("Arial Unicode MS", Font.BOLD, 18);
        g.setFont(font);

        // 사과 그리기
        for (Apple apple : game.getApples()) {
            g.setColor(apple.selected ? Color.YELLOW : new Color(255, 69, 0));
            int size = apple.selected ? 45 : 35;
            g.fillOval(apple.x - size / 2, apple.y - size / 2, size, size);

            // 그림자 추가
            g.setColor(new Color(0, 0, 0, 50));
            g.fillOval(apple.x - size / 2 + 5, apple.y - size / 2 + 5, size, size);

            // 숫자 그리기
            g.setColor(Color.WHITE);
            
            // 폰트 설정 후 숫자 그리기
            FontMetrics metrics = g.getFontMetrics();
            String numberText = Integer.toString(apple.number);
            int textWidth = metrics.stringWidth(numberText);
            int textHeight = metrics.getHeight();
            g.drawString(numberText, apple.x - textWidth / 2, apple.y + textHeight / 4);
        }

        // 타이머와 점수 표시
        g.setColor(Color.BLACK);
        g.drawString("Time: " + game.getTimeLeft(), 700, 20);
        g.drawString("Score: " + game.getScore(), 700, 40);

        // 배너 메시지 그리기
        if (showBanner) {
            g.setColor(new Color(255, 0, 0, 150));  // 빨간 배경
            g.fillRect(0, getHeight() - 30, getWidth(), 30);  // 배너 영역
            g.setColor(Color.WHITE);

            // 'Arial Unicode MS' 폰트로 배너 메시지 출력
            Font fontBanner = new Font("Arial Unicode MS", Font.PLAIN, 14);
            g.setFont(fontBanner);
            g.drawString(bannerMessage, 10, getHeight() - 10);
        }
    }
}