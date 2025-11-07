package com.zombiemmo.game3d;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Creates and manages particle effects
 */
public class ParticleEffects {
    private final AssetManager assetManager;

    public ParticleEffects(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Create blood splatter effect for combat
     */
    public ParticleEmitter createBloodEffect() {
        ParticleEmitter blood = new ParticleEmitter("Blood", ParticleMesh.Type.Triangle, 20);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
        blood.setMaterial(mat);

        blood.setImagesX(2);
        blood.setImagesY(2);
        blood.setEndColor(new ColorRGBA(0.8f, 0f, 0f, 0f));
        blood.setStartColor(new ColorRGBA(1f, 0.1f, 0.1f, 1f));
        blood.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2f, 0));
        blood.setStartSize(0.2f);
        blood.setEndSize(0.01f);
        blood.setGravity(0, -5f, 0);
        blood.setLowLife(0.3f);
        blood.setHighLife(0.8f);
        blood.getParticleInfluencer().setVelocityVariation(0.3f);
        blood.setParticlesPerSec(0); // Manual emission

        return blood;
    }

    /**
     * Create sparkle effect for gathering resources
     */
    public ParticleEmitter createGatherEffect(ColorRGBA color) {
        ParticleEmitter sparkle = new ParticleEmitter("Sparkle", ParticleMesh.Type.Triangle, 15);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
        sparkle.setMaterial(mat);

        sparkle.setImagesX(1);
        sparkle.setImagesY(1);
        sparkle.setEndColor(new ColorRGBA(color.r, color.g, color.b, 0f));
        sparkle.setStartColor(new ColorRGBA(color.r, color.g, color.b, 1f));
        sparkle.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 1.5f, 0));
        sparkle.setStartSize(0.15f);
        sparkle.setEndSize(0.01f);
        sparkle.setGravity(0, -2f, 0);
        sparkle.setLowLife(0.5f);
        sparkle.setHighLife(1f);
        sparkle.getParticleInfluencer().setVelocityVariation(0.4f);
        sparkle.setParticlesPerSec(0); // Manual emission

        return sparkle;
    }

    /**
     * Create level up effect
     */
    public ParticleEmitter createLevelUpEffect() {
        ParticleEmitter levelUp = new ParticleEmitter("LevelUp", ParticleMesh.Type.Triangle, 30);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
        levelUp.setMaterial(mat);

        levelUp.setImagesX(1);
        levelUp.setImagesY(1);
        levelUp.setEndColor(new ColorRGBA(1f, 1f, 0f, 0f));
        levelUp.setStartColor(new ColorRGBA(1f, 0.8f, 0f, 1f));
        levelUp.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 3f, 0));
        levelUp.setStartSize(0.3f);
        levelUp.setEndSize(0.01f);
        levelUp.setGravity(0, 0f, 0); // Float up
        levelUp.setLowLife(1f);
        levelUp.setHighLife(2f);
        levelUp.getParticleInfluencer().setVelocityVariation(0.5f);
        levelUp.setParticlesPerSec(0); // Manual emission

        return levelUp;
    }

    /**
     * Create zombie death effect (green mist)
     */
    public ParticleEmitter createZombieDeathEffect() {
        ParticleEmitter death = new ParticleEmitter("ZombieDeath", ParticleMesh.Type.Triangle, 25);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
        death.setMaterial(mat);

        death.setImagesX(1);
        death.setImagesY(3);
        death.setEndColor(new ColorRGBA(0.3f, 0.8f, 0.3f, 0f));
        death.setStartColor(new ColorRGBA(0.5f, 1f, 0.5f, 0.8f));
        death.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 1f, 0));
        death.setStartSize(0.5f);
        death.setEndSize(1.5f);
        death.setGravity(0, 0.5f, 0); // Float up
        death.setLowLife(1f);
        death.setHighLife(2f);
        death.getParticleInfluencer().setVelocityVariation(0.6f);
        death.setParticlesPerSec(0); // Manual emission

        return death;
    }

    /**
     * Create healing effect
     */
    public ParticleEmitter createHealingEffect() {
        ParticleEmitter heal = new ParticleEmitter("Healing", ParticleMesh.Type.Triangle, 20);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
        heal.setMaterial(mat);

        heal.setImagesX(1);
        heal.setImagesY(1);
        heal.setEndColor(new ColorRGBA(0f, 1f, 0f, 0f));
        heal.setStartColor(new ColorRGBA(0.2f, 1f, 0.2f, 1f));
        heal.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 2f, 0));
        heal.setStartSize(0.2f);
        heal.setEndSize(0.01f);
        heal.setGravity(0, -1f, 0);
        heal.setLowLife(0.5f);
        heal.setHighLife(1.2f);
        heal.getParticleInfluencer().setVelocityVariation(0.3f);
        heal.setParticlesPerSec(0); // Manual emission

        return heal;
    }

    /**
     * Create infection/poison effect
     */
    public ParticleEmitter createInfectionEffect() {
        ParticleEmitter infection = new ParticleEmitter("Infection", ParticleMesh.Type.Triangle, 15);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/smoketrail.png"));
        infection.setMaterial(mat);

        infection.setImagesX(1);
        infection.setImagesY(3);
        infection.setEndColor(new ColorRGBA(0.5f, 0f, 0.5f, 0f));
        infection.setStartColor(new ColorRGBA(0.8f, 0.2f, 0.8f, 0.6f));
        infection.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0.5f, 0));
        infection.setStartSize(0.3f);
        infection.setEndSize(0.8f);
        infection.setGravity(0, 0.2f, 0);
        infection.setLowLife(0.8f);
        infection.setHighLife(1.5f);
        infection.getParticleInfluencer().setVelocityVariation(0.4f);
        infection.setParticlesPerSec(0); // Manual emission

        return infection;
    }

    /**
     * Emit particles once (burst)
     */
    public void emitBurst(ParticleEmitter emitter, int count) {
        emitter.emitAllParticles();
        // Auto-remove after particles die
        emitter.setParticlesPerSec(0);
    }

    /**
     * Attach effect to a node and emit
     */
    public void playEffect(Node parentNode, ParticleEmitter effect, Vector3f position) {
        effect.setLocalTranslation(position);
        parentNode.attachChild(effect);
        emitBurst(effect, 20);

        // Schedule removal after effect completes
        // (In a real game loop, you'd track this more carefully)
    }
}
