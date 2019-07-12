package GlobalMiner;

import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.sound.midi.SysexMessage;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

public class UserInterface extends JFrame {
    private final int WIDTH = 300;
    private ClientContext ctx;
    private Rock selected_rock;
    private JPanel container = new JPanel();
    private JPanel button_panel = new JPanel();
    private JPanel list_panel = new JPanel();
    private JButton start_button;
    private JButton refresh_button;
    private JList list;
    private DefaultListModel model;

    public UserInterface(ClientContext ctx) {
        this.ctx = ctx;

        // TODO: Find out if there is a way to get the original JFrame for the client as
        // this would make it possible to start the GUI relative to it, nice QOL change.
        this.setTitle("GlobalMiner");
        this.setPreferredSize(new Dimension(WIDTH, 300));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 10));

        createButtons();
        createList();

        JLabel label = new JLabel("Select rock to mine using the list below");
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        this.button_panel.setBackground(Color.WHITE);
        this.getContentPane().add(label, BorderLayout.PAGE_START);
        this.getContentPane().add(this.list, BorderLayout.LINE_START);
        this.getContentPane().add(this.button_panel, BorderLayout.PAGE_END);
        this.getContentPane().setBackground(Color.WHITE);

        this.start_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected_rock = Rock.getEnumFromDisplayName(list.getSelectedValue().toString());
                hideInterface();
            }
        });

        this.refresh_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                populateInterface();
            }
        });

        // Check for nearby ores immediately after the script is launched
        populateInterface();

        this.add(this.container);
        this.pack();
        this.setVisible(true);
    }

    private void createList() {
        this.model = new DefaultListModel();
        this.list = new JList(this.model);
        this.list.setPreferredSize(new Dimension(WIDTH, 120));
        this.list.setCellRenderer(getRenderer());
        this.list.setFixedCellHeight(28);
        this.list.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(200, 200, 200)));
        this.list.setBackground(Color.WHITE);
        this.list.setForeground(Color.BLACK);
    }

    private void createButtons() {
        this.refresh_button = new JButton("Refresh list");
        this.refresh_button.setBorderPainted(false);
        this.refresh_button.setFocusPainted(false);
        this.refresh_button.setBackground(new Color(52, 73, 94));
        this.refresh_button.setContentAreaFilled(true);
        this.refresh_button.setForeground(Color.WHITE);
        this.refresh_button.setPreferredSize(new Dimension(100, 35));
        this.button_panel.add(this.refresh_button);

        this.start_button = new JButton("Start script");
        this.start_button.setBorderPainted(false);
        this.start_button.setFocusPainted(false);
        this.start_button.setBackground(new Color(39, 174, 96));
        this.start_button.setContentAreaFilled(true);
        this.start_button.setForeground(Color.WHITE);
        this.start_button.setPreferredSize(new Dimension(100, 35));
        this.button_panel.add(this.start_button);
    }

    void populateInterface() {
        // Likely the wrong datastructures to use, but this makes it
        // so easy to avoid duplicates that it's probably worth it
        Set<GameObject> nearbyObjects = new HashSet<>();
        Set<GameObject> nearbyOres = new HashSet<>();
        Set<String> objectStrings = new HashSet<>();

        ctx.objects.within(3).select().addTo(nearbyObjects);

        // Cubic time because processing power is cheap
        // probably should fine a better way to do this
        // but the operation happens so rarely anyway
        for(GameObject object : nearbyObjects) {
            for(Rock rock : Rock.values()) {
                for(int id : rock.rockIds) {
                    if(id == object.id()) {
                        nearbyOres.add(object);
                        objectStrings.add(Rock.getDisplayNameFromId(id));
                    }
                }
            }
        }

        this.model.clear();

        for(String ore : objectStrings) {
            this.model.addElement(ore);
        }
    }

    private ListCellRenderer<? super String> getRenderer() {
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                JLabel listCellRendererComponent = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,cellHasFocus);
                listCellRendererComponent.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
                return listCellRendererComponent;
            }
        };
    }

    public void displayInterface() {
        this.setVisible(true);
    }

    public void hideInterface() {
        this.setVisible(false);
    }

    public Rock getSelectedRock() {
        return selected_rock;
    }
}