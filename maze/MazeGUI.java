package maze;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * 迷路の画面処理
 * @author cook1293
 */

public class MazeGUI extends JFrame{

	public static final int SIZECELL = 30;
	public static int rows;
	public static int columns;
	public static int sizePnX;
	public static int sizePnY;
	public static int sizeWinX;
	public static int sizeWinY;

	//GUI部品
	MazePanel pn = new MazePanel();
	JButton resetBtn,stopBtn, depthBtn, breadthBtn, randomBtn;

	SearchAction sa;
	Timer timer;

	//迷路のオブジェクト
	MazeBasic mdata;

	int step = 0;	//ステップ数
	int move = 0;	//0:一時停止中 1:再生中
	String searchName = "";
	int fastestStep = -1;	//最短ステップ


	//コンストラクタ
	public MazeGUI(String title, MazeBasic mdata, int callTime){

		rows = mdata.rows;
		columns = mdata.columns;
		sizePnX = SIZECELL * columns;
		sizePnY = SIZECELL * rows;
		sizeWinX = sizePnX + 30;
		sizeWinY = sizePnY + 130;

		//ウィンドウサイズの調整
		if(sizeWinX < 500){
			sizeWinX = 500;
		}

		//フレームの準備
		setSize(sizeWinX, sizeWinY);
		setTitle(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		setLayout(null);
		pn.setBounds(0, 0, sizePnX, sizePnY);
		add(pn);

		MyActionListener als = new MyActionListener();

		//ボタン
		resetBtn = new JButton("リセット");
		resetBtn.addActionListener(als);
		resetBtn.setBounds(10, sizePnY + 10, 90, 30);
		add(resetBtn);

		stopBtn = new JButton("一時停止");
		stopBtn.addActionListener(als);
		stopBtn.setBounds(100, sizePnY + 10, 90, 30);
		add(stopBtn);
		stopBtn.setEnabled(false);

		depthBtn = new JButton("深さ優先");
		depthBtn.addActionListener(als);
		depthBtn.setBounds(190, sizePnY + 10, 90, 30);
		add(depthBtn);

		breadthBtn = new JButton("幅優先");
		breadthBtn.addActionListener(als);
		breadthBtn.setBounds(280, sizePnY + 10, 90, 30);
		add(breadthBtn);

		randomBtn = new JButton("ランダム");
		randomBtn.addActionListener(als);
		randomBtn.setBounds(370, sizePnY + 10, 90, 30);
		add(randomBtn);

		//迷路データ
		this.mdata = mdata;

		sa = new SearchAction();

		//タイマーの呼び出し間隔
		timer = new Timer(callTime, sa);
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
				g.drawLine(0, SIZECELL*i, SIZECELL*columns, SIZECELL*i);
			}
			for(int j=1; j<mdata.maze[0].length; j++){
				g.drawLine(SIZECELL*j, 0, SIZECELL*j, SIZECELL*rows);
			}

			//ステップ数の描画
			g.setFont(new Font("SansSerif",Font.PLAIN,20));
			g.setColor(Color.white);
			g.drawString(step + "ステップ", 10, 20);

			//探索名の描画
			g.drawString(searchName, sizePnX/3 * 2, 20);

			//最短ステップの描画
			if(fastestStep != -1){
				g.drawString("最短：" + fastestStep, sizePnX/3, 20);
			}

		}
	}

	//探索の描画
	public class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent ae){

			//リセット
			if(ae.getSource() == resetBtn){
				mdata.setMaze();
				step = 0;
				fastestStep = -1;
				searchName = "";
				repaint();

				stopBtn.setText("一時停止");
				stopBtn.setEnabled(false);
				depthBtn.setEnabled(true);
				breadthBtn.setEnabled(true);
				randomBtn.setEnabled(true);
			}

			//一時停止・再生
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

			//深さ優先探索
			if(ae.getSource() == depthBtn){
				MazeDepth md = new MazeDepth(mdata);
				sa.setMethod(md);
				searchName = "深さ優先探索";

				resetBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				depthBtn.setEnabled(false);
				breadthBtn.setEnabled(false);
				randomBtn.setEnabled(false);

				move = 1;
				timer.start();
			}

			//幅優先探索
			if(ae.getSource() == breadthBtn){
				MazeBreadth mb = new MazeBreadth(mdata);
				mb.addStart();
				sa.setMethod(mb);
				searchName = "幅優先探索";

				resetBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				depthBtn.setEnabled(false);
				breadthBtn.setEnabled(false);
				randomBtn.setEnabled(false);

				move = 1;
				timer.start();
			}

			//ランダムに探索
			if(ae.getSource() == randomBtn){
				MazeRandom mr = new MazeRandom(mdata);
				sa.setMethod(mr);
				searchName = "ランダム探索";

				resetBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				depthBtn.setEnabled(false);
				breadthBtn.setEnabled(false);
				randomBtn.setEnabled(false);

				move = 1;
				timer.start();
			}

		}
	}

	//一定間隔で呼び出される
	public class SearchAction implements ActionListener{

		MazeDepth md;
		MazeBreadth mb;
		MazeRandom mr;
		int method;
		boolean isGoal;

		void setMethod(MazeDepth md){
			this.md = md;
			method = 0;
		}
		void setMethod(MazeBreadth mb){
			this.mb = mb;
			method = 1;
		}
		void setMethod(MazeRandom mr){
			this.mr = mr;
			method = 2;
		}

		public void actionPerformed(ActionEvent ae){

			if(method == 0){		//深さ優先探索
				isGoal = md.moveDepth();
				step++;
				repaint();
				if(isGoal){
					timer.stop();
					md.fastestRoute();	//通ったルートを表示
					repaint();
					resetBtn.setEnabled(true);
					stopBtn.setEnabled(false);
				}

			} else if(method ==1){	//幅優先探索
				isGoal = mb.moveBreadth();
				step++;
				repaint();
				if(isGoal){
					timer.stop();
					fastestStep = mb.fastestRoute();	//最短ルートを表示
					repaint();
					resetBtn.setEnabled(true);
					stopBtn.setEnabled(false);
				}

			} else if(method == 2){	//ランダム探索
				isGoal = mr.moveRandom();
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

}
