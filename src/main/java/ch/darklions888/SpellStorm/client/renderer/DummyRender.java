package ch.darklions888.SpellStorm.client.renderer;

import ch.darklions888.SpellStorm.client.renderer.entity.model.DummyModel;
import ch.darklions888.SpellStorm.lib.Lib;
import ch.darklions888.SpellStorm.objects.entities.DummyEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class DummyRender extends MobRenderer<DummyEntity, DummyModel<DummyEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation("textures/environment/dummy_entity.png");

    public DummyRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new DummyModel(), 0.7f);
    }

    @Override
    public ResourceLocation getEntityTexture(DummyEntity entity) {
        return TEXTURE;
    }
}