package runner;

import java.io.IOException;
import java.util.Scanner;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import utils.Utils;

public class ClientRunner {

	public static final String USAGE = "Usage: \n"
			+ "ftp <hostname> <username> <password> - connect ftp server with login:password\n"
			+ "ftp <hostname> - connect ftp server anonymously\n" + "dload <filename> - save file\n"
			+ "cd <path> - go into folder\n" + "back - go to parent dir\n";

	public static final String USER = "anonymous";
	public static final String PASS = "ftp4j";
	public static final int AUTH_PARAMS = 4;
	public static final int COMMAND = 0;
	public static final int PARAM1 = 1;
	public static final int PARAM2 = 2;
	public static final int PARAM3 = 3;

	public static void main(String[] args) {
		FTPClient client = new FTPClient();
		boolean loop = false;

		System.out.println("--------FTP Client----------\n" + "Type \"help\" to get USAGE Manual\n"
				+ "Type \"exit\" to exit client\n");

		do {
			Scanner sc = new Scanner(System.in);
			String commandInput = sc.nextLine();

			String[] commandData = commandInput.split(" ");
			String command = commandData[COMMAND];
			switch (command) {

			case "ftp":

				Utils.initLoginData(client, commandData);

				try {
					Utils.connectFTP(client);
				} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e) {
					System.out.println("Unable to connect to server\n");
					loop = true;
					break;
				}

				try {
					Utils.loginFTP(client);
				} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e1) {
					System.out.println("Unable to log in to the server\n");
					loop = true;
					break;
				}

				Utils.printListOfFiles(client);
				loop = true;
				break;

			case "cd":
				try {
					Utils.goIntoFolder(client, commandData);
				} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e1) {
					System.out.println("Unable to go to the directory\n");
				}

				Utils.printListOfFiles(client);
				loop = true;
				break;

			case "back":
				try {
					client.changeDirectoryUp();
				} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException e1) {
					System.out.println("Unable to go to the parent directory\n");
				}
				
				Utils.printListOfFiles(client);
				loop = true;
				break;

			case "dload":
				try {
					Utils.downloadFile(client, commandData);
				} catch (IllegalStateException | IOException | FTPIllegalReplyException | FTPException
						| FTPDataTransferException | FTPAbortedException e) {
					System.out.println("Unable to save the file\n");
					loop = true;
					break;
				}
				loop = true;
				break;

			case "help":
				System.out.println(USAGE);
				loop = true;
				break;

			case "exit":
				sc.close();
				loop = false;
				break;

			default:
				System.out.println("Incorrect command. Type \"help\" to see list of available commands");
			}

		} while (loop == true);

		do {
			System.exit(0);

		} while (loop == false);

	}
}
