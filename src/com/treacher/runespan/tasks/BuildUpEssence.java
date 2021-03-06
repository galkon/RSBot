package com.treacher.runespan.tasks;

import com.treacher.runespan.Runespan;
import com.treacher.runespan.enums.EssenceMonster;
import com.treacher.runespan.util.RunespanQuery;
import com.treacher.util.Task;
import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Michael Treacher
 */
public class BuildUpEssence extends Task<ClientContext> {

    private Runespan runespan;
    private RunespanQuery runespanQuery;

    public BuildUpEssence(ClientContext ctx, Runespan runespan) {
        super(ctx);
        this.runespan = runespan;
    }

    @Override
    public boolean activate() {
        runespanQuery = new RunespanQuery(ctx, runespan);
        final boolean hasNoNodes = !runespanQuery.hasNodes();
        return  ctx.players.local().idle()
                && runespanQuery.essenceStackSize() > -1
                && (runespanQuery.essenceStackSize() < 50 || hasNoNodes)
                && (runespanQuery.hasEssenceMonsters())
                && !ctx.chat.chatting()
                && !runespan.hasTarget();
    }

    @Override
    public void execute() {
        Runespan.STATE = "Building up essence";
        runespan.log.info("Building up essence");
        EssenceMonster.siphonMonster(runespanQuery.highestPriorityEssenceMonster(), ctx, runespan);
    }
}
