package game.View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import game.Controller.DiamondThread;
import game.Controller.HailThread;
import game.VO.Diamond;
import game.VO.Hail;

public class GameView extends JFrame {
	public static final int FRAME_WIDTH = 1200;
	public static final int FRAME_HEIGHT = 600;
//	20개의 우박 객체 참조값이 저장되는 배열
	int hailCount = 16;
	Hail[] hails; 
	JLabel[] lblHails;
	Diamond[] diamonds = new Diamond[hailCount / 2];
	JLabel[] lblDiamonds = new JLabel[diamonds.length];
	JLabel charLbl = new JLabel(new ImageIcon("images/character.gif"));
	JLabel scoreLbl = new JLabel("점수: 1000점");
	String[] comboCharStr = {"짱구", "도깨비", "펭귄", "옴팡이", "라이언"};
	JComboBox<String> comboChar = new JComboBox<String>(comboCharStr);
	String[] comboHailStr = {"폭탄", "골룸", "노랑악마", "오렌지악마"};
	JComboBox<String> comboHail = new JComboBox<String>(comboHailStr);
	int score = 1000;
	HailThread hThread = null;
	DiamondThread dThread = null;
	Random random = new Random();
	int hailX = 70, level = 1;
	JLabel lblLevel = new JLabel("Level 1");
	
	public GameView(GameStart startFrame) {
		startFrame.dispose();
		setLayout(null);
		getContentPane().setBackground(Color.WHITE);
		scoreLbl.setBounds(1100, 20, 100, 30);
		comboChar.setBounds(1030, 20, 60, 30);
		comboChar.addItemListener(comboL);
		comboHail.setBounds(960, 20, 60, 30);
		comboHail.addItemListener(comboL);
		lblLevel.setBounds(20, 20, 60, 30);
		add(scoreLbl);
		add(comboChar);
		add(comboHail);
		add(lblLevel);
		changeHailCount();
		
		charLbl.setBounds(550, 450, 60, 70);
		add(charLbl);
		addKeyListener(keyL);
		
		setTitle("우박을 피해봐");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBounds(10, 10, FRAME_WIDTH, FRAME_HEIGHT);
		setVisible(true);
//		setResizable(false);
		setFocusable(true);
		requestFocus();
	}
	
	public void changeHailCount() {
		hails = new Hail[hailCount]; 
		lblHails = new JLabel[hails.length];
//		20개의 우박 객체를 생성해서 배열에 저장
		for (int i = 0; i < hailCount; i++) {
			lblLevel.setText("Level " + level);
			hails[i] = new Hail();
			hails[i].setX(i * hailX);
			hails[i].setY(i * random.nextInt(70));
			lblHails[i] = new JLabel(new ImageIcon(hails[i].getImgName()));
			lblHails[i].setBounds(hails[i].getX(), hails[i].getY(), hails[i].getW(), hails[i].getH());
			add(lblHails[i]);
			hThread = new HailThread(lblHails[i], hails[i], level);
			hThread.start();
		}
		
//		10개의 다이아몬드 객체를 생성해서 배열에 저장
		
		for (int i = 0; i < diamonds.length; i++) {
			diamonds[i] = new Diamond();
			diamonds[i].setX(i * 140 + random.nextInt(70));
			diamonds[i].setY(i * random.nextInt(30));
			lblDiamonds[i] = new JLabel(new ImageIcon(diamonds[i].getImgName()));
			lblDiamonds[i].setBounds(diamonds[i].getX(), diamonds[i].getY(), diamonds[i].getW(), diamonds[i].getH());
			add(lblDiamonds[i]);
			dThread = new DiamondThread(lblDiamonds[i], diamonds[i]);
			dThread.start();
		}
	}
	
	public void changeScore() {
		for (int i = 0; i < lblHails.length; i++) {				
			if(charLbl.getX() >= lblHails[i].getX() && charLbl.getX() <= lblHails[i].getX()+lblHails[i].getWidth()) {
				if(charLbl.getY() >= lblHails[i].getY() && charLbl.getY() <= lblHails[i].getY()+lblHails[i].getHeight()) {
					if(score > hails[i].getPoint()) {
						score -= hails[i].getPoint();
						scoreLbl.setText("점수: "+score+"점");
					}
				}
			}
		}
		
		for (int i = 0; i < lblDiamonds.length; i++) {				
			if(charLbl.getX() >= lblDiamonds[i].getX() && charLbl.getX() <= lblDiamonds[i].getX()+lblDiamonds[i].getWidth()) {
				if(charLbl.getY() >= lblDiamonds[i].getY() && charLbl.getY() <= lblDiamonds[i].getY()+lblDiamonds[i].getHeight()) {
					if(score < 1300) {
						score += diamonds[i].getPoint();	
						scoreLbl.setText("점수: "+score+"점");
					}
				}
			}
		}
		
		if(score >= 1200 && score < 1300) {
			if(level != 2) {
				hailCount = 20;
				hailX = 60;
				level = 2;
				JOptionPane.showMessageDialog(this, "Level "+level+"가 시작됩니다.");
				score = 1000;
				scoreLbl.setText("점수: "+score+"점");
				changeHailCount();
			}	
		}else if(score >= 1300) {
			JOptionPane.showMessageDialog(this, "~~ You Win ~~");
			dispose();
		}
		
		scoreLbl.setText("점수: "+score+"점");
	}
	
	ItemListener comboL = new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			ImageIcon icon = null;
			String imgName = null;
			
			if(e.getSource() == comboChar) {
				switch (comboChar.getSelectedIndex()) {
				case 0:
					imgName = "character";
					break;
				case 1:
					imgName = "goblin";
					break;
				case 2:
					imgName = "penguin";
					break;
				case 3:
					imgName = "ompang";
					break;
				case 4:
					imgName = "lion";
					break;
				}
				icon = new ImageIcon("images/" + imgName + ".gif");
				charLbl.setIcon(icon);
			}else if(e.getSource() == comboHail) {
				switch (comboHail.getSelectedIndex()) {
				case 0:
					imgName = "hail.gif";
					break;
				case 1:
					imgName = "gollum.png";
					break;
				case 2:
					imgName = "devil1.png";
					break;
				case 3:
					imgName = "devil2.png";
					break;
				}
				icon = new ImageIcon("images/" + imgName);
				for (int j = 0; j < lblHails.length; j++) {
					lblHails[j].setIcon(icon);
				}
				
			}
			GameView.this.setFocusable(true);
			GameView.this.requestFocus();
		}
	};
	
	KeyAdapter keyL = new KeyAdapter() {
		
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP: 
				if(charLbl.getY() > 0)
					charLbl.setLocation(charLbl.getX(), charLbl.getY()-10);
				break;
			case KeyEvent.VK_DOWN: 
				if(charLbl.getY() < FRAME_HEIGHT-charLbl.getHeight()*2)
					charLbl.setLocation(charLbl.getX(), charLbl.getY()+10);
				break;
			case KeyEvent.VK_LEFT: 
				if(charLbl.getX() > 0)
					charLbl.setLocation(charLbl.getX()-10, charLbl.getY());
				break;
			case KeyEvent.VK_RIGHT: 
				if(charLbl.getX() < FRAME_WIDTH-charLbl.getWidth())
					charLbl.setLocation(charLbl.getX()+10, charLbl.getY());
				break;
			}
			changeScore();
		}
	};
	
}