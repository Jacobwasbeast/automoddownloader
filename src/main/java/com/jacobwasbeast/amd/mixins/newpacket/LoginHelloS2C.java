package com.jacobwasbeast.amd.mixins.newpacket;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.encryption.NetworkEncryptionUtils;
import net.minecraft.network.listener.ClientLoginPacketListener;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;

import java.security.PublicKey;

public class LoginHelloS2C extends LoginHelloS2CPacket {
    private final String serverId;
    private final boolean hasmod;
    private final byte[] publicKey;
    private final byte[] nonce;
    private final int a;

    public LoginHelloS2C(String serverId, byte[] publicKey, byte[] nonce) {
        super(serverId,publicKey,nonce);
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
        this.hasmod = false;
        this.a = 0;
    }
    public LoginHelloS2C(String serverId, byte[] publicKey, byte[] nonce, int b) {
        super(serverId,publicKey,nonce);
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.nonce = nonce;
        this.hasmod = true;
        this.a = b;
    }
    public LoginHelloS2C(PacketByteBuf buf) {
        super(buf);
        this.serverId = buf.readString(20);
        this.publicKey = buf.readByteArray();
        this.nonce = buf.readByteArray();
        this.hasmod = buf.readBoolean();
        this.a = buf.readInt();
    }

    public void write(PacketByteBuf buf) {
        buf.writeString(this.serverId);
        buf.writeByteArray(this.publicKey);
        buf.writeByteArray(this.nonce);
        buf.writeBoolean(this.hasmod);
        buf.writeInt(this.a);
    }

    public void apply(ClientLoginPacketListener clientLoginPacketListener) {
        clientLoginPacketListener.onHello(this);
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() throws NetworkEncryptionException {
        return NetworkEncryptionUtils.readEncodedPublicKey(this.publicKey);
    }
    public int shouldPass() {
        return this.a;
    }
    public boolean isHasmod() {
        return this.hasmod;
    }
    public byte[] getNonce() {
        return this.nonce;
    }
}
