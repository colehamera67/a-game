package com.zombiemmo.game3d;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Handles animations for game entities
 */
public class AnimationController {
    private float time = 0;
    private boolean isWalking = false;
    private AnimationType currentAnimation = AnimationType.IDLE;

    public enum AnimationType {
        IDLE,
        WALKING,
        ATTACKING,
        GATHERING,
        DYING
    }

    /**
     * Update animations based on elapsed time
     */
    public void update(float tpf) {
        time += tpf;
    }

    /**
     * Animate a humanoid model (player or zombie)
     */
    public void animateHumanoid(Node model, AnimationType animation, float tpf) {
        this.currentAnimation = animation;
        time += tpf;

        Spatial leftArm = model.getChild("LeftArm");
        Spatial rightArm = model.getChild("RightArm");
        Spatial leftLeg = model.getChild("LeftLeg");
        Spatial rightLeg = model.getChild("RightLeg");
        Spatial body = model.getChild("Body");
        Spatial head = model.getChild("Head");

        if (leftArm == null || rightArm == null || leftLeg == null || rightLeg == null) {
            return;
        }

        switch (animation) {
            case WALKING:
                animateWalking(leftArm, rightArm, leftLeg, rightLeg, body);
                break;
            case ATTACKING:
                animateAttacking(leftArm, rightArm, body);
                break;
            case GATHERING:
                animateGathering(leftArm, rightArm, body);
                break;
            case IDLE:
                animateIdle(leftArm, rightArm, body, head);
                break;
            case DYING:
                animateDying(model);
                break;
        }
    }

    /**
     * Animate walking motion
     */
    private void animateWalking(Spatial leftArm, Spatial rightArm, Spatial leftLeg, Spatial rightLeg, Spatial body) {
        float walkSpeed = 4f;
        float swingAmount = 0.5f;

        // Swing arms opposite to legs
        float armSwing = FastMath.sin(time * walkSpeed) * swingAmount;
        float legSwing = FastMath.sin(time * walkSpeed) * swingAmount * 0.8f;

        // Reset to base rotation
        Quaternion baseRotation = new Quaternion();
        baseRotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);

        // Left arm swings forward when right leg moves forward
        Quaternion leftArmRot = new Quaternion();
        leftArmRot.fromAngleAxis(FastMath.HALF_PI + armSwing * 0.5f, Vector3f.UNIT_Z);
        leftArmRot.multLocal(baseRotation);
        leftArm.setLocalRotation(leftArmRot);

        // Right arm swings forward when left leg moves forward
        Quaternion rightArmRot = new Quaternion();
        rightArmRot.fromAngleAxis(FastMath.HALF_PI - armSwing * 0.5f, Vector3f.UNIT_Z);
        rightArmRot.multLocal(baseRotation);
        rightArm.setLocalRotation(rightArmRot);

        // Leg rotation
        Quaternion legBase = new Quaternion();
        legBase.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);

        Quaternion leftLegRot = new Quaternion();
        leftLegRot.fromAngleAxis(legSwing, Vector3f.UNIT_X);
        leftLegRot.multLocal(legBase);
        leftLeg.setLocalRotation(leftLegRot);

        Quaternion rightLegRot = new Quaternion();
        rightLegRot.fromAngleAxis(-legSwing, Vector3f.UNIT_X);
        rightLegRot.multLocal(legBase);
        rightLeg.setLocalRotation(rightLegRot);

        // Slight body bob
        float bob = FastMath.sin(time * walkSpeed * 2) * 0.05f;
        Vector3f bodyPos = body.getLocalTranslation();
        body.setLocalTranslation(bodyPos.x, 0.6f + bob, bodyPos.z);
    }

    /**
     * Animate attacking motion
     */
    private void animateAttacking(Spatial leftArm, Spatial rightArm, Spatial body) {
        float attackSpeed = 8f;
        float attackProgress = (FastMath.sin(time * attackSpeed) + 1) / 2; // 0 to 1

        Quaternion baseRotation = new Quaternion();
        baseRotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);

        // Swing right arm forward in attacking motion
        Quaternion rightArmRot = new Quaternion();
        float swingAngle = FastMath.HALF_PI + (attackProgress * FastMath.PI * 0.7f);
        rightArmRot.fromAngleAxis(swingAngle, Vector3f.UNIT_Z);
        rightArmRot.multLocal(baseRotation);
        rightArm.setLocalRotation(rightArmRot);

        // Left arm stays relatively still
        Quaternion leftArmRot = new Quaternion();
        leftArmRot.fromAngleAxis(FastMath.HALF_PI + 0.2f, Vector3f.UNIT_Z);
        leftArmRot.multLocal(baseRotation);
        leftArm.setLocalRotation(leftArmRot);

        // Body leans into attack
        Quaternion bodyRot = new Quaternion();
        bodyRot.fromAngleAxis(attackProgress * 0.3f, Vector3f.UNIT_X);
        body.setLocalRotation(bodyRot);
    }

    /**
     * Animate gathering/mining motion
     */
    private void animateGathering(Spatial leftArm, Spatial rightArm, Spatial body) {
        float gatherSpeed = 3f;
        float gatherProgress = (FastMath.sin(time * gatherSpeed) + 1) / 2;

        Quaternion baseRotation = new Quaternion();
        baseRotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);

        // Both arms move together in gathering motion
        float armAngle = FastMath.HALF_PI + (gatherProgress * 0.8f);

        Quaternion armRot = new Quaternion();
        armRot.fromAngleAxis(armAngle, Vector3f.UNIT_Z);
        armRot.multLocal(baseRotation);

        leftArm.setLocalRotation(armRot);
        rightArm.setLocalRotation(armRot);

        // Body bends forward
        Quaternion bodyRot = new Quaternion();
        bodyRot.fromAngleAxis(-0.4f + gatherProgress * 0.2f, Vector3f.UNIT_X);
        body.setLocalRotation(bodyRot);
    }

    /**
     * Animate idle motion (subtle breathing/swaying)
     */
    private void animateIdle(Spatial leftArm, Spatial rightArm, Spatial body, Spatial head) {
        float idleSpeed = 1.5f;
        float breathe = FastMath.sin(time * idleSpeed) * 0.02f;

        // Reset arms to neutral position
        Quaternion baseRotation = new Quaternion();
        baseRotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);

        Quaternion armRot = new Quaternion();
        armRot.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Z);
        armRot.multLocal(baseRotation);

        leftArm.setLocalRotation(armRot);
        rightArm.setLocalRotation(armRot);

        // Subtle body movement (breathing)
        Vector3f bodyPos = body.getLocalTranslation();
        body.setLocalTranslation(bodyPos.x, 0.6f + breathe, bodyPos.z);

        // Reset body rotation
        body.setLocalRotation(Quaternion.IDENTITY);

        // Subtle head movement if it exists
        if (head != null) {
            Quaternion headRot = new Quaternion();
            headRot.fromAngleAxis(FastMath.sin(time * idleSpeed * 0.5f) * 0.1f, Vector3f.UNIT_Y);
            head.setLocalRotation(headRot);
        }
    }

    /**
     * Animate death/falling
     */
    private void animateDying(Node model) {
        float fallProgress = Math.min(time * 2f, 1f); // Fall over 0.5 seconds

        // Rotate entire model to fall down
        Quaternion fallRot = new Quaternion();
        fallRot.fromAngleAxis(-FastMath.HALF_PI * fallProgress, Vector3f.UNIT_Z);
        model.setLocalRotation(fallRot);

        // Lower to ground
        Vector3f pos = model.getLocalTranslation();
        model.setLocalTranslation(pos.x, 1f * (1 - fallProgress), pos.z);
    }

    /**
     * Animate floating/bobbing for resources
     */
    public void animateFloating(Spatial spatial, float tpf) {
        time += tpf;
        float bobHeight = FastMath.sin(time * 2f) * 0.1f;
        float rotationSpeed = 0.5f;

        Vector3f pos = spatial.getLocalTranslation();
        spatial.setLocalTranslation(pos.x, 0.5f + bobHeight, pos.z);

        Quaternion rotation = new Quaternion();
        rotation.fromAngleAxis(time * rotationSpeed, Vector3f.UNIT_Y);
        spatial.setLocalRotation(rotation);
    }

    /**
     * Animate zombie shambling/lurching
     */
    public void animateZombieShamble(Node model, float tpf) {
        time += tpf;

        Spatial leftArm = model.getChild("LeftArm");
        Spatial rightArm = model.getChild("RightArm");
        Spatial leftLeg = model.getChild("LeftLeg");
        Spatial rightLeg = model.getChild("RightLeg");
        Spatial body = model.getChild("Body");

        if (leftArm == null || rightArm == null) return;

        // Slow, uneven shambling motion
        float shambleSpeed = 2f;
        float unevenness = FastMath.sin(time * shambleSpeed * 1.3f) * 0.3f;

        // Arms reach forward
        Quaternion baseRotation = new Quaternion();
        baseRotation.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_Y);

        float armReach = FastMath.HALF_PI + 0.8f + FastMath.sin(time * shambleSpeed) * 0.3f;

        Quaternion leftArmRot = new Quaternion();
        leftArmRot.fromAngleAxis(armReach + unevenness, Vector3f.UNIT_Z);
        leftArmRot.multLocal(baseRotation);
        leftArm.setLocalRotation(leftArmRot);

        Quaternion rightArmRot = new Quaternion();
        rightArmRot.fromAngleAxis(armReach - unevenness, Vector3f.UNIT_Z);
        rightArmRot.multLocal(baseRotation);
        rightArm.setLocalRotation(rightArmRot);

        // Uneven leg movement
        if (leftLeg != null && rightLeg != null) {
            float legSwing = FastMath.sin(time * shambleSpeed) * 0.4f;

            Quaternion legBase = new Quaternion();
            legBase.fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);

            Quaternion leftLegRot = new Quaternion();
            leftLegRot.fromAngleAxis(legSwing + unevenness, Vector3f.UNIT_X);
            leftLegRot.multLocal(legBase);
            leftLeg.setLocalRotation(leftLegRot);

            Quaternion rightLegRot = new Quaternion();
            rightLegRot.fromAngleAxis(-legSwing - unevenness, Vector3f.UNIT_X);
            rightLegRot.multLocal(legBase);
            rightLeg.setLocalRotation(rightLegRot);
        }

        // Swaying body
        if (body != null) {
            Quaternion bodyRot = new Quaternion();
            bodyRot.fromAngleAxis(-0.3f + FastMath.sin(time * shambleSpeed) * 0.15f, Vector3f.UNIT_X);
            body.setLocalRotation(bodyRot);
        }
    }

    public void setAnimation(AnimationType animation) {
        if (this.currentAnimation != animation) {
            this.currentAnimation = animation;
            this.time = 0; // Reset animation time
        }
    }

    public AnimationType getCurrentAnimation() {
        return currentAnimation;
    }
}
