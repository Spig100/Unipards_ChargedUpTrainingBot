// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();


  // Controller Define (for Xbox Controller)
  // "port:" is set in the Driver Station
  private final XboxController driver = new XboxController(0);
  private final XboxController operator = new XboxController(1);

  // Motor Controller Define
  // "deviceNumber:" the CAN Bus ID of Victor SPX Motor Controller
  // For the introduction of CAN Bus, pls visit: https://en.wikipedia.org/wiki/CAN_bus

  // Chassis
  private final WPI_VictorSPX motor_chassis_left_1 = new WPI_VictorSPX(1);
  private final WPI_VictorSPX motor_chassis_left_2 = new WPI_VictorSPX(2);
  private final WPI_VictorSPX motor_chassis_right_1 = new WPI_VictorSPX(3);
  private final WPI_VictorSPX motor_chassis_right_2 = new WPI_VictorSPX(4);

  private final MotorControllerGroup motor_chassis_left = new MotorControllerGroup(motor_chassis_left_1, motor_chassis_left_2);
  private final MotorControllerGroup motor_chassis_right = new MotorControllerGroup(motor_chassis_right_1, motor_chassis_right_2);

  private final DifferentialDrive chassis = new DifferentialDrive(motor_chassis_left, motor_chassis_right);

  // Payloads
  private final VictorSPX motor_arm = new VictorSPX(5);
  private final VictorSPX motor_claw = new VictorSPX(6);

  // Constants
  private final double kChassisFactorXSpeed = 0.5;
  private final double kChassisFactorZRotation = 0.7;
  private final double kClawPercentage = 0.8;
  private final double kArmPercentage = 0.6;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    
    motor_chassis_right.setInverted(true);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    System.out.println("TeleOperation Initialized Successfully.");
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    // Chassis operation
    double x_speed = -driver.getLeftX() * kChassisFactorXSpeed;
    double z_rotation = -driver.getRightY() * kChassisFactorZRotation;
    chassis.arcadeDrive(x_speed, z_rotation);

    // Arm operation
    if (operator.getPOV(0) == 1){
      motor_arm.set(VictorSPXControlMode.PercentOutput, -kArmPercentage);
    }
    else if (operator.getPOV(180) == 1){
      motor_arm.set(VictorSPXControlMode.PercentOutput, kArmPercentage);
    }
    else{
      motor_arm.set(VictorSPXControlMode.PercentOutput, 0);
    }

    // Claw operation
    if (operator.getAButton()){
      motor_claw.set(VictorSPXControlMode.PercentOutput, kClawPercentage);
    }
    else if (operator.getBButton()){
      motor_claw.set(VictorSPXControlMode.PercentOutput, -kClawPercentage);
    }
    else{
      motor_claw.set(VictorSPXControlMode.PercentOutput, 0);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
