package GlobalMiner;

import java.util.HashMap;
import java.util.Map;

public enum Rock {
    // Padding the displayName with spaces because Swing sucks and I want padding without resorting
    // to using a CompoundBorder with an EmptyBorder and LineBorder that sucks even more.
    // TODO: Update with additional ore values. Need to find a resource that has all the rockIds
    IRON (440, "  Iron ore", 11365, 11364),
    COAL (443, "  Coal ore", 11366, 11367),
    ADAMANTITE (449, "  Adamantite ore", 11374, 11375);

    public int[] rockIds;
    public int oreId;
    public String displayName;

    private static final Map<String, Rock> map;

    static {
        map = new HashMap<String, Rock>();

        for (Rock v : Rock.values()) {
            map.put(v.displayName, v);
        }
    }

    public static Rock getEnumFromDisplayName(String displayName) {
        return map.get(displayName);
    }

    // This is an expensive function, but the process of checking the IDs happens very rarely
    // we could switch to ArrayList and use .contains() in the future.
    public static String getDisplayNameFromId(int id) {
        for(Rock rock : Rock.values()) {
            for(int rock_id : rock.rockIds) {
                if(rock_id == id)
                    return rock.displayName;
            }
        }

        return null;
    }

    public static int[] getRockIdsFromDisplayName(String displayName) {
        for(Rock rock : Rock.values()) {
            if (rock.displayName.equals(displayName)) {
                return rock.rockIds;
            }
        }

        return null;
    }

    Rock(int oreId, String displayName, int... rockIds) {
        this.rockIds = rockIds;
        this.oreId = oreId;
        this.displayName = displayName;
    }
}
