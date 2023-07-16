package com.jacobwasbeast.amd.mixins.newpacket;

import com.jacobwasbeast.amd.MainClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.FabricUtil;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.security.PublicKey;

public class SyncPacket implements Packet<ClientLoginPacketListener> {
    private final String serverId;
    private final boolean shoulda;
    private String ftpserver = "a complete placeholder:8080";
    public SyncPacket(String serverId,boolean should) {
        this.serverId = serverId;
        this.shoulda = should;
    }
    public SyncPacket(String serverId,boolean should,String ftp) {
        this.serverId = serverId;
        this.shoulda = should;
        this.ftpserver = ftp;
    }
    public void write(PacketByteBuf buf) {
        buf.writeString(this.serverId);
    }

    @Override
    public void apply(ClientLoginPacketListener listener) {
        if (this.shoulda) {
            if (FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) {
                if (!ftpserver.equals("a complete placeholder:8080")) {
//                    if (MainClient.sync(ftpserver)) {
//                        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new AllowAccess(MinecraftClient.getInstance().player.getGameProfile()));
//                    }
                }
            }
        }
        else {
            listener.onDisconnected(new TranslatableText("Accept Before Joining"));
        }
    }

    public String getServerId() {
        return this.serverId;
    }

}
