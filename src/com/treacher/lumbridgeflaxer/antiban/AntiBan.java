package com.treacher.lumbridgeflaxer.antiban;

import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import com.treacher.lumbridgeflaxer.enums.FlaxerState;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Hud.Window;

/**
 * Created by Michael Treacher
 */

public class AntiBan {

    private long lastTimeAFKing = System.currentTimeMillis();
    private long lastTimeRandomEvent = System.currentTimeMillis();

    private final ClientContext ctx;

    public AntiBan(ClientContext ctx) {
        this.ctx = ctx;
    }

    public void execute() {
        if(timeForRandomEvent()) triggerRandomEvent();
        if(timeForAFK()) goAFK();
    }

    // AFK every 60 minutes
    private boolean timeForAFK() {
        return (System.currentTimeMillis() - lastTimeAFKing) > (Random.nextInt(50000,70000) * 60);
    }

    private boolean timeForRandomEvent() {
        return (System.currentTimeMillis() - lastTimeRandomEvent) > (Random.nextInt(50000,70000) * 15);
    }

    private void triggerRandomEvent() {
        LumbridgeFlaxer.STATE = FlaxerState.ANTIBAN;

        lastTimeRandomEvent = System.currentTimeMillis();

        int randomNumber = Random.nextInt(1,3);

        switch(randomNumber) {
            case 1:
                moveMouse();
                break;
            case 2:
                checkSkill();
                break;
            case 3:
                mouseOffScreen();
                break;
            default:
                break;
        }
    }

    // Don't really want to go AFK all that often.
    private void goAFK(){
        lastTimeAFKing = System.currentTimeMillis();
        LumbridgeFlaxer.STATE = FlaxerState.ANTIBAN;

        Condition.sleep(Random.nextInt(60000, 120000));
    }

    /*
     * Opens up friends list and hovers over a random amount of your friends. The more friends the better but it has 
     * a upper limit of checking 5 friends because I think any more over that starts to look a bit strange. Once finished looking
     * it goes back to your backpack.
     */
    // Retiring this as I don't think it is a good anti-ban
//    private void checkFriendsList() {
//        ctx.hud.open(Window.FRIENDS);
//
//        final int maxFriendComponents = 5;
//        final Component friendsListComponent = ctx.widgets.component(550, 6);
//        final List<Component> friendsComponents =  Arrays.asList(friendsListComponent.components());
//
//        //Restrict it to only look at a max of 5 friends
//        final int friendsToHoverCount = friendsComponents.size() > maxFriendComponents ? maxFriendComponents : friendsComponents.size();
//
//        // Shuffle up the list to give some randomness
//        Collections.shuffle(friendsComponents);
//
//        Point firstPoint = friendsComponents.get(0).nextPoint();
//
//        //Always move to the first point because if you don't the social hover widget will cover the social menu.
//        ctx.input.move(firstPoint);
//
//        //Start from the second element
//        for(int i=1; i < friendsToHoverCount; i++) {
//            if(Random.nextInt(0, 2) == 0) {
//                // Only slightly vary the y coordinate
//                final int yVariance = friendsComponents.get(i).centerPoint().y += Random.nextInt(-2, 2);
//                ctx.input.move(firstPoint.x, yVariance);
//                Condition.sleep(Random.nextInt(1400, 2000));
//            }
//        }
//
//        backToBackpack();
//    }

    /*
     * Opens up skill list and randomly selects a few skills to check the experience of. Once finished checking it goes back to your backpack.
     */
    private void checkSkill() {
        ctx.hud.open(Window.SKILLS);

        final int skillId = 13;

        final Component skillsWindowComponent = ctx.widgets.component(1466, 11);
        final Component randomSkillComponent = skillsWindowComponent.component(skillId);

        ctx.input.move(randomSkillComponent.nextPoint());
        Condition.sleep(Random.nextInt(1500, 2000));

        backToBackpack();
    }

    private void moveMouse() {
        ctx.input.move(Random.nextInt(100, 400), Random.nextInt(100, 400));
    }

    public void mouseOffScreen() {
        ctx.input.move(getOffScreenX(), getOffScreenY());
    }

    /*
     * Sets the HUD to backpack and moves the mouse away from the backpack
     */
    private void backToBackpack() {
        ctx.hud.open(Window.BACKPACK);
        Condition.sleep();
        moveMouse();
    }

    private int getOffScreenX() {
        if (Random.nextBoolean()) {
            final int width = ctx.client().getCanvas().getWidth();
            return width + Random.nextInt(20, 50);
        } else {
            return 0 - Random.nextInt(20, 50);
        }
    }

    private int getOffScreenY() {
        if (Random.nextBoolean()) {
            final int height = ctx.client().getCanvas().getHeight();
            return height + Random.nextInt(20, 50);
        } else {
            return 0 - Random.nextInt(20, 50);
        }
    }

}