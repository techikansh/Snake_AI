import java.io.IOException;
import java.util.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SnakeGame {

	
	public static String password = null;

	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		// TODO Auto-generated method stub
//		 password = args[0];
		
		System.out.print("Enter password to Database : ");
		Scanner sc = new Scanner(System.in);
		password = sc.nextLine();
		
		new MenuBar();

	}

}
