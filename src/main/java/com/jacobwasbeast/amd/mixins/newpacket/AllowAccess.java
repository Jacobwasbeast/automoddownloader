package com.jacobwasbeast.amd.mixins.newpacket;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;

import java.util.UUID;

public class AllowAccess extends LoginHelloC2SPacket implements Packet<ServerLoginPacketListener> {
    private final GameProfile profile;

    public AllowAccess(GameProfile profile) {
        super(profile);
        this.profile = profile;
    }

    public AllowAccess(PacketByteBuf buf) {
        super(buf);
        this.profile = new GameProfile((UUID)null, buf.readString(16));
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.profile.getName());
    }

    public void apply(ServerLoginPacketListener serverLoginPacketListener) {
        serverLoginPacketListener.onHello(this);
    }

    @Override
    public boolean isWritingErrorSkippable() {
        return false;
    }

    public GameProfile getProfile() {
        return this.profile;
    }
}
