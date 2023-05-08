import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.sql.*;
import java.util.Scanner;




class startPanelSingle extends JFrame implements ActionListener{
    JLabel name = new JLabel("name : ");
    public static JTextField tf = new JTextField();
    public static String[] choices = { "blue","BLACK", "cyan","orange","lightgray","white", "yellow"};
   public static JComboBox<String> cb = new JComboBox<String>(choices);
    JButton next = new JButton("GAME!!!");
    JPanel newPanel = new JPanel();
    
    startPanelSingle(){
        this.setSize(600,200);
        this.setTitle("Snake SinglePlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
//        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        newPanel.setLayout(null);
        newPanel.setPreferredSize(new Dimension(500,200));
        next.addActionListener(this);
        newPanel.add(name);
        newPanel.add(tf);
        newPanel.add(cb);
        newPanel.add(next);

        name.setBounds(210, 40, 100,40);
        tf.setBounds(260, 45, 150,30);
//        cb.setBounds(350, 40, 150,40);
        next.setBounds(245, 90, 100,40);

        this.add(newPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == next){
            this.dispose();
            try {
                SinglePlayer singleplayer = new SinglePlayer();
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

public class SinglePlayer extends JFrame{
	
    SinglePlayer() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        this.setSize(600,650);
        this.add(new SinglePlayerPanel(), BorderLayout.CENTER);
        this.setTitle("Snake SinglePlayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}

class SinglePlayerPanel extends JPanel implements ActionListener{
	Scanner sc = new Scanner(System.in);

	//ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("b_g.png") );
	
	JLabel apple = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("apple.png")));
    JLabel label = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("apple.png")));

    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("b_g.png") );
//    ImageIcon img = new ImageIcon("b_g.png");

    Image obj;

    //gameover logo at the end
   
    
    ImageIcon gameover = new ImageIcon(getClass().getClassLoader().getResource("gameover.png"));
    JLabel gameoverimage = new JLabel(gameover);


    //    gameover sound
   URL gameover_audio = this.getClass().getResource("gameover_audio.wav");
//   File file2 = new File("Snake/gameover_audio.wav");
   AudioInputStream audioStream2 = AudioSystem.getAudioInputStream(gameover_audio);
   Clip clip2 = AudioSystem.getClip();


    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 70;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 4;
    int applesEaten ;
    public int applesx ;
    public int applesy ;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;



    JLabel name1 = new JLabel();

    URL hiss = this.getClass().getResource("hiss.wav");
//    File file = new File("Snake/hiss.wav");
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(hiss);
    Clip clip = AudioSystem.getClip();



    //eat audio
    URL eat2 = this.getClass().getResource("eat2.wav");
//    File file1 = new File("Snake/eat2.wav");
    AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(eat2);
    Clip clip1 = AudioSystem.getClip();



    SinglePlayerPanel() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        //setting background image
        obj = img.getImage();

        //starting audio clip


        clip.open(audioStream);
        clip1.open(audioStream1);
        clip2.open(audioStream2);

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

        //drawing background
        g.drawImage(obj, 0, 0, 600, 600,this);


//        painting apple as icon
        this.add(apple);
        apple.setBounds(applesx, applesy,25,25);

        for(int i = 0; i < bodyParts; i++){
                g.setColor(Color.cyan);
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }

        //Padding
//        for(int j = 0; j < 25; j++){
//            int x = 25, y = UNIT_SIZE*23;
//            g.setColor(Color.blue);
//            g.fillRect((x*j),y, UNIT_SIZE,UNIT_SIZE);
//        }
//
//        for(int j = 0; j < 25; j++){
//            int x = 25, y = UNIT_SIZE*22;
//            g.setColor(Color.blue);
//            g.fillRect((x*j),y, UNIT_SIZE,UNIT_SIZE);
//        }



        //name
        name1.setText(startPanelSingle.tf.getText() + " :");

        this.setFont(new Font("Ink", Font.BOLD, 80));
        this.add(name1);
        name1.setForeground(Color.white);
        name1.setBackground(Color.black);
        name1.setHorizontalAlignment(SwingConstants.RIGHT);
        name1.setBounds(200, 534, 120,80);

//        g.setColor(Color.WHITE);
//        g.setFont(new Font("Ink", Font.BOLD, 30));
//        g.drawString(startPanelSingle.tf.getText() + " :", 200, 585);


        //Score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("" + applesEaten, 325, 585);

//        score apple icon
        if(applesEaten > 9){
            this.add(label);
            label.setBounds(360,560,30,30);
        }
        else{
            this.add(label);
            label.setBounds(345,560,30,30);
        }
    }


    public void newApple(){
        applesx = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE;
        applesy = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE;

        for(int i = 0; i < bodyParts; i++) {
            if (applesx == x[i] && applesy == y[i]){
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
//        for(int i = bodyParts2; i > 0; i--){
//            a[i] = a[i-1];
//            s[i] = s[i-1];
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

    }
    public void checkApple(){
        if((x[0] == applesx) && y[0] == applesy){
            clip1.start();
            applesEaten++;
            bodyParts++;
            newApple();
        }
        else {
            clip1.stop();
        }
    }
    public void checkCollisions(){
//        check if head collides with the body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        if (x[0] < 0)               {running = false;}
        if (x[0] >= SCREEN_WIDTH)    {running = false;}
        if (y[0] < 0)               {running = false;}
        if (y[0] >= SCREEN_HEIGHT)   {running = false;}


        if(!running) {
            Database();
            timer.stop();
            clip.stop();
            clip.close();
            clip1.close();
        }

    }
    public void gameOver(Graphics g){

//        System.out.println(startPanelSingle.tf.getText() + " : " + applesEaten);
        clip2.start();
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
            }

        }
    }

    //Database code 2
    public void Database(){
//    	System.out.print("Enter Password to you Database server : ");
//    	String password = sc.nextLine();
        String mysql_db_url = "jdbc:mysql://127.0.0.1:3306/jdbc_snake_ai";       // url to connect, in this case is local but you can connect to an ip
        String mysql_db_user = "root";                                          // DB user name MYSQL
//        String mysql_pass = "devanshkumar";                                        //DB password
        String mysql_pass = SnakeGame.password;                                        //DB password

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(mysql_db_url, mysql_db_user, mysql_pass);
            Statement statement = connection.createStatement();

            String name = startPanelSingle.tf.getText();

            // Check if the name already exists in the database
            PreparedStatement checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM scoreboard WHERE name = ?");
            checkStmt.setString(1, name);
            ResultSet checkResult = checkStmt.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);

            // Insert or update the data in the table
            if (count == 0) {
                // The name does not already exist, so insert a new row
                PreparedStatement insertStmt = connection.prepareStatement("INSERT INTO scoreboard (name, score) VALUES (?, ?)");
                insertStmt.setString(1, name);
                insertStmt.setInt(2, applesEaten);
                insertStmt.executeUpdate();
                System.out.println("Inserted new user: " + name + " with score: " + applesEaten);
            } else {
                // The name already exists, so update the existing row
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE scoreboard SET score = ? WHERE name = ?");
                updateStmt.setInt(1, applesEaten);
                updateStmt.setString(2, name);
                updateStmt.executeUpdate();
                System.out.println("Updated user: " + name + " with new score: " + applesEaten);
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



