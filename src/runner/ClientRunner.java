package runner;

import java.io.IOException;
import java.util.Scanner;
import it.sauronsoftware.ftp4j.FTPClient;
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
		boolean loop = true;

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
					Utils.enterFTP(client);
				} catch (IllegalStateException | FTPIllegalReplyException e) {
					break;
				} catch (IOException e) {
					System.out.println("Unable connect to the server");
					break;
				} catch (FTPException e) {
					System.out.println("Unable authorise on the server");
					break;
				}
				Utils.printListOfFiles(client);
				break;

			case "cd":

				Utils.goIntoFolder(client, commandData);
				break;

			case "back":

				Utils.goToParentDirectory(client, commandData);
				break;

			case "dload":

				Utils.downloadFile(client, commandData);
				break;

			case "help":

				System.out.println(USAGE);
				break;

			case "exit":

				sc.close();
				loop = false;
				break;

			default:
				System.out.println("Incorrect command. Type \"help\" to see list of available commands");
			}

		} while (loop == true);
	}
}
