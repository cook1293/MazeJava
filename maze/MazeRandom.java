package maze;

import java.util.ArrayList;
import java.util.Random;

/**
 * ランダム探索
 * @author proglight
 */

public class MazeRandom {
	Random rand = new Random();

	MazeBasic mdata;
	int step;
	int nowX, nowY;

	//移動用配列の設定
	int[] moveX = {1, 0, -1, 0};
	int[] moveY = {0, 1, 0, -1};


	//コンストラクタ
	MazeRandom(MazeBasic mdata){
		this.mdata = mdata;
		nowX = mdata.startX;
		nowY = mdata.startY;
	}

	//ランダムに移動
	boolean moveRandom(){
		//現在位置を探索済みに
		mdata.maze[nowY][nowX] = 4;

		//進行可能方向からランダムで選択
		ArrayList<Integer> movePossible = getMovePossible(nowX, nowY);
		int dir = rand.nextInt(movePossible.size());
		dir = movePossible.get(dir);

		//現在位置を更新
		nowX += moveX[dir];
		nowY += moveY[dir];
		mdata.maze[nowY][nowX] = 2;

		//ゴールしたかどうか
		if(nowX == mdata.goalX && nowY == mdata.goalY){
			return true;
		} else {
			return false;
		}

	}

	//進行可能な方向を返す
	ArrayList<Integer> getMovePossible(int x, int y){
		ArrayList<Integer> movePossible = new ArrayList<Integer>();
		for(int i=0; i<moveX.length; i++){
			if(mdata.maze[y+moveY[i]][x+moveX[i]] != 1){	//壁以外は全て進行可
				movePossible.add(i);
			}
		}
		return movePossible;
	}

}
