package prv.zozi.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;

public class AndroidInternalFileManager {
	private String filename;
	private Activity context;

	public AndroidInternalFileManager(Activity context) {
		filename = "default.txt";
		this.context = context;
		setFile(filename);
	}

	public AndroidInternalFileManager(Activity context, String filename) {
		this.filename = filename;
		this.context = context;
		setFile(filename);
	}

	public void setFile(String filename) {
		this.filename = filename;
		/*
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Config.toast(context, "ERR_CODE : 401 관리자에게 문의");
			}
		}
		*/
	}

	/*
	public boolean write(String filename, String data) {
		setFile(filename);
		return write(data);
	}
	 */
	public boolean write(String data, String key){
		try {
			data = ZoziEncrypt.Encrypt(data,key);
		} catch (Exception e) {
			return false;
		}
		return write(data);
	}
	public boolean write(String data) {
		try {
			FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
			/*
			 * PrintWriter pw = new PrintWriter(fos); pw.write(data);
			 * pw.flush(); pw.close();
			 */
			fos.write(data.getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ZMethod.toast(context, "ERR_CODE : 402 관리자에게 문의");
			return false;
		} catch (IOException e) {
			ZMethod.toast(context, "ERR_CODE : 403 관리자에게 문의");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String read() {
		String str = null;
		try {
			FileInputStream fis = context.openFileInput(filename);
			byte[] tmp = new byte[fis.available()];
			while(fis.read(tmp) != -1){;}
			fis.close();
			str = new String(tmp);
		} catch (FileNotFoundException e) {
			//Config.toast(context, "ERR_CODE : 404 관리자에게 문의");
			e.printStackTrace();
			return "err";
		} catch (IOException e) {
			e.printStackTrace();
			ZMethod.toast(context, "ERR_CODE : 405 관리자에게 문의");
			return "err";
		}
		return str;
	}
	public String read(String key) {
		try {
			return ZoziEncrypt.Decrypt(read(), key);
		} catch (Exception e) {
			return "err";
		}
	}
	/*
	public String read(String filename) {
		setFile(filename);
		return read();
	}
	*/
	public void delete(String filename){
		context.deleteFile(filename);
	}
	public boolean isFileExist(String filename){
		String list[] = context.fileList();
		for(int i=0;i<list.length;i++){
			if(list[i].equals(filename)){
				return true;
			}
		}
		return false;
	}
	public boolean isFileExist(){
		return isFileExist(filename);
	}
}
