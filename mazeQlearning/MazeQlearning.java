package mazeQlearning;

import java.util.Random;

import maze.MazeBasic;

/**
 * Q学習で迷路を解く
 * @author proglight
 */

public class MazeQlearning {

	Random rand = new Random();
	MazeBasic mdata;

	//報酬配列 0:未探索のマス 1:壁 2:現在位置 3:ゴール 4:探索済み
	int[] r = {0, -5, 0, 100, 0};

	//Q値	[y座標][x座標][進行方向]
	double[][][] q;

	//最大学習回数
	int learningMax;
	//割引率
	double gammaRate;
	//学習率
	double learningRate;
	//ε
	double epsilonRate;

	//迷路の現在位置
	int nowX, nowY;

	//移動用配列の設定
	int[] moveX = {1, 0, -1, 0};
	int[] moveY = {0, 1, 0, -1};

	//ステップ数
	int step;
	//学習回数
	int learningCnt;


	//コンストラクタ
	MazeQlearning(MazeBasic mdata, int lm, double gr, double lr, double er){
		this.mdata = mdata;

		q = new double[mdata.ROWS][mdata.COLUMNS][4];
		this.learningMax = lm;
		this.gammaRate = gr;
		this.learningRate = lr;
		this.epsilonRate = er;

		//Q値の初期化
		initializeQ();
	}

	//Q値の初期化
	void initializeQ(){
		for(int i=0; i<q.length; i++){
			for(int j=0; j<q[i].length; j++){
				for(int k=0; k<q[i][j].length; k++){
					q[i][j][k] = rand.nextInt(30);
				}
			}
		}
	}


	//迷路の初期化
	void resetMaze(){
		mdata.setMaze();
		nowX = mdata.startX;
		nowY = mdata.startY;
	}

	//Q学習
	int qlearn(){
		//現在位置の設定
		resetMaze();

		learningCnt = 0;

		int dir;
		int nextX, nextY;
		int nowR;
		boolean isGoal = false;
		double maxNextQ;

		//最大学習回数分ほど学習
		for(int i=0; i<learningMax; i++){

			//移動方向をε-greedy法で決める
			dir = eGreedy();

			if(moveCheck(nowX, nowY, dir)){
				nextX = nowX + moveX[dir];
				nextY = nowY + moveY[dir];
			} else {
				nextX = nowX;
				nextY = nowY;
			}

			//報酬の計算
			nowR = r[mdata.maze[nowY + moveY[dir]][nowX + moveX[dir]]];
			//移動後のQ値の最大値を計算
			maxNextQ = q[nextY][nextX][getMaxQdir(nextX, nextY)];

			//Q値の計算
			q[nowY][nowX][dir] =
					(1-learningRate) * q[nowY][nowX][dir] + learningRate * (nowR+gammaRate*maxNextQ);

			//実際の移動
			if(moveCheck(nowX, nowY, dir)){
				move(dir);
			}

			learningCnt++;

			//ゴールに着いた時
			if(nowX == mdata.goalX && nowY == mdata.goalY){
				mdata.setMaze();
				nowX = mdata.startX;
				nowY = mdata.startY;

				//最短ステップで解けていたら終了
				step = 0;
				while(step < mdata.fastestStep){
					isGoal = moveTest();
					step++;
				}
				if(isGoal){
					System.out.println("学習回数：" + learningCnt);
					break;
				} else {	//最短で解けていなければ、迷路を初期化して更に学習
					resetMaze();
				}
			}
		}
		resetMaze();

		//学習回数を返す
		return learningCnt;
	}

	//ε-greedy法：εの確率でランダムに選び、1-εの確率でQ値が最大となるものを選ぶ
	int eGreedy(){
		int dir;
		double rate = rand.nextDouble();

		if(rate < epsilonRate){	//ランダム
			dir = rand.nextInt(4);
		} else {				//Q値が最大となる移動方向
			dir = getMaxQdir(nowX, nowY);
		}
		return dir;
	}


	//進行可能かチェック
	boolean moveCheck(int x, int y, int dir){
		if(mdata.maze[y+moveY[dir]][x+moveX[dir]] != 1){	//壁以外は全て進行可
			return true;
		} else {
			return false;
		}
	}

	//Q値が最大となる移動方向を返す
	int getMaxQdir(int x, int y){
		int dir = 0;
		for(int i=1; i<4; i++){
			if(q[y][x][i] > q[y][x][dir]){
				dir = i;
			}
		}
		return dir;
	}

	//移動
	void move(int dir){
		//現在位置を探索済みに
		mdata.maze[nowY][nowX] = 4;

		//現在位置を更新
		nowX += moveX[dir];
		nowY += moveY[dir];
		mdata.maze[nowY][nowX] = 2;
	}

	//Q値の出力
	void outputQ(){
		System.out.println();
		for(int k=0; k<4; k++){
			if(k == 0){
				System.out.println("右");
			} else if(k == 1){
				System.out.println("下");
			} else if(k == 2){
				System.out.println("左");
			} else if(k == 3){
				System.out.println("上");
			}

			for(int i=0; i<q.length; i++){
				for(int j=0; j<q[i].length; j++){
					System.out.print(q[i][j][k]+",");
				}
				System.out.println();
			}
			System.out.println();
		}
	}


	//Q学習後のテスト
	boolean moveTest(){
		int dir = getMaxQdir(nowX, nowY);
		if(moveCheck(nowX, nowY, dir)){
			move(dir);
		}
		//ゴールしたかどうか
		if(nowX == mdata.goalX && nowY == mdata.goalY){
			return true;
		} else {
			return false;
		}
	}

}
