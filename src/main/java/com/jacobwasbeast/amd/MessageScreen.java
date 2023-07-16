package com.jacobwasbeast.amd;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class MessageScreen extends Screen {
    protected MessageScreen(String Message) {
        super(Text.of(Message));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 70, 16777215);
    }


}
