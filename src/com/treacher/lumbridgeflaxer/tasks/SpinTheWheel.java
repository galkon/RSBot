package com.treacher.lumbridgeflaxer.tasks;

import com.treacher.lumbridgeflaxer.LumbridgeFlaxer;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.util.concurrent.Callable;

/**
 * Created by Michael Treacher on 8/11/14.
 */
public class SpinTheWheel extends Task<ClientContext> {

    private final Component spinTheWheelComponent = ctx.widgets.component(1370,38);
    private final Component spinningComponent = ctx.widgets.component(1251, 8);

    private final int flaxId;

    public SpinTheWheel(ClientContext ctx, int flaxId) {
        super(ctx);
        this.flaxId = flaxId;
    }

    @Override
    public boolean activate() {
        return spinTheWheelComponent.visible() && !spinningComponent.visible();
    }

    @Override
    public void execute() {
        LumbridgeFlaxer.STATE = "Spinning the wheel!";
        LumbridgeFlaxer.timeSinceLastMovement = -1;
        spinTheWheelComponent.click();
        waitTilSpinningHasStarted();

    }

    private void waitTilSpinningHasStarted() {
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return spinningComponent.visible() && ctx.backpack.select().id(flaxId).count() < 28;
            }
        },150, 20);
    }
}
