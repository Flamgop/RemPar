package net.flamgop.rempar.mixin;

import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BillboardParticle.class)
public class BillboardParticleMixin {
    /**
     * @author flamgop
     * @reason haha no
     */
    @Overwrite
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        // Do nothing, hopefully it doesn't render anything
    }
}
