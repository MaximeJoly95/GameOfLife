package gui;

public interface GOL {
	void init();
	void start();
	void stop();
	void run();
	boolean isRunning();
	void nextGeneration();
	void setSpeed(int fps);
	void setCellSize(int size);
	int getCellSize();
}
