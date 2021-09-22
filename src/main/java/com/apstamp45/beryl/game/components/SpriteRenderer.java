package com.apstamp45.beryl.game.components;

import org.joml.Vector4f;

public class SpriteRenderer extends Component {
    private Vector4f color;

    public SpriteRenderer(Vector4f color) {
        this.color = color;
    }

    @Override
    public void update(float dt) {

    }

    /**
     * Gets the sprite renderer's color.
     * @return
     */
    public Vector4f getColor() {
        return color;
    }
}
