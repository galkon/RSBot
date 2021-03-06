package com.treacher.runespan.util;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.Platform;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher
 */
public class PlatformConnection {

    private FloatingIsland connection;
    private final Platform platform;
    private final Tile platformTile;
    private ClientContext ctx;
    private int travelledCount;
    private Runespan runespan;

    public PlatformConnection(Platform platform, Tile platformTile, ClientContext ctx, Runespan runespan){
        this.platform = platform;
        this.platformTile = platformTile;
        this.ctx = ctx;
        this.runespan = runespan;
        if(ctx.players.local().tile().equals(platformTile)) travelledCount++;
    }

    public void setConnection(FloatingIsland connection){
        this.connection = connection;
    }

    public Tile getPlatformTile() {
        return platformTile;
    }

    public Platform getPlatform() {
        return platform;
    }

    public FloatingIsland getConnection() {
        return connection;
    }

    public void travelToIsland() {
        final GameObject nextPlatform = ctx.objects.select().id(platform.getPlatformIds()).at(this.getPlatformTile()).peek();
        final Tile tile = Runespan.getReachableTile(nextPlatform, ctx);

        if(tile != null) {
            ctx.camera.turnTo(nextPlatform);

            runespan.log.info("Travelling to next island using platform: " + nextPlatform.toString());

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    boolean interactHappened = nextPlatform.interact(false, "Use");
                    if(interactHappened) {
                        runespan.log.info("Interacted with platform");
                        waitTillOnNewIsland();
                        travelledCount++;
                        return true;
                    } else {
                        runespan.log.info("Traversing to platform.");
                        ctx.movement.findPath(tile).traverse();
                        return false;
                    }
                }
            }, 750, 15);
        }
    }

    public String toString() {
        return "Platform: " + platform.name() + " TravelledCount: " + travelledCount;
    }

    private void waitTillOnNewIsland() {
        runespan.log.info("Waiting till on new island");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().idle()
                        && !runespan.getPreviousIsland().onIsland(ctx.players.local().tile())
                        && playerOnPlatform();
            }
        }, 500, 30);
        runespan.log.info("On new island");
    }

    private boolean playerOnPlatform() {
        return Platform.hasPlatform(ctx.objects.select().at(ctx.players.local().tile()).peek().id(), ctx);
    }
}
