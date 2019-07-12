package GlobalMiner;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public abstract class Task<C extends org.powerbot.script.ClientContext<Client>> extends ClientAccessor {
    public Task(ClientContext ctx) {
        super(ctx);
    }

    public abstract boolean activate(Rock rock);
    public abstract void execute(Rock rock);
}