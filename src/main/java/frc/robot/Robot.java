// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cscore.MjpegServer;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  // Constants such as camera and target height stored. Change per robot and goal!
  final double CAMERA_HEIGHT_METERS = Units.inchesToMeters(24);
  final double TARGET_HEIGHT_METERS = Units.feetToMeters(5);
  // Angle between horizontal and the camera.
  final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);

  // How far from the target we want to be
  final double GOAL_RANGE_METERS = Units.feetToMeters(3);

  // Change this to match the name of your camera
  PhotonCamera camera = new PhotonCamera("photonvision");

  // PID constants should be tuned per robot
  final double LINEAR_P = 0.1;
  final double LINEAR_D = 0.0;
  PIDController forwardController = new PIDController(LINEAR_P, 0, LINEAR_D);

  final double ANGULAR_P = 0.1;
  final double ANGULAR_D = 0.0;
  PIDController turnController = new PIDController(ANGULAR_P, 0, ANGULAR_D);

  XboxController xboxController = new XboxController(0);

  // Drive motors
  PWMVictorSPX leftMotor = new PWMVictorSPX(0);
  PWMVictorSPX rightMotor = new PWMVictorSPX(1);
  DifferentialDrive drive = new DifferentialDrive(leftMotor, rightMotor);

  @Override
  public void teleopPeriodic() {
      double forwardSpeed;
      double rotationSpeed;

      forwardSpeed = -xboxController.getRightY();

      if (xboxController.getAButton()) {
          // Vision-alignment mode
          // Query the latest result from PhotonVision
          var result = camera.getLatestResult();

          if (result.hasTargets()) {
              // Calculate angular turn power
              // -1.0 required to ensure positive PID controller effort _increases_ yaw
              rotationSpeed = -turnController.calculate(result.getBestTarget().getYaw(), 0);
          } else {
              // If we have no targets, stay still.
              rotationSpeed = 0;
          }
      } else {
          // Manual Driver Mode
          rotationSpeed = xboxController.getLeftX();
      }

      // Use our forward/turn speeds to control the drivetrain
      drive.arcadeDrive(forwardSpeed, rotationSpeed);
  }
}