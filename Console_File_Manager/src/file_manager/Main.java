package file_manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the command (enter \"exit\" if you want to quit): ");
      
        while (scan.hasNext()) {
            String fileString = scan.next();
            if(fileString.equals("exit")) {
                break;
            }

            switch (fileString) {
                case "list":
                    Scanner scanList = new Scanner(System.in);
                    System.out.print("Enter folder's absolute path: ");

                    try {
                        File fileList = new File(scanList.nextLine());

                        String[] list = fileList.list();
                        for (int i = 0; i < list.length; i++) {
                            System.out.println("Folder contents: " + list[i]);
                        }
                    } catch (NullPointerException npexc) {
                        System.out.println("Error! You should enter folder's absolute path!");
                    }
                    break;

                case "info":
                    Scanner scanInfo = new Scanner(System.in);
                    System.out.print("Enter file absolute path: ");

                    try {
                        File fileInfo = new File(scanInfo.nextLine());

                        System.out.println("Name: " + fileInfo.getName());
                        System.out.println("Absolute path: " + fileInfo.getAbsolutePath());
                        System.out.println("Path: " + fileInfo.getPath());
                        System.out.println("Size: " + fileInfo.length() + "B");

                        BasicFileAttributes attr;
                        try {
                            attr = Files.readAttributes(fileInfo.toPath(), BasicFileAttributes.class);
                            System.out.println("Creation date: " + attr.creationTime());
                        } catch (IOException ex) {
                            System.out.println(ex);
                        }

                        Instant instant = Instant.ofEpochMilli(fileInfo.lastModified());
                        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy. HH:mm:ss");

                        System.out.println("Last modified: " + dateTime.format(dateTimeFormatter));
                    } catch (NullPointerException npexc) {
                        System.out.println("Error! You should enter file's absolute path!");
                    }
                    break;

                case "create_dir":
                    Scanner scan_create = new Scanner(System.in);
                    System.out.print("Enter absolute path for directory creation: ");

                    try {
                        File directory = new File(scan_create.nextLine());

                        try {
                            if (!directory.exists()) {
                                directory.mkdir();
                                System.out.println("Created a directory called " + directory.getName());
                            } else {
                                System.out.println("Directory called " + directory.getName() + " already exists.");
                            }
                        } catch (Exception e) {
                            System.out.println("Couldn't create a directory called " + directory.getName());
                        }
                    } catch (NullPointerException npexc) {
                        System.out.println("Error! You should enter file's absolute path!");
                    }
                    break;

                case "rename":
                    Scanner scan_old = new Scanner(System.in);
                    System.out.print("Enter absolute path of renamed file: ");
                    File oldFile = new File(scan_old.nextLine());

                    Scanner scan_new = new Scanner(System.in);
                    System.out.print("Enter absolute path and new name of file: ");
                    File newFile = new File(scan_new.nextLine());

                    try {
                        if (!oldFile.exists()) {
                            System.out.println("File doesn't exists!");
                            return;
                        }
                        if (newFile.exists()) {
                            System.out.println("File with desired name already exists!");
                            return;
                        }
                        if (oldFile.renameTo(newFile)) {
                            System.out.println("Renamed successful");
                        } else {
                            System.out.println("Rename failed");
                        }
                    } catch (NullPointerException npexc) {
                        System.out.println("Error! You should enter file's absolute path!");
                    }
                    break;

                case "copy":
                    String path = "";
                    String destination = "";

                    try (BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in))) {
                        System.out.println("Enter file path: ");
                        path = bufferRead.readLine();

                        System.out.println("Enter destination folder: ");
                        destination = bufferRead.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                    File aFile = new File(path);
                    File bFile = new File(destination + "\\" + aFile.getName());

                    try (FileInputStream inStream = new FileInputStream(aFile);
                            FileOutputStream outStream = new FileOutputStream(bFile);) {

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inStream.read(buffer)) > 0) {
                            outStream.write(buffer, 0, length);
                        }
                        System.out.println("File is copied successfuly!");

                    } catch (IOException exc) {
                        System.out.println(exc);
                    }
                    break;

                case "move":
                    String movePath = "";
                    String moveDestination = "";

                    try (BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in))) {
                        System.out.println("Enter file path: ");
                        movePath = bufferRead.readLine();

                        System.out.println("Enter destination folder: ");
                        moveDestination = bufferRead.readLine();
                    } catch (IOException e) {
                        System.out.println(e);
                    }

                    File afile = new File(movePath);
                    File bfile = new File(moveDestination + "\\" + afile.getName());

                    try (FileInputStream inStream = new FileInputStream(afile);
                            FileOutputStream outStream = new FileOutputStream(bfile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inStream.read(buffer)) > 0) {
                            outStream.write(buffer, 0, length);
                        }
                        System.out.println("File is moved successfuly!");

                        inStream.close();
                        outStream.close();

                        afile.delete();
                    } catch (IOException exc) {
                        System.out.println(exc);
                    }
                    break;

                case "delete":
                    Scanner scan_del = new Scanner(System.in);
                    System.out.print("Enter the path of file: ");
                    File delFile = new File(scan_del.nextLine());

                    if (delFile.exists()) {
                        delFile.delete();
                        System.out.println("File successfuly deleted!");
                    } else {
                        System.out.println("Cannot delete " + delFile.getName() + " because "
                                + delFile.getName() + " doesn't exist.");
                    }
                    break;

                default:
                    System.out.println("Unknown command!");
                    
            }

        }
    }

}
