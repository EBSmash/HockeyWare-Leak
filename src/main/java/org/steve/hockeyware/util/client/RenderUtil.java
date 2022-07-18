package org.steve.hockeyware.util.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.steve.hockeyware.features.Globals;
import org.steve.hockeyware.util.Timer;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

public class RenderUtil implements Globals {
    protected static final ResourceLocation shader = new ResourceLocation("blur.json");
    static final Timer timer = new Timer();
    protected static int lastScale;
    protected static int lastScaleWidth;
    protected static int lastScaleHeight;
    protected static Framebuffer buffer;
    protected static ShaderGroup blurShader;
    private static float[] rgba;

    public static void drawRect(double x, double y, double width, double height, int color) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        getUpdatedColor(color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(x + width, y, 0.0).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        buffer.pos(x, y, 0.0).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        buffer.pos(x, y + height, 0.0).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        buffer.pos(x + width, y + height, 0.0).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }

    private static void fakeGuiRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void enableGL2D() {
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    private static void disableGL2D() {
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }


    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        RenderUtil.glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (float) distance / 2.0f / (2.0f + (2.0f - scale));
        if (scaleDistance < 1.0f) {
            scaleDistance = 1.0f;
        }
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.02666667f;
        GlStateManager.translate((double) x - RenderUtil.mc.getRenderManager().renderPosX, (double) y - RenderUtil.mc.getRenderManager().renderPosY, (double) z - RenderUtil.mc.getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-RenderUtil.mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(RenderUtil.mc.player.rotationPitch, RenderUtil.mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    private static void getUpdatedColor(int color) {
        rgba = ColorUtil.toRGBA(color);
    }

    public static void drawLine(double startX, double startY, double endX, double endY, float width, int color) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(width);

        glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

        getUpdatedColor(color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(startX, startY, 0.0).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        buffer.pos(endX, endY, 0.0).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        tessellator.draw();

        glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
    }


    public static void drawBlurryRect(float x, float y, float x1, float y1, int intensity, int size) {
        drawAltRect(
                (int) x,
                (int) y,
                (int) x1,
                (int) y1, new Color(50, 50, 50, 50).getRGB());
        blurArea(
                (int) x,
                (int) y,
                (int) x1 - (int) x,
                (int) y1 - (int) y,
                intensity, size, size);
    }

    public static void blurArea(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
        ScaledResolution scale = new ScaledResolution(Minecraft.getMinecraft());
        int factor = scale.getScaleFactor();
        int factor2 = scale.getScaledWidth();
        int factor3 = scale.getScaledHeight();
        if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null
                || blurShader == null) {
            initFboAndShader();
        }
        lastScale = factor;
        lastScaleWidth = factor2;
        lastScaleHeight = factor3;

        if (OpenGlHelper.isFramebufferEnabled()) {

            if (timer.hasReached(1000)) {
                buffer.framebufferClear();
                timer.reset();
            }

            GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor,
                    (height) * factor);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            setShaderConfigs(intensity, blurWidth, blurHeight);
            buffer.bindFramebuffer(true);
            blurShader.render(mc.getRenderPartialTicks());

            mc.getFramebuffer().bindFramebuffer(true);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO,
                    GL11.GL_ONE);
            buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
            GlStateManager.disableBlend();
            GL11.glScalef(factor, factor, 0);

        }
    }

    public static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
        Objects.requireNonNull(((IShaderGroup) blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("Radius")).set(intensity);
        Objects.requireNonNull(((IShaderGroup) blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("Radius")).set(intensity);

        Objects.requireNonNull(((IShaderGroup) blurShader).getListShaders().get(0).getShaderManager().getShaderUniform("BlurDir")).set(blurWidth, blurHeight);
        Objects.requireNonNull(((IShaderGroup) blurShader).getListShaders().get(1).getShaderManager().getShaderUniform("BlurDir")).set(blurHeight, blurWidth);
    }

    public static void drawAltRect(float startX, float startY, float endX, float endY, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(startX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(endX, endY, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(endX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(startX, startY, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void initFboAndShader() {
        try {
            if (buffer != null) {
                buffer.deleteFramebuffer();
            }
            blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
            blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
            buffer = ((IShaderGroup) blurShader).getListFramebuffers().get(0);
            // buffer.enableStencil();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

