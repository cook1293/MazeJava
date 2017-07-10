package maze;

import java.util.ArrayDeque;
import java.util.Queue;


/**
 * 幅優先探索
 * @author proglight
 */

public class MazeBreadth {

	MazeBasic mdata;
	int step;
	public int nowX, nowY;

	public Queue<Integer> queueX = new ArrayDeque<Integer>();
	public Queue<Integer> queueY = new ArrayDeque<Integer>();

	//移動用配列の設定
	int[] moveX = {1, 0, -1, 0};
	int[] moveY = {0, 1, 0, -1};

	//経路履歴保存用配列
	public int[][] historyX;
	public int[][] historyY;

	//コンストラクタ
	public MazeBreadth(MazeBasic mdata){
		this.mdata = mdata;
		nowX = mdata.startX;
		nowY = mdata.startY;
		historyX = new int[mdata.ROWS][mdata.COLUMNS];
		historyY = new int[mdata.ROWS][mdata.COLUMNS];
	}

	//キューにスタート位置を入れる
	public void addStart(){

		queueX.add(mdata.startX);
		queueY.add(mdata.startY);
	}

	//幅優先探索
	public boolean moveBreadth(){
		//現在位置取り出し
		nowX = queueX.poll();
		nowY = queueY.poll();

		//ゴールしたら終わり
		if(nowX == mdata.goalX && nowY == mdata.goalY){
			return true;
		}

		//現在位置を探索済みに
		mdata.maze[nowY][nowX] = 4;

		//進行可能方向を判断する処理。進行可能な方向は、全てキューに登録する
		for(int i=0; i<moveX.length; i++){
			if(moveCheck(nowX, nowY, i)){
				//進行可能位置をキューに記録
				queueX.add(nowX+moveX[i]);
				queueY.add(nowY+moveY[i]);
				//どこからきたのか、経路の履歴を記録
				historyX[nowY+moveY[i]][nowX+moveX[i]] = nowX;
				historyY[nowY+moveY[i]][nowX+moveX[i]] = nowY;
			}
		}

		return false;
	}

	//進行可能かチェック
	boolean moveCheck(int x, int y, int dir){
		if(mdata.maze[y+moveY[dir]][x+moveX[dir]] == 0 || mdata.maze[y+moveY[dir]][x+moveX[dir]] == 3){
			return true;
		} else {
			return false;
		}
	}

	//最短経路を表示
	public void fastestRoute(){
		int bestX, bestY;
		int pastX, pastY;

		bestX = nowX;
		bestY = nowY;
		while(bestX != mdata.startX || bestY != mdata.startY){
			pastX = bestX;
			pastY = bestY;
			bestX = historyX[pastY][pastX];
			bestY = historyY[pastY][pastX];
			mdata.maze[bestY][bestX] = 3;
		}
	}

}
