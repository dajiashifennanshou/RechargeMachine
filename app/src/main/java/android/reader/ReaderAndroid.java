package android.reader;

import android.util.Log;

import java.io.File;
import java.io.IOException;

public class ReaderAndroid {
	// JNI
	private native static int open(String path, int baudrate);
	public native void close(int handle);
	// For ISO14443 Type-A
	public native int getCardSN(int handle,int address,byte mode,byte cmd,byte[] hald_flag,byte[] SN);
	public native int MFRead(int handle,int address,byte mode,byte blk_add,byte num_blk,byte[] PWS,byte[] buffer);
	public native int MFWrite(int handle,int address,byte mode,byte blk_add,byte num_blk,byte[] PWS,byte[] buffer);

	// For CPU Card
	public native int CPU_RATS(int handle, int address, byte[] param, byte[] buff, byte[] retlen);
	public native int CPU_APDU(int handle, int address, byte[] param, byte paramlen, byte[] buff, byte[] retlen);
	public native int CPU_RST_Ant(int handle, int address, byte[] buff, byte[] retlen);


	static {
		System.loadLibrary("ReaderAndroid");
		Log.e("ReaderAndroid", "Android LIB install");
	}

	private static final String TAG = "ReaderAndroid";
	private int mFd;
	public  ReaderAndroid(File device, int baudrate) throws SecurityException, IOException {
		Log.i("ReaderLIB", "ReaderAndroid");
		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			Log.i("ReaderLIB", "device chmod");
			try {
				Process su;
				su = Runtime.getRuntime().exec("/system/xbin/su");
				String cmd = "chmod 777 " + device.getAbsolutePath() + "\n" + "exit\n";
				Log.i("level", "command = " + cmd);
				su.getOutputStream().write(cmd.getBytes());
				su.getOutputStream().flush();
				if ((su.waitFor() != 0) ||
						!device.canRead()
						|| !device.canWrite()) {
				}
			}
			catch (Exception e) {
//				e.printStackTrace();
			}
		}
		mFd = open(device.getAbsolutePath(), baudrate);

		if (mFd == -1) {
			Log.e(TAG, "native open returns null");
		}
	}

	// Getters and setters
	public int getHandle() {
		return mFd;
	}
}
