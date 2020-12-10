package com.robotca.ControlApp.Core;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Container for information about connections to specific Robots.
 *
 * Created by Michael Brunson on 1/23/16.
 */
public class RobotInfo implements Comparable<RobotInfo>, Savable {

    // Number of RobotInfos in storage
    private static int robotCount = 1;

    /** Bundle key for UUID */
    public static final String UUID_KEY = "UUID_KEY";
    /** Bundle key for robot name */
    public static final String ROBOT_NAME_KEY = "ROBOT_NAME_KEY";
    /** Bundle key for master URI */
    public static final String MASTER_URI_KEY = "MASTER_URI_KEY";
    /** Bundle key for joystick topic */
    public static final String JOYSTICK_TOPIC_KEY = "JOYSTICK_TOPIC_KEY";
    /** Bundle key for laser scan topic */
    public static final String LASER_SCAN_TOPIC_KEY = "LASER_SCAN_TOPIC_KEY";
    /** Bundle key for camera topic */
    public static final String CAMERA_TOPIC_KEY = "CAMERA_TOPIC_KEY";
    /** Bundle key for navsat topic */
    public static final String NAVSAT_TOPIC_KEY = "NAVSAT_TOPIC_KEY";
    /** Bundle key for odometry topic */
    public static final String ODOMETRY_TOPIC_KEY = "ODOMETRY_TOPIC_KEY";
    /** Bundle key for pose topic */
    public static final String POSE_TOPIC_KEY = "POSE_TOPIC_KEY";
    /** Bundle key for Imu topic */
    public static final String IMU_TOPIC_KEY = "IMU_TOPIC_KEY";
    /** Bundle key for reverse laser scan */
    public static final String REVERSE_LASER_SCAN_KEY = "REVERSE_LASER_SCAN_KEY";
    /** Bundle key for flip x- and y-axis */
    public static final String FLIP_X_Y_AXIS_KEY = "FLIP_X_Y_AXIS_KEY";
    /** Bundle key for enable holonomic  */
    public static final String ENABLE_HOLONOMIC_KEY = "ENABLE_HOLONOMIC_KEY";
    /** Bundle key for invert x-axis */
    public static final String INVERT_X_KEY = "INVERT_X_KEY";
    /** Bundle key for invert x-axis */
    public static final String INVERT_Y_KEY = "INVERT_Y_KEY";
    /** Bundle key for invert x-axis */
    public static final String INVERT_ANGULAR_VELOCITY_KEY = "INVERT_ANGULAR_VELOCITY_KEY";


    // UUID for this RobotInfo
    private UUID id = UUID.randomUUID();

    // Name of this RobotInfo
    private String name;
    // Master URI of this RobotInfo
    private String masterUriString;

    // Topic names
    private String joystickTopic;
    private String cameraTopic;
    private String laserTopic;
    private String navsatTopic;
    private String odometryTopic;
    private String poseTopic;
    private String imuTopic;
    private boolean reverseLaserScan;
    private boolean flipXYAxis;
    private boolean enableHolonomic;
    private boolean invertX;
    private boolean invertY;
    private boolean invertAngularVelocity;

    @SuppressWarnings("unused")
    private static final String TAG = "RobotInfo";

    /**
     * Default Constructor.
     */
    public RobotInfo() {
        //id = UUID.randomUUID();
        name = "Robot" + robotCount++;
        masterUriString = "http://192.168.0.102:11311";
        joystickTopic = "/cmd_vel";
        cameraTopic = "/image_raw/compressed";
        laserTopic = "/scan";
        navsatTopic = "/navsat/fix";
        odometryTopic = "/odometry/gim_localization_node/filtered";
        poseTopic = "/pose";
        imuTopic = "/ubloxTest/navheading";
        reverseLaserScan = false;
        flipXYAxis = false;
        enableHolonomic = false;
        invertX = false;
        invertY = false;
        invertAngularVelocity = false;
    }

//    public RobotInfo(String mName, String mMasterUri) {
//        this.name = mName;
//        this.masterUriString = mMasterUri;
//    }

    /**
     * Creates a RobotInfo.
     * @param id UUID
     * @param name Name to show when displaying this RobotInfo
     * @param masterUriString Master URI for this RobotInfo
     * @param joystickTopic JoystickTopic name for this RobotInfo
     * @param laserTopic LaserTopic name for this RobotInfo
     * @param cameraTopic CameraTopic name for this RobotInfo
     * @param navsatTopic NavSatTopic name for this RobotInfo
     * @param odometryTopic OdometryTopic name for this RobotInfo
     * @param poseTopic PoseTopic name for this RobotInfo
     * @param imuTopic ImuTopic name for this RobotInfo
     * @param reverseLaserScan Reverse Laser Scan flag value for this RobotInfo
     * @param invertX Invert X-axis flag value for this RobotInfo
     * @param invertY Invert Y-axis flag value for this RobotInfo
     * @param invertAngularVelocity Invert Angular Velocity flag value for this RobotInfo
     * @param flipXYAxis Flip X- and Y-axis flag value for this RobotInfo
     * @param enableHolonomic Enable Holonomic flag value for this RobotInfo
     */
    public RobotInfo(UUID id, String name, String masterUriString, String joystickTopic,
                     String laserTopic, String cameraTopic,String navsatTopic,
                     String odometryTopic, String poseTopic, String imuTopic,
                     boolean reverseLaserScan, boolean invertX, boolean invertY,
                     boolean invertAngularVelocity, boolean flipXYAxis,
                     boolean enableHolonomic) {
        this.id = id;
        this.name = name;
        this.masterUriString = masterUriString;
        this.joystickTopic = joystickTopic;
        this.laserTopic = laserTopic;
        this.cameraTopic = cameraTopic;
        this.navsatTopic = navsatTopic;
        this.odometryTopic = odometryTopic;
        this.poseTopic = poseTopic;
        this.imuTopic = imuTopic;
        this.reverseLaserScan = reverseLaserScan;
        this.flipXYAxis = flipXYAxis;
        this.enableHolonomic = enableHolonomic;
        this.invertX = invertX;
        this.invertY = invertY;
        this.invertAngularVelocity = invertAngularVelocity;
    }

    /**
     * @return UUID of this RobotInfo
     */
    public UUID getId(){return id;}

    /**
     * Sets the UUID of this RobotInfo
     * @param id The new UUID
     */
    public void setId(UUID id){ this.id = id; }

    /**
     * @return The PoseTopic name of this RobotInfo
     */
    public String getPoseTopic() {
        return poseTopic;
    }

    /**
     * Sets the PoseTopic for this RobotInfo.
     * @param poseTopic The new PoseTopic
     */
    public void setPoseTopic(String poseTopic) {
        this.poseTopic = poseTopic;
    }

    /**
     * @return The imuTopic name of this RobotInfo
     */
    public String getImuTopic() {
        return imuTopic;
    }

    /**
     * Sets the imuTopic for this RobotInfo.
     * @param imuTopic The new PoseTopic
     */
    public void setImuTopic(String imuTopic) {
        this.imuTopic = imuTopic;
    }
    
     /**
     * @return The OdometryTopic name of this RobotInfo
     */
    public String getOdometryTopic() {
        return odometryTopic;
    }

    /**
     * Sets the OdometryTopic for this RobotInfo.
     * @param odometryTopic The new JoystickTopic
     */
    public void setOdometryTopic(String odometryTopic) {
        this.odometryTopic = odometryTopic;
    }

     /**
     * @return The NavSatTopic name of this RobotInfo
     */
    public String getNavSatTopic() {
        return navsatTopic;
    }

    /**
     * Sets the NavSatTopic for this RobotInfo.
     * @param navsatTopic The new JoystickTopic
     */
    public void setNavSatTopic(String navsatTopic) {
        this.navsatTopic = navsatTopic;
    }

    /**
     * @return The JoystickTopic name of this RobotInfo
     */
    public String getJoystickTopic() {
        return joystickTopic;
    }

    /**
     * Sets the JoystickTopic for this RobotInfo.
     * @param joystickTopic The new JoystickTopic
     */
    public void setJoystickTopic(String joystickTopic) {
        this.joystickTopic = joystickTopic;
    }

    /**
     * @return The CameraTopic of this RobotInfo
     */
    public String getCameraTopic() {
        return cameraTopic;
    }

    /**
     * Sets the CameraTopic of this RobotInfo.
     * @param cameraTopic The new CameraTopic
     */
    public void setCameraTopic(String cameraTopic) {
        this.cameraTopic = cameraTopic;
    }

    /**
     * @return The LaserTopic of this RobotInfo
     */
    public String getLaserTopic() {
        return laserTopic;
    }

    /**
     * Sets the LaserTopic of this RobotInfo.
     * @param laserTopic The new LaserTopic
     */
    public void setLaserTopic(String laserTopic) {
        this.laserTopic = laserTopic;
    }

    /**
     * @return The Master URI of this RobotInfo
     */
    public String getMasterUri() {
        return masterUriString;
    }

    /**
     * Sets the Master URI of this RobotInfo.
     * @param mMasterUri The new Master URI
     */
    public void setMasterUri(String mMasterUri) {
        this.masterUriString = mMasterUri;
    }

    /**
     * @return The name of this RobotInfo
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this RobotInfo.
     * @param mName The new name
     */
    public void setName(String mName) {
        this.name = mName;
    }

    /**
     * @return The URI of Master URI of this RobotInfo
     */
    public URI getUri(){
        return URI.create(getMasterUri());
    }

    /**
     * @return If laser scan should be reversed
     */
    public boolean isReverseLaserScan() {
        return reverseLaserScan;
    }

    /**
     * @return If x- and y-axis should be flipped
     */
    public boolean isFlipXYAxis() {
        return flipXYAxis;
    }

    /**
     * @return If holonomic is enabled or not
     */
    public boolean isEnableHolonomic() {
        return enableHolonomic;
    }

    /**
     * Sets whether the laser scan should be reversed
     * @param reverseLaserScan Reverse if true, false otherwise
     */
    public void setReverseLaserScan(boolean reverseLaserScan) {
        this.reverseLaserScan = reverseLaserScan;
    }

    /**
     * Sets whether the x and y axis should be flipped
     * @param flipXYAxis Flipped if true, false otherwise
     */
    public void setFlipXYAxis(boolean flipXYAxis) {
        this.flipXYAxis = flipXYAxis;
    }

    /**
     * Sets whether holonomic is enabled or not (enabled for omni-directional drive)
     * @param enableHolonomic Enabled if true, false otherwise
     */
    public void setEnableHolonomic(boolean enableHolonomic) {
        this.enableHolonomic = enableHolonomic;
    }

    /**
     * @return If x-axis should be inverted
     */
    public boolean isInvertX() {
        return invertX;
    }

    /**
     * Sets whether to invert x-axis
     * @param invertX Invert if true, false otherwise
     */
    public void setInvertX(boolean invertX) {
        this.invertX = invertX;
    }

    /**
     * @return If y-axis should be inverted
     */
    public boolean isInvertY() {
        return invertY;
    }

    /**
     * Sets whether to invert y-axis
     * @param invertY Invert if true, false otherwise
     */
    public void setInvertY(boolean invertY) {
        this.invertY = invertY;
    }

    /**
     * @return If angular velocity should be inverted
     */
    public boolean isInvertAngularVelocity() {
        return invertAngularVelocity;
    }

    /**
     * Sets whether to invert angular velocity
     * @param invertAngularVelocity Invert if true, false otherwise
     */
    public void setInvertAngularVelocity(boolean invertAngularVelocity) {
        this.invertAngularVelocity = invertAngularVelocity;
    }

    /**
     * Compares this RobotInfo to another based on UUID.
     * @param another The other RobotInfo
     * @return The comparison result
     */
    @Override
    public int compareTo(@NonNull RobotInfo another) {

        if (this.getId() == null) {
            return -1;
        }

        if (another.getId() == null) {
            return 1;
        }

        return this.getId().compareTo(another.getId());
    }

    /**
     * Determines the correct value for robotCount.
     * @param list The list of loaded RobotInfos
     */
    public static void resolveRobotCount(List<RobotInfo> list)
    {
//        Log.d(TAG, "resolveRobotCount(" + list + ")");

        int max = 0;
        int val;

        for (RobotInfo info: list) {
            if (info.getName().startsWith("Robot"))
            {
//                Log.d(TAG, "name = " + info.getName().substring(5));
                try {
                    val = Integer.parseInt(info.getName().substring(5));
                }
                catch (NumberFormatException e) {
                    val = -1;
                }

                if (val > max)
                    max = val;
            }
        }

        robotCount = max + 1;
    }

    /**
     * @return The robot count.
     */
    @SuppressWarnings("unused")
    static int getRobotCount()
    {
        return robotCount;
    }

    @Override
    public void load(@NonNull Bundle bundle) {
        id = UUID.fromString(bundle.getString(UUID_KEY, UUID.randomUUID().toString()));
        name = bundle.getString(ROBOT_NAME_KEY, "");
        masterUriString = bundle.getString(MASTER_URI_KEY, "http://localhost:11311");
        joystickTopic = bundle.getString(JOYSTICK_TOPIC_KEY, "/joy_teleop/cmd_vel");
        cameraTopic = bundle.getString(CAMERA_TOPIC_KEY, "/image_raw/compressed");
        laserTopic = bundle.getString(LASER_SCAN_TOPIC_KEY, "/scan");
        navsatTopic = bundle.getString(NAVSAT_TOPIC_KEY, "/navsat/fix");
        odometryTopic = bundle.getString(ODOMETRY_TOPIC_KEY, "/odometry/gim_localization_node/filtered");
        poseTopic = bundle.getString(POSE_TOPIC_KEY, "/pose");
        imuTopic = bundle.getString(IMU_TOPIC_KEY, "/ubloxTest/navheading");
        reverseLaserScan = bundle.getBoolean(REVERSE_LASER_SCAN_KEY, false);
        flipXYAxis = bundle.getBoolean(FLIP_X_Y_AXIS_KEY, false);
        enableHolonomic = bundle.getBoolean(ENABLE_HOLONOMIC_KEY, false);
        invertX = bundle.getBoolean(INVERT_X_KEY, false);
        invertY = bundle.getBoolean(INVERT_Y_KEY, false);
        invertAngularVelocity = bundle.getBoolean(INVERT_ANGULAR_VELOCITY_KEY, false);
    }

    public void load(@NonNull SharedPreferences prefs) {
        joystickTopic = prefs.getString(RobotStorage.getPreferenceKey(JOYSTICK_TOPIC_KEY), "/joy_teleop/cmd_vel");
        cameraTopic = prefs.getString(RobotStorage.getPreferenceKey(CAMERA_TOPIC_KEY), "/image_raw/compressed");
        laserTopic = prefs.getString(RobotStorage.getPreferenceKey(LASER_SCAN_TOPIC_KEY), "/scan");
        navsatTopic = prefs.getString(RobotStorage.getPreferenceKey(NAVSAT_TOPIC_KEY), "/navsat/fix");
        odometryTopic = prefs.getString(RobotStorage.getPreferenceKey(ODOMETRY_TOPIC_KEY), "/odometry/gim_localization_node/filtered");
        poseTopic = prefs.getString(RobotStorage.getPreferenceKey(POSE_TOPIC_KEY), "/pose");
        imuTopic = prefs.getString(RobotStorage.getPreferenceKey(IMU_TOPIC_KEY), "/ubloxTest/navheading");
        reverseLaserScan = prefs.getBoolean(RobotStorage.getPreferenceKey(REVERSE_LASER_SCAN_KEY), false);
        flipXYAxis = prefs.getBoolean(RobotStorage.getPreferenceKey(FLIP_X_Y_AXIS_KEY), true);
        enableHolonomic = prefs.getBoolean(RobotStorage.getPreferenceKey(ENABLE_HOLONOMIC_KEY), false);
        invertX = prefs.getBoolean(RobotStorage.getPreferenceKey(INVERT_X_KEY), false);
        invertY = prefs.getBoolean(RobotStorage.getPreferenceKey(INVERT_Y_KEY), false);
        invertAngularVelocity = prefs.getBoolean(RobotStorage.getPreferenceKey(INVERT_ANGULAR_VELOCITY_KEY), false);
    }

    @Override
    public void save(@NonNull Bundle bundle) {
        bundle.putString(UUID_KEY, id.toString());
        bundle.putString(ROBOT_NAME_KEY, name);
        bundle.putString(MASTER_URI_KEY, masterUriString);
        bundle.putString(JOYSTICK_TOPIC_KEY, joystickTopic);
        bundle.putString(CAMERA_TOPIC_KEY, cameraTopic);
        bundle.putString(LASER_SCAN_TOPIC_KEY, laserTopic);
        bundle.putString(NAVSAT_TOPIC_KEY, navsatTopic);
        bundle.putString(ODOMETRY_TOPIC_KEY, odometryTopic);
        bundle.putString(POSE_TOPIC_KEY, poseTopic);
        bundle.putString(IMU_TOPIC_KEY, imuTopic);
        bundle.putBoolean(REVERSE_LASER_SCAN_KEY, reverseLaserScan);
        bundle.putBoolean(FLIP_X_Y_AXIS_KEY, flipXYAxis);
        bundle.putBoolean(ENABLE_HOLONOMIC_KEY, enableHolonomic);
        bundle.putBoolean(INVERT_X_KEY, invertX);
        bundle.putBoolean(INVERT_Y_KEY, invertY);
        bundle.putBoolean(INVERT_ANGULAR_VELOCITY_KEY, invertAngularVelocity);
    }

    public void save(@NonNull SharedPreferences.Editor prefs) {
        prefs.putString(RobotStorage.getPreferenceKey(JOYSTICK_TOPIC_KEY), joystickTopic);
        prefs.putString(RobotStorage.getPreferenceKey(CAMERA_TOPIC_KEY), cameraTopic);
        prefs.putString(RobotStorage.getPreferenceKey(LASER_SCAN_TOPIC_KEY), laserTopic);
        prefs.putString(RobotStorage.getPreferenceKey(NAVSAT_TOPIC_KEY), navsatTopic);
        prefs.putString(RobotStorage.getPreferenceKey(ODOMETRY_TOPIC_KEY), odometryTopic);
        prefs.putString(RobotStorage.getPreferenceKey(POSE_TOPIC_KEY), poseTopic);
        prefs.putString(RobotStorage.getPreferenceKey(IMU_TOPIC_KEY), imuTopic);
        prefs.putBoolean(RobotStorage.getPreferenceKey(REVERSE_LASER_SCAN_KEY), reverseLaserScan);
        prefs.putBoolean(RobotStorage.getPreferenceKey(FLIP_X_Y_AXIS_KEY), flipXYAxis);
        prefs.putBoolean(RobotStorage.getPreferenceKey(ENABLE_HOLONOMIC_KEY), enableHolonomic);
        prefs.putBoolean(RobotStorage.getPreferenceKey(INVERT_X_KEY), invertX);
        prefs.putBoolean(RobotStorage.getPreferenceKey(INVERT_Y_KEY), invertY);
        prefs.putBoolean(RobotStorage.getPreferenceKey(INVERT_ANGULAR_VELOCITY_KEY), invertAngularVelocity);
    }
}
