package java_mini_project;


public class Apple {
    int x, y;
    int number;
    boolean selected;// 드래그로 인한 선택 여부

    public Apple(int x, int y, int number) {//사과 초반 위치 및 할당된 숫자 설정
        this.x = x;
        this.y = y;
        this.number = number;
        this.selected = false;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}