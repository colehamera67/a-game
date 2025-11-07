package com.zombiemmo.game3d;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.jme3.texture.Image;
import com.jme3.texture.image.ImageRaster;
import com.jme3.util.BufferUtils;

import java.nio.ByteBuffer;

/**
 * Creates and manages 3D models for the game
 */
public class ModelFactory {
    private final AssetManager assetManager;

    public ModelFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * Create a humanoid player model
     */
    public Node createPlayerModel() {
        Node playerNode = new Node("PlayerModel");

        // Body (torso)
        Box bodyBox = new Box(0.4f, 0.6f, 0.3f);
        Geometry body = new Geometry("Body", bodyBox);
        Material bodyMat = createColoredMaterial(new ColorRGBA(0.2f, 0.5f, 0.9f, 1f)); // Blue
        body.setMaterial(bodyMat);
        body.setLocalTranslation(0, 0.6f, 0);
        playerNode.attachChild(body);

        // Head
        Sphere headSphere = new Sphere(16, 16, 0.3f);
        Geometry head = new Geometry("Head", headSphere);
        Material headMat = createColoredMaterial(new ColorRGBA(0.9f, 0.8f, 0.7f, 1f)); // Skin color
        head.setMaterial(headMat);
        head.setLocalTranslation(0, 1.4f, 0);
        playerNode.attachChild(head);

        // Arms
        Cylinder armShape = new Cylinder(8, 8, 0.12f, 0.7f, true);

        Geometry leftArm = new Geometry("LeftArm", armShape);
        leftArm.setMaterial(bodyMat);
        leftArm.setLocalTranslation(-0.6f, 0.6f, 0);
        leftArm.rotate((float) Math.PI / 2, 0, (float) Math.PI / 2);
        playerNode.attachChild(leftArm);

        Geometry rightArm = new Geometry("RightArm", armShape);
        rightArm.setMaterial(bodyMat);
        rightArm.setLocalTranslation(0.6f, 0.6f, 0);
        rightArm.rotate((float) Math.PI / 2, 0, (float) Math.PI / 2);
        playerNode.attachChild(rightArm);

        // Legs
        Cylinder legShape = new Cylinder(8, 8, 0.15f, 0.8f, true);

        Geometry leftLeg = new Geometry("LeftLeg", legShape);
        Material legMat = createColoredMaterial(new ColorRGBA(0.3f, 0.3f, 0.3f, 1f)); // Dark gray
        leftLeg.setMaterial(legMat);
        leftLeg.setLocalTranslation(-0.2f, -0.4f, 0);
        leftLeg.rotate((float) Math.PI / 2, 0, 0);
        playerNode.attachChild(leftLeg);

        Geometry rightLeg = new Geometry("RightLeg", legShape);
        rightLeg.setMaterial(legMat);
        rightLeg.setLocalTranslation(0.2f, -0.4f, 0);
        rightLeg.rotate((float) Math.PI / 2, 0, 0);
        playerNode.attachChild(rightLeg);

        return playerNode;
    }

    /**
     * Create a zombie model (varies by type)
     */
    public Node createZombieModel(ColorRGBA color, float size) {
        Node zombieNode = new Node("ZombieModel");

        // Hunched body
        Box bodyBox = new Box(0.35f * size, 0.5f * size, 0.28f * size);
        Geometry body = new Geometry("Body", bodyBox);
        Material bodyMat = createColoredMaterial(color.mult(0.7f));
        body.setMaterial(bodyMat);
        body.setLocalTranslation(0, 0.5f * size, -0.1f * size);
        body.rotate(-0.3f, 0, 0); // Slight hunch
        zombieNode.attachChild(body);

        // Zombie head (larger and misshapen)
        Sphere headSphere = new Sphere(12, 12, 0.32f * size);
        Geometry head = new Geometry("Head", headSphere);
        Material headMat = createColoredMaterial(color.mult(0.8f));
        head.setMaterial(headMat);
        head.setLocalTranslation(0, 1.1f * size, 0);
        zombieNode.attachChild(head);

        // Glowing eyes
        Sphere eyeShape = new Sphere(8, 8, 0.06f * size);

        Geometry leftEye = new Geometry("LeftEye", eyeShape);
        Material eyeMat = createGlowingMaterial(new ColorRGBA(1f, 0.1f, 0.1f, 1f));
        leftEye.setMaterial(eyeMat);
        leftEye.setLocalTranslation(-0.12f * size, 1.15f * size, 0.25f * size);
        zombieNode.attachChild(leftEye);

        Geometry rightEye = new Geometry("RightEye", eyeShape);
        rightEye.setMaterial(eyeMat);
        rightEye.setLocalTranslation(0.12f * size, 1.15f * size, 0.25f * size);
        zombieNode.attachChild(rightEye);

        // Long arms (zombies have long reaching arms)
        Cylinder armShape = new Cylinder(6, 6, 0.1f * size, 0.9f * size, true);

        Geometry leftArm = new Geometry("LeftArm", armShape);
        leftArm.setMaterial(bodyMat);
        leftArm.setLocalTranslation(-0.5f * size, 0.5f * size, 0);
        leftArm.rotate((float) Math.PI / 2, 0, (float) Math.PI / 2.5f);
        zombieNode.attachChild(leftArm);

        Geometry rightArm = new Geometry("RightArm", armShape);
        rightArm.setMaterial(bodyMat);
        rightArm.setLocalTranslation(0.5f * size, 0.5f * size, 0);
        rightArm.rotate((float) Math.PI / 2, 0, (float) Math.PI / 2.5f);
        zombieNode.attachChild(rightArm);

        // Legs
        Cylinder legShape = new Cylinder(6, 6, 0.12f * size, 0.7f * size, true);

        Geometry leftLeg = new Geometry("LeftLeg", legShape);
        leftLeg.setMaterial(bodyMat);
        leftLeg.setLocalTranslation(-0.15f * size, -0.35f * size, 0);
        leftLeg.rotate((float) Math.PI / 2, 0, 0);
        zombieNode.attachChild(leftLeg);

        Geometry rightLeg = new Geometry("RightLeg", legShape);
        rightLeg.setMaterial(bodyMat);
        rightLeg.setLocalTranslation(0.15f * size, -0.35f * size, 0);
        rightLeg.rotate((float) Math.PI / 2, 0, 0);
        zombieNode.attachChild(rightLeg);

        return zombieNode;
    }

    /**
     * Create a tree model
     */
    public Node createTreeModel(float height) {
        Node treeNode = new Node("Tree");

        // Trunk
        Cylinder trunk = new Cylinder(12, 12, 0.2f, height, true);
        Geometry trunkGeom = new Geometry("Trunk", trunk);
        Material trunkMat = createColoredMaterial(new ColorRGBA(0.4f, 0.3f, 0.2f, 1f)); // Brown
        trunkGeom.setMaterial(trunkMat);
        trunkGeom.setLocalTranslation(0, height / 2, 0);
        trunkGeom.rotate((float) Math.PI / 2, 0, 0);
        treeNode.attachChild(trunkGeom);

        // Leaves (sphere)
        Sphere leaves = new Sphere(16, 16, height * 0.4f);
        Geometry leavesGeom = new Geometry("Leaves", leaves);
        Material leavesMat = createColoredMaterial(new ColorRGBA(0.2f, 0.6f, 0.2f, 1f)); // Green
        leavesGeom.setMaterial(leavesMat);
        leavesGeom.setLocalTranslation(0, height + height * 0.3f, 0);
        treeNode.attachChild(leavesGeom);

        return treeNode;
    }

    /**
     * Create a rock/ore model
     */
    public Node createRockModel(ColorRGBA color) {
        Node rockNode = new Node("Rock");

        // Create irregular rock shape using multiple boxes
        for (int i = 0; i < 5; i++) {
            float size = 0.3f + (float) Math.random() * 0.3f;
            Box rockBox = new Box(size, size * 0.8f, size);
            Geometry rockPiece = new Geometry("RockPiece" + i, rockBox);
            Material mat = createColoredMaterial(color.mult(0.8f + (float) Math.random() * 0.2f));
            rockPiece.setMaterial(mat);

            float offsetX = ((float) Math.random() - 0.5f) * 0.4f;
            float offsetY = ((float) Math.random()) * 0.3f;
            float offsetZ = ((float) Math.random() - 0.5f) * 0.4f;
            rockPiece.setLocalTranslation(offsetX, offsetY, offsetZ);
            rockPiece.rotate((float) Math.random(), (float) Math.random(), (float) Math.random());

            rockNode.attachChild(rockPiece);
        }

        return rockNode;
    }

    /**
     * Create water/fishing spot model
     */
    public Node createWaterModel() {
        Node waterNode = new Node("Water");

        // Flat water surface
        Box waterBox = new Box(1f, 0.1f, 1f);
        Geometry water = new Geometry("WaterSurface", waterBox);
        Material waterMat = createWaterMaterial();
        water.setMaterial(waterMat);
        water.setLocalTranslation(0, 0.05f, 0);
        waterNode.attachChild(water);

        return waterNode;
    }

    /**
     * Create plant/herb model
     */
    public Node createPlantModel() {
        Node plantNode = new Node("Plant");

        // Stem
        Cylinder stem = new Cylinder(8, 8, 0.05f, 0.5f, true);
        Geometry stemGeom = new Geometry("Stem", stem);
        Material stemMat = createColoredMaterial(new ColorRGBA(0.3f, 0.5f, 0.2f, 1f));
        stemGeom.setMaterial(stemMat);
        stemGeom.setLocalTranslation(0, 0.25f, 0);
        stemGeom.rotate((float) Math.PI / 2, 0, 0);
        plantNode.attachChild(stemGeom);

        // Leaves
        for (int i = 0; i < 4; i++) {
            Box leaf = new Box(0.2f, 0.01f, 0.15f);
            Geometry leafGeom = new Geometry("Leaf" + i, leaf);
            Material leafMat = createColoredMaterial(new ColorRGBA(0.4f, 0.8f, 0.3f, 1f));
            leafGeom.setMaterial(leafMat);

            float angle = (float) (i * Math.PI / 2);
            float height = 0.3f + i * 0.1f;
            leafGeom.setLocalTranslation(
                (float) Math.cos(angle) * 0.15f,
                height,
                (float) Math.sin(angle) * 0.15f
            );
            leafGeom.rotate(0, angle, (float) Math.PI / 4);

            plantNode.attachChild(leafGeom);
        }

        return plantNode;
    }

    /**
     * Create a colored material with lighting
     */
    private Material createColoredMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color.mult(0.5f));
        mat.setColor("Specular", ColorRGBA.White.mult(0.3f));
        mat.setFloat("Shininess", 32f);
        return mat;
    }

    /**
     * Create a glowing emissive material
     */
    private Material createGlowingMaterial(ColorRGBA color) {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);
        mat.setColor("GlowColor", color);
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 64f);
        return mat;
    }

    /**
     * Create water material with transparency
     */
    private Material createWaterMaterial() {
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", new ColorRGBA(0.2f, 0.4f, 0.8f, 0.7f));
        mat.setColor("Ambient", new ColorRGBA(0.2f, 0.4f, 0.8f, 0.7f));
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 128f);
        mat.getAdditionalRenderState().setBlendMode(com.jme3.material.RenderState.BlendMode.Alpha);
        return mat;
    }
}
