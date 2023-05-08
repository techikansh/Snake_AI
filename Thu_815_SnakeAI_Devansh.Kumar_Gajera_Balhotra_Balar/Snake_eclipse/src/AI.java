import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


class Snakebody{
    int snake_x_coordinate;
    int snake_y_coordinate;

    public Snakebody(int snake_x_coordinate, int snake_y_coordinate){
        this.snake_x_coordinate = snake_x_coordinate;
        this.snake_y_coordinate = snake_y_coordinate;
    }
    public int getSnake_x_coordinate(){
        return snake_x_coordinate;
    }
    public int getSnake_y_coordinate(){
        return snake_y_coordinate;
    }

}




public class AI extends JFrame{

    public  static JFrame  frame = new JFrame();
    AI() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        frame.add(new AIPanel());
        frame.setTitle("Snake AI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}

class AIPanel extends JPanel implements ActionListener{


	ImageIcon _apple = new ImageIcon(getClass().getClassLoader().getResource("apple.png"));
    JLabel label = new JLabel(_apple);
    JLabel label2 = new JLabel(_apple);
    JLabel apple = new JLabel(_apple);
    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("b_g.png"));
    Image obj;
    
    

    static boolean run_again;

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 40;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];

    final int a[] = new int[GAME_UNITS];
    final int s[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int bodyParts2 = 6;
    int applesEaten, applesEaten2;
    public int applesx ;
    public int applesy ;

    char direction = 'R';
    char direction2 = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    //audio
    URL hiss = this.getClass().getResource("hiss.wav");
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(hiss);
    Clip clip = AudioSystem.getClip();

    URL eat2 = this.getClass().getResource("eat2.wav");
    AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(eat2);
    Clip clip1 = AudioSystem.getClip();
    
    URL gameover_audio = this.getClass().getResource("gameover_audio.wav");
    AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(gameover_audio);
    Clip clip2 = AudioSystem.getClip();
    
    //gameover image
    ImageIcon gameover = new ImageIcon(getClass().getClassLoader().getResource("gameover.png"));
    JLabel gameoverimage = new JLabel(gameover);
    

    //Parths system
    ArrayList<Snakebody> snakes = new ArrayList<>();
    ArrayList<Snakebody> snakes2 = new ArrayList<>();


    AIPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        run_again = false;
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        clip.open(audioStream);
        clip1.open(audioStream1);
        clip2.open(audioStream2);

        obj = img.getImage();

        startGame();


    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        try {
            draw(g);
        } catch (InterruptedException | UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void draw(Graphics g) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {

        if(!running) gameOver(g);
        clip.start();

//        for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
//            g.setColor(Color.BLUE);
//            g.drawLine((i*UNIT_SIZE), (0), (i*UNIT_SIZE), (SCREEN_HEIGHT));
//            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//        }

        //painting background image
        g.drawImage(obj, 0, 0, 600, 600,this);



        //        painting apple as icon
        this.add(apple);
        apple.setBounds(applesx, applesy,25,25);



        for(int i = 0; i < bodyParts; i++){
            if(i == 0){
                g.setColor(Color.orange );
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

            }
            else {
                g.setColor(Color.orange);
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        if(bodyParts > 6){
            if(bodyParts == 7){
                for(int i = 0; i < bodyParts2; i++){
                    a[i] = 0;
                    s[i] = 0;
                }
            }
            for(int i = 0; i < bodyParts2; i++){
                g.setColor(Color.CYAN);
                g.fillOval(a[i], s[i], UNIT_SIZE, UNIT_SIZE);

            }
        }



        //Padding
//        for(int j = 0; j < 25; j++){
//            int x = 25, y = UNIT_SIZE*23;
//            g.setColor(Color.blue);
//            g.fillRect((x*j),y, UNIT_SIZE,UNIT_SIZE);
//        }
//        for(int j = 0; j < 25; j++){
//            int x = 25, y = UNIT_SIZE*22;
//            g.setColor(Color.blue);
//            g.fillRect((x*j),y, UNIT_SIZE,UNIT_SIZE);
//        }

        //Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(": " + applesEaten, 190+50, 580);


        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString(": " + applesEaten2, 190+140+50, 580);


        g.setColor(Color.orange);
        g.fillRect(178, 561, 22, 22);
        this.add(label);
        label.setBounds(206,557,30,30);
//        adding second time
        g.setColor(Color.CYAN);
        g.fillRect(322, 561, 22, 22);
        this.add(label2);
        label2.setBounds(300+50,557,30,30);


    }
    public void newApple(){
            applesx = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            applesy = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        for(int i = 0; i < bodyParts; i++) {
            if (applesx == x[i] && applesy == y[i] || applesx == x[0]|| applesy == y[0] || applesx == x[bodyParts-1]|| applesy == y[bodyParts-1] || applesx == 0 ||
                    applesx == a[i] && applesy == s[i] || applesx == a[0]|| applesy == s[0] || applesx == a[bodyParts2-1]|| applesy == s[bodyParts2-1] ){
                fixApple();
            }
        }
        for(int i = 0; i < 25; i++){
            if(applesx == UNIT_SIZE*i && applesy == UNIT_SIZE*22     ||      applesx == UNIT_SIZE*i && applesy == UNIT_SIZE*23){
                fixApple();
            }
        }

//        System.out.println("Apple x : " + applesx);
//        System.out.println("Apple y : " + applesy);
    }

    public void fixApple(){
        newApple();
    }
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        for(int i = bodyParts2; i > 0; i--){
            a[i] = a[i-1];
            s[i] = s[i-1];
        }
        int check = 0;

        if(applesx > x[0]){

            if (direction != 'L') {
                direction = 'R';
            }
            if(direction =='R'){
                boolean snakethere = false;
                for (int i = 0; i < bodyParts; i++) {
                    if (x[0]+25 == x[i] && y[0] == y[i] || x[0]+50 == x[i] && y[0]== y[i] ||
                            x[0]+75 == x[i] && y[0] == y[i]  ||
                            x[0] == 575 /*|| x[0]+100 == x[i] && y[0] == y[i]*/
                            || x[0]+25 == a[i] && y[0] == s[i] || x[0]+50 == a[i] && y[0]== s[i]
                            /*|| x[0]+75 == x[i] && y[0] == y[i]*/  ||  x[0] == 575){
                        snakethere = true;
                        break;
                    }
                }
                if(snakethere){
                    boolean snakethere1 = false;
                    for (int i = 0; i < bodyParts; i++) {
                        if (x[0] == x[i] && y[0]+25 == y[i] || x[0] == x[i] && y[0]+50 == y[i] ||
                                x[0] == x[i] && y[0]+75 == y[i]  ||
                                y[0] == 575 /*|| x[0] == x[i] && y[0]+100 == y[i]*/ ||
                                x[0] == a[i] && y[0]+25 == s[i] || x[0] == a[i] && y[0]+50 == s[i]
                                /*|| x[0] == x[i] && y[0]+75 == y[i]*/  ||  y[0] == 575){
                            snakethere1 = true;
                            break;
                        }
                    }
                    if(snakethere1){
                        direction = 'U';
                    }
                    else {
                        direction = 'D';
                    }
                    check = 1;
                }
                else{
                    check = 1;
                }
            }
        }

        if(check == 0) {
            if (applesx < x[0]) {
                if (direction != 'R') {
                        direction = 'L';
                }
                if (direction == 'L') {
                    boolean snakethere = false;
                    for (int i = 0; i < bodyParts; i++) {
                        if (x[0] - 25 == x[i] && y[0] == y[i] || x[0] - 50 == x[i] && y[0] == y[i] || x[0] - 75 == x[i] && y[0] == y[i] || x[0] == 0 /*|| x[0] - 75 == x[i] && y[0] == y[i]*/
                        || x[0] - 25 == a[i] && y[0] == s[i] || x[0] - 50 == a[i] && y[0] == s[i] /*|| x[0] - 75 == x[i] && y[0] == y[i]*/ || x[0] == 0) {
                            snakethere = true;
                        }
                    }
                    if (snakethere) {
                        boolean snakethere1 = false;
                        for (int i = 0; i < bodyParts; i++) {
                            if (x[0] == x[i] && y[0] + 25 == y[i] || x[0] == x[i] && y[0] + 50 == y[i] || x[0] == x[i] && y[0]+75== y[i] || y[0] == 575 /*|| x[0] == x[i] && y[0]+100== y[i]*/
                            || x[0] == a[i] && y[0] + 25 == s[i] || x[0] == a[i] && y[0] + 50 == s[i] /*|| x[0] == x[i] && y[0]+75== y[i]*/ || y[0] == 575) {
                                snakethere1 = true;
                                break;
                            }
                        }
                        if (snakethere1) {
                            direction = 'U';
                        } else {
                            direction = 'D';
                        }
                        check = 1;
                    }else{check = 1;}
                }
            }
        }

        if(check == 0) {
            if (applesy < y[0]) {
                if (direction != 'D') {
                    direction = 'U';
                }
                if (direction == 'U') {
                    boolean snakethere = false;
                    for (int i = 0; i < bodyParts; i++) {
                        if (x[0] == x[i] && y[0] - 25 == y[i] || x[0] == x[i] && y[0] - 50 == y[i] || x[0] == x[i] && y[0] - 75 == y[i] || y[0] == 0 /*|| x[0] == x[i] && y[0] - 100 == y[i]*/ ||
                                x[0] == a[i] && y[0] - 25 == s[i] || x[0] == a[i] && y[0] - 50 == y[i] /*|| x[0] == x[i] && y[0] - 75 == y[i]*/ || y[0] == 0) {
                            snakethere = true;
                        }
                    }
                    if (snakethere) {
                        boolean snakethere1 = false;
                        for (int i = 0; i < bodyParts; i++) {
                            if (x[0] - 25 == x[i] && y[0] == y[i] || x[0] - 50 == x[i] && y[0] == y[i] || x[0]-75 == x[i] && y[0] == y[i] || x[0] == 0 /*|| x[0]-100 == x[i] && y[0] == y[i]/*/ ||
                                    x[0] - 25 == a[i] && y[0] == s[i] || x[0] - 50 == a[i] && y[0] == s[i] /*|| x[0]-75 == x[i] && y[0] == y[i]*/ || x[0] == 0) {
                                snakethere1 = true;
                                break;
                            }
                        }
                        if (snakethere1) {
                            direction = 'R';
                        } else {
                            direction = 'L';
                        }
                        check = 1;
                    }else{check = 1;}

                }
            }
        }

        if(check == 0) {
            if (applesy > y[0]) {
                if (direction != 'U') {
                    direction = 'D';
                }
                if (direction == 'D') {
                    boolean snakethere = false;
                    for (int i = 0; i < bodyParts; i++) {
                        if (x[0] == x[i] && y[0] + 25 == y[i] || x[0] == x[i] && y[0] + 50 == y[i] || x[0] == x[i] && y[0] + 75 == y[i] || y[0] == 575 /*|| x[0] == x[i] && y[0] + 100 == y[i]*/ ||
                                x[0] == a[i] && y[0] + 25 == s[i] || x[0] == a[i] && y[0] + 50 == s[i]/* || x[0] == x[i] && y[0] + 75 == y[i]*/ || y[0] == 575) {
                            snakethere = true;
                            break;
                        }
                    }
                    if (snakethere) {
                        boolean snakethere1 = false;
                        for (int i = 0; i < bodyParts; i++) {
                            if (x[0] - 25 == x[i] && y[0] == y[i] || x[0] - 50 == x[i] && y[0] == y[i] || x[0] - 75 == x[i] && y[0] == y[i]  || x[0] == 0 /*|| x[0] - 100 == x[i] && y[0] == y[i] */ ||
                                    x[0] - 25 == a[i] && y[0] == s[i] || x[0] - 50 == a[i] && y[0] == s[i] /*|| x[0] - 75 == x[i] && y[0] == y[i] */ || x[0] == 0) {
                                snakethere1 = true;
                                break;
                            }
                        }
                        if (snakethere1) {
                            direction = 'R';
                        } else {
                            direction = 'L';
                        }
//                        check = 1;
                    }else{check = 1;}


                }
            }
        }



        //first snake
        for(int i = 0; i< bodyParts; i++){
            Snakebody s = new Snakebody(x[i], y[i]);
            snakes.add(s);
        }
        //second snake
        for(int i = 0; i< bodyParts2; i++){
            Snakebody s2 = new Snakebody(a[i], s[i]);
            snakes2.add(s2);
        }
        //first snake
//        A_Star_Algorithm ai = new A_Star_Algorithm(snakes, snakes2, applesx,applesy,snakes.get(0).getSnake_x_coordinate(),snakes.get(0).getSnake_y_coordinate(),applesEaten);
////        direction =  ai.find_neighbors(snakes.get(0).getSnake_x_coordinate(),snakes.get(0).getSnake_y_coordinate());
//        direction =  ai.find_neighbours2(/*snakes.get(0).getSnake_x_coordinate(),snakes.get(0).getSnake_y_coordinate()*/);

        //second snake
        A_Star_Algorithm ai2 = new A_Star_Algorithm(snakes2, snakes, applesx,applesy,snakes2.get(0).getSnake_x_coordinate(),snakes2.get(0).getSnake_y_coordinate(),applesEaten2);
//        algorithm ai2 = new algorithm(snakes, snakes2, applesx,applesy,snakes2.get(0).getSnake_x_coordinate(),snakes2.get(0).getSnake_y_coordinate(),applesEaten2);
        direction2 =  ai2.find_neighbours2(/*snakes2.get(0).getSnake_x_coordinate(),snakes2.get(0).getSnake_y_coordinate()*/);

        snakes2.clear();
        snakes.clear();






//        int check1 = 0;
//
//        if(applesx > a[0]){
//
//            if (direction2 != 'L') {
//                direction2 = 'R';
//            }
//            if(direction2 =='R'){
//                boolean snakethere = false;
//                for (int i = 0; i < bodyParts2; i++) {
//                    if (a[0]+25 == a[i] && s[0] == s[i] || a[0]+50 == a[i] && s[0]== s[i] || a[0]+75 == a[i] && s[0] == s[i]  ||  a[0] == 575 /*|| x[0]+100 == x[i] && y[0] == y[i]*/
//                            || a[0]+25 == x[i] && s[0] == y[i] || a[0]+50 == x[i] && s[0] == y[i] /*|| x[0]+75 == x[i] && y[0] == y[i]*/  ||  a[0] == 575){
////                        if(x[0]+UNIT_SIZE)
//                        snakethere = true;
//                        break;
//                    }
//                }
//                if(snakethere){
//                    boolean snakethere1 = false;
//                    for (int i = 0; i < bodyParts2; i++) {
//                        if (a[0] == a[i] && s[0]+25 == s[i] || a[0] == a[i] && s[0]+50 == s[i] || a[0] == a[i] && s[0]+75 == s[i]  ||  s[0] == 575 /*|| x[0] == x[i] && y[0]+100 == y[i]*/
//                                || a[0] == x[i] && s[0]+25 == y[i] || a[0] == x[i] && s[0]+50 == y[i] /*|| x[0] == x[i] && y[0]+75 == y[i]*/  ||  y[0] == 575){
//                            snakethere1 = true;
//                            break;
//                        }
//                    }
//                    if(snakethere1){
//                        direction2 = 'U';
//                    }
//                    else {
//                        direction2 = 'D';
//                    }
//                    check1 = 1;
//                }
//                else{
//                    check1 = 1;
//                }
//            }
//        }
//
//        if(check1 == 0) {
//            if (applesx < a[0]) {
//                if (direction2 != 'R') {
//                    direction2 = 'L';
//                }
//                if (direction2 == 'L') {
//                    boolean snakethere = false;
//                    for (int i = 0; i < bodyParts2; i++) {
//                        if (a[0] - 25 == a[i] && s[0] == s[i] || a[0] - 50 == a[i] && s[0] == s[i] || a[0] - 75 == a[i] && s[0] == s[i] || a[0] == 0 /*|| x[0] - 75 == x[i] && y[0] == y[i]*/
//                                || a[0] - 25 == x[i] && s[0] == y[i] || a[0] - 50 == x[i] && s[0] == y[i] /*|| x[0] - 75 == x[i] && y[0] == y[i]*/ || a[0] == 0) {
//                            snakethere = true;
//                        }
//                    }
//                    if (snakethere) {
//                        boolean snakethere1 = false;
//                        for (int i = 0; i < bodyParts2; i++) {
//                            if (a[0] == a[i] && s[0] + 25 == s[i] || a[0] == a[i] && s[0] + 50 == s[i] || a[0] == a[i] && s[0]+75== s[i] || s[0] == 575 /*|| x[0] == x[i] && y[0]+100== y[i]*/
//                                    || a[0] == x[i] && s[0] + 25 == y[i] || a[0] == x[i] && s[0] + 50 == y[i] /*|| x[0] == s[i] && y[0]+75== y[i]*/ || s[0] == 575) {
//                                snakethere1 = true;
//                                break;
//                            }
//                        }
//                        if (snakethere1) {
//                            direction2 = 'U';
//                        } else {
//                            direction2 = 'D';
//                        }
//                        check1 = 1;
//                    }else{check1 = 1;}
//                }
////
//            }
//        }
//
//        if(check1 == 0) {
//            if (applesy < s[0]) {
//                if (direction2 != 'D') {
//                    direction2 = 'U';
//                }
//                if (direction2 == 'U') {
//                    boolean snakethere = false;
//                    for (int i = 0; i < bodyParts2; i++) {
//                        if (a[0] == a[i] && s[0] - 25 == s[i] || a[0] == a[i] && s[0] - 50 == s[i] || a[0] == a[i] && s[0] - 75 == s[i] || s[0] == 0 /*|| x[0] == x[i] && y[0] - 100 == y[i]*/ ||
//                                a[0] == x[i] && s[0] - 25 == y[i] || a[0] == x[i] && s[0] - 50 == s[i] /*|| x[0] == x[i] && y[0] - 75 == y[i]*/ || s[0] == 0) {
//                            snakethere = true;
//                        }
//                    }
//                    if (snakethere) {
//                        boolean snakethere1 = false;
//                        for (int i = 0; i < bodyParts2; i++) {
//                            if (a[0] - 25 == a[i] && s[0] == s[i] || a[0] - 50 == a[i] && s[0] == s[i] || a[0]-75 == a[i] && s[0] == s[i] || a[0] == 0 /*|| x[0]-100 == x[i] && y[0] == y[i]/*/ ||
//                                    a[0] - 25 == x[i] && s[0] == y[i] || a[0] - 50 == x[i] && s[0] == y[i] /*|| x[0]-75 == x[i] && y[0] == y[i]*/ || a[0] == 0) {
//                                snakethere1 = true;
//                                break;
//                            }
//                        }
//                        if (snakethere1) {
//                            direction2 = 'R';
//                        } else {
//                            direction2 = 'L';
//                        }
//                        check1 = 1;
//                    }else{check1 = 1;}
//
//                }
//
//
//            }
//        }
//
//        if(check1 == 0) {
//            if (applesy > s[0]) {
//                if (direction2 != 'U') {
//                    direction2 = 'D';
//                }
//                if (direction2 == 'D') {
//                    boolean snakethere = false;
//                    for (int i = 0; i < bodyParts2; i++) {
//                        if (a[0] == a[i] && s[0] + 25 == s[i] || a[0] == a[i] && s[0] + 50 == s[i] || a[0] == a[i] && s[0] + 75 == s[i] || s[0] == 575 /*|| x[0] == x[i] && y[0] + 100 == y[i]*/ ||
//                                a[0] == x[i] && s[0] + 25 == y[i] || a[0] == x[i] && s[0] + 50 == y[i]/* || x[0] == x[i] && y[0] + 75 == y[i]*/ || s[0] == 575) {
//                            snakethere = true;
//                            break;
//                        }
//                    }
//                    if (snakethere) {
//                        boolean snakethere1 = false;
//                        for (int i = 0; i < bodyParts2; i++) {
//                            if (a[0] - 25 == a[i] && s[0] == s[i] || a[0] - 50 == a[i] && s[0] == s[i] || a[0] - 75 == a[i] && s[0] == s[i]  || a[0] == 0 /*|| x[0] - 100 == x[i] && y[0] == y[i] */ ||
//                                    a[0] - 25 == x[i] && s[0] == y[i] || a[0] - 50 == x[i] && s[0] == y[i] /*|| x[0] - 75 == x[i] && y[0] == y[i] */ || a[0] == 0) {
//                                snakethere1 = true;
//                                break;
//                            }
//                        }
//                        if (snakethere1) {
//                            direction2 = 'R';
//                        } else {
//                            direction2 = 'L';
//                        }
//
//                    }
//
//
//                }
//            }
//        }





        switch(direction){
            case 'U' :
                y[0] =y[0] - UNIT_SIZE;
                break;
            case 'D' :
                y[0] =y[0] + UNIT_SIZE;
                break;
            case 'L' :
                x[0] =x[0] - UNIT_SIZE;
                break;
            case 'R' :
                x[0] =x[0] + UNIT_SIZE;
                break;
        }

        switch(direction2){
            case 'U' :
                s[0] =s[0] - UNIT_SIZE;
                break;
            case 'D' :
                s[0] =s[0] + UNIT_SIZE;
                break;
            case 'L' :
                a[0] =a[0] - UNIT_SIZE;
                break;
            case 'R' :
                a[0] =a[0] + UNIT_SIZE;
                break;
        }

    }
    public void checkApple(){
        if((x[0] == applesx) && y[0] == applesy){
            clip1.start();
            applesEaten++;
            bodyParts++;
            newApple();
        }
        else{
            clip1.stop();
        }
        if(a[0] == applesx && s[0] == applesy){
            clip1.start();
            applesEaten2++;
            bodyParts2++;
            newApple();
        }
    }
    public void checkCollisions(){
//        check if head collides with the body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                System.out.println("YELLOW snake OUT");
                running = false;
            }
//            if ((a[0] == x[i]) && (s[0] == y[i])) {
//                running = false;
//            }
//            if ((x[0] == a[i]) && (y[0] == s[i])) {
//                running = false;
//            }
//            if((a[0] == a[i]) && (s[0] == s[i])){
//                running = false;
//            }
        }
        for(int i = 1; i < bodyParts2; i++){
//            if((x[0] == x[i]) && (y[0] == y[i])){
//                running = false;
//            }
//            if ((a[0] == x[i]) && (s[0] == y[i])) {
//                running = false;
//            }
//            if ((x[0] == a[i]) && (y[0] == s[i])) {
//                running = false;
//            }
            if((a[0] == a[i]) && (s[0] == s[i])){
                System.out.println("BLUE snake OUT");
                running = false;
            }
        }

        if (x[0] < 0)               {running = false;}
        if (x[0] > SCREEN_WIDTH)    {running = false;}
        if (y[0] < 0)               {running = false;}
        if (y[0] > SCREEN_HEIGHT)   {running = false;}
        if (a[0] < 0)               {running = false;}
        if (a[0] > SCREEN_WIDTH)    {running = false;}
        if (s[0] < 0)               {running = false;}
        if (s[0] > SCREEN_HEIGHT)   {running = false;}


        if(!running) {
            clip.stop();
            clip.close();
            clip1.close();
            timer.stop();
//            startGame();
        }

    }
    public void gameOver(Graphics g) throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {

        clip2.start();
//        for(int i = 0; i < bodyParts; i++){
//            g.clearRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//        }
//        for(int i = 0; i < bodyParts2; i++){
//            g.clearRect(a[i], s[i], UNIT_SIZE, UNIT_SIZE);
//        }
        this.add(gameoverimage);
        gameoverimage.setBounds(0,0,600,500);


//        Thread.sleep(1000);
//        startGame();
//        new AI();
//        AI.frame.dispose();
//        AI.frame.dispose();
//        new MenuBar();
//        run_again = true;
//        AI.frame.dispose();
//        clip2.stop();
//        clip2.close();
//        new MenuBar();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running = true){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){

            switch(e.getKeyCode()){

                case KeyEvent.VK_LEFT:
                    if(direction != 'R') direction = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') direction = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') direction = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') direction = 'D';
                    break;
                case KeyEvent.VK_A:
                    if(direction2 != 'R') direction2 = 'L';
                    break;
                case KeyEvent.VK_D:
                    if(direction2 != 'L') direction2 = 'R';
                    break;
                case KeyEvent.VK_W:
                    if(direction2 != 'D') direction2 = 'U';
                    break;
                case KeyEvent.VK_S:
                    if(direction2 != 'U') direction2 = 'D';
                    break;
            }

        }
    }
}



