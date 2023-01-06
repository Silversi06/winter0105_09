package game.Controller;

import java.util.Random;

import javax.swing.JLabel;

import game.View.GameView;
import game.VO.Hail;

public class HailThread extends Thread{
	JLabel hailLbl;
	Hail hail;
	
	public HailThread(JLabel hailLbl, Hail hail) {
		this.hailLbl = hailLbl;
		this.hail = hail;
	}
	
	
	@Override
	public void run() {
		while (true) {
			Random random = new Random();
			if( hailLbl.getY() <= GameView.FRAME_HEIGHT)
				hailLbl.setLocation(hailLbl.getX(), hailLbl.getY() + 10);
			else {
				hailLbl.setLocation(hailLbl.getX(), random.nextInt(70));
			}
			//changeScore();
			try {
				sleep(20 * random.nextInt(100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
