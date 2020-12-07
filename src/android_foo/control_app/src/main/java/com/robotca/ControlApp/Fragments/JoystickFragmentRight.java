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
public class JoystickFragmentRight extends Fragment {
    private JoystickView virtualJoystick_right;
    private View view;
    private ControlMode controlMode = ControlMode.Joystick;

    /**
     * Default Constructor.
     */
    public JoystickFragmentRight() {
    }

    /**
     * Create this Fragments View.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the view if needed
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_joystick_view_right, container, false);

            // Grab the JoystickView and set its topic
            virtualJoystick_right = (JoystickView) view.findViewById(R.id.joystick_view_right);
        }

        return view;
    }

    /**
     * Returns the JoystickView
     * @return The JoystickView
     */
    public JoystickView getJoystickView() {
        return virtualJoystick_right;
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
        return virtualJoystick_right.hasAccelerometer();
    }

    /**
     * Invalidate the Fragment, updating the visibility of the Joystick based on the ControlMode.
     *
     */
    public void invalidate() {

        switch (controlMode) {
            case Joystick:
                show();
                break;

            case TwoJoystick:
                show();
                break;

            case Tilt:
                show();
                break;

            default:
                hide();
                break;
        }

        virtualJoystick_right.setControlMode(controlMode);
        virtualJoystick_right.controlSchemeChanged();
    }

    /**
     * Stops the JoystickFragmentRight.
     */
    public void stop() {
        virtualJoystick_right.stop();
    }

    /**
     * Shows the JoystickFragmentRight.
     */
    public void show(){
        getFragmentManager()
                .beginTransaction()
                .show(this)
                .commit();
    }

    /**
     * Hides the JoystickFragmentRight.
     */
    public void hide(){
        getFragmentManager()
                .beginTransaction()
                .hide(this)
                .commit();
    }
}
