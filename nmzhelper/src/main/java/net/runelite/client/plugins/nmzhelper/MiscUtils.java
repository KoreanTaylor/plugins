package net.runelite.client.plugins.nmzhelper;

import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.widgets.WidgetInfo;

import java.util.Arrays;

public class MiscUtils {
    private static final int[] NMZ_MAP_REGION = {9033};

    public static boolean isInNightmareZone(Client client) {
        if (client.getLocalPlayer() == null) {
            return false;
        }

        // NMZ and the KBD lair uses the same region ID but NMZ uses planes 1-3 and KBD uses plane 0
        return client.getLocalPlayer().getWorldLocation().getPlane() > 0 && Arrays.equals(client.getMapRegions(), NMZ_MAP_REGION);
    }

    public static LegacyMenuEntry getConsumableEntry(String itemName, int itemId, int itemIndex) {
        return new LegacyMenuEntry("Drink", "<col=ff9040>" + itemName, itemId, MenuAction.ITEM_FIRST_OPTION.getId(), itemIndex, WidgetInfo.INVENTORY.getId(), false);
    }

    public static DreamType getDreamType(Client client) {
        int varbitValue = client.getVarbitValue(3946);
        return DreamType.fromId(varbitValue);
    }

    public static boolean isDreamCreated(Client client) {
        DreamType dreamType = getDreamType(client);
        return !dreamType.equals(DreamType.NOT_SUPPORTED) && !dreamType.equals(DreamType.NOT_DREAMING);
    }

}
