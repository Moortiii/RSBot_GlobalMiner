package GlobalMiner;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class Mine extends Task<ClientContext> {
    public Mine(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate(Rock rock) {
        return ctx.inventory.select().count() < 28
                && !ctx.objects.select().id(rock.rockIds).isEmpty()
                && ctx.players.local().animation() == -1
                && !ctx.players.local().inMotion();
    }

    @Override
    public void execute(Rock rock) {
        GameObject ore = ctx.objects.nearest().poll();

        if(ore.inViewport()) {
            ore.interact("Mine");
        } else {
            ctx.movement.step(ore);
            ctx.camera.turnTo(ore);
        }
    }
}
