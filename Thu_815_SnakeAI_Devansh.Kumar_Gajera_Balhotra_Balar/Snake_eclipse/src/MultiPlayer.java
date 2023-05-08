import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.sql.*;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

class startPanelMultiPlayer extends JFrame implements ActionListener{
	
    JLabel name = new JLabel("name 1 : ");
    JLabel name2 = new JLabel("name 2 : ");
    public static JTextField tf = new JTextField();
    public static JTextField tf2 = new JTextField();
    public static String[] choices = { "blue","BLACK", "cyan","orange","lightgray","white", "yellow"};
    public static JComboBox<String> cb = new JComboBox<String>(choices);
    JButton next = new JButton("GAME!!!");
    JPanel newPanel = new JPanel();
    startPanelMultiPlayer(){
        this.setSize(400,400);
        this.setTitle("Snake MultiPlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
//        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        newPanel.setLayout(null);
        newPanel.setPreferredSize(new Dimension(400,400));

        next.addActionListener(this);

        newPanel.add(name);
        newPanel.add(name2);
        newPanel.add(tf);
        newPanel.add(tf2);
//        newPanel.add(cb);
        newPanel.add(next);

        name.setBounds(90, 90, 100,40);
        name2.setBounds(90, 160, 100,40);

        tf.setBounds(142, 95, 150,30);
        tf2.setBounds(142, 165, 150,30);

        next.setBounds(150, 220, 100,40);

        this.add(newPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == next){
            this.dispose();
            try {
                MultiPlayer multiplayer = new MultiPlayer();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}


public class MultiPlayer extends JFrame{
    MultiPlayer() throws UnsupportedAudioFileException, LineUnavailableException, IOException {

        this.add(new MultiPlayerPanel());
        this.setTitle("Snake MultiPlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

class MultiPlayerPanel extends JPanel implements ActionListener{
	
	ImageIcon _apple = new ImageIcon(getClass().getClassLoader().getResource("apple.png"));
	
    JLabel label = new JLabel(_apple);
    JLabel label2 = new JLabel(_apple);
    JLabel apple = new JLabel(_apple);
    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("b_g.png"));
    Image obj;
    




    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 60;
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
    

    JLabel name1 = new JLabel();
    JLabel name2 = new JLabel();

    MultiPlayerPanel() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
//        this.add(wood);
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        clip.open(audioStream);
        clip1.open(audioStream1);
        clip2.open(audioStream2);

        startGame();
        obj = img.getImage();

    }


    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){

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
//        g.setColor(Color.green);
//        g.fillOval(applesx, applesy, UNIT_SIZE, UNIT_SIZE);


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


        //name1
        name1.setText(startPanelMultiPlayer.tf.getText() + " :");
        this.setFont(new Font("Ink", Font.BOLD, 80));
        this.add(name1);
        name1.setForeground(Color.white);
        name1.setBackground(Color.black);
        name1.setHorizontalAlignment(SwingConstants.RIGHT);
        name1.setBounds(50, 534, 120,80);

        //name 2
        name2.setText(": " + startPanelMultiPlayer.tf2.getText());
        this.setFont(new Font("Ink", Font.BOLD, 80));
        this.add(name2);
        name2.setForeground(Color.white);
        name2.setBackground(Color.black);
        name2.setHorizontalAlignment(SwingConstants.LEFT);
        name2.setBounds(420, 534, 120,80);


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

        System.out.println("Apple x : " + applesx);
        System.out.println("Apple y : " + applesy);
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
            if ((a[0] == x[i]) && (s[0] == y[i])) {
                running = false;
            }
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
            if ((x[0] == a[i]) && (y[0] == s[i])) {
                running = false;
            }
            if((a[0] == a[i]) && (s[0] == s[i])){
                System.out.println("BLUE snake OUT");
                running = false;
            }
        }

        if (x[0] < 0)               {running = false;}
        if (x[0] >= SCREEN_WIDTH)    {running = false;}
        if (y[0] < 0)               {running = false;}
        if (y[0] >= SCREEN_HEIGHT)   {running = false;}
        
        if(bodyParts > 8){
            if (a[0] < 0)               {running = false;}
            if (a[0] >= SCREEN_WIDTH)    {running = false;}
            if (s[0] < 0)               {running = false;}
            if (s[0] >= SCREEN_HEIGHT)   {running = false;}
        }


        if(!running) {
            Database();
            clip.stop();
            clip.close();
            clip1.close();
            timer.stop();
        }

    }
    public void gameOver(Graphics g){
        clip2.start();
        for(int i = 0; i < bodyParts; i++){
            g.clearRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
        for(int i = 0; i < bodyParts2; i++){
            g.clearRect(a[i], s[i], UNIT_SIZE, UNIT_SIZE);
        }
        this.add(gameoverimage);
        gameoverimage.setBounds(0,0,600,500);

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


    public void Database(){
        String mysql_db_url = "jdbc:mysql://127.0.0.1:3306/jdbc_snake_ai";       // url to connect, in this case is local but you can connect to an ip
        String mysql_db_user = "root";                                          // DB user name MYSQL
//        String mysql_pass = "devanshkumar";                                        //DB password
      String mysql_pass = SnakeGame.password;  

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(mysql_db_url, mysql_db_user, mysql_pass);
            Statement statement = connection.createStatement();

            String name = startPanelMultiPlayer.tf.getText();
            String name2 = startPanelMultiPlayer.tf2.getText();
            String [] names = {name, name2};
            int [] applesEaten_array = {applesEaten, applesEaten2};

            for(int i = 0; i < 2; i++){
                // Check if the name already exists in the database
                int count = 0;
                PreparedStatement checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM scoreboard WHERE name = ?");
                checkStmt.setString(1, names[i]);
                ResultSet checkResult = checkStmt.executeQuery();
                checkResult.next();
                count = checkResult.getInt(1);

                // Insert or update the data in the table
                if (count == 0) {
                    // The name does not already exist, so insert a new row
                    PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO scoreboard (name, score) VALUES (?, ?)");
                    insertStmt.setString(1, names[i]);
                    insertStmt.setInt(2, applesEaten_array[i]);
                    insertStmt.executeUpdate();
                    System.out.println("Inserted new user: " + names[i] + " with score: " + applesEaten_array[i]);
                } else {
                    // The name already exists, so update the existing row
                    PreparedStatement updateStmt = connection.prepareStatement("UPDATE scoreboard SET score = ? WHERE name = ?");
                    updateStmt.setInt(1, applesEaten_array[i]);
                    updateStmt.setString(2, names[i]);
                    updateStmt.executeUpdate();
                    System.out.println("Updated user: " + names[i] + " with new score: " + applesEaten_array[i]);
                }
            }



            // Print all the data of the table
            PreparedStatement selectStmt = connection.prepareStatement("SELECT * FROM scoreboard ORDER BY score DESC");
            ResultSet result = selectStmt.executeQuery();

            while (result.next()) {
                System.out.println("Name: " + result.getString("name") + ", Score: " + result.getInt("score"));
            }

            // Find the maximum score in the table
            PreparedStatement maxStmt = connection.prepareStatement("SELECT MAX(score) as max_score FROM scoreboard");
            ResultSet maxResult = maxStmt.executeQuery();
            maxResult.next();
            int maxScore = maxResult.getInt("max_score");

            // Find the user with the maximum score
            PreparedStatement selectS = connection.prepareStatement("SELECT name FROM scoreboar  WHERE score = ?");
            selectS.setInt(1, maxScore);
            ResultSet res= selectStmt.executeQuery();
            res.next();
            String maxName = res.getString("name");

            // Print the result
            System.out.println("The PLAYER with the highest score is: " + maxName + " with score: " + maxScore);


            // Close the connection
            connection.close();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}





//public class MultiPlayer extends JFrame {
//
//    MultiPlayer(){
//
//        this.add(new MultiPlayerPanel());
//        this.setTitle("Snake");
//        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setResizable(false);
//        this.pack();
//        this.setVisible(true);
//        this.setLocationRelativeTo(null);
//    }
//}

//class MultiPlayerPanel extends JPanel implements ActionListener {
//    ImageIcon img = new ImageIcon("Snake/b_g.png");
//
//    Image obj;
//
//    static final int SCREEN_WIDTH = 600;
//    static final int SCREEN_HEIGHT = 600;
//    static final int UNIT_SIZE = 25;
//    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
//    static final int DELAY = 100;
//    final int x[] = new int[GAME_UNITS];
//    final int y[] = new int[GAME_UNITS];
//
//    final int a[] = new int[GAME_UNITS];
//
//    final int s[] = new int[GAME_UNITS];
//
//    int bodyParts = 6;
//    int bodyParts2 = 6;
//    int applesEaten ;
//    public int applesx ;
//    public int applesy ;
//
//    char direction = 'R';
//    char direction2 = 'R';
//    boolean running = false;
//    Timer timer;
//    Random random;
//
//
//    MultiPlayerPanel(){
//        random = new Random();
//        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
//        this.setBackground(Color.BLACK);
//        this.setFocusable(true);
//        this.addKeyListener(new MyKeyAdapter());
//        obj = img.getImage();
//        startGame();
//
//    }
//    public void startGame(){
//        newApple();
//        running = true;
//        timer = new Timer(DELAY, this);
//        timer.start();
//    }
//
//    public void paintComponent(Graphics g){
//        super.paintComponent(g);
//        draw(g);
//    }
//    public void draw(Graphics g){
//
//        if(!running) gameOver(g);
//
//        for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++){
//            g.setColor(Color.BLUE);
//            g.drawLine((i*UNIT_SIZE), (0), (i*UNIT_SIZE), (SCREEN_HEIGHT));
//            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
//        }
//
//        //drawing background
//        g.drawImage(obj, 0, 0, 600, 600,this);
//
//        g.setColor(Color.red);
//        g.fillOval(applesx, applesy, UNIT_SIZE, UNIT_SIZE);
//
//        for(int i = 0; i < bodyParts; i++){
//            if(i == 0){
//                g.setColor(Color.orange);
//                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//
//            }
//            else {
//                g.setColor(Color.blue);
//                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//            }
//
//        }
//
//
//        if(bodyParts > 6){
//            if(bodyParts == 7){
//                for(int i = 0; i < bodyParts2; i++){
//                    a[i] = 0;
//                    s[i] = 0;
//                }
//            }
//            for(int i = 0; i < bodyParts2; i++){
//                g.setColor(Color.CYAN);
//                g.fillRect(a[i], s[i], UNIT_SIZE, UNIT_SIZE);
//
//            }
//        }
//
//        //Score
//        g.setColor(Color.red);
//        g.setFont(new Font("Ink Free", Font.BOLD, 40));
//        FontMetrics metrics = getFontMetrics(g.getFont());
//        g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score : " + applesEaten))/2, g.getFont().getSize());
//
//    }
//    public void newApple(){
//        applesx = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
//        applesy = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;
//        System.out.println("Apple x : " + applesx);
//        System.out.println("Apple y : " + applesy);
//    }
//    public void move(){
//        for(int i = bodyParts; i > 0; i--){
//            x[i] = x[i-1];
//            y[i] = y[i-1];
//        }
//        for(int i = bodyParts2; i > 0; i--){
//            a[i] = a[i-1];
//            s[i] = s[i-1];
//        }
//
//
//        switch(direction){
//            case 'U' :
//                y[0] =y[0] - UNIT_SIZE;
//                break;
//            case 'D' :
//                y[0] =y[0] + UNIT_SIZE;
//                break;
//            case 'L' :
//                x[0] =x[0] - UNIT_SIZE;
//                break;
//            case 'R' :
//                x[0] =x[0] + UNIT_SIZE;
//                break;
//        }
//
//        switch(direction2){
//            case 'U' :
//                s[0] =s[0] - UNIT_SIZE;
//                break;
//            case 'D' :
//                s[0] =s[0] + UNIT_SIZE;
//                break;
//            case 'L' :
//                a[0] =a[0] - UNIT_SIZE;
//                break;
//            case 'R' :
//                a[0] =a[0] + UNIT_SIZE;
//                break;
//        }
//
//    }
//    public void checkApple(){
//        if((x[0] == applesx) && y[0] == applesy){
//            applesEaten++;
//            bodyParts++;
//            newApple();
//        }
//    }
//    public void checkCollisions(){
////        check if head collides with the body
//        for(int i = bodyParts; i > 0; i--){
//            if((x[0] == x[i]) && (y[0] == y[i])){
//                running = false;
//            }
//        }
//
//        if (x[0] < 0)               {running = false;}
//        if (x[0] > SCREEN_WIDTH)    {running = false;}
//        if (x[0] < 0)               {running = false;}
//        if (x[0] > SCREEN_HEIGHT)   {running = false;}
//
//
//        if(!running) timer.stop();
//
//    }
//    public void gameOver(Graphics g){
//        g.setColor(Color.red);
//        g.setFont(new Font("Ink Free", Font.BOLD, 75));
//        FontMetrics metrics = getFontMetrics(g.getFont());
//        g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!!"))/2, SCREEN_HEIGHT/2);
//
////        score display
//        g.setColor(Color.red);
//        g.setFont(new Font("Ink Free", Font.BOLD, 40));
//        FontMetrics metrics1 = getFontMetrics(g.getFont());
//        g.drawString("Score : " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score : " + applesEaten))/2, g.getFont().getSize());
//
//
//    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if(running = true){
//            move();
//            checkApple();
//            checkCollisions();
//        }
//        repaint();
//    }
//
//    public class MyKeyAdapter extends KeyAdapter{
//        @Override
//        public void keyPressed(KeyEvent e){
//
//            switch(e.getKeyCode()){
//
//                case KeyEvent.VK_LEFT:
//                    if(direction != 'R') direction = 'L';
//                    break;
//                case KeyEvent.VK_RIGHT:
//                    if(direction != 'L') direction = 'R';
//                    break;
//                case KeyEvent.VK_UP:
//                    if(direction != 'D') direction = 'U';
//                    break;
//                case KeyEvent.VK_DOWN:
//                    if(direction != 'U') direction = 'D';
//                    break;
//                case KeyEvent.VK_A:
//                    if(direction2 != 'R') direction2 = 'L';
//                    break;
//                case KeyEvent.VK_D:
//                    if(direction2 != 'L') direction2 = 'R';
//                    break;
//                case KeyEvent.VK_W:
//                    if(direction2 != 'D') direction2 = 'U';
//                    break;
//                case KeyEvent.VK_S:
//                    if(direction2 != 'U') direction2 = 'D';
//                    break;
//            }
//
//        }
//    }
//}
