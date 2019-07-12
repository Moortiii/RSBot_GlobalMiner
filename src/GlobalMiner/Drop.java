package GlobalMiner;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Drop extends Task<ClientContext> {
    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate(Rock rock) {
        return ctx.inventory.isFull();
    }

    @Override
    public void execute(Rock rock) {
        for(Item i : ctx.inventory.id(rock.oreId)) {
            i.interact("Drop");
        }
    }
}
