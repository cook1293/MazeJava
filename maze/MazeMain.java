package maze;

/**
 * 迷路実行のメインクラス
 * @author proglight
 */

public class MazeMain {

	public static void main(String[] args) {
		MazeBasic mdata = new MazeBasic();
		mdata.inputMaze();
		mdata.setMaze();

		//最後はタイマーの呼び出し間隔(ミリ秒指定)
		MazeGUI mgui = new MazeGUI("迷路", mdata, 100);
		mgui.setVisible(true);
	}

}
