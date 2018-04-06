package mazeQlearning;

import maze.MazeBasic;

/**
 * Q学習による迷路実行のメインクラス
 * @author cook1293
 */


public class MazeQMain {

	public static void main(String[] args) {
		MazeBasic mdata = new MazeBasic();
		mdata.inputMaze();
		mdata.setMaze();

		//最後はタイマーの呼び出し間隔(ミリ秒指定)
		MazeQGUI mqgui = new MazeQGUI("迷路", mdata, 100);
		mqgui.setVisible(true);

	}

}
