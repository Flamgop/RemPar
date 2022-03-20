package net.flamgop.rempar.mixin;

import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.WaterSplashParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WaterSplashParticle.class)
public class WaterSplashParticleMixin extends RainSplashParticle {
    protected WaterSplashParticleMixin(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Quaternion camRot;
        Vec3d camPos = camera.getPos();
        float x = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - camPos.getX());
        float y = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - camPos.getY());
        float z = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - camPos.getZ());
        if (this.angle == 0.0f) {
            camRot = camera.getRotation();
        } else {
            camRot = new Quaternion(camera.getRotation());
            float i = MathHelper.lerp(tickDelta, this.prevAngle, this.angle);
            camRot.hamiltonProduct(Vec3f.POSITIVE_Z.getRadialQuaternion(i));
        }
        Vec3f mainVec = new Vec3f(-1, -1, 0);
        mainVec.rotate(camRot);
        Vec3f[] rotationVecs = new Vec3f[]{new Vec3f(-1.0f, -1.0f, 0.0f), new Vec3f(-1.0f, 1.0f, 0.0f), new Vec3f(1.0f, 1.0f, 0.0f), new Vec3f(1.0f, -1.0f, 0.0f)};
        float size = this.getSize(tickDelta);
        for (int k = 0; k < 4; ++k) {
            Vec3f vec3f2 = rotationVecs[k];
            vec3f2.rotate(camRot);
            vec3f2.scale(size);
            vec3f2.add(x, y, z);
        }
        float minU = this.getMinU();
        float maxU = this.getMaxU();
        float minV = this.getMinV();
        float maxV = this.getMaxV();
        int brightness = this.getBrightness(tickDelta);
        vertexConsumer.vertex(rotationVecs[0].getX(), rotationVecs[0].getY(), rotationVecs[0].getZ()).texture(maxU, maxV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(rotationVecs[1].getX(), rotationVecs[1].getY(), rotationVecs[1].getZ()).texture(maxU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(rotationVecs[2].getX(), rotationVecs[2].getY(), rotationVecs[2].getZ()).texture(minU, minV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
        vertexConsumer.vertex(rotationVecs[3].getX(), rotationVecs[3].getY(), rotationVecs[3].getZ()).texture(minU, maxV).color(this.red, this.green, this.blue, this.alpha).light(brightness).next();
    }
}
