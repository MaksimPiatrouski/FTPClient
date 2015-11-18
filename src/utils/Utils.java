package utils;

import java.io.FileNotFoundException;
import java.io.IOException;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class Utils {
	public static final String USER = "anonymous";
	public static final String PASS = "ftp4j";
	public static final int AUTH_PARAMS = 4;
	public static final int PARAM1 = 1;
	public static final int PARAM2 = 2;
	public static final int PARAM3 = 3;

	static String server = null;
	static String login = null;
	static String pass = null;

	public static void initLoginData(FTPClient client, String[] commandData) {
		if (commandData.length == AUTH_PARAMS) {
			Utils.server = commandData[PARAM1];
			Utils.login = commandData[PARAM2];
			Utils.pass = commandData[PARAM3];

		} else {
			Utils.server = commandData[PARAM1];
			Utils.login = USER;
			Utils.pass = PASS;
		}
	}

	public static void connectFTP(FTPClient client)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		client.connect(server);
		System.out.println("Successful connection to the server\n");
	}

	public static void loginFTP(FTPClient client)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		client.login(login, pass);
		System.out.println("Successful authentication on the server\n");

	}

	public static void goIntoFolder(FTPClient client, String[] commandData)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		String dir = commandData[PARAM1];
		client.changeDirectory(dir);
	}


	public static void downloadFile(FTPClient client, String[] commandData)
			throws IllegalStateException, FileNotFoundException, IOException, FTPIllegalReplyException, FTPException,
			FTPDataTransferException, FTPAbortedException {
		String file = commandData[PARAM1];

		client.download(file, new java.io.File(file));
		System.out.println("File " + file + " has been saved succesfully");

	}

	public static void printListOfFiles(FTPClient client) {
		FTPFile[] list = null;
		try {
			list = client.list();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException
				| FTPDataTransferException | FTPAbortedException | FTPListParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Cannot get list of files \n");
		}
		System.out.println("-----List Of Files----- \n");
		for (FTPFile f : list) {
			System.out.println(f.getName());
		}
		System.out.println("\n");
	}
}
