package com.tww.utils;


/** 
 * @ClassName: StopWatch 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author taoww
 * @date 2017年3月13日 上午9:55:23 
 * 
 *  秒表计时工具类
 */
public class StopWatch {
	static public int AN_HOUR = 60 * 60 * 1000;

	static public int A_MINUTE = 60 * 1000;

	private long startTime = -1;

	private long stopTime = -1;

	/**
	 * 启动秒表
	 * <p>
	 * <br>
	 */
	public void start() {
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * 停止秒表。
	 * <p>
	 * <br>
	 */
	public void stop() {
		this.stopTime = System.currentTimeMillis();
	}

	/**
	 * 
	 * 重置秒表
	 * <p>
	 * <br>
	 */
	public void reset() {
		this.startTime = -1;
		this.stopTime = -1;
	}

	/**
	 * 分割时间
	 * <p>
	 * <br>
	 */
	public void split() {
		this.stopTime = System.currentTimeMillis();
	}

	/**
	 * 
	 * 移除分割
	 * 
	 * <p>
	 * <br>
	 */
	public void unsplit() {
		this.stopTime = -1;
	}

	/**
	 * 
	 * 获得秒表的时间，这个时间或者是启动时和最后一个分割时刻的时间差，
	 * <p>
	 * 
	 * 或者是启动时和停止时的时间差，或者是启动时和这个方法被调用时的差<br>
	 *  <p>
	 * 毫秒数
	 * @return
	 */
	public long getTime() {
		if (stopTime != -1) {
			return (System.currentTimeMillis() - this.startTime);

		} else {
			return this.stopTime - this.startTime;
		}
	}

	/**
	 * 取得String类型的时间差
	 * <p>
	 * 形式为小时，分钟，秒和毫秒 <br>
	 * 
	 * @return
	 */
	public String getTimeString() {
		int hours, minutes, seconds, milliseconds;
		long time = getTime();
		hours = (int) (time / AN_HOUR);
		time = time - (hours * AN_HOUR);
		minutes = (int) (time / A_MINUTE);
		time = time - (minutes * A_MINUTE);
		seconds = (int) (time / 1000);
		time = time - (seconds * 1000);
		milliseconds = (int) time;
		return hours + "h:" + minutes + "m:" + seconds + "s:" + milliseconds
				+ "ms";
	}

	public String toString() {
		return getTimeString();
	}

	public long getStartTime() {
		return startTime;
	}

	public long getStopTime() {
		return stopTime;
	}

	public static void main(String[] args) throws InterruptedException {
		StopWatch obj = new StopWatch();
		obj.start();
		try {
			Thread.currentThread();
			Thread.sleep(1500);
		} catch (InterruptedException ie) {
			// ignore
		}
		obj.stop();
		System.out.println(obj);
	}
}
