import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;

public class MenuBar extends JPanel implements ActionListener {

    JPanel panel = new JPanel();
    JFrame frame = new JFrame();
    public static JButton b1,b2,b3,b4;
    JLabel label = new JLabel();


    ImageIcon background = new ImageIcon("bg.png");

    JLabel bg_l = new JLabel();

    
    // creating Snake image
    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("snake.png") );
    
//    File file = new File(url);
    URL url = this.getClass().getResource("bg.wav");
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
    Clip clip = AudioSystem.getClip();


    MenuBar() throws LineUnavailableException, UnsupportedAudioFileException, IOException {

//        if(AIPanel.run_again == true){
//            new AI();
//        }

        clip.open(audioStream);
        clip.start();

        //panel
        this.setPreferredSize(new Dimension(500,500));
        this.setLayout(null);
        bg_l.setIcon(background);
        this.add(bg_l);
        bg_l.setBounds(0,0,500,500);

        //Snake Label
        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        label.setIcon(img);
        label.setText("Snake Game");
        label.setFont(new Font("Monospaced", Font.PLAIN, 25));
        this.add(label);
        label.setBounds(130,50,300 , 128);
//        label.setBorder(border);


        //Snake MultiPlayer label

        b1 = new JButton("Multiplayer");
        b2 = new JButton("Single player");
        b3 = new JButton("AI");
        b4 = new JButton("High Score!");
        b1.setBounds(170,220, 150, 50);
        b2.setBounds(170,285, 150, 50);
        b3.setBounds(170,350, 150, 50);
        b4.setBounds(170,415, 150, 50);
        this.add(b1);
        this.add(b2);
        this.add(b3);
        this.add(b4);

        frame.add(this);
        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);


    }


    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource() == b1){
            clip.stop();
            clip.close();
            frame.dispose();
            startPanelMultiPlayer multiplayer = new startPanelMultiPlayer();
        }

        if(e.getSource() == b3){
            clip.stop();
            clip.close();
            frame.dispose();
            try {
                AI ai = new AI();
            } catch (UnsupportedAudioFileException ex) {
                throw new RuntimeException(ex);
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == b2){
            frame.dispose();
            clip.stop();
            clip.close();
            startPanelSingle sps = new startPanelSingle();
        }
        if(e.getSource() == b4){
            clip.stop();
            clip.close();
            frame.dispose();
            HighScore highscore = new HighScore();
        }
    }
}
