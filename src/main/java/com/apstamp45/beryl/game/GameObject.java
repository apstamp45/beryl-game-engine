package com.apstamp45.beryl.game;

import com.apstamp45.beryl.game.components.Component;

import java.util.ArrayList;

/**
 * A GameObject is used to serve multiple
 * purposes in a game. Based on what Component/s
 * are added, it can be used to create a drawn
 * sprite on the screen, or do some background
 * calculations.
 */
public class GameObject {

    /**
     * The GameObject's name.
     */
    private String name;

    /**
     * The list of this GameObject's
     * components.
     */
    private ArrayList<Component> components;

    /**
     * The GameObject's position and size.
     */
    public Transform transform;

    /**
     * Creates a GameObject.
     * @param name The GameObject's name.
     */
    public GameObject(String name) {
        this.name = name;
        components = new ArrayList<>();
        transform = new Transform();
    }

    /**
     * Creates a GameObject.
     * @param name The GameObject's name.
     * @param transform The GameObject's Transform.
     */
    public GameObject(String name, Transform transform) {
        this.name = name;
        components = new ArrayList<>();
        this.transform = transform;
    }

    /**
     * Adds a Component to this GameObject.
     * @param component The Component to add.
     */
    public void addComponent(Component component) {
        components.add(component);
        component.gameObject = this;
    }

    /**
     * Gets a Component from the GameObject
     * based on the Component's type.
     * @param componentClass The class of the
     * Component you are trying to get.
     * @param <T> The class type that you are
     * trying to find.
     * @return The Component of type <T>.
     */
    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    System.err.println("ERROR: unable to cast type " + component.getClass()
                                       + " to " + componentClass);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Removes a Component from the GameObject
     * based on the Component's type.
     * @param componentClass The class of the
     * Component you are trying to remove.
     * @param <T> The class type that you are
     * trying to remove.
     * @return The removed Component of type <T>.
     */
    public <T extends Component> T removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    components.remove(i);
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    System.err.println("ERROR: unable to cast type " + component.getClass()
                            + " to " + componentClass);
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * Runs when the Scene that is using
     * this GameObject starts running.
     */
    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    /**
     * Runs on every game loop.
     * @param dt The time that passed
     * since the last loop (in seconds).
     */
    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }
}
