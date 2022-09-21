
import static com.warpedcity.game.screens.GameScreen.Pause;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.warpedcity.game.vehicles.Bike;
import com.warpedcity.game.vehicles.Vehicle;

public class Player implements ControllerListener
{
    public static final int IDLE = 0;
    public static final int RUN = 1;
    public static final int JUMP = 2;
    public static final int SPAWN = 3;
    public static final int DYING = 4;
    public static final int DEAD = 5;
    public static final int PAUSE = 6;
    public static final int SHOOT = 7;
    public static final int RUN_SHOOT = 8;
    public static final int WALK = 9;
    public static final int CROUCH = 10;
    public static final int CROUCH_SHOOT = 11;
    public static final int JUMP_SHOOT = 12;
    public static final int BACK_JUMP = 13;
    public static final int CLIMB = 14;
    public static final int RIDING = 15;
    public static final int SLIDE = 16;
    public static final int SLIDE_SHOOT = 17;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final float ACCELERATION = 50f;
    public static final float JUMP_VELOCITY = 14.5f;
    public static final float BACK_JUMP_VELOCITY = 17.7f;
    public static final float GRAVITY = 30.0f;
    public static final float MAX_VEL = 7.5f;
    public static final float MAX_WALK_VEL = 3f;
    public static final float MAX_SLIDE_VEL = 10f;
    public static final float MAX_RIDING_VEL = 14f;
    public static final float DAMP = 0.85f;

    //PS4 Controller button flags
    static boolean jumpFlag = false;
    static boolean shootFlag = false;
    static boolean backJumpFlag = false;
    static boolean interactFlag = false;
    static boolean leftFlag = false;
    static boolean rightFlag = false;
    static boolean upFlag = false;
    static boolean downFlag = false;
    static boolean startFlag = false;

    public float interactDelay = 0.0f;

    public static float startHeight;

    public static String lastKey = "";

    public Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.ogg"));
    public Sound slideSound = Gdx.audio.newSound(Gdx.files.internal("data/slide.ogg"));
    public Sound jumpLandSound = Gdx.audio.newSound(Gdx.files.internal("data/jump-land.ogg"));
    public Sound backJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/back-jump.ogg"));
    public Sound shipHumSound = Gdx.audio.newSound(Gdx.files.internal("data/ship-hum1.ogg"));


    Controller controller = null;

    public static Vector2 pos = new Vector2();
    Vector2 accel = new Vector2();
    public static Vector2 vel = new Vector2();
    public Rectangle bounds = new Rectangle();
    Rectangle controlButtonRect = new Rectangle(480 - 64, 320 - 64, 64, 64);
    Rectangle backJumpButtonRect = new Rectangle(0, 320 - 64, 64, 64);
    public int health;
    public int energy;
    public boolean crouchLocked = false;

    public static int state = SPAWN;
    public float stateTime;
    float timer = 0.0f;
    int lives;
    int dir = RIGHT;
    Map map;
    public boolean grounded = false;
    public boolean activeBackJump = false;
    public boolean slideFlag = true;
    public boolean forceCrouch = false;

    public boolean upButton;
    public boolean downButton;

    public Player (Map map, float x, float y)
    {
        this.map = map;
        pos.x = x;
        pos.y = y;
        bounds.width = 1.1f;
        bounds.height = 2.5f;
        bounds.x = pos.x + 0.75f;
        bounds.y = pos.y;
        state = SPAWN;
        health = 15;
        energy = 5;
        stateTime = 0;
        lives = 5;


        if(Gdx.app.getType() != Application.ApplicationType.Android)
        {

            for (Controller c : Controllers.getControllers())
            {
                System.out.println(c.getName());
            }


            Array<Controller> controllers = Controllers.getControllers();

            for (Controller c : controllers)
            {
                if (c.getName().contains("PS4") && c.getName().contains("Controller"))
                {
                    controller = c;
                }
            }

            if (controller != null)
            {
                controller.addListener(new ControllerAdapter()
                {
                    @Override
                    public boolean buttonDown(Controller controller, int buttonIndex) {
                        if (buttonIndex == PS4Controller.BUTTON_CROSS) {
                            jumpFlag = true;
                        } else if (buttonIndex == PS4Controller.BUTTON_SQUARE) {
                            shootFlag = true;
                        } else if (buttonIndex == PS4Controller.BUTTON_CIRCLE) {
                            backJumpFlag = true;
                        } else if (buttonIndex == PS4Controller.BUTTON_TRIANGLE) {
                            interactFlag = true;
                        }else if (buttonIndex == PS4Controller.D_PAD_RIGHT) {
                            rightFlag = true;
                        } else if (buttonIndex == PS4Controller.D_PAD_LEFT) {
                            leftFlag = true;
                        } else if (buttonIndex == PS4Controller.D_PAD_UP) {
                            upFlag = true;
                        } else if (buttonIndex == PS4Controller.D_PAD_DOWN) {
                            downFlag = true;
                        } else if (buttonIndex == PS4Controller.BUTTON_START) {
                            startFlag = true;
                        }

                        return false;
                    }

                    @Override
                    public boolean buttonUp(Controller controller, int buttonIndex) {

                        if (buttonIndex == PS4Controller.D_PAD_RIGHT) {
                            rightFlag = false;
                        } else if (buttonIndex == PS4Controller.D_PAD_LEFT) {
                            leftFlag = false;
                        } else if (buttonIndex == PS4Controller.D_PAD_UP) {
                            upFlag = false;
                        } else if (buttonIndex == PS4Controller.D_PAD_DOWN) {
                            downFlag = false;
                        } else if (buttonIndex == PS4Controller.BUTTON_SQUARE) {
                            shootFlag = false;
                        } else if (buttonIndex == PS4Controller.BUTTON_CROSS) {
                            jumpFlag = false;
                        } else if (buttonIndex == PS4Controller.BUTTON_CIRCLE) {
                            backJumpFlag = false;
                        } else if (buttonIndex == PS4Controller.BUTTON_TRIANGLE) {
                            interactFlag = false;
                        } else if (buttonIndex == PS4Controller.BUTTON_START) {
                            startFlag = false;
                        }


                        return false;
                    }

                    @Override
                    public boolean axisMoved(Controller controller, int axisIndex, float value)
                    {
                        if (axisIndex == PS4Controller.AXIS_LX)
                        {
                            speedx = value;
                        }
                        else if (axisIndex == PS4Controller.AXIS_LY)
                        {
                            speedy = value;
                        }

                        return false;
                    }



                @Override
                public boolean povMoved(Controller controller, int povIndex, PovDirection value)
                {
                    return false;
                }
                });
            }
        }
    }
}//end of Player class
