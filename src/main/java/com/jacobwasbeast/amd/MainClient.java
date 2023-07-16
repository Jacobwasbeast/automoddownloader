package com.jacobwasbeast.amd;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;

import java.io.*;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class MainClient implements ClientModInitializer {
    public static int strike = 0;
    public static boolean runonce = false;
    public static ArrayList<String> list = new ArrayList<>();
    public static ArrayList<String> todownload = new ArrayList<>();

    @Override
    public void onInitializeClient() {


    }
    public static void showmessage(String message) {



    }

    public static boolean sync(String ftpserver, String port, String username, String password) {
        int reply;
        String protocol = "TLS"; // TLS / SSL
        boolean isImpicit = true;
        int timeoutInMillis = 300000;
        FTPSClient ftp = new FTPSClient(protocol, isImpicit);
        try {
            MainClient.runonce = true;
            FTPClientConfig config = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
            config.setUnparseableEntries(true);
            ftp.configure(config);
            ftp.setDataTimeout(timeoutInMillis);
            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            ftp.connect(ftpserver, Integer.parseInt(port));
            ftp.login(username, password);
            ftp.enterLocalPassiveMode();
            ftp.execPBSZ(0);
            ftp.execPROT("P");
            System.out.println("Connected to " + ftpserver + ".");
            System.err.println("made it");
            //  System.err.println(ftp.listFiles());
            //  System.err.println(ftp.listDirectories());
            todownload = new ArrayList<>();
            ArrayList<String> names = new ArrayList<>();
            ArrayList<String> namesa = new ArrayList<>();
            File folder = new File(MinecraftClient.getInstance().runDirectory.getPath().toString() + "\\mods\\");
            File[] listOfFiles = folder.listFiles();
            FTPFile[] listOfFilesftp = ftp.listFiles("/");
            for (FTPFile file : listOfFilesftp) {
                namesa.add(file.getName());
            }
            for (File file : listOfFiles) {
                names.add(file.getName());
                if (!namesa.contains(file.getName())) {
                    file.delete();
                }

            }
            System.err.println(listOfFiles);

            ftp.disconnect();

        }
        catch (Exception e) {
            if (strike>30) {
                try {
                    ftp.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                showmessage("Failed to download mods error code: " + e.toString());
            }
            else {
                strike = strike +1;
                try {
                    ftp.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                sync(ftpserver,port,username,password);
            }

        }

        return true;
    }
    public static ArrayList<String> setTodownload(FTPClient ftp, File folder, ArrayList<String> names) throws IOException {
        FTPFile[] remoteFiles = ftp.listFiles("/");
        for (FTPFile remoteFile : remoteFiles) {
            if (!remoteFile.getName().equals(".") &&
                    !remoteFile.getName().equals("..")) {
                boolean beef = false;
                if (names.contains(remoteFile.getName())) {
                    names.remove(remoteFile.getName());

//                    if (new File(folder.getPath() + remoteFile.getName()).hashCode() != remoteFile.hashCode()) {
//                        beef = true;
//                    }
                } else {
                    beef = true;
                }

                if (beef) {
                    String localPath = folder.getPath();
                    String remotePath = "/";
                    String remoteFilePath = remotePath + "/" + remoteFile.getName();
                    String localFilePath = localPath + "/" + remoteFile.getName();
                    System.out.println(
                            "Downloading remote folder " + remotePath + " to " + localPath);
                    if (remoteFile.isDirectory()) {
                        new File(localFilePath).mkdirs();
                    } else {
                        OutputStream outputStream =
                                new BufferedOutputStream(new FileOutputStream(localFilePath));
                        if (!ftp.retrieveFile(remoteFilePath, outputStream)) {
                            System.out.println(
                                    "Failed to download file " + remoteFilePath);
                        }
                        outputStream.close();
                        System.err.println(remoteFile.getName());
                        todownload.add(remoteFile.getName());

                    }

                }
            }


//                                if (!file1.getName().equals(fileFtp.getName())) {
//                                    beans.set(true);
//                                }
//                                else {
//                                    if (file1.length() != fileFtp.getSize()) {
//                                        beans.set(true);
//                                    }
//                                }
//                            if (beans.get()) {
//
//                            }
        }
        return names;
    }
    public static byte[] readFileToBytes(String filePath) throws IOException {

        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file);

            //read file into bytes[]
            fis.read(bytes);

        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return bytes;
    }
}
