package com.example.cvg_demofinal;

import static com.example.cvg_demofinal.AStar.astar_main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.segway.robot.algo.Pose2D;
import com.segway.robot.algo.minicontroller.CheckPoint;
import com.segway.robot.algo.minicontroller.CheckPointStateListener;
import com.segway.robot.sdk.base.bind.ServiceBinder;
import com.segway.robot.sdk.locomotion.sbv.Base;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // For Loomo
    Base mBase;

    // initializes at start (0,8)
    private int lastX = 0;
    public int getLastX() {
        return lastX;
    }
    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    // initializes at start (0,8)
    private int lastY = 8;
    public int getLastY() {
        return lastY;
    }
    public void setLastY(int lastY) {
        this.lastY = lastY;
    }

    private float startingTheta = 0;
    public float getStartingTheta() {
        return startingTheta;
    }
    public void setStartingTheta(float startingTheta) {
        this.startingTheta = startingTheta;
    }

    // keeps track of whether Loomo is moving backwards
    private boolean isLoomoMovingBackwardsBool = false;
    public boolean getIsLoomoMovingBackwardsBool() {
        return isLoomoMovingBackwardsBool;
    }
    public void setIsLoomoMovingBackwardsBool(boolean isLoomoMovingBackwardsBool) {
        this.isLoomoMovingBackwardsBool = isLoomoMovingBackwardsBool;
    }

    // keeps track of whether Loomo is moving backwards
    private boolean wasLoomoMovingBackwardsBool = false;
    public boolean getWasLoomoMovingBackwardsBool() {
        return wasLoomoMovingBackwardsBool;
    }
    public void setWasLoomoMovingBackwardsBool(boolean wasLoomoMovingBackwardsBool) {
        this.wasLoomoMovingBackwardsBool = wasLoomoMovingBackwardsBool;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loomo Implementation
        mBase = Base.getInstance();
        mBase.bindService(getApplicationContext(), new ServiceBinder.BindStateListener() {
            @Override
            public void onBind() {
                mBase.setOnCheckPointArrivedListener(new CheckPointStateListener() {
                    @Override
                    public void onCheckPointArrived(CheckPoint checkPoint, Pose2D realPose, boolean isLast) {

                    }

                    @Override
                    public void onCheckPointMiss(CheckPoint checkPoint, Pose2D realPose, boolean isLast, int reason) {

                    }
                });
            }

            @Override
            public void onUnbind(String reason) {

            }
        });

        // onClick listener for Room 205 button
        Button button205 = (Button) findViewById(R.id.room205);
        button205.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if Loomo is already at the door, don't move Loomo
                if (getLastX() != 17 || getLastY() != 8)
                {
                    main(17, 8);
                }
            }
        });

        // onClick listener for Room 206 button
        Button button206 = (Button) findViewById(R.id.room206);
        button206.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if Loomo is already at the door, don't move Loomo
                if (getLastX() != 50 || getLastY() != 8)
                {
                    main(50, 8);
                }
            }
        });

        // onClick listener for Room 207 button
        Button button207 = (Button) findViewById(R.id.room207);
        button207.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if Loomo is already at the door, don't move Loomo
                if (getLastX() != 50 || getLastY() != 8)
                {
                    main(50, 8);
                }
            }
        });

        // onClick listener for Room 208 button
        Button button208 = (Button) findViewById(R.id.room208);
        button208.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if Loomo is already at the door, don't move Loomo
                if (getLastX() != 50 || getLastY() != 41)
                {
                    main(50, 41);
                }
            }
        });

        // onClick listener for Room 209 button
        Button button209 = (Button) findViewById(R.id.room209);
        button209.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if Loomo is already at the door, don't move Loomo
                if (getLastX() != 49 || getLastY() != 78)
                {
                    main(49, 78);
                }
            }
        });

        // onClick listener for Home button
        Button buttonHome = (Button) findViewById(R.id.goHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // if Loomo is already at the door, don't move Loomo
                if (getLastX() != 0 || getLastY() != 8)
                {
                    main(0, 8);
                }
            }
        });
    }

    public void main(int endX, int endY)
    {
        // list that holds the distances Loomo needs to travel
        ArrayList<Point> distances = new ArrayList();

        // astar_main will take the start x, start y, end x, and end y
        distances = astar_main(getLastX(), getLastY(), endX, endY);

        //setIsLoomoMovingBackwardsBool(isLoomoMovingBackwards(endX, endY));

        // move Loomo to each coordinate provided by the astar algorithm
        moveLoomo(distances);

        // keep track of where Loomo is
        setLastX(endX);
        setLastY(endY);
    }

    // finds the difference between coordinates
    public int findDifferenceOfCoordinates(int coord1, int coord2)
    {
        // returns the distance needed to travel.
        // we mapped out the astar grid to give us a movement of 1.
        return coord2 - coord1;
    }

    public boolean isLoomoMovingBackwards(int destinationX, int destinationY)
    {
        // source compared to destination
        // if the source is greater than the destination, then Loomo will be traveling backwards
        if (lastX > destinationX || lastY > destinationY)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    // returns the direction Loomo is facing
    public int getDirection(int x, int y)
    {
        // if Loomo traveled diagonally (x,y) will be different

        // Loomo is already aligned
        if (x == 1 && y == 0)
        {
            return 0;
        }
        // Loomo is turned left
        else if (x == 0 && y == 1)
        {
            return 1;
        }
        // Loomo is turned backwards
        else if (x == -1 && y == 0)
        {
            return 2;
        }
        // Loomo is turned right
        else if (x == 0 && y == -1)
        {
            return 3;
        }
        // Loomo is diagonal
        else
        {
            return 4;
        }

    }

    // aligns Loomo's x coordinate to be equal to its starting direction
    public void alignLoomo(int direction, float x, float y)
    {
        mBase.cleanOriginalPoint();
        Pose2D pose2D = mBase.getOdometryPose(-1);
        mBase.setOriginalPoint(pose2D);

        // Loomo is turned left
        if (direction == 1)
        {
            // rotate Loomo right
            mBase.addCheckPoint((float)x, (float)y, (float) ((1 / 6) * Math.PI));
        }
        // Loomo is turned backwards
        else if (direction == 2)
        {
            // rotate Loomo forwards
            mBase.addCheckPoint((float)x, (float)y, (float) (2 * Math.PI));
        }
        // Loomo is turned right
        else if (direction == 3)
        {
            // rotate Loomo left
            mBase.addCheckPoint((float)x, (float)y, (float) (2 * Math.PI));
        }
        // Loomo is turned diagonally
        else if (direction == 4)
        {
            // do nothing for now (should never reach this in final demo)
        }
    }

    // moves Loomo based on the difference between the current and next coordinates
    public void moveLoomo(ArrayList<Point> distances)
    {
        mBase.setControlMode(Base.CONTROL_MODE_NAVIGATION);
        mBase.cleanOriginalPoint();
        Pose2D pose2D = mBase.getOdometryPose(-1);
        mBase.setOriginalPoint(pose2D);

        if (getStartingTheta() == 0)
        {
            setStartingTheta(pose2D.getTheta());
        }

        int x1 = 0, x2 = 0;
        int y1 = 0, y2 = 0;

        int difX;
        int difY;

        float moveX = 0;
        float moveY = 0;

        Point point;

        // traverse the ArrayList of coordinates backwards
        for(int i = (distances.size() - 1); i >= 0; i--)
        {
            // to prevent an index error (else: it's at its end location)
            if (i - 1 >= 0)
            {
                // get first point
                point = distances.get(i);
                x1 = point.getX();
                y1 = point.getY();

                // get the next point
                point = distances.get(i - 1);
                x2 = point.getX();
                y2 = point.getY();

                difX = findDifferenceOfCoordinates(x1, x2);
                difY = findDifferenceOfCoordinates(y1, y2);

                moveX += difX * 0.1f;
                moveY += difY * 0.1f;

                // move Loomo
                mBase.addCheckPoint((float)moveX, (float)moveY);

            }
        }

        int lastDirection = getDirection(findDifferenceOfCoordinates(x1, x2), findDifferenceOfCoordinates(y1, y2));

        // aligns Loomo to face its starting positive x direction
        // without this, keeping track of where Loomo is facing is a bit more challenging
        // IF THIS DOESN'T WORK - you can create properties to see where Loomo is facing
        // and maybe come up with a solution that way too
        if (lastDirection > 0)
        {
            alignLoomo(lastDirection, moveX, moveY);
        }

    }
}

