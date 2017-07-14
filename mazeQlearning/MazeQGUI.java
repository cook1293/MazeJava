package mazeQlearning;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import maze.MazeBasic;


/**
 * Q学習による迷路の画面処理
 * @author proglight
 */

public class MazeQGUI extends JFrame{
	public static final int SIZECELL = 30;
	public static final int ROWS = 10;
	public static final int COLUMNS = 16;
	public static final int SIZEPNX = SIZECELL * COLUMNS;
	public static final int SIZEPNY = SIZECELL * ROWS;
	public static final int SIZEWINX = SIZEPNX + 30;
	public static final int SIZEWINY = SIZEPNY + 130;

	//GUI部品
	MazePanel pn = new MazePanel();
	JButton resetBtn, stopBtn, qlearnBtn, testMoveBtn, outputQBtn;

	SearchAction sa;
	Timer timer;

	//迷路のオブジェクト
	MazeBasic mdata;

	MazeQlearning mq;


	int step;	//ステップ数
	int move;	//0:一時停止中 1:再生中
	int learningCnt = 0;	//学習回数

	//コンストラクタ
	public MazeQGUI(String title, MazeBasic mdata, int callTime){
		//フレームの準備
		setSize(SIZEWINX, SIZEWINY);
		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(null);
		pn.setBounds(0, 0, SIZEPNX, SIZEPNY);
		add(pn);

		MyActionListener als = new MyActionListener();

		//ボタン
		resetBtn = new JButton("リセット");
		resetBtn.addActionListener(als);
		resetBtn.setBounds(10, SIZEPNY + 10, 90, 30);
		add(resetBtn);

		stopBtn = new JButton("一時停止");
		stopBtn.addActionListener(als);
		stopBtn.setBounds(10, SIZEPNY + 40, 90, 30);
		add(stopBtn);
		stopBtn.setEnabled(false);

		qlearnBtn = new JButton("Q学習");
		qlearnBtn.addActionListener(als);
		qlearnBtn.setBounds(100, SIZEPNY + 10, 90, 30);
		add(qlearnBtn);

		testMoveBtn = new JButton("テスト");
		testMoveBtn.addActionListener(als);
		testMoveBtn.setBounds(100, SIZEPNY + 40, 90, 30);
		add(testMoveBtn);
		testMoveBtn.setEnabled(false);

		outputQBtn = new JButton("Q値出力");
		outputQBtn.addActionListener(als);
		outputQBtn.setBounds(190, SIZEPNY + 10, 120, 30);
		add(outputQBtn);
		outputQBtn.setEnabled(false);


		//迷路データ
		this.mdata = mdata;

		//Q学習 (迷路データ, 学習回数, 割引率, 学習率, ε)
		mq = new MazeQlearning(mdata, 100000, 0.9, 0.1, 0.3);

		sa = new SearchAction();

		//タイマーの呼び出し間隔
		timer = new Timer(callTime, sa);

		step = 0;
	}


	class MazePanel extends JPanel{
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			//迷路の描画
			for(int i=0; i<mdata.maze.length; i++){
				for(int j=0; j<mdata.maze[i].length; j++){

					if(mdata.maze[i][j] == 0){			//通路
						g.setColor(Color.white);
					} else if(mdata.maze[i][j] == 1){	//壁
						g.setColor(Color.black);
					} else if(mdata.maze[i][j] == 2){	//現在位置
						g.setColor(Color.green);
					} else if(mdata.maze[i][j] == 3){	//ゴール
						g.setColor(Color.red);
					} else if(mdata.maze[i][j] == 4){	//探索済み
						g.setColor(Color.cyan);
					}
					g.fillRect(SIZECELL * j, SIZECELL * i, SIZECELL, SIZECELL);
				}
			}

			//線の描画
			g.setColor(Color.black);
			for(int i=1; i<mdata.maze.length; i++){
				g.drawLine(0, SIZECELL*i, SIZECELL*COLUMNS, SIZECELL*i);
			}
			for(int j=1; j<mdata.maze[0].length; j++){
				g.drawLine(SIZECELL*j, 0, SIZECELL*j, SIZECELL*ROWS);
			}

			//ステップ数の描画
			g.setFont(new Font("SansSerif",Font.PLAIN,20));
			g.setColor(Color.white);
			g.drawString(step + "ステップ", 10, 20);

			//学習回数の描画
			g.drawString("学習回数：" + learningCnt, SIZEPNX/2, 20);
		}
	}

	//探索の描画
	public class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){

			//リセット
			if(ae.getSource() == resetBtn){
				mdata.setMaze();
				mq.initializeQ();
				step = 0;
				learningCnt = 0;
				repaint();

				stopBtn.setText("一時停止");
				stopBtn.setEnabled(false);
				qlearnBtn.setEnabled(true);
				outputQBtn.setEnabled(false);
			}

			//ストップ
			if(ae.getSource() == stopBtn){
				if(move == 1){
					move = 0;
					resetBtn.setEnabled(true);
					stopBtn.setText("再生");
					timer.stop();
				} else {
					move = 1;
					resetBtn.setEnabled(false);
					stopBtn.setText("一時停止");
					timer.start();
				}
			}

			//Q学習
			if(ae.getSource() == qlearnBtn){
				learningCnt = mq.qlearn();
				repaint();

				qlearnBtn.setEnabled(false);
				testMoveBtn.setEnabled(true);
				outputQBtn.setEnabled(true);
			}

			//Q学習後のテスト
			if(ae.getSource() == testMoveBtn){
				move = 1;
				timer.start();
				resetBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				testMoveBtn.setEnabled(false);
			}

			//Q値の出力
			if(ae.getSource() == outputQBtn){
				mq.outputQ();
			}
		}
	}

	//一定間隔で呼び出される
	public class SearchAction implements ActionListener{

		boolean isGoal;

		//Q学習後の探索
		public void actionPerformed(ActionEvent ae){
			isGoal = mq.moveTest();
			step++;
			repaint();

			if(isGoal){
				timer.stop();
				resetBtn.setEnabled(true);
				stopBtn.setEnabled(false);
			}
		}
	}
}
