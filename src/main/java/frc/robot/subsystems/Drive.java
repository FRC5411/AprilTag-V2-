// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

//shooter 
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Drive extends SubsystemBase {
  private DifferentialDrive differentialDrive;
  private PWMVictorSPX m_frontLeft;
  private PWMVictorSPX m_rearLeft;
  private PWMVictorSPX m_frontRight;
  private PWMVictorSPX m_rearRight;
  private WPI_TalonSRX shooter;
  /** Creates a new ExampleSubsystem. */
  public Drive() {
    
    m_frontLeft = new PWMVictorSPX(4);
    m_rearLeft = new PWMVictorSPX(2);
    m_frontRight = new PWMVictorSPX(1);
    m_rearRight = new PWMVictorSPX(3);

    shooter = new WPI_TalonSRX(10);

    // shooter.set(0.25);
    NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    NetworkTableEntry tx = table.getEntry("tx");
    NetworkTableEntry ty = table.getEntry("ty");
    NetworkTableEntry ta = table.getEntry("ta");

    //read values periodically
    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);

    //post to smart dashboard periodically
    SmartDashboard.putNumber("LimelightX", x);
    SmartDashboard.putNumber("LimelightY", y);
    SmartDashboard.putNumber("LimelightArea", area);

    if (x < -1) {
      shooter.set(0.05);
    }
    else if (x > 1) {
      shooter.set(-0.05);
    } else {
      shooter.set(0.05);
    }

    // m_frontLeft.follow(m_rearLeft);
    // m_frontRight.follow(m_rearRight);
    MotorControllerGroup m_right = new MotorControllerGroup(m_frontRight, m_rearRight);
    MotorControllerGroup m_left = new MotorControllerGroup(m_frontLeft, m_rearLeft);
    m_right.setInverted(true);
    m_left.setInverted(true);

    differentialDrive = new DifferentialDrive(m_left, m_right);
  }
  public void arcadeDrive(double speed, double rotation) {
    differentialDrive.arcadeDrive(-speed, -rotation);
  }
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
