package utils;

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

	public static final int BYTE = 0;
	public static final int KILOBYTE = 1;
	public static final int MEGABYTE = 2;
	public static final int GIGABYTE = 3;
	public static final int TERABYTE = 4;

	public static final double SIZE_UNIT = 1024;

	static String server = null;
	static String login = null;
	static String pass = null;

	public static boolean isCorrectCommand(String[] commandData, String command) {

		if (command == "ftp" && (commandData.length == 2 || commandData.length == 4)) {
			return true;
		}
		if (command == "cd" && (commandData.length == 2 || commandData.length == 3)) {
			return true;
		}
		if (command == "back" && commandData.length == 1) {
			return true;
		}
		if (command == "dload" && commandData.length == 2) {
			return true;
		}
		if (command == "help" && commandData.length == 1) {
			return true;
		}
		if (command == "exit" && commandData.length == 1) {
			return true;
		} else {
			return true;
		}
	}

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
		System.out.println("Successful connection to the server");
	}

	public static void loginFTP(FTPClient client)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		client.login(login, pass);
		System.out.println("Successful authentication on the server\n");

	}

	public static void enterFTP(FTPClient client)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		Utils.connectFTP(client);
		Utils.loginFTP(client);
	}

	public static void goIntoFolder(FTPClient client, String[] commandData) {
		String dir = commandData[PARAM1];
		try {
			client.changeDirectory(dir);
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			System.out.println("Unable to go to the directory (" + e.getMessage() + ")\n");
		}
		Utils.printListOfFiles(client);
	}

	public static void goToParentDirectory(FTPClient client, String[] commandData) {
		try {
			client.changeDirectoryUp();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
			System.out.println("Unable to go to the parent directory (" + e.getMessage() + ")\n");
		}
		Utils.printListOfFiles(client);
	}

	public static void downloadFile(FTPClient client, String[] commandData) {
		String file = commandData[PARAM1];

		try {
			client.download(file, new java.io.File(file));

		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException
				| FTPDataTransferException | FTPAbortedException e) {
			System.out.println("Unable to save the file \n (" + e.getMessage() + ")\n");
		}
		System.out.println("File " + file + " has been saved succesfully");
	}

	public static FTPFile[] getListOfFiles(FTPClient client) {
		FTPFile[] list = null;
		try {
			list = client.list();
		} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException
				| FTPDataTransferException | FTPAbortedException | FTPListParseException e) {
			System.out.println("Cannot get list of files  (" + e.getMessage() + ")\n");
		}
		return list;

	}

	public static void printListOfFiles(FTPClient client) {
		FTPFile[] list = Utils.getListOfFiles(client);
		System.out.println("-----List Of Files-----");
		for (FTPFile f : list) {
			if (f.getType() == 1) {
				System.out.println("[" + f.getName() + "]");
			} else {
				System.out.println(f.getName() + "(" + f.getSize() + " bytes)");
			}
		}
		System.out.println("----------------------- \n");
	}
}
