package com.jacobwasbeast.amd;

//import me.shedaniel.autoconfig.AutoConfig;
//import me.shedaniel.autoconfig.ConfigData;
//import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.Bootstrap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.crash.CrashReport;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPSClient;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static net.minecraft.client.MinecraftClient.printCrashReport;

public class Main implements ModInitializer {
    public static String ftpserver = "";
    public static String ip = "";
    public static String pass = "";
    public static String port = "";
    public static String user = "";
    private static Logger LOGGER;

    @Override
    public void onInitialize() {
//        AutoConfig.register(Config.class, Toml4jConfigSerializer::new);
//        ftpserver = AutoConfig.getConfigHolder(Config.class).get().ftpserver;
//        if (!Main.runonce) {

     //   }
    }
    public static int strike = 0;
    public static boolean runonce = false;
    public static Runnable crash = null;
    public static ArrayList<String> list = new ArrayList<>();
    public static ArrayList<String> todownload = new ArrayList<>();
    public static void showmessage(String message) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            crash = new Runnable() {
                @Override
                public void run() {
                    // open Minecraft screen with message
                    try {
                        MinecraftClient.getInstance().setScreen(new MessageScreen(message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };









        }
    }

    public static boolean sync(String ftpserver, String port, String username, String password) {
        int reply;
        String protocol = "TLS"; // TLS / SSL
        boolean isImpicit = true;
        int timeoutInMillis = 300000;
        FTPSClient ftp = new FTPSClient(protocol, isImpicit);
        try {
            Main.runonce = true;
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
            File folder = new File(FabricLoader.getInstance().getGameDir().toString() + "\\mods\\");
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
            for (File file : listOfFiles) {
                names.add(file.getName());
            }
            System.err.println(listOfFiles);
            setTodownload(ftp,folder,names);
            ftp.disconnect();

        }
        catch (Exception e) {
            showmessage("Failed to download mods error code: " + e.toString());
        }

        return true;
    }
    public static void setTodownload(FTPClient ftp, File folder, ArrayList<String> names) throws IOException {
        FTPFile[] remoteFiles = ftp.listFiles("/");
        for (FTPFile remoteFile : remoteFiles) {
            if (!remoteFile.getName().equals(".") &&
                    !remoteFile.getName().equals("..")) {
                boolean beef = false;
                if (names.contains(remoteFile.getName())) {
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
