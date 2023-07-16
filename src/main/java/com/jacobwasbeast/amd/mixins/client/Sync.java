package com.jacobwasbeast.amd.mixins.client;


import com.jacobwasbeast.amd.Main;
import com.jacobwasbeast.amd.MainClient;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashOverlay.class)
public class Sync {
    @Inject(method = "render",at=@At("INVOKE"))
    public void run(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Main.crash != null) {
            Main.crash.run();
        }
    }
}
