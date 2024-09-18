package main;

import java.awt.Graphics2D;
import java.util.Random;

public class ScreenShakeManager {
    private Random random;
    private int shakeIntensity;     // Cường độ rung (số pixel)
    private int shakeDuration;      // Thời gian rung (số khung hình)
    private int remainingShakeDuration;

    public ScreenShakeManager() {
        random = new Random();
        remainingShakeDuration = 0;
    }

    public void startShake(int intensity, int duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
        this.remainingShakeDuration = duration;
    }

    public void update() {
        if (remainingShakeDuration > 0) {
            remainingShakeDuration--;
        }
    }

    public void applyShake(Graphics2D g2) {
        if (remainingShakeDuration > 0) {
            int offsetX = random.nextInt(shakeIntensity * 2) - shakeIntensity;
            int offsetY = random.nextInt(shakeIntensity * 2) - shakeIntensity;
            g2.translate(offsetX, offsetY);
        }
    }

    public boolean isShaking() {
        return remainingShakeDuration > 0;
    }
}