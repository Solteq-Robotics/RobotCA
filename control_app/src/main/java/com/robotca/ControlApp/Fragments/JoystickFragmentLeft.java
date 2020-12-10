package com.robotca.ControlApp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robotca.ControlApp.Core.ControlMode;
import com.robotca.ControlApp.R;
import com.robotca.ControlApp.Views.JoystickView;

/**
 * Fragment containing the JoystickView.
 *
 * Created by Michael Brunson on 11/7/15.
 */
public class JoystickFragmentLeft extends Fragment {
    private JoystickView virtualJoystick_left;
    private View view;
    private ControlMode controlMode = ControlMode.Joystick;

    /**
     * Default Constructor.
     */
    public JoystickFragmentLeft() {
    }

    /**
     * Create this Fragments View.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view if needed
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_joystick_view_left, container, false);

            // Grab the JoystickView and set its topic
            virtualJoystick_left = (JoystickView) view.findViewById(R.id.joystick_view_left);
        }

        return view;
    }

    /**
     * Returns the JoystickView
     * @return The JoystickView
     */
    public JoystickView getJoystickView() {
        return virtualJoystick_left;
    }

    /**
     * Get the currently active ControlMode.
     * @return The current ControlMode
     */
    public ControlMode getControlMode() {
        return controlMode;
    }

    /**
     * Set the ControlMode for controlling the Joystick.
     * @param controlMode The new ControlMode
     */
    public void setControlMode(ControlMode controlMode) {
        this.controlMode = controlMode;
        this.invalidate();
    }

    /**
     * Tests whether the Joystick supports accelerometer control.
     * @return True if the Joystick supports accelerometer control, false otherwise
     */
    @SuppressWarnings("unused") // Maybe later...
    public boolean hasAccelerometer() {
        return virtualJoystick_left.hasAccelerometer();
    }

    /**
     * Invalidate the Fragment, updating the visibility of the Joystick based on the ControlMode.
     *
     */
    public void invalidate() {

        switch (controlMode) {
            case Joystick:
                hide();
                break;

            case TwoJoystick:
                show();
                break;

            case Tilt:
                hide();
                break;

            default:
                hide();
                break;
        }

        virtualJoystick_left.setControlMode(controlMode);
        virtualJoystick_left.controlSchemeChanged();
    }

    /**
     * Stops the JoystickFragmentLeft.
     */
    public void stop() {
        virtualJoystick_left.stop();
    }

    /**
     * Shows the JoystickFragmentLeft.
     */
    public void show(){
        getFragmentManager()
                .beginTransaction()
                .show(this)
                .commit();
    }

    /**
     * Hides the JoystickFragmentLeft.
     */
    public void hide(){
        getFragmentManager()
                .beginTransaction()
                .hide(this)
                .commit();
    }
}
