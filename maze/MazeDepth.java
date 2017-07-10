package maze;

import java.util.Stack;

/**
 * 深さ優先探索
 * @author proglight
 */

public class MazeDepth {

	MazeBasic mdata;
	int nowX, nowY;

	Stack<Integer> stackX = new Stack<Integer>();
	Stack<Integer> stackY = new Stack<Integer>();

	//探索方向優先度の配列	0:右 1:下 2:左 3:上
	int[] priDir = {0, 1, 2, 3};
	//移動用配列の設定
	int[] moveX = {1, 0, -1, 0};
	int[] moveY = {0, 1, 0, -1};

	//コンストラクタ
	MazeDepth(MazeBasic mdata){
		this.mdata = mdata;
		nowX = mdata.startX;
		nowY = mdata.startY;
	}

	//深さ優先探索
	boolean moveDepth(){
		//現在位置を探索済みに
		mdata.maze[nowY][nowX] = 4;

		//進行可能方向を判断する処理。優先度は設定値によって決定。どれか一方向にしか進めない
		if(moveCheck(nowX, nowY, priDir[0])){
			//現在位置をスタックに記録
			stackX.push(nowX);
			stackY.push(nowY);
			//現在位置更新
			nowX += moveX[priDir[0]];
			nowY += moveY[priDir[0]];

		} else if(moveCheck(nowX, nowY, priDir[1])){
			//現在位置をスタックに記録
			stackX.push(nowX);
			stackY.push(nowY);
			//現在位置更新
			nowX += moveX[priDir[1]];
			nowY += moveY[priDir[1]];

		} else if(moveCheck(nowX, nowY, priDir[2])){
			//現在位置をスタックに記録
			stackX.push(nowX);
			stackY.push(nowY);
			//現在位置更新
			nowX += moveX[priDir[2]];
			nowY += moveY[priDir[2]];

		} else if(moveCheck(nowX, nowY, priDir[3])){
			//現在位置をスタックに記録
			stackX.push(nowX);
			stackY.push(nowY);
			//現在位置更新
			nowX += moveX[priDir[3]];
			nowY += moveY[priDir[3]];

		//どこへも進めなければスタックから取り出して引き返す
		} else {
			nowX = stackX.pop();
			nowY = stackY.pop();
		}

		mdata.maze[nowY][nowX] = 2;

		//ゴールしたかどうか
		if(nowX == mdata.goalX && nowY == mdata.goalY){
			return true;
		} else {
			return false;
		}
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
	void fastestRoute(){
		while(!stackX.isEmpty()){
			mdata.maze[stackY.pop()][stackX.pop()] = 3;
		}
	}

}
