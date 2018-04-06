package maze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 迷路の基本データ
 * @author cook1293
 */

public class MazeBasic {


	public int rows = 10;
	public int columns = 16;

	public int[][] mazeBasic = new int[rows][columns];
	public int[][] maze = new int[rows][columns];
	/*
	 * 0:通路
	 * 1:壁
	 * 2:現在位置
	 * 3:ゴール
	 * 4:探索済み
	 */

	public int startX, startY, goalX, goalY;
	public int fastestStep = 38;	//最短ステップ

	//ファイルから迷路を読み込む
	public void inputMaze(){

		String[] numString;

		try{
			//jarで実行の場合
			//File f = new File("./initialMaze.csv");
			//eclipseで実行の場合
			File f = new File("./src/maze/initialMaze.csv");
			BufferedReader br = new BufferedReader(new FileReader(f));

			// 1行ずつCSVファイルを読み込む
			String line;
			int row = 0;
			while ((line = br.readLine()) != null) {
				//行をカンマ区切りで配列に格納
				numString = line.split(",", 0);
				for(int i=0; i<numString.length; i++){
					mazeBasic[row][i] = Integer.parseInt(numString[i]);

					//スタートの位置とゴールの位置を記録
					if(mazeBasic[row][i] == 2){
						startX = i;
						startY = row;
					}
					if(mazeBasic[row][i] == 3){
						goalX = i;
						goalY = row;
					}
				}
				row++;
			}
			br.close();

		} catch(IOException e){
			System.out.println(e);
			return ;
		}
	}


	//迷路をセットする
	public void setMaze(){
		for(int i=0; i<mazeBasic.length; i++){
			for(int j=0; j<mazeBasic[i].length; j++){
				maze[i][j] = mazeBasic[i][j];
			}
		}
	}

}
