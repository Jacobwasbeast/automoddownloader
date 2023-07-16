package com.jacobwasbeast.amd.mixins.server;

import com.jacobwasbeast.amd.Main;
import com.jacobwasbeast.amd.mixins.newpacket.LoginHelloS2C;
import com.jacobwasbeast.amd.mixins.newpacket.SyncPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import net.minecraft.network.packet.s2c.login.LoginHelloS2CPacket;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public class OnJoin {
    @Shadow @Final public ClientConnection connection;

    @Inject(method = "onHello", at = @At("INVOKE"),cancellable = true)
    public void onHello(LoginHelloC2SPacket packet, CallbackInfo ci) {
        Packet packet1 = packet;
        if (packet1 instanceof LoginHelloC2SPacket) {
            if (((LoginHelloS2C) packet1).isHasmod()) {
                if (((LoginHelloS2C) packet1).shouldPass()==1) {
                    connection.send(new SyncPacket(Main.ip,true, Main.ftpserver));

                }
                else {
                    connection.send(new SyncPacket(Main.ip,false));
                    ci.cancel();
                }

            }
            else {

            }

        }
        else {

        }
    }
}
