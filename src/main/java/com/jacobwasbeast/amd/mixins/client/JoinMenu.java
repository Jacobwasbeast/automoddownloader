package com.jacobwasbeast.amd.mixins.client;

import com.jacobwasbeast.amd.Main;
import com.jacobwasbeast.amd.MainClient;
import com.jacobwasbeast.amd.mixins.newpacket.LoginHelloC2S;
import com.jacobwasbeast.amd.mixins.newpacket.LoginHelloS2C;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.encryption.NetworkEncryptionException;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public abstract class JoinMenu {
    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract void send(Packet<?> packet);

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At("INVOKE"), allow = 1)
    public void onConnect(Packet<?> packet, CallbackInfo ci) throws NetworkEncryptionException {
        if (packet instanceof LoginHelloC2SPacket) {
            if (packet instanceof LoginHelloC2S) {
                LOGGER.debug("Sent Packet Your Free");
            }
            else {
                if (MainClient.list.contains((Main.ip))) {
                    send(new LoginHelloC2S(((LoginHelloC2SPacket) packet).getProfile(),true,1));
                }
                else {
                    //send to gui to confirm
                    send(new LoginHelloC2S(((LoginHelloC2SPacket) packet).getProfile(),true,0));
                }

                ci.cancel();
            }
        }
    }

}
