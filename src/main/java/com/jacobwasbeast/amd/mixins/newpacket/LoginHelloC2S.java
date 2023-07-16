package com.jacobwasbeast.amd.mixins.newpacket;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerLoginPacketListener;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;

import java.util.UUID;

public class LoginHelloC2S extends LoginHelloC2SPacket implements Packet<ServerLoginPacketListener> {
    private final GameProfile profile;
    private final boolean hasmod;
    private final int a;
    public LoginHelloC2S(GameProfile profile) {
        super(profile);
        this.profile = profile;
        this.hasmod = false;
        this.a=0;
    }
    public LoginHelloC2S(GameProfile profile,boolean hasmod) {
        super(profile);
        this.profile = profile;
        this.hasmod = hasmod;
        this.a=0;
    }
    public LoginHelloC2S(GameProfile profile,boolean hasmod,int ab) {
        super(profile);
        this.profile = profile;
        this.hasmod = hasmod;
        this.a=ab;
    }

    public LoginHelloC2S(PacketByteBuf buf) {
        super(buf);
        this.profile = new GameProfile((UUID)null, buf.readString(16));
        this.hasmod = buf.readBoolean();
        this.a=buf.readInt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.profile.getName());
        buf.writeBoolean(this.hasmod);
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
    public boolean isHasmod() {
        return this.hasmod;
    }
}
