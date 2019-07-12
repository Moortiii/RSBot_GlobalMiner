package GlobalMiner;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

@Script.Manifest(name="GlobalMiner", description="Locate nearby ore to power mine anywhere", properties="client=4")
public class GlobalMiner extends PollingScript<ClientContext> implements PaintListener, MouseListener
{
    private static final Rectangle TOGGLE_GUI = new Rectangle(10, 160, 105, 20);
    private ArrayList<Task> taskList = new ArrayList<>();
    private int EXP_START;
    private int EXP_CURRENT;
    private int EXP_HOUR;
    private Timer timer = new Timer();
    private UserInterface ui = new UserInterface(ctx);

    @Override
    public void poll() {
        // Pause the script while the user is choosing new settings
        if(ui.isVisible()) {
            return;
        }

        for(Task task : taskList) {
            if(task.activate(ui.getSelectedRock())) {
                task.execute(ui.getSelectedRock());
            }
        }
    }

    @Override
    public void start() {
        System.out.println("GlobalMiner began execution");
        taskList.addAll(Arrays.asList(new Mine(ctx), new Drop(ctx)));
        EXP_START = ctx.skills.experience(Constants.SKILLS_MINING);

        // Calculate the hourly experience once every minute
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                calculateExperiencePerHour();
            }
        }, 1000 * 60,  1000 * 60);

        ui.displayInterface();
    }

    @Override
    public void stop() {
        System.out.println("GlobalMiner has stopped execution");
        timer.cancel();
        timer.purge();
    }

    @Override
    public void repaint(Graphics g) {
        ArrayList<String> strings = new ArrayList<>();
        g.setColor(new Color(0, 0, 0, 75));
        g.fillRect(0, 0, 300, 200);

        g.setColor(new Color(0, 255, 0));
        g.drawString("[Global Miner]", 10, 40);
        strings.add("EXP Gained: " + getExperienceGainedAsString());
        strings.add("EXP until level up: " + getRemainingExperienceAsString());
        strings.add("EXP / hr: " + getExperiencePerHourAsString());
        strings.add("Current level: " + ctx.skills.level(Constants.SKILLS_MINING));

        if(ui.getSelectedRock() != null) {
            strings.add("Currently mining: " + ui.getSelectedRock().displayName);
        }

        strings.add("Elapsed time: " + getElapsedTimeAsString());

        int x = 10;
        int y = 40;

        for(String string : strings) {
            y += 20;

            g.setColor(new Color(255, 255, 255));
            g.drawString(string, x, y);
        }

        // Toggle box for user interface
        g.setColor(new Color(255, 227, 8, 215));
        g.fillRect(x,y + 10, 105, 20);
        g.setColor(new Color(0, 0, 0));
        g.drawString("Click to open GUI", x + 5, y + 25);
    }

    private String getElapsedTimeAsString() {
        int hours = 0;
        int minutes = 0;

        long remainder = getTotalRuntime() / 1000;

        while(remainder >= 3600) {
            remainder -= 3600;
            hours++;
        }

        while(remainder >= 60) {
            remainder -= 60;
            minutes++;
        }

        // The remainder is equal to the number of seconds left
        return String.format("%d hours %d minutes %d seconds", hours, minutes, (int)remainder);
    }

    private String getExperienceGainedAsString() {
        EXP_CURRENT = ctx.skills.experience(Constants.SKILLS_MINING);
        return String.format("%d", EXP_CURRENT - EXP_START);
    }

    private String getRemainingExperienceAsString() {
        EXP_CURRENT = ctx.skills.experience(Constants.SKILLS_MINING);
        int NEXT_LEVEL_EXP = ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_MINING ) + 1);
        return String.format("%d", NEXT_LEVEL_EXP - EXP_CURRENT);
    }

    private void calculateExperiencePerHour() {
        EXP_CURRENT = ctx.skills.experience(Constants.SKILLS_MINING);

        long totalSeconds = getTotalRuntime() / 1000;
        final int EXP_GAINED = EXP_CURRENT - EXP_START;

        if(totalSeconds > 0)
            EXP_HOUR = (EXP_GAINED / (int)totalSeconds) * 3600;
    }

    private String getExperiencePerHourAsString() {
        return String.format("%d", EXP_HOUR);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(TOGGLE_GUI.contains(e.getPoint())) {
            ui.displayInterface();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
