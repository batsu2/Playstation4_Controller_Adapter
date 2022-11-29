import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

package com.warpedcity.game;


public class PS4Controller
{
    public static final int BUTTON_CROSS = 0;
    public static final int BUTTON_CIRCLE = 1;
    public static final int BUTTON_SQUARE = 2;
    public static final int BUTTON_TRIANGLE = 3;
    public static final int SHARE_BUTTON = 4;
    public static final int PS_BUTTON = 5;
    public static final int BUTTON_START = 6;
    public static final int BUTTON_LS = 7; //Left Stick pressed down
    public static final int BUTTON_RS = 8; //Right Stick pressed down
    public static final int BUTTON_L1 = 9;
    public static final int BUTTON_R1 = 10;

    public static final int D_PAD_UP = 11;
    public static final int D_PAD_DOWN = 12;
    public static final int D_PAD_LEFT = 13;
    public static final int D_PAD_RIGHT = 14;

    public static final int POV = 0;

    public static final int AXIS_LY = 0; //-1 is up | +1 is down
    public static final int AXIS_LX = 1; //-1 is left | +1 is right
    public static final int AXIS_RY = 2; //-1 is up | +1 is down
    public static final int AXIS_RX = 3; //-1 is left | +1 is right
    public static final int AXIS_TRIGGER = 4;

}
