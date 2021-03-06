/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.climb.HatchRotator;
import frc.robot.commands.climb.Reset;
import frc.robot.commands.climb.Slow;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI implements RobotMap {
  
  public static OI instance;
    private Controller mainController;
    private JoystickButton rotate, slow;

    private Thread m_visionThread;

    public static OI getInstance()
    {
        if (instance == null){
            instance =  new OI();
        }
        return instance;
    }

    OI(){
        mainController = new Controller(MAIN_CONTROLLER);

        createButtons();
        dashboardInit();
    }

    private void dashboardInit(){
        m_visionThread = new Thread(() -> {
            // Get the Axis camera from CameraServer
            AxisCamera camera = CameraServer.getInstance().addAxisCamera("axis-camera.local");

            // Get a CvSink. This will capture Mats from the camera
            CvSink cvSink = CameraServer.getInstance().getVideo();
        });
        m_visionThread.setDaemon(true);
        m_visionThread.start();

        SmartDashboard.putData("Reset height", new Reset());
    }

    private void createButtons(){
        //Create Buttons for main controller
        rotate = new JoystickButton(mainController, ROTATE_BUTTON);
        slow = new JoystickButton(mainController, SLOW_BUTTON);


        assignButtons();
    }

    private void assignButtons(){
        //Assign commands to main controller buttons
        rotate.whileHeld(new HatchRotator());
        slow.whileHeld(new Slow());
    }

    public Controller getController(){
        return mainController;
    }

}
